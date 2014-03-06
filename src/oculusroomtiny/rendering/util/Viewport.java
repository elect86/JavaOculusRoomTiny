/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.rendering.util;

/**
 *
 * @author gbarbieri
 */
public class Viewport {

    public int x, y;
    public int w, h;

    public Viewport(int x, int y, int w, int h) {

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
