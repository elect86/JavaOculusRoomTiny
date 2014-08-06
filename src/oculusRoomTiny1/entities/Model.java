/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1.entities;

import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.media.opengl.GL3;
import jglm.Mat4;
import jglm.Vec3;
import jglm.Vec3i;
import oculusRoomTiny1.core.OculusRoomTiny;
import oculusRoomTiny1.rendering.OculusRoomModel;
import oculusRoomTiny1.rendering.Texture.BuiltinTexture;
import oculusRoomTiny1.rendering.glsl.FillCollection;
import oculusRoomTiny1.rendering.glsl.Program;
import oculusRoomTiny1.rendering.glsl.Program1;
import oculusRoomTiny1.rendering.glsl.ShaderFill;
import oculusRoomTiny1.rendering.glsl.LitTexturesProgram;

/**
 *
 * @author gbarbieri
 */
public class Model extends Node {

    private int primitiveType;
    private Vec3 position;
    private ArrayList<Vertex> vertices;
    private ArrayList<Integer> indices;
    private int[] vbo;
    private int[] ibo;
//    private Program program;
//    private Program1 program1;
    public BuiltinTexture builtinTexture;
    private Texture texture;
    private int[] tex;
//    private boolean test = true;
    private ShaderFill fill;

    public Model(int primitiveType) {

        this.primitiveType = primitiveType;

        vertices = new ArrayList<>();
        indices = new ArrayList<>();
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setFill(ShaderFill fill) {
        this.fill = fill;
    }

    public void addSolidColorBox(Vec3 p1, Vec3 p2, Vec3i c) {

        float t;

        if (p1.x > p2.x) {

            t = p1.x;
            p1.x = p2.x;
            p2.x = t;
        }
        if (p1.y > p2.y) {

            t = p1.y;
            p1.y = p2.y;
            p2.y = t;
        }
        if (p1.z > p2.z) {

            t = p1.z;
            p1.z = p2.z;
            p2.z = t;
        }
        Vec3[][] cubeVertices = new Vec3[][]{
            {new Vec3(p1.x, p2.y, p1.z), new Vec3(p1.z, p1.x, 0), new Vec3(0.0f, 1.0f, 0.0f)},
            {new Vec3(p2.x, p2.y, p1.z), new Vec3(p1.z, p2.x, 0), new Vec3(0.0f, 1.0f, 0.0f)},
            {new Vec3(p2.x, p2.y, p2.z), new Vec3(p2.z, p2.x, 0), new Vec3(0.0f, 1.0f, 0.0f)},
            {new Vec3(p1.x, p2.y, p2.z), new Vec3(p2.z, p1.x, 0), new Vec3(0.0f, 1.0f, 0.0f)},
            //
            {new Vec3(p1.x, p1.y, p1.z), new Vec3(p1.z, p1.x, 0), new Vec3(0.0f, -1.0f, 0.0f)},
            {new Vec3(p2.x, p1.y, p1.z), new Vec3(p1.z, p2.x, 0), new Vec3(0.0f, -1.0f, 0.0f)},
            {new Vec3(p2.x, p1.y, p2.z), new Vec3(p2.z, p2.x, 0), new Vec3(0.0f, -1.0f, 0.0f)},
            {new Vec3(p1.x, p1.y, p2.z), new Vec3(p2.z, p1.x, 0), new Vec3(0.0f, -1.0f, 0.0f)},
            //
            {new Vec3(p1.x, p1.y, p2.z), new Vec3(p2.z, p1.y, 0), new Vec3(-1.0f, 0.0f, 0.0f)},
            {new Vec3(p1.x, p1.y, p1.z), new Vec3(p1.z, p1.y, 0), new Vec3(-1.0f, 0.0f, 0.0f)},
            {new Vec3(p1.x, p2.y, p1.z), new Vec3(p1.z, p2.y, 0), new Vec3(-1.0f, 0.0f, 0.0f)},
            {new Vec3(p1.x, p2.y, p2.z), new Vec3(p2.z, p2.y, 0), new Vec3(-1.0f, 0.0f, 0.0f)},
            //
            {new Vec3(p2.x, p1.y, p2.z), new Vec3(p2.z, p1.y, 0), new Vec3(1.0f, 0.0f, 0.0f)},
            {new Vec3(p2.x, p1.y, p1.z), new Vec3(p1.z, p1.y, 0), new Vec3(1.0f, 0.0f, 0.0f)},
            {new Vec3(p2.x, p2.y, p1.z), new Vec3(p1.z, p2.y, 0), new Vec3(1.0f, 0.0f, 0.0f)},
            {new Vec3(p2.x, p2.y, p2.z), new Vec3(p2.z, p2.y, 0), new Vec3(1.0f, 0.0f, 0.0f)},
            //
            {new Vec3(p1.x, p1.y, p1.z), new Vec3(p1.x, p1.y, 0), new Vec3(0.0f, 0.0f, -1.0f)},
            {new Vec3(p2.x, p1.y, p1.z), new Vec3(p2.x, p1.y, 0), new Vec3(0.0f, 0.0f, -1.0f)},
            {new Vec3(p2.x, p2.y, p1.z), new Vec3(p2.x, p2.y, 0), new Vec3(0.0f, 0.0f, -1.0f)},
            {new Vec3(p1.x, p2.y, p1.z), new Vec3(p1.x, p2.y, 0), new Vec3(0.0f, 0.0f, -1.0f)},
            //
            {new Vec3(p1.x, p1.y, p2.z), new Vec3(p1.x, p1.y, 0), new Vec3(0.0f, 0.0f, 1.0f)},
            {new Vec3(p2.x, p1.y, p2.z), new Vec3(p2.x, p1.y, 0), new Vec3(0.0f, 0.0f, 1.0f)},
            {new Vec3(p2.x, p2.y, p2.z), new Vec3(p2.x, p2.y, 0), new Vec3(0.0f, 0.0f, 1.0f)},
            {new Vec3(p1.x, p2.y, p2.z), new Vec3(p1.x, p2.y, 0), new Vec3(0.0f, 0.0f, 1.0f)}
        };

        int startIndex = vertices.size();

        int cubeVertexCount = cubeVertices.length;
        int cubeIndexCount = cubeIndices.length;

        for (int v = 0; v < cubeVertexCount; v++) {

            vertices.add(new Vertex(cubeVertices[v][0], c, cubeVertices[v][1].x, cubeVertices[v][1].y, cubeVertices[v][2]));
//            System.out.println("Vertex pos(" + cubeVertices[v][0].x + ", " + cubeVertices[v][0].y + ", " + cubeVertices[v][0].z + "), "
//                    + " c(" + c.x + ", " + c.y + ", " + c.z + "), u " + cubeVertices[v][1].x + ", v " + cubeVertices[v][1].y
//                    + ", norm(" + cubeVertices[v][2].x + ", " + cubeVertices[v][2].y + ", " + cubeVertices[v][2].z + ")");
        }

        for (int i = 0; i < cubeIndexCount / 3; i++) {

            indices.add(cubeIndices[i * 3] + startIndex);
            indices.add(cubeIndices[i * 3 + 1] + startIndex);
            indices.add(cubeIndices[i * 3 + 2] + startIndex);

//            System.out.println("Triangle " + (cubeIndices[i * 3] + startIndex)
//                    + " " + (cubeIndices[i * 3 + 1] + startIndex)
//                    + " " + (cubeIndices[i * 3 + 2] + startIndex));
        }
    }

    @Override
    public void render(GL3 gl3, Mat4 projection, Mat4 view) {

        if (vbo == null) {

            initVBO(gl3);
        }
        if (ibo == null) {

            initIBO(gl3);
        }
        if (fill.getShader() instanceof LitTexturesProgram) {

//            if (OculusRoomTiny.getInstance().frame % 2 == 1) {
            renderLight(gl3, projection, view);
//            }
        }
        OculusRoomTiny.getInstance().frame++;
    }

    public static Model createModel(Vec3 position, SlabModel slabModel, FillCollection fillCollection) {

        Model model = new Model(GL3.GL_TRIANGLES);

        model.setPosition(position);

        for (int i = 0; i < slabModel.getCount(); i++) {

            Slab slab = slabModel.getSlabs()[i];

            model.addSolidColorBox(slab.getP1(), slab.getP2(), slab.getColor());
        }
        model.builtinTexture = slabModel.getTex();

        if (slabModel.getTex().ordinal() > 0) {

            model.setFill(fillCollection.getLitTextures()[slabModel.getTex().ordinal()]);
        }

        return model;
    }

    private void renderLit(GL3 gl3, Mat4 projection, Mat4 view) {

//        program.bind(gl3);
//        {
//            if (test) {
////                test = false;
//                gl3.glUniformMatrix4fv(program.getProjectionUL(), 1, false, projection.toFloatArray(), 0);
//                gl3.glUniformMatrix4fv(program.getViewUL(), 1, false, view.toFloatArray(), 0);
////                projection.print("projection");
////                view.print("view");
//            }
//
//            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
//            {
//                for (int i = 0; i < 4; i++) {
//
//                    gl3.glEnableVertexAttribArray(i);
//                }
//                gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 0);
//                gl3.glVertexAttribPointer(1, 4, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 3 * 4);
//                gl3.glVertexAttribPointer(2, 2, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 7 * 4);
//                gl3.glVertexAttribPointer(3, 3, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 9 * 4);
//
//                if (indices != null) {
//
//                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
//                    {
//                        gl3.glEnable(GL3.GL_TEXTURE_2D);
//                        {
//                            gl3.glActiveTexture(GL3.GL_TEXTURE0);
//                            gl3.glUniform1i(program.getTexture0UL(), 0);
//                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, tex[0]);
//                            {
////                                System.out.println("render");
//                                gl3.glDrawElements(primitiveType, indices.size(), GL3.GL_UNSIGNED_INT, ibo[0]);
//                            }
//                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, 0);
//                        }
//                        gl3.glDisable(GL3.GL_TEXTURE_2D);
//                    }
//                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
//                }
//
//                for (int i = 0; i < 4; i++) {
//
//                    gl3.glDisableVertexAttribArray(i);
//                }
//            }
//            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
//        }
//        program.unbind(gl3);
    }

    private void renderLight(GL3 gl3, Mat4 projection, Mat4 view) {

        LitTexturesProgram program = (LitTexturesProgram) fill.getShader();

        program.bind(gl3);
        {
//            if (test) {
//                test = false;
            gl3.glUniformMatrix4fv(program.getProjectionUL(), 1, false, projection.toFloatArray(), 0);
            gl3.glUniformMatrix4fv(program.getViewUL(), 1, false, view.toFloatArray(), 0);
//                projection.print("projection");
//                view.print("view");
//            }

            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
            {
                for (int i = 0; i < 4; i++) {

                    gl3.glEnableVertexAttribArray(i);
                }
                gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 0);
                gl3.glVertexAttribPointer(1, 4, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 3 * 4);
                gl3.glVertexAttribPointer(2, 2, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 7 * 4);
                gl3.glVertexAttribPointer(3, 3, GL3.GL_FLOAT, false, (3 + 4 + 2 + 3) * 4, 9 * 4);

                if (indices != null) {

                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                    {
                        gl3.glEnable(GL3.GL_TEXTURE_2D);
                        {
                            gl3.glActiveTexture(GL3.GL_TEXTURE0);
                            gl3.glUniform1i(program.getTexture0UL(), 0);
//                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, tex[0]);
                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, fill.getTexture().getId());
                            {
//                                System.out.println("render");
                                gl3.glDrawElements(primitiveType, indices.size(), GL3.GL_UNSIGNED_INT, ibo[0]);
                            }
                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, 0);
                        }
                        gl3.glDisable(GL3.GL_TEXTURE_2D);
                    }
                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
                }

                for (int i = 0; i < 4; i++) {

                    gl3.glDisableVertexAttribArray(i);
                }
            }
            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        }
        program.unbind(gl3);
    }

    private void initVBO(GL3 gl3) {

        vbo = new int[1];
        gl3.glGenBuffers(1, vbo, 0);
//        System.out.println("vbo "+vbo[0]);
//        if (OculusRoomTiny.getInstance().frame % 2 == 1) {
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        {
            float[] buffer = new float[vertices.size() * (3 + 4 + 2 + 3)];

            for (int i = 0; i < vertices.size(); i++) {
                // Position
                buffer[i * (3 + 4 + 2 + 3)] = vertices.get(i).getPos().x;
                buffer[i * (3 + 4 + 2 + 3) + 1] = vertices.get(i).getPos().y;
                buffer[i * (3 + 4 + 2 + 3) + 2] = vertices.get(i).getPos().z;
                // Color
                buffer[i * (3 + 4 + 2 + 3) + 3] = (float) vertices.get(i).getC().x;
                buffer[i * (3 + 4 + 2 + 3) + 4] = (float) vertices.get(i).getC().y;
                buffer[i * (3 + 4 + 2 + 3) + 5] = (float) vertices.get(i).getC().z;
//                buffer[i * (3 + 4 + 2 + 3) + 3] = 1;
//                buffer[i * (3 + 4 + 2 + 3) + 4] = 1;
//                buffer[i * (3 + 4 + 2 + 3) + 5] = 1;
                buffer[i * (3 + 4 + 2 + 3) + 6] = 1;
//                System.out.println(""+(float) vertices.get(i).getC().x / 255f);
//                System.out.println(""+(float) vertices.get(i).getC().y / 255f);
//                System.out.println(""+(float) vertices.get(i).getC().z / 255f);
                // UV
                buffer[i * (3 + 4 + 2 + 3) + 7] = vertices.get(i).getU();
                buffer[i * (3 + 4 + 2 + 3) + 8] = vertices.get(i).getV();
                // Normal
                buffer[i * (3 + 4 + 2 + 3) + 9] = vertices.get(i).getNorm().x;
                buffer[i * (3 + 4 + 2 + 3) + 10] = vertices.get(i).getNorm().y;
                buffer[i * (3 + 4 + 2 + 3) + 11] = vertices.get(i).getNorm().z;
            }

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, buffer.length * 4, GLBuffers.newDirectFloatBuffer(buffer), GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
//        }
    }

    private void initIBO(GL3 gl3) {

        ibo = new int[1];

        gl3.glGenBuffers(1, ibo, 0);

        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
        {
            gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, cubeIndices.length * 4, GLBuffers.newDirectIntBuffer(cubeIndices), GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private static int[] cubeIndices = new int[]{
        0, 1, 3,
        3, 1, 2,
        5, 4, 6,
        6, 4, 7,
        8, 9, 11,
        11, 9, 10,
        13, 12, 14,
        14, 12, 15,
        16, 17, 19,
        19, 17, 18,
        21, 20, 22,
        22, 20, 23
    };
}
