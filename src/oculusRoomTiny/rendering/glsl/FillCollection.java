/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny.rendering.glsl;

import com.jogamp.opengl.GL3;
import jglm.Vec2i;
import jglm.Vec4;
import oculusRoomTiny.rendering.Texture;
import static oculusRoomTiny.rendering.Texture.BuiltinTexture.tex_checker;
import static oculusRoomTiny.rendering.Texture.BuiltinTexture.tex_count;
import static oculusRoomTiny.rendering.Texture.BuiltinTexture.tex_panel;
import oculusRoomTiny.rendering.TextureFormat;
import oculusRoomTiny.rendering.glsl.shaders.BuiltinShaders;

/**
 *
 * @author gbarbieri
 */
public class FillCollection {

    private ShaderFill litSolid;
    private ShaderFill[] litTextures;

    public FillCollection(GL3 gl3) {

        Texture[] builtinTextures = new Texture[tex_count.ordinal()];

        /**
         * Create floor checkerboard texture.
         */
        {
            Vec4 a = new Vec4(180f, 180f, 180f, 255f);

            Vec4 b = new Vec4(80f, 80f, 80f, 255f);

            byte[] checker = new byte[256 * 256 * 4];

            for (int j = 0; j < 256; j++) {

                for (int i = 0; i < 256; i++) {

                    Vec4 color = (((i / 4 >> 5) ^ (j / 4 >> 5)) & 1) == 1 ? b : a;

                    checker[(j * 256 + i) * 4] = (byte) color.x;
                    checker[(j * 256 + i) * 4 + 1] = (byte) color.y;
                    checker[(j * 256 + i) * 4 + 2] = (byte) color.z;
                    checker[(j * 256 + i) * 4 + 3] = (byte) color.w;
                }
            }
            builtinTextures[tex_checker.ordinal()] = Texture.create(gl3, TextureFormat.RGBA, new Vec2i(256, 256), checker);
        }
        /**
         * Ceiling panel texture.
         */
        {
            Vec4 a = new Vec4(80f, 80f, 80f, 255f);

            Vec4 b = new Vec4(180f, 180f, 180f, 255f);

            byte[] panel = new byte[256 * 256 * 4];

            for (int j = 0; j < 256; j++) {

                for (int i = 0; i < 256; i++) {

                    Vec4 color = (i / 4 == 0 || j / 4 == 0) ? a : b;

                    panel[(j * 256 + i) * 4] = (byte) color.x;
                    panel[(j * 256 + i) * 4 + 1] = (byte) color.y;
                    panel[(j * 256 + i) * 4 + 2] = (byte) color.z;
                    panel[(j * 256 + i) * 4 + 3] = (byte) color.w;
                }
            }
            builtinTextures[tex_panel.ordinal()] = Texture.create(gl3, TextureFormat.RGBA, new Vec2i(256, 256), panel);
        }
        litTextures = new ShaderFill[tex_count.ordinal()];
        LitTexturesProgram program = new LitTexturesProgram(gl3, BuiltinShaders.filepath, BuiltinShaders.standard_VS, BuiltinShaders.litTexture_FS);

        for (int i = 1; i < 3; i++) {           

            litTextures[i] = new ShaderFill(program, builtinTextures[i]);
        }
    }

    public ShaderFill getLitSolid() {
        return litSolid;
    }

    public ShaderFill[] getLitTextures() {
        return litTextures;
    }
}
