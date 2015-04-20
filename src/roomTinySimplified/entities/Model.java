/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.entities;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import java.util.ArrayList;
import jglm.Mat4;
import jglm.Vec3;
import jglm.Vec3i;
import roomTinySimplified.core.OculusRoomTiny;
import roomTinySimplified.rendering.Texture;
import roomTinySimplified.rendering.Texture.BuiltinTexture;
import static roomTinySimplified.rendering.Texture.BuiltinTexture.tex_none;
import roomTinySimplified.rendering.glsl.LitSolid;
import roomTinySimplified.rendering.glsl.LitTexture;

/**
 *
 * @author gbarbieri
 */
public final class Model {

    private Vec3 position;
    private ArrayList<Vertex> vertices;
    private ArrayList<Integer> indices;
    private int[] vbo;
    private int[] ibo;
    public BuiltinTexture builtinTexture;
    private Texture texture;

    public Model(Vec3 position, SlabModel slabModel) {

        vertices = new ArrayList<>();
        indices = new ArrayList<>();

        this.position = position;

        for (int i = 0; i < slabModel.getCount(); i++) {

            Slab slab = slabModel.getSlabs()[i];

            addSolidColorBox(slab.getP1(), slab.getP2(), slab.getColor());
        }
        builtinTexture = slabModel.getTex();
    }

    private void addSolidColorBox(Vec3 p1, Vec3 p2, Vec3i c) {

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
//        System.out.println("startIndex " + startIndex);
        for (int i = 0; i < cubeIndexCount / 3; i++) {

            indices.add(cubeIndices[i * 3 + 0] + startIndex);
            indices.add(cubeIndices[i * 3 + 1] + startIndex);
            indices.add(cubeIndices[i * 3 + 2] + startIndex);

//            System.out.println("Triangle " + (cubeIndices[i * 3] + startIndex)
//                    + " " + (cubeIndices[i * 3 + 1] + startIndex)
//                    + " " + (cubeIndices[i * 3 + 2] + startIndex));
        }
    }

    public void render(GL3 gl3, Mat4 projection, Mat4 view) {

        view = view.mult(Mat4.translate(position));

        if (vbo == null) {

            initVBO(gl3);
        }
        if (ibo == null) {

            initIBO(gl3);
        }
        if (builtinTexture == BuiltinTexture.tex_none) {

            renderLitSolid(gl3, projection, view);

        } else {

            if (texture == null) {

                texture = Texture.createBuiltinTexture(gl3, builtinTexture);
            }
            renderLitTexture(gl3, projection, view);
        }
    }

    private void renderLitSolid(GL3 gl3, Mat4 projection, Mat4 view) {

        LitSolid program = OculusRoomTiny.getInstance().getGlViewer().getLitSolid();

        program.bind(gl3);
        {
            gl3.glUniformMatrix4fv(program.getProjectionUL(), 1, false, projection.toFloatArray(), 0);
            gl3.glUniformMatrix4fv(program.getViewUL(), 1, false, view.toFloatArray(), 0);
//                projection.print("projection");
//                view.print("view");

            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
            {
                for (int i = 0; i < 3; i++) {

                    gl3.glEnableVertexAttribArray(i);
                }
                gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, (3 + 4 + 3) * 4, 0);
                gl3.glVertexAttribPointer(1, 4, GL3.GL_FLOAT, false, (3 + 4 + 3) * 4, 3 * 4);
                gl3.glVertexAttribPointer(2, 3, GL3.GL_FLOAT, false, (3 + 4 + 3) * 4, 7 * 4);

                if (indices != null) {

                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);

                    {
//                        System.out.println("render");
                        gl3.glDrawElements(GL3.GL_TRIANGLES, indices.size(), GL3.GL_UNSIGNED_INT, 0);
                    }
                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
                }
                for (int i = 0; i < 3; i++) {

                    gl3.glDisableVertexAttribArray(i);
                }
            }
            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        }
        program.unbind(gl3);
    }

    private void renderLitTexture(GL3 gl3, Mat4 projection, Mat4 view) {

        LitTexture program = OculusRoomTiny.getInstance().getGlViewer().getLitTexture();

        program.bind(gl3);
        {
            gl3.glUniformMatrix4fv(program.getProjectionUL(), 1, false, projection.toFloatArray(), 0);
            gl3.glUniformMatrix4fv(program.getViewUL(), 1, false, view.toFloatArray(), 0);
//                projection.print("projection");
//                view.print("view");

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
                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, texture.getId());
                            {
                                gl3.glDrawElements(GL3.GL_TRIANGLES, indices.size(), GL3.GL_UNSIGNED_INT, 0);
                            }
//                            gl3.glBindTexture(GL3.GL_TEXTURE_2D, 0);
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
//        System.out.println("vbo " + vbo[0]);
//        if (OculusRoomTiny.getInstance().frame % 2 == 1) {
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        {
            float[] buffer;

            if (builtinTexture == tex_none) {

                int vertexSize = 3 + 4 + 3;

                buffer = new float[vertices.size() * vertexSize];

                float factor = 255;

                for (int i = 0; i < vertices.size(); i++) {
                    // Position
                    buffer[i * vertexSize + 0] = vertices.get(i).getPos().x;
                    buffer[i * vertexSize + 1] = vertices.get(i).getPos().y;
                    buffer[i * vertexSize + 2] = vertices.get(i).getPos().z;
                    // Color
                    buffer[i * vertexSize + 3] = (float) vertices.get(i).getC().x / factor;
                    buffer[i * vertexSize + 4] = (float) vertices.get(i).getC().y / factor;
                    buffer[i * vertexSize + 5] = (float) vertices.get(i).getC().z / factor;
                    buffer[i * vertexSize + 6] = 1;
                    // Normal
                    buffer[i * vertexSize + 7] = vertices.get(i).getNorm().x;
                    buffer[i * vertexSize + 8] = vertices.get(i).getNorm().y;
                    buffer[i * vertexSize + 9] = vertices.get(i).getNorm().z;

//                    System.out.println("vertex " + i + " Position " + vertices.get(i).getPos().x + " " + vertices.get(i).getPos().y + " " + vertices.get(i).getPos().z
//                            + " Color " + vertices.get(i).getC().x + " " + vertices.get(i).getC().y + " " + vertices.get(i).getC().z
//                            + " normal " + vertices.get(i).getNorm().x + " " + vertices.get(i).getNorm().y + " " + vertices.get(i).getNorm().z);
                }
            } else {

                int vertexSize = 3 + 4 + 2 + 3;

                buffer = new float[vertices.size() * vertexSize];

                float factor = 255;

                for (int i = 0; i < vertices.size(); i++) {
                    // Position
                    buffer[i * vertexSize + 0] = vertices.get(i).getPos().x;
                    buffer[i * vertexSize + 1] = vertices.get(i).getPos().y;
                    buffer[i * vertexSize + 2] = vertices.get(i).getPos().z;
                    // Color
                    buffer[i * vertexSize + 3] = (float) vertices.get(i).getC().x / factor;
                    buffer[i * vertexSize + 4] = (float) vertices.get(i).getC().y / factor;
                    buffer[i * vertexSize + 5] = (float) vertices.get(i).getC().z / factor;
//                buffer[i * (3 + 4 + 2 + 3) + 3] = 1;
//                buffer[i * (3 + 4 + 2 + 3) + 4] = 1;
//                buffer[i * (3 + 4 + 2 + 3) + 5] = 1;
                    buffer[i * vertexSize + 6] = 1;
//                System.out.println(""+(float) vertices.get(i).getC().x / 255f);
//                System.out.println(""+(float) vertices.get(i).getC().y / 255f);
//                System.out.println(""+(float) vertices.get(i).getC().z / 255f);
                    // UV
                    buffer[i * vertexSize + 7] = vertices.get(i).getU();
                    buffer[i * vertexSize + 8] = vertices.get(i).getV();
                    // Normal
                    buffer[i * vertexSize + 9] = vertices.get(i).getNorm().x;
                    buffer[i * vertexSize + 10] = vertices.get(i).getNorm().y;
                    buffer[i * vertexSize + 11] = vertices.get(i).getNorm().z;

//                    System.out.println("vertex " + i + " Position " + vertices.get(i).getPos().x + " " + vertices.get(i).getPos().y + " " + vertices.get(i).getPos().z
//                            + " Color " + vertices.get(i).getC().x + " " + vertices.get(i).getC().y + " " + vertices.get(i).getC().z + " u " + vertices.get(i).getU()
//                            + " v " + vertices.get(i).getV() + " normal " + vertices.get(i).getNorm().x + " " + vertices.get(i).getNorm().y + " " + vertices.get(i).getNorm().z);
                }
            }
            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, buffer.length * 4, GLBuffers.newDirectFloatBuffer(buffer), GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
//        }
    }

    private void initIBO(GL3 gl3) {

        ibo = new int[1];
        gl3.glGenBuffers(1, ibo, 0);
//        System.out.println("ibo " + ibo[0]);

        int[] buffer = new int[indices.size()];

        for (int i = 0; i < indices.size(); i++) {

            buffer[i] = indices.get(i);
        }

        for (int i = 0; i < indices.size() / 3; i++) {

//            System.out.println("Triangle " + buffer[i * 3 + 0] + " " + buffer[i * 3 + 1] + " " + buffer[i * 3 + 2]);
        }

        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
        {
            gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, buffer.length * 4,
                    GLBuffers.newDirectIntBuffer(buffer), GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private static final int[] cubeIndices = new int[]{
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
