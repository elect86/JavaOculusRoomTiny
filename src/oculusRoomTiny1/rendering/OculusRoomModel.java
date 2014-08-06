/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1.rendering;

import javax.media.opengl.GL3;
import jglm.Vec3;
import jglm.Vec3i;
import jglm.Vec4;
import oculusRoomTiny1.entities.Model;
import oculusRoomTiny1.entities.Scene;
import oculusRoomTiny1.entities.Slab;
import oculusRoomTiny1.entities.SlabModel;
import static oculusRoomTiny1.rendering.Texture.BuiltinTexture.tex_checker;
import static oculusRoomTiny1.rendering.Texture.BuiltinTexture.tex_panel;
import oculusRoomTiny1.rendering.glsl.FillCollection;

/**
 *
 * @author gbarbieri
 *
 */
public class OculusRoomModel {

    private static final Slab[] floorSlabs = new Slab[]{
        new Slab(new Vec3(-10f, -0.1f, -20f), new Vec3(10f, 0f, 20.1f), new Vec3i(128, 128, 128))
    };

    private static final SlabModel floor = new SlabModel(floorSlabs.length, floorSlabs, tex_checker);

    private static final Slab[] ceilingSlabs = new Slab[]{
        new Slab(new Vec3(-10f, 4f, -20f), new Vec3(10f, 4.1f, 20.1f), new Vec3i(128, 128, 128))
    };

    private static final SlabModel ceiling = new SlabModel(ceilingSlabs.length, ceilingSlabs, tex_panel);

    public OculusRoomModel() {

    }

    public static void populateRoomScene(GL3 gl3, Scene scene) {

        FillCollection fillCollection = new FillCollection(gl3);

//        scene.getWorld().add(Model.createModel(new Vec3(0f, 0f, 0f), floor, fillCollection));
        scene.getWorld().add(Model.createModel(new Vec3(0f, 0f, 0f), ceiling, fillCollection));

        scene.setAmbient(new Vec4(0.65f, 0.65f, 0.65f, 1f));
        scene.addLight(new Vec3(-2f, 4f, -2f), new Vec4(8f, 8f, 8f, 1f));
        scene.addLight(new Vec3(3f, 4f, -3f), new Vec4(2f, 1f, 1f, 1f));
        scene.addLight(new Vec3(-4f, 3f, 25f), new Vec4(3f, 6f, 3f, 1f));
    }
}
