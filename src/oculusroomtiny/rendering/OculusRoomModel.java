/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.rendering;

import jglm.Vec3;
import jglm.Vec3i;
import jglm.Vec4;
import oculusroomtiny.entities.Model;
import oculusroomtiny.entities.Scene;
import oculusroomtiny.entities.Slab;
import oculusroomtiny.entities.SlabModel;

/**
 *
 * @author gbarbieri
 *
 */
public class OculusRoomModel {

    public OculusRoomModel() {

    }

    public static void populateRoomScene(Scene scene) {

        Slab[] floorSlabs = new Slab[]{
            new Slab(new Vec3(-10f, -0.1f, -20f), new Vec3(10f, 0f, 20.1f), new Vec3i(128, 128, 128))
        };

        SlabModel floor = new SlabModel(floorSlabs.length, floorSlabs, BuiltinTexture.tex_checker);

        Slab[] ceilingSlabs = new Slab[]{
            new Slab(new Vec3(-10f, 4f, -20f), new Vec3(10f, 4.1f, 20.1f), new Vec3i(128, 128, 128))
        };
        
        SlabModel ceiling = new SlabModel(ceilingSlabs.length, ceilingSlabs, BuiltinTexture.tex_panel);

        scene.getWorld().add(Model.createModel(new Vec3(0f, 0f, 0f), floor));
        scene.getWorld().add(Model.createModel(new Vec3(0f, 0f, 0f), ceiling));

        scene.setAmbient(new Vec4(0.65f, 0.65f, 0.65f, 1f));
        scene.addLight(new Vec3(-2f, 4f, -2f), new Vec4(8f, 8f, 8f, 1f));
        scene.addLight(new Vec3(3f, 4f, -3f), new Vec4(2f, 1f, 1f, 1f));
        scene.addLight(new Vec3(-4f, 3f, 25f), new Vec4(3f, 6f, 3f, 1f));
    }

    public enum BuiltinTexture {

        tex_none,
        tex_checker,
        tex_block,
        tex_panel,
        tex_count
    }

    public static byte[] getTexture(BuiltinTexture texture) {

        switch (texture) {

            case tex_checker:
                return getCheckerTexture();

        }
        return new byte[]{};
    }

    private static byte[] getCheckerTexture() {

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
        return checker;
    }

    private static byte[] getPanelTexture() {

        Vec4 a = new Vec4(80f, 80f, 80f, 255f);

        Vec4 b = new Vec4(180f, 180f, 180f, 255f);

        byte[] checker = new byte[256 * 256 * 4];

        for (int j = 0; j < 256; j++) {

            for (int i = 0; i < 256; i++) {

                Vec4 color = (((i / 4 >> 5) ^ (j / 4 >> 5)) & 1) == 1 ? a : b;

                checker[(j * 256 + i) * 4] = (byte) color.x;
                checker[(j * 256 + i) * 4 + 1] = (byte) color.y;
                checker[(j * 256 + i) * 4 + 2] = (byte) color.z;
                checker[(j * 256 + i) * 4 + 3] = (byte) color.w;
            }
        }
        return checker;
    }
}
