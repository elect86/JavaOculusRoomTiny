/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.entities;

import roomTinySimplified.rendering.Texture;


/**
 *
 * @author gbarbieri
 */
public class SlabModel {

    private int count;
    private Slab[] slabs;
    private Texture.BuiltinTexture tex;

    public SlabModel(int count, Slab[] slabs, Texture.BuiltinTexture tex) {

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

    public Texture.BuiltinTexture getTex() {
        return tex;
    }
}
