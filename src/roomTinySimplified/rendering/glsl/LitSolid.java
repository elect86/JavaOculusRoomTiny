/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.rendering.glsl;

import com.jogamp.opengl.GL3;
import roomTinySimplified.core.OculusRoomTiny;

/**
 *
 * @author gbarbieri
 */
public class LitSolid extends glsl.GLSLProgramObject {

    private int projectionUL;
    private int viewUL;

    public LitSolid(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {

        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        projectionUL = gl3.glGetUniformLocation(getProgramId(), "projection");

        viewUL = gl3.glGetUniformLocation(getProgramId(), "view");

        int lightingUBI = gl3.glGetUniformBlockIndex(getProgramId(), "lighting");
        gl3.glUniformBlockBinding(getProgramId(), lightingUBI, OculusRoomTiny.getInstance().getGlViewer().getLightingUBB());
    }

    public int getProjectionUL() {
        return projectionUL;
    }

    public int getViewUL() {
        return viewUL;
    }
}
