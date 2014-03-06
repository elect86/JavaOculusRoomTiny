/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.rendering;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import jglm.Jglm;
import static jglm.Jglm.calculateFrustumScale;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec3;
import oculusroomtiny.InputListener;
import oculusroomtiny.core.OculusRoomTiny;
import oculusroomtiny.entities.Scene;
import org.saintandreas.math.Quaternion;
import org.saintandreas.oculus.RiftTracker;
import org.saintandreas.oculus.fusion.SensorFusion;

/**
 *
 * @author gbarbieri
 */
public class GlViewer implements GLEventListener {

    private GLWindow glWindow;
    private NewtCanvasAWT newtCanvasAWT;
    private boolean fullscreen;
    private InputListener inputListener;
    private Animator animator;
    private SensorFusion sensorFusion;
    private Vec3 upVector;
    private Vec3 forwardVector;
    private Vec3 rightVector;
    private float yawInitial;
    private float sensitivity;
    private float moveSpeed;
    private Vec3 eyePos;
    private StereoMode stereoMode;
    private PostProcess postProcess;
    private int lightingUBB;
    private int[] lightingUBO;

    public GlViewer() {

        setup();
    }

    private void setup() {
        GLProfile gLProfile = GLProfile.getDefault();

        GLCapabilities gLCapabilities = new GLCapabilities(gLProfile);

        glWindow = GLWindow.create(gLCapabilities);
        /*
         *  We combine NEWT GLWindow inside existing AWT application (the main JFrame)
         *  by encapsulating the glWindow inside a NewtCanvasAWT canvas.
         */
        newtCanvasAWT = new NewtCanvasAWT(glWindow);

//        newtCanvasAWT.setSize(1280, 800);
//        initViewPole();
//        inputListener = new EC_InputListener(this, viewPole, new EC_InputsState());
//
//        glWindow.addMouseListener(inputListener);
//        glWindow.addKeyListener(inputListener);
//        glWindow.setSize(1280, 800);
        glWindow.addGLEventListener(this);
        inputListener = new InputListener(this);
        glWindow.addKeyListener(inputListener);

        fullscreen = false;
        glWindow.setFullscreen(fullscreen);

        animator = new Animator(glWindow);
        animator.start();

//        glWindow.setVisible(true);
//        glWindow.setAutoSwapBufferMode(false);
        sensorFusion = new SensorFusion();
        sensorFusion.SetPredictionEnabled(true);
        sensorFusion.setMotionTrackingEnabled(true);
        try {
            RiftTracker.startListening(sensorFusion.createHandler());
        } catch (IOException ex) {
            Logger.getLogger(GlViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
//            RiftTracker.startListening(new Predicate<TrackerMessage>() {
//                @Override
//                public boolean apply(TrackerMessage message) {
//                        // Just enough data to see we're getting stuff
//                        System.out.println("Sample count: "+message.sampleCount);
//                        System.out.println("Sample 0: "+message.samples.get(0));
//                    return true;
//                }
//            });
//        } catch (IOException ex) {
//            Logger.getLogger(GlViewer.class.getName()).log(Level.SEVERE, null, ex);
//        }

        upVector = new Vec3(0f, 1f, 0f);
        forwardVector = new Vec3(0f, 0f, -1f);
        rightVector = new Vec3(1f, 0f, 0f);

        yawInitial = 3.141592f;
        sensitivity = 1f;
        moveSpeed = 3f;

        eyePos = new Vec3(0f, 1.6f, -5f);

        stereoMode = StereoMode.none;
        postProcess = PostProcess.none;

        lightingUBB = 0;
    }

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("init");

        GL3 gl3 = glad.getGL().getGL3();

        gl3.glClearColor(1, 0, 0, 1);
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

        initUBO(gl3);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {

        System.out.println("dispose");

        System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable glad) {

//        System.out.println("display");
        GL3 gl3 = glad.getGL().getGL3();
        /**
         * Rotate and position View Camera, using YawPitchRoll in BodyFrame
         * coordinates.
         */
//        Quaternion q = sensorFusion.getPredictedOrientation();
//        System.out.println("" + q.toString());
//        Quat quat = new Quat(q.x, q.y, q.z, q.w);
        Quat quat = Jglm.angleAxis(180, new Vec3(0f, 1f, 0f));
//        Quat quat = new Quat(0f, 0f, 0f, 1f);
        Vec3 up = quat.mult(upVector);
        Vec3 forward = quat.mult(forwardVector);
        /**
         * Minimal head modelling.
         */
        float headBaseToEyeHeight = 0.15f;  // Vertical height of eye from base of head
        float headBaseToEyeProtrusion = 0.09f;  // Distance forward of eye from base of head

        Vec3 eyeCenterInHeadFrame = new Vec3(0f, headBaseToEyeHeight, -headBaseToEyeProtrusion);
        eyeCenterInHeadFrame = quat.mult(eyeCenterInHeadFrame);
        Vec3 shiftedEyePos = eyePos.plus(eyeCenterInHeadFrame);
        shiftedEyePos.y -= eyeCenterInHeadFrame.y;  // Bring the head back down to original height

        shiftedEyePos.print("shiftedEyePos");
        (shiftedEyePos.plus(forward)).print("shiftedEyePos.plus(forward)");
        up.print("up");
        Mat4 view = lookAtRH(shiftedEyePos, shiftedEyePos.plus(forward), up);

        float yFov;
        float aspectRatio = (float) glWindow.getWidth() / (float) glWindow.getHeight();

        if (stereoMode == StereoMode.none) {

            yFov = 80f;

        } else {

            yFov = (float) (2f * Math.atan(0.093600 / (2 * 0.041000)));
        }
        Mat4 projection = perspectiveRH(yFov, aspectRatio, 0.01f, 2000f);

//        projection.print("projection");
        view.print("view");
        switch (stereoMode) {

            case none:
                render(gl3, StereoEye.center, projection, view);
                break;

            case leftRight_multipass:
                break;
        }

        checkError(gl3);
    }

    // Render the scene for one eye.
    private void render(GL3 gl3, StereoEye stereoEye, Mat4 projection, Mat4 view) {

        beginScene(gl3);

        applyStereoParams(gl3, stereoEye);

        clear(gl3);

        setDepthMode(gl3, true, true, GL3.GL_LESS);

        OculusRoomTiny.getInstance().getScene().render(gl3, projection, view);

        finishScene(gl3);
    }

    private void beginScene(GL3 gl3) {

        beginRendering(gl3);

        setRenderTarget(gl3, 0);
    }

    private void beginRendering(GL3 gl3) {

//        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glEnable(GL3.GL_CULL_FACE);
        gl3.glFrontFace(GL3.GL_CW);

        gl3.glLineWidth(3.0f);
        gl3.glEnable(GL3.GL_LINE_SMOOTH);
        gl3.glEnable(GL3.GL_BLEND);
        gl3.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);

//        glMatrixMode(GL_MODELVIEW);
//        glLoadIdentity();
    }

    private void applyStereoParams(GL3 gl3, StereoEye stereoEye) {

        switch (stereoEye) {

            case center:
                break;
        }
    }

    private void setRenderTarget(GL3 gl3, int renderTarget) {

        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, renderTarget);
    }

    private void clear(GL3 gl3) {

        gl3.glClearColor(0f, 0f, 0f, 1f);
        gl3.glClearDepthf(1f);
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT | GL3.GL_STENCIL_BUFFER_BIT);
    }

    private void setDepthMode(GL3 gl3, boolean enable, boolean write, int func) {

        if (enable) {

            gl3.glEnable(GL3.GL_DEPTH_TEST);
            gl3.glDepthMask(write);
            gl3.glDepthFunc(GL3.GL_LESS);

        } else {

            gl3.glDisable(GL3.GL_DEPTH_TEST);
        }
    }

    private void finishScene(GL3 gl3) {

    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

        System.out.println("reshape (" + i + ", " + i1 + ") (" + i2 + ", " + i3 + ")");

        GL3 gl3 = glad.getGL().getGL3();
        gl3.glViewport(i, i1, i2, i3);
    }

    private void initUBO(GL3 gl3) {
        /**
         * vec3 ambient;
         * float lightCount;
         * vec4 lightPos[8];
         * vec4 lightColor[8];
         *
         * times 4 Bytes/Float
         */
        int size = (3 + 1 + 4 * 8 * 2) * 4;
        int offset = 0;
        lightingUBO = new int[1];

        gl3.glGenBuffers(1, lightingUBO, 0);

        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, lightingUBO[0]);
        {
            gl3.glBufferData(GL3.GL_UNIFORM_BUFFER, size, null, GL3.GL_DYNAMIC_DRAW);

            gl3.glBindBufferRange(GL3.GL_UNIFORM_BUFFER, lightingUBB, lightingUBO[0], offset, size);
        }
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, 0);
    }

    public void toggleFullscreen() {

        fullscreen = !fullscreen;

        glWindow.setFullscreen(fullscreen);

//        glWindow.display();
    }

    public GLWindow getGlWindow() {
        return glWindow;
    }

    public NewtCanvasAWT getNewtCanvasAWT() {
        return newtCanvasAWT;
    }

    public Animator getAnimator() {
        return animator;
    }

    private Mat4 lookAtRH(Vec3 eye, Vec3 at, Vec3 up) {

        Vec3 z = (eye.minus(at)).normalize();   // Forward
        Vec3 x = (up.crossProduct(z)).normalize();  // Right
        Vec3 y = z.crossProduct(x);

        Mat4 result = new Mat4(1f);

        result.c0.x = x.x;
        result.c0.y = y.x;
        result.c0.z = z.x;

        result.c1.x = x.y;
        result.c1.y = y.y;
        result.c1.z = z.y;

        result.c2.x = x.z;
        result.c2.y = y.z;
        result.c2.z = z.z;

        result.c3.x = -x.dot(eye);
        result.c3.y = -y.dot(eye);
        result.c3.z = -z.dot(eye);

        return result;
    }

    private static Mat4 perspectiveRH(float yFov, float aspect, float zNear, float zFar) {
//        System.out.println("perspectiveRH, yFov " + yFov + ", aspect " + aspect + ", zNear " + zNear + ", zFar " + zFar);
        float frustumScale = calculateFrustumScale(yFov);

        Mat4 perspectiveRH = new Mat4(0);

        perspectiveRH.c0.x = frustumScale / aspect;
        perspectiveRH.c1.y = frustumScale;
        perspectiveRH.c2.z = zFar / (zNear - zFar);
        perspectiveRH.c2.w = -1;
        perspectiveRH.c3.z = zFar * zNear / (zNear - zFar);

        return perspectiveRH;
    }

    public void setStereoMode(StereoMode stereoMode) {
        this.stereoMode = stereoMode;
    }

    public int getLightingUBB() {
        return lightingUBB;
    }

    public int[] getLightingUBO() {
        return lightingUBO;
    }

    public enum StereoMode {

        none,
        leftRight_multipass
    }

    public enum StereoEye {

        center,
        left,
        right
    }

    public enum PostProcess {

        none,
        distorsion
    }

    private void checkError(GL3 gl3) {

        int error = gl3.glGetError();

        if (error != 0) {
            System.out.println("Error " + error + " !");
        }
    }
}
