/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.core;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import roomTinySimplified.entities.Scene;
import roomTinySimplified.rendering.GlViewer;

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
    public int frame = 0;

    public OculusRoomTiny() {

        scene = new Scene();
    }

    public void init() {

        glViewer = new GlViewer();

        setSize(1280 + (1280 - 1264), 800 + (800 - 762));
//        setSize(2300, 1600);
//        this.setUndecorated(true);
        
        
//        JPanel p1 = new JPanel();
//        JPanel p2 = new JPanel();
//        p1.setLayout(new BorderLayout());
//        p2.setLayout(new BorderLayout());
//        
//        this.getContentPane().setLayout(new BorderLayout());
//        this.getContentPane().add(p1,BorderLayout.CENTER);
//        p1.add(p2,BorderLayout.CENTER);
//        
//        p2.add(glViewer.getNewtCanvasAWT(),BorderLayout.CENTER);
        
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
