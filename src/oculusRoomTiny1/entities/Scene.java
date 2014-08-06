/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1.entities;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import javax.media.opengl.GL3;
import jglm.Mat4;
import jglm.Vec3;
import jglm.Vec4;
import oculusRoomTiny1.core.OculusRoomTiny;
import oculusRoomTiny1.rendering.LightingParams;

/**
 *
 * @author gbarbieri
 */
public class Scene {

    private Container world;
    private Vec4[] lightPos;
    private LightingParams lighting;

    public Scene() {

        world = new Container();

        lightPos = new Vec4[8];

        lighting = new LightingParams();
    }

    public Container getWorld() {
        return world;
    }

    public void render(GL3 gl3, Mat4 projection, Mat4 view) {

        lighting.update(view, lightPos);

        setLighting(gl3);

        world.render(gl3, projection, view);
    }

    public void setAmbient(Vec4 color) {

        lighting.setAmbient(color);
    }

    public void addLight(Vec3 pos, Vec4 color) {

        int n = lighting.getLightCount();

        lightPos[n] = new Vec4(pos, 1);

        lighting.getLightColor()[n] = color;

        lighting.setLightCount(n + 1);
    }

    private void setLighting(GL3 gl3) {

        int offset = 0;

        Vec4 ambient = lighting.getAmbient();
        int lightCount = lighting.getLightCount();

        float[] fs = new float[3 + 1 + 4 * 8 * 2];

        fs[0] = ambient.x;
        fs[1] = ambient.y;
        fs[2] = ambient.z;
        fs[3] = lightCount;

        for (int i = 0; i < lightCount; i++) {

            fs[3 + 1 + i * 4] = lighting.getLightPos()[i].x;
            fs[3 + 1 + i * 4 + 1] = lighting.getLightPos()[i].y;
            fs[3 + 1 + i * 4 + 2] = lighting.getLightPos()[i].z;
            fs[3 + 1 + i * 4 + 3] = lighting.getLightPos()[i].w;

            fs[3 + 1 + 4 * 8 + i * 4] = lighting.getLightColor()[i].x/255;
            fs[3 + 1 + 4 * 8 + i * 4 + 1] = lighting.getLightColor()[i].y/255;
            fs[3 + 1 + 4 * 8 + i * 4 + 2] = lighting.getLightColor()[i].z/255;
            fs[3 + 1 + 4 * 8 + i * 4 + 3] = lighting.getLightColor()[i].w/255;
        }

        FloatBuffer floatBuffer = GLBuffers.newDirectFloatBuffer(fs);
//        System.out.println("fs.length "+fs.length);
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, OculusRoomTiny.getInstance().getGlViewer().getLightingUBO()[0]);
        {
            gl3.glBufferSubData(GL3.GL_UNIFORM_BUFFER, offset, fs.length * 4, floatBuffer);
        }
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, 0);
    }
}
