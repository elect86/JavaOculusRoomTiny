/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.rendering.glsl;

import javax.media.opengl.GL3;
import jglm.Vec2i;
import jglm.Vec4;
import oculusroomtiny.rendering.OculusRoomModel;
import oculusroomtiny.rendering.Texture;
import static oculusroomtiny.rendering.Texture.BuiltinTexture.tex_checker;
import static oculusroomtiny.rendering.Texture.BuiltinTexture.tex_count;
import oculusroomtiny.rendering.TextureFormat;

/**
 *
 * @author gbarbieri
 */
public class FillCollection {

    private ShaderFill litSolid;
    private ShaderFill[] litTextures;

    public FillCollection(GL3 gl3) {

        Texture[] builtInTextures = new Texture[tex_count.ordinal()];

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
            builtInTextures[tex_checker.ordinal()] = Texture.create(gl3, TextureFormat.RGBA, new Vec2i(256, 256), checker);
        }

        litTextures = new ShaderFill[tex_count.ordinal()];

        for (int i = 1; i < tex_count.ordinal(); i++) {

            
        }
    }
}
