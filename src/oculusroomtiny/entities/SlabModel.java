/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.entities;

import oculusroomtiny.rendering.OculusRoomModel;

/**
 *
 * @author gbarbieri
 */
public class SlabModel {

    private int count;
    private Slab[] slabs;
    private OculusRoomModel.BuiltinTexture tex;

    public SlabModel(int count, Slab[] slabs, OculusRoomModel.BuiltinTexture tex) {

        this.count = count;
        this.slabs = slabs;
        this.tex = tex;
    }

    public int getCount() {
        return count;
    }

    public Slab[] getSlabs() {
        return slabs;
    }

    public OculusRoomModel.BuiltinTexture getTex() {
        return tex;
    }
}
