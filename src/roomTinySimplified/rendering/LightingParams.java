/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.rendering;

import jglm.Mat4;
import jglm.Vec4;

/**
 *
 * @author gbarbieri
 */
public class LightingParams {

    private Vec4 ambient;
    private Vec4[] lightPos;
    private Vec4[] lightColor;
    private int lightCount;

    public LightingParams() {

        lightPos = new Vec4[8];
        lightColor = new Vec4[8];

        lightCount = 0;
    }

    public void update(Mat4 view, Vec4[] sceneLightPos) {

        for (int i = 0; i < lightCount; i++) {

            lightPos[i] = view.mult(sceneLightPos[i]);
        }
    }

    public int getLightCount() {
        return lightCount;
    }

    public void setAmbient(Vec4 ambient) {
        this.ambient = ambient;
    }

    public Vec4[] getLightColor() {
        return lightColor;
    }

    public void setLightCount(int lightCount) {
        this.lightCount = lightCount;
    }

    public Vec4 getAmbient() {
        return ambient;
    }

    public Vec4[] getLightPos() {
        return lightPos;
    }
}
