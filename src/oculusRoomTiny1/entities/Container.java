/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1.entities;

import java.util.ArrayList;
import javax.media.opengl.GL3;
import jglm.Mat4;

/**
 *
 * @author gbarbieri
 */
public class Container extends Node {

    private ArrayList<Node> nodes;
            
    public Container() {

        nodes = new ArrayList<>();
    }
    
    public void add(Node node){
        
        nodes.add(node);
    }
    
    @Override
    public void render(GL3 gl3, Mat4 projection, Mat4 view){
//        System.out.println("render");
        for(Node node : nodes){
            
            node.render(gl3, projection, view);
        }
//        nodes.get(0).render(gl3, projection, view);
    }
}
