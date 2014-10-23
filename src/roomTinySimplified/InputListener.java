/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec2i;
import jglm.Vec3;
import roomTinySimplified.rendering.GlViewer;

/**
 *
 * @author gbarbieri
 */
public class InputListener implements KeyListener, MouseListener {

    private GlViewer glViewer;
    private float yaw;
    private float sensitivity;
    private Vec2i startingPoint;
    private Move move;

    public InputListener(GlViewer glViewer) {

        this.glViewer = glViewer;

        yaw = 0f;

        sensitivity = 1f;

        move = Move.undefined;
    }

    @Override
    public void keyPressed(KeyEvent ke) {

        switch (ke.getKeyCode()) {

            case KeyEvent.VK_F5:
                glViewer.toggleFullscreen();
                break;
                
            case KeyEvent.VK_W:
                glViewer.move(new Vec3(0, 0f, .5f));
                break;
                
            case KeyEvent.VK_S:
                glViewer.move(new Vec3(0, 0f, -.5f));
                break;
                
            case KeyEvent.VK_A:
                glViewer.move(new Vec3(.5f, 0f, 0f));
                break;
                
            case KeyEvent.VK_D:
                glViewer.move(new Vec3(-.5f, 0f, 0f));
                break;
        }
    }

    public float retrieveMouseYaw() {

        float tempYaw = yaw;

        yaw = 0f;

        return tempYaw;
    }

    public Vec3 updateEyePos(Vec3 eyePos, float headYaw, Quat poseOrientation) {

        Vec3 localMoveVector = new Vec3();
        Mat4 yawRotation = Mat4.rotationY(headYaw);

        switch (move) {

            case forward:

                localMoveVector.plus(new Vec3(0f, 0f, -1f));

                break;

            case back:

                localMoveVector.plus(new Vec3(0f, 0f, 1f));

                break;

            case left:

                localMoveVector.plus(new Vec3(1f, 0f, 0f));

                break;

            case right:

                localMoveVector.plus(new Vec3(-1f, 0f, 0f));

                break;
        }
        return localMoveVector;
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseMoved(MouseEvent me) {
//        System.out.println("mouseMoved");
        if (startingPoint == null) {

            startingPoint = new Vec2i(me.getX(), me.getY());

        } else {

            float x = me.getX() - startingPoint.x;

            yaw -= (sensitivity * x) / 360f;

            startingPoint = new Vec2i(me.getX(), me.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent me) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent me) {

    }

    private enum Move {

        forward,
        back,
        left,
        right,
        undefined
    }
}
