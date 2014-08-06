/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1.entities;

import jglm.Vec3;
import jglm.Vec3i;
import jglm.Vec4;

/**
 *
 * @author gbarbieri
 */
public class Vertex {

    private Vec3 pos;
    private Vec3i c;
    private float u;
    private float v;
    private Vec3 norm;

    public Vertex(Vec3 pos, Vec3i c, float u, float v, Vec3 norm) {

        this.pos = pos;
        this.c = c;
        this.u = u;
        this.v = v;
        this.norm = norm;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vec3i getC() {
        return c;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public Vec3 getNorm() {
        return norm;
    }
}
