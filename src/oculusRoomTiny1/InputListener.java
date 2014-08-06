/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusRoomTiny1;

import oculusRoomTiny1.rendering.GlViewer;
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
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }
}
