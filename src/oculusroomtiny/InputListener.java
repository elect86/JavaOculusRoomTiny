/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny;

import oculusroomtiny.rendering.GlViewer;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

/**
 *
 * @author gbarbieri
 */
public class InputListener implements KeyListener {

    private GlViewer glViewer;

    public InputListener(GlViewer glViewer) {

        this.glViewer = glViewer;
    }

    @Override
    public void keyPressed(KeyEvent ke) {

        if (ke.getKeyCode() == KeyEvent.VK_F5) {

            glViewer.toggleFullscreen();
        }
        if (ke.getKeyCode() == KeyEvent.VK_F1) {

            glViewer.setStereoMode(GlViewer.StereoMode.none);
        }
        if (ke.getKeyCode() == KeyEvent.VK_F2) {

            glViewer.setStereoMode(GlViewer.StereoMode.leftRight_multipass);
        }
//        if (ke.getKeyCode() == KeyEvent.VK_F2) {
//
//            glViewer.toggleFullscreen();
//        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

}
