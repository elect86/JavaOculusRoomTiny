/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.rendering;

import javax.media.opengl.GL3;
import jglm.Vec3;
import jglm.Vec3i;
import jglm.Vec4;
import roomTinySimplified.entities.Model;
import roomTinySimplified.entities.Scene;
import roomTinySimplified.entities.Slab;
import roomTinySimplified.entities.SlabModel;
import static roomTinySimplified.rendering.Texture.BuiltinTexture.tex_block;
import static roomTinySimplified.rendering.Texture.BuiltinTexture.tex_checker;
import static roomTinySimplified.rendering.Texture.BuiltinTexture.tex_none;
import static roomTinySimplified.rendering.Texture.BuiltinTexture.tex_panel;

/**
 *
 * @author gbarbieri
 *
 */
public class OculusRoomModel {

    public OculusRoomModel() {

    }

    public static void populateRoomScene(GL3 gl3, Scene scene) {

//        scene.addModel(new Model(GL3.GL_TRIANGLES, new Vec3(0f, 0f, 0f), floor));
//        scene.addModel(new Model(GL3.GL_TRIANGLES, new Vec3(0f, 0f, 0f), ceiling));
//        scene.addModel(new Model(GL3.GL_TRIANGLES, new Vec3(0f, 0f, 0f), room));
//        scene.addModel(new Model(GL3.GL_TRIANGLES, new Vec3(0f, 0f, 0f), fixtures));
        scene.addModel(new Model(GL3.GL_TRIANGLES, new Vec3(0f, 0f, 0f), furniture));
//        scene.addModel(new Model(GL3.GL_TRIANGLES, new Vec3(0f, 0f, 4f), furniture));

        scene.setAmbient(new Vec4(0.65f, 0.65f, 0.65f, 1f));
        scene.addLight(new Vec3(-2f, 4f, -2f), new Vec4(8f, 8f, 8f, 1f));
        scene.addLight(new Vec3(3f, 4f, -3f), new Vec4(2f, 1f, 1f, 1f));
        scene.addLight(new Vec3(-4f, 3f, 25f), new Vec4(3f, 6f, 3f, 1f));
    }
    
    private static final Slab[] floorSlabs = new Slab[]{
        new Slab(new Vec3(-10f, -0.1f, -20f), new Vec3(10f, 0f, 20.1f), new Vec3i(128, 128, 128))
    };

    private static final SlabModel floor = new SlabModel(floorSlabs.length, floorSlabs, tex_checker);

    private static final Slab[] ceilingSlabs = new Slab[]{
        new Slab(new Vec3(-10f, 4f, -20f), new Vec3(10f, 4.1f, 20.1f), new Vec3i(128, 128, 128))
    };

    private static final SlabModel ceiling = new SlabModel(ceilingSlabs.length, ceilingSlabs, tex_panel);

    private static final Slab[] roomSlabs = new Slab[]{
        // Left Wall
        new Slab(new Vec3(-10.1f, 0.0f, -20.0f), new Vec3(-10.0f, 4.0f, 20.0f), new Vec3i(128, 128, 128)),
        // Back Wall
        new Slab(new Vec3(-10.0f, -0.1f, -20.1f), new Vec3(10.0f, 4.0f, -20.0f), new Vec3i(128, 128, 128)),
        // Right Wall
        new Slab(new Vec3(10.0f, -0.1f, -20.0f), new Vec3(10.1f, 4.0f, 20.0f), new Vec3i(128, 128, 128))
    };

    private static final SlabModel room = new SlabModel(roomSlabs.length, roomSlabs, tex_block);

    private static final Slab[] fixtureSlabs = new Slab[]{
        // Right side shelf
        new Slab(new Vec3(9.5f, 0.75f, 3.0f), new Vec3(10.1f, 2.5f, 3.1f), new Vec3i(128, 128, 128)), // Verticals
        new Slab(new Vec3(9.5f, 0.95f, 3.7f), new Vec3(10.1f, 2.75f, 3.8f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(9.5f, 1.20f, 2.5f), new Vec3(10.1f, 1.30f, 3.8f), new Vec3i(128, 128, 128)), // Horizontals
        new Slab(new Vec3(9.5f, 2.00f, 3.0f), new Vec3(10.1f, 2.10f, 4.2f), new Vec3i(128, 128, 128)),
        // Right railing    
        new Slab(new Vec3(5.0f, 1.1f, 20.0f), new Vec3(10.0f, 1.2f, 20.1f), new Vec3i(128, 128, 128)),
        // Bars
        new Slab(new Vec3(9.0f, 1.1f, 20.0f), new Vec3(9.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(8.0f, 1.1f, 20.0f), new Vec3(8.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(7.0f, 1.1f, 20.0f), new Vec3(7.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(6.0f, 1.1f, 20.0f), new Vec3(6.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(5.0f, 1.1f, 20.0f), new Vec3(5.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        // Left railing    
        new Slab(new Vec3(-10.0f, 1.1f, 20.0f), new Vec3(-5.0f, 1.2f, 20.1f), new Vec3i(128, 128, 128)),
        // Bars
        new Slab(new Vec3(-9.0f, 1.1f, 20.0f), new Vec3(-9.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(-8.0f, 1.1f, 20.0f), new Vec3(-8.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(-7.0f, 1.1f, 20.0f), new Vec3(-7.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(-6.0f, 1.1f, 20.0f), new Vec3(-6.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        new Slab(new Vec3(-5.0f, 1.1f, 20.0f), new Vec3(-5.1f, 0.0f, 20.1f), new Vec3i(128, 128, 128)),
        // Bottom Floor 2
        new Slab(new Vec3(-15.0f, -6.1f, 18.0f), new Vec3(15.0f, -6.0f, 30.0f), new Vec3i(128, 128, 128))
    };

    private static final SlabModel fixtures = new SlabModel(fixtureSlabs.length, fixtureSlabs, tex_none);

    private static final Slab[] furnitureSlabs = new Slab[]{
        // Table
        new Slab(new Vec3(-1.8f, 0.7f, 1.0f), new Vec3(0.0f, 0.8f, 0.0f), new Vec3i(128, 128, 88)),
        new Slab(new Vec3(-1.8f, 0.7f, 0.0f), new Vec3(-1.8f + 0.1f, 0.0f, 0.0f + 0.1f), new Vec3i(128, 128, 88)), // Leg 1
        new Slab(new Vec3(-1.8f, 0.7f, 1.0f), new Vec3(-1.8f + 0.1f, 0.0f, 1.0f - 0.1f), new Vec3i(128, 128, 88)), // Leg 2
        new Slab(new Vec3(0.0f, 0.7f, 1.0f), new Vec3(0.0f - 0.1f, 0.0f, 1.0f - 0.1f), new Vec3i(128, 128, 88)), // Leg 2
        new Slab(new Vec3(0.0f, 0.7f, 0.0f), new Vec3(0.0f - 0.1f, 0.0f, 0.0f + 0.1f), new Vec3i(128, 128, 88)), // Leg 2
        // Chair
        new Slab(new Vec3(-1.4f, 0.5f, -1.1f), new Vec3(-0.8f, 0.55f, -0.5f), new Vec3i(88, 88, 128)), // Set
        new Slab(new Vec3(-1.4f, 1.0f, -1.1f), new Vec3(-1.4f + 0.06f, 0.0f, -1.1f + 0.06f), new Vec3i(88, 88, 128)), // Leg 1
        new Slab(new Vec3(-1.4f, 0.5f, -0.5f), new Vec3(-1.4f + 0.06f, 0.0f, -0.5f - 0.06f), new Vec3i(88, 88, 128)), // Leg 2
        new Slab(new Vec3(-0.8f, 0.5f, -0.5f), new Vec3(-0.8f - 0.06f, 0.0f, -0.5f - 0.06f), new Vec3i(88, 88, 128)), // Leg 2
        new Slab(new Vec3(-0.8f, 1.0f, -1.1f), new Vec3(-0.8f - 0.06f, 0.0f, -1.1f + 0.06f), new Vec3i(88, 88, 128)), // Leg 2
        new Slab(new Vec3(-1.4f, 0.97f, -1.05f), new Vec3(-0.8f, 0.92f, -1.10f), new Vec3i(88, 88, 128)) // Back high bar
    };
    
    private static final SlabModel furniture = new SlabModel(furnitureSlabs.length, furnitureSlabs, tex_none);
}
