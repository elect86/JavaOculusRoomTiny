/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1.entities;

import jglm.Vec3;
import jglm.Vec3i;

/**
 *
 * @author gbarbieri
 */
public class Slab {

    private Vec3 p1;
    private Vec3 p2;
    private Vec3i color;

    public Slab(Vec3 p1, Vec3 p2, Vec3i color) {

        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
    }

    public Vec3 getP1() {
        return p1;
    }

    public Vec3 getP2() {
        return p2;
    }

    public Vec3i getColor() {
        return color;
    }
}
