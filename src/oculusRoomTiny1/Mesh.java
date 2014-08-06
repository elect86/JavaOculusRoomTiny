/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oculusRoomTiny1;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Mesh {
    
    private int[] VBO;
    private int[] VAO;
    private float[] verticesAttributes;
    
    public Mesh (GL3 gl3){
        
        initializeVBO(gl3);
        
        initializeVAO(gl3);
    }
    
    private void initializeVBO(GL3 gl3) {

        VBO = new int[1];

        gl3.glGenBuffers(1, IntBuffer.wrap(VBO));

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, VBO[0]);
        {
            FloatBuffer floatBuffer = GLBuffers.newDirectFloatBuffer(verticesAttributes);

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, verticesAttributes.length * 4, floatBuffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    private void initializeVAO(GL3 gl3) {

        VAO = new int[1];
        //  3 components * 2 attributes * 4 Bytes/Float
        int stride = 3 * 2 * 4;

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, VBO[0]);

        gl3.glGenVertexArrays(1, IntBuffer.wrap(VAO));

        gl3.glBindVertexArray(VAO[0]);
        {
            gl3.glEnableVertexAttribArray(0);
            gl3.glEnableVertexAttribArray(1);
            {
                gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, stride, 0);
                gl3.glVertexAttribPointer(1, 3, GL3.GL_FLOAT, false, stride, 3 * 4);
            }
        }
        gl3.glBindVertexArray(0);
    }
}
