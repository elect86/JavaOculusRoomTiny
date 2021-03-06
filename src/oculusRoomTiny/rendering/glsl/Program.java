/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny.rendering.glsl;

import com.jogamp.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Program extends glsl.GLSLProgramObject {

    private int projectionUL;
    private int viewUL;
    private int texture0UL;

    public Program(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        projectionUL = gl3.glGetUniformLocation(getProgramId(), "projection");

        viewUL = gl3.glGetUniformLocation(getProgramId(), "view");
        
        texture0UL = gl3.glGetUniformLocation(getProgramId(), "texture0");
    }

    public int getProjectionUL() {  
        return projectionUL;
    }

    public int getViewUL() {
        return viewUL;
    }

    public int getTexture0UL() {
        return texture0UL;
    }
}
