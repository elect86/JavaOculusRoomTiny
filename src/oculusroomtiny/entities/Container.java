/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.entities;

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
        
        for(Node node : nodes){
            
            node.render(gl3, projection, view);
        }
    }
}
