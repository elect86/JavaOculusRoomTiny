/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.rendering.glsl;

import javax.media.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Distortion extends glsl.GLSLProgramObject {

    private int eyeToSourceUVscaleUL;
    private int eyeToSourceUVoffsetUL;
    private int eyeRotationStartUL;
    private int eyeRotationEndUL;
    private int texture0UL;

    public Distortion(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {

        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        eyeToSourceUVscaleUL = gl3.glGetUniformLocation(getProgramId(), "eyeToSourceUVscale");
        eyeToSourceUVoffsetUL = gl3.glGetUniformLocation(getProgramId(), "eyeToSourceUVoffset");
        eyeRotationStartUL = gl3.glGetUniformLocation(getProgramId(), "eyeRotationStart");
        eyeRotationEndUL = gl3.glGetUniformLocation(getProgramId(), "eyeRotationEnd");

        texture0UL = gl3.glGetUniformLocation(getProgramId(), "texture0");
    }

    public int getEyeToSourceUVscaleUL() {
        return eyeToSourceUVscaleUL;
    }

    public int getEyeToSourceUVoffsetUL() {
        return eyeToSourceUVoffsetUL;
    }

    public int getEyeRotationStartUL() {
        return eyeRotationStartUL;
    }

    public int getEyeRotationEndUL() {
        return eyeRotationEndUL;
    }

    public int getTexture0UL() {
        return texture0UL;
    }
}
