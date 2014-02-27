/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import oculusroomtiny.rendering.GlViewer;
import oculusroomtiny.rendering.OculusRoomModel;
import oculusroomtiny.entities.Scene;

/**
 *
 * @author gbarbieri
 */
public class OculusRoomTiny extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        oculusRoomTiny = new OculusRoomTiny();

        oculusRoomTiny.init();

//        while(oculusRoomTiny.getGlViewer().getGlWindow().isRealized()){
//            
//        }
    }

    private static OculusRoomTiny oculusRoomTiny;
    private GlViewer glViewer;
    private Scene scene;

    public OculusRoomTiny() {

        scene = new Scene();

        OculusRoomModel.populateRoomScene(scene);
    }

    public void init() {

        glViewer = new GlViewer();

        setSize(1280 + (1280 - 1264), 800 + (800 - 762));
        add(glViewer.getNewtCanvasAWT());

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                glViewer.getAnimator().stop();
                remove(glViewer.getNewtCanvasAWT());
                glViewer.getGlWindow().destroy();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static OculusRoomTiny getInstance() {
        return oculusRoomTiny;
    }

    public GlViewer getGlViewer() {
        return glViewer;
    }

    public Scene getScene() {
        return scene;
    }
}
