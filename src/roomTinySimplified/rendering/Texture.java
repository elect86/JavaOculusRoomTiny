/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.rendering;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.ByteBuffer;
import jglm.Vec2i;
import jglm.Vec4;
import static roomTinySimplified.rendering.TextureFormat.RGBA;

/**
 *
 * @author gbarbieri
 */
public class Texture {

    private int[] id;
    private Vec2i size;
    private int internalFormat;
    private int glFormat;
    private int glType;
    private ByteBuffer data;

    private Texture(int[] id, Vec2i size, int internalFormat, int glFormat, int glType, ByteBuffer data) {
        this.id = id;
        this.size = size;
        this.internalFormat = internalFormat;
        this.glFormat = glFormat;
        this.glType = glType;
        this.data = data;
    }

    private static Texture create(GL3 gl3, int format, Vec2i sizei, byte[] data) {

        int glFormat, glType = GL3.GL_UNSIGNED_BYTE;

        switch (format & TextureFormat.TypeMask) {

            case TextureFormat.RGBA:
                glFormat = GL3.GL_RGBA;
                break;

            default:
                return null;
        }

        int[] textureId = new int[1];
        gl3.glGenTextures(1, textureId, 0);

        gl3.glBindTexture(GL3.GL_TEXTURE_2D, textureId[0]);

        boolean isSRGB = ((format & TextureFormat.TypeMask) == TextureFormat.RGBA && (format & TextureFormat.SRGB) != 0);
        boolean isDepth = ((format & TextureFormat.Depth) != 0);

        int internalFormat = isSRGB ? GL3.GL_SRGB_ALPHA : isDepth ? GL3.GL_DEPTH_COMPONENT32F : glFormat;

        ByteBuffer buffer = data != null ? GLBuffers.newDirectByteBuffer(data) : null;

        gl3.glTexImage2D(GL3.GL_TEXTURE_2D, 0, internalFormat, sizei.x, sizei.y, 0, glFormat, glType, buffer);

        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR_MIPMAP_LINEAR);
        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);

        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAX_LEVEL, 0);

        return new Texture(textureId, sizei, internalFormat, glFormat, glType, buffer);
    }

    public static Texture createBuiltinTexture(GL3 gl3, BuiltinTexture builtinTexture) {

        byte[] data = new byte[256 * 256 * 4];

        switch (builtinTexture) {

            case tex_checker:

                Vec4 a = new Vec4(180f, 180f, 180f, 255f);

                Vec4 b = new Vec4(80f, 80f, 80f, 255f);

                for (int j = 0; j < 256; j++) {

                    for (int i = 0; i < 256; i++) {

                        Vec4 color = (((i / 4 >> 5) ^ (j / 4 >> 5)) & 1) == 1 ? b : a;

                        data[(j * 256 + i) * 4] = (byte) color.x;
                        data[(j * 256 + i) * 4 + 1] = (byte) color.y;
                        data[(j * 256 + i) * 4 + 2] = (byte) color.z;
                        data[(j * 256 + i) * 4 + 3] = (byte) color.w;
                    }
                }
                break;

            case tex_panel:

                a = new Vec4(80f, 80f, 80f, 255f);

                b = new Vec4(180f, 180f, 180f, 255f);

                for (int j = 0; j < 256; j++) {

                    for (int i = 0; i < 256; i++) {

                        Vec4 color = (i / 4 == 0 || j / 4 == 0) ? a : b;

                        data[(j * 256 + i) * 4] = (byte) color.x;
                        data[(j * 256 + i) * 4 + 1] = (byte) color.y;
                        data[(j * 256 + i) * 4 + 2] = (byte) color.z;
                        data[(j * 256 + i) * 4 + 3] = (byte) color.w;
                    }
                }
                break;

            default:

                a = new Vec4(60f, 60f, 60f, 255f);

                b = new Vec4(180f, 180f, 180f, 255f);

                for (int j = 0; j < 256; j++) {

                    for (int i = 0; i < 256; i++) {

                        boolean c = ((j / 4 & 15) == 0) || (((i / 4 & 15) == 0) && ((((i / 4 & 31) == 0) ^ (((j / 4 >> 4) & 1)== 1)) == false));
                        
                        Vec4 color = c ? a : b;
//                        boolean c = ((j / 4 & 15) == 0) || ((i / 4 & 15) == 0);
//                        int d = ((i / 4 & 31) == 0) ? 0 : 1;
//                        d = d ^ (j / 4 >> 4);
//                        boolean e = c && (d == 1);
//                        Vec4 color = e ? a : b;

                        data[(j * 256 + i) * 4] = (byte) color.x;
                        data[(j * 256 + i) * 4 + 1] = (byte) color.y;
                        data[(j * 256 + i) * 4 + 2] = (byte) color.z;
                        data[(j * 256 + i) * 4 + 3] = (byte) color.w;
                    }
                }
        }
        return Texture.create(gl3, RGBA, new Vec2i(256, 256), data);
    }

    public Vec2i getSize() {
        return size;
    }

    public int getId() {
        return id[0];
    }

    public enum BuiltinTexture {

        tex_none,
        tex_checker,
        tex_panel,
        tex_block,
        tex_count
    }
}
