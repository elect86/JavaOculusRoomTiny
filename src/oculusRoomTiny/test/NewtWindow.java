package oculusRoomTiny.test;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;


public class NewtWindow {

        public static void main(String... args) {
                NewtWindow app = new NewtWindow();
                app.run();
        }

        private void run() {
                final GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL4));
                caps.setBackgroundOpaque(true);
                caps.setDoubleBuffered(true);
                GLWindow glWindow = GLWindow.create(caps);
                glWindow.setSize(1024, 768);
                glWindow.setUndecorated(false);
                glWindow.setPointerVisible(true);
                glWindow.setVisible(true);
                glWindow.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

                final Animator animator = new Animator(glWindow);
                glWindow.addGLEventListener(new GLEventListener() {
                        @Override
                        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                                System.out.println("Reshape <" + width + "," + height + ">");
                        }

                        @Override
                        public void init(GLAutoDrawable drawable) {
                                System.out.println("Init");
                        }

                        @Override
                        public void dispose(GLAutoDrawable drawable) {
                                animator.stop() ;
                                System.exit(0);
                                System.out.println("Dispose");
                        }

                        @Override
                        public void display(GLAutoDrawable drawable) {
                                System.out.println("Display");
                        }
                });

                animator.start();

        }
} 