/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.rendering.glsl;

import glsl.GLSLProgramObject;
import oculusroomtiny.rendering.Texture;

/**
 *
 * @author gbarbieri
 */
public class ShaderFill {

    private GLSLProgramObject shader;
    private Texture texture;

    public ShaderFill(GLSLProgramObject shader, Texture texture) {

        this.shader = shader;
        this.texture = texture;
    }
}
