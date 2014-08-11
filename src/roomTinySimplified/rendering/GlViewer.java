/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified.rendering;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.oculusvr.capi.DistortionMesh;
import com.oculusvr.capi.DistortionVertex;
import com.oculusvr.capi.EyeRenderDesc;
import com.oculusvr.capi.FovPort;
import com.oculusvr.capi.HmdDesc;
import com.oculusvr.capi.OvrLibrary;
import static com.oculusvr.capi.OvrLibrary.ovrDistortionCaps.ovrDistortionCap_Chromatic;
import static com.oculusvr.capi.OvrLibrary.ovrDistortionCaps.ovrDistortionCap_TimeWarp;
import static com.oculusvr.capi.OvrLibrary.ovrEyeType.ovrEye_Count;
import static com.oculusvr.capi.OvrLibrary.ovrEyeType.ovrEye_Left;
import static com.oculusvr.capi.OvrLibrary.ovrEyeType.ovrEye_Right;
import static com.oculusvr.capi.OvrLibrary.ovrHmdType.ovrHmd_DK1;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_MagYawCorrection;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_Orientation;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_Position;
import com.oculusvr.capi.OvrRecti;
import com.oculusvr.capi.OvrSizei;
import com.oculusvr.capi.OvrVector2f;
import com.oculusvr.capi.OvrVector2i;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import jglm.Jglm;
import static jglm.Jglm.calculateFrustumScale;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec2;
import jglm.Vec3;
import roomTinySimplified.InputListener;
import roomTinySimplified.core.OculusRoomTiny;
import roomTinySimplified.rendering.glsl.LitSolid;
import roomTinySimplified.rendering.glsl.LitTexture;

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
    private Vec3 upVector;
    private Vec3 forwardVector;
    private Vec3 rightVector;
    private float yawInitial;
    private float sensitivity;
    private float moveSpeed;
    private Vec3 headPos;
    private int lightingUBB;
    private int[] lightingUBO;
    private LitTexture litTexture;
    private LitSolid litSolid;
    
    private HmdDesc hmdDesc;
    private OvrRecti[] eyeRenderViewport;
    private int frameCount;
    private FrameBuffer frameBuffer;
    private EyeRenderDesc[] eyeRenderDescs;
    private OvrVector2f[][] uvScaleOffset;
    private int indicesCount;
    
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

        headPos = new Vec3(0f, 1.6f, -5f);

        lightingUBB = 0;
        
        setupOculus();
    }
    
    private void setupOculus() {
        
        System.out.println("setupOculus()");
        // Initializes LibOVR, and the Rift
        HmdDesc.initialize();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        hmdDesc = openFirstHmd();

        if (null == hmdDesc) {
            throw new IllegalStateException("Unable to initialize HMD");
        }
        if (hmdDesc.configureTracking(ovrTrackingCap_Orientation, 0) == 0) {
            throw new IllegalStateException("Unable to start the sensor");
        }
        frameCount = -1;

        System.out.println("/setupOculus()");
    }
    
    private static HmdDesc openFirstHmd() {
        HmdDesc hmdDesc = HmdDesc.create(0);
        if (hmdDesc == null) {
            hmdDesc = HmdDesc.createDebug(ovrHmd_DK1);
        }
        return hmdDesc;
    }

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("init");

        GL3 gl3 = glad.getGL().getGL3();

        String shadersFilepath = "/roomTinySimplified/rendering/glsl/shaders/";
        
        litTexture = new LitTexture(gl3, shadersFilepath, "LitTexture_VS.glsl", "LitTexture_FS.glsl");
        litSolid = new LitSolid(gl3, shadersFilepath, "LitSolid_VS.glsl", "LitSolid_FS.glsl");
        
        initUBO(gl3);
        
        OculusRoomModel.populateRoomScene(gl3, OculusRoomTiny.getInstance().getScene());
        
        initOculus(gl3);
    }
    
    private void initOculus(GL3 gl3) {
        //Configure Stereo settings.
        OvrSizei recommendedTex0Size = hmdDesc.getFovTextureSize(ovrEye_Left, hmdDesc.DefaultEyeFov[0], 1f);
        OvrSizei recommendedTex1Size = hmdDesc.getFovTextureSize(ovrEye_Right, hmdDesc.DefaultEyeFov[1], 1f);
        int x = recommendedTex0Size.w + recommendedTex1Size.w;
        int y = Math.max(recommendedTex0Size.h, recommendedTex1Size.h);
        OvrSizei renderTargetSize = new OvrSizei(x, y);

//        Texture pRenderTargetTexture = Texture.create(gl3, TextureFormat.RGBA, renderTargetSize, null);
        frameBuffer = new FrameBuffer(gl3, renderTargetSize);
        // Initialize eye rendering information.
        FovPort[] eyeFov = new FovPort[]{hmdDesc.DefaultEyeFov[0], hmdDesc.DefaultEyeFov[1]};

        eyeRenderViewport = new OvrRecti[]{new OvrRecti(), new OvrRecti()};
        eyeRenderViewport[0].Pos = new OvrVector2i(0, 0);
        eyeRenderViewport[0].Size = new OvrSizei(renderTargetSize.w / 2, renderTargetSize.h);
        eyeRenderViewport[1].Pos = new OvrVector2i((renderTargetSize.w + 1) / 2, 0);
        eyeRenderViewport[1].Size = eyeRenderViewport[0].Size;

//        ibo = new int[IboBuffer.count.ordinal()];
//        gl3.glGenBuffers(IboBuffer.count.ordinal(), ibo, 0);

        uvScaleOffset = new OvrVector2f[2][2];
        /**
         * Hardcoded.
         */
        uvScaleOffset[ovrEye_Left][0] = new OvrVector2f(0.163722f, 0.233835f);
        uvScaleOffset[ovrEye_Left][1] = new OvrVector2f(0.341069f, 0.5f);
        uvScaleOffset[ovrEye_Right][0] = new OvrVector2f(0.163722f, 0.233835f);
        uvScaleOffset[ovrEye_Right][1] = new OvrVector2f(0.658931f, 0.5f);
        eyeRenderDescs = new EyeRenderDesc[2];

        for (int eyeNum = 0; eyeNum < ovrEye_Count; eyeNum++) {
//        if (1 == 1) {
//            int eyeNum = 0;

            int distortionCaps = ovrDistortionCap_Chromatic | ovrDistortionCap_TimeWarp;

            DistortionMesh meshData = hmdDesc.createDistortionMesh(eyeNum, eyeFov[eyeNum], distortionCaps);

            DistortionVertex[] distortionVertices = new DistortionVertex[meshData.VertexCount];
            meshData.pVertexData.toArray(distortionVertices);
            {
//                initDistortionVBO(gl3, eyeNum, distortionVertices);
            }
            short[] indicesData = meshData.pIndexData.getPointer().getShortArray(0, meshData.IndexCount);
            indicesCount = indicesData.length;
            {
//                initDistortionIBO(gl3, eyeNum, indicesData);
            }
//            initDistortionVAO(gl3, eyeNum);

            eyeRenderDescs[eyeNum] = hmdDesc.getRenderDesc(eyeNum, eyeFov[eyeNum]);

//            OvrLibrary.INSTANCE.ovrHmd_GetRenderScaleAndOffset((FovPort.ByValue) eyeFov[eyeNum], (OvrSizei.ByValue) frameBuffer.getSize(),
//                    (OvrRecti.ByValue) eyeRenderViewport[eyeNum], uvScaleOffset[eyeNum]);            
        }
        hmdDesc.setEnabledCaps(OvrLibrary.ovrHmdCaps.ovrHmdCap_LowPersistence | OvrLibrary.ovrHmdCaps.ovrHmdCap_DynamicPrediction);

        hmdDesc.configureTracking(ovrTrackingCap_Orientation | ovrTrackingCap_MagYawCorrection | ovrTrackingCap_Position, 0);
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
        Quat rollPitchYaw = Jglm.angleAxis(180, new Vec3(0f, 1f, 0f));
        Vec3 finalUp = rollPitchYaw.mult(upVector);
        Vec3 finalForward = rollPitchYaw.mult(forwardVector);
        
        Mat4 view = lookAt(headPos, headPos.plus(finalForward), finalUp);

//        Mat4 projection = perspectiveRH(yFov, aspectRatio, 0.01f, 2000f);
        Mat4 projection = Jglm.perspective(80f, 0.01f, 10000f);

//        projection.print("projection");
//        view.print("view");
        render(gl3, projection, view);

        checkError(gl3);
    }

    // Render the scene for one eye.
    private void render(GL3 gl3, Mat4 projection, Mat4 view) {

        beginScene(gl3);

        clear(gl3);

//        setDepthMode(gl3, true, true, GL3.GL_LESS);

        OculusRoomTiny.getInstance().getScene().render(gl3, projection, view);

        finishScene(gl3);
    }

    private void beginScene(GL3 gl3) {

        beginRendering(gl3);

        setRenderTarget(gl3, 0);
    }

    private void beginRendering(GL3 gl3) {

        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glDisable(GL3.GL_CULL_FACE);
        gl3.glFrontFace(GL3.GL_CW);

        gl3.glLineWidth(3.0f);
        gl3.glEnable(GL3.GL_LINE_SMOOTH);
        gl3.glEnable(GL3.GL_BLEND);
        gl3.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void setRenderTarget(GL3 gl3, int renderTarget) {

        gl3.glBindFramebuffer(GL3.GL_FRAMEBUFFER, renderTarget);
    }

    private void clear(GL3 gl3) {

        gl3.glClearColor(0f, 0f, 0f, 1f);
//        gl3.glClearDepthf(1f);
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
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

    private Mat4 lookAt(Vec3 eye, Vec3 at, Vec3 up) {

        Vec3 z = (eye.minus(at)).normalize();   // Forward
        Vec3 x = (up.crossProduct(z)).normalize();  // Right
        Vec3 y = z.crossProduct(x);
        
        return new Mat4(new float[]{
            x.x, y.x, z.x, 0,
            x.y, y.y, z.y, 0,
            x.z, y.z, z.z, 0,
            -(x.dot(eye)), -(y.dot(eye)), -(z.dot(eye)), 1});
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
    
    private Mat4 createProjection(FovPort tanHalfFov, float zNear, float zFar) {

        ScaleAndOffset scaleAndOffset = createNDCscaleAndOffset(tanHalfFov);

        float handednessScale = -1f;

        Mat4 projection = new Mat4();

        projection.c0.x = scaleAndOffset.scale.x;
        projection.c1.x = 0f;
        projection.c2.x = handednessScale * scaleAndOffset.offset.x;
        projection.c3.x = 0f;

        projection.c0.y = 0f;
        projection.c1.y = scaleAndOffset.scale.y;
        projection.c2.y = handednessScale * -scaleAndOffset.offset.y;
        projection.c3.y = 0f;

        projection.c0.z = 0f;
        projection.c1.z = 0f;
        projection.c2.z = -handednessScale * zFar / (zNear - zFar);
        projection.c3.z = (zFar * zNear) / (zNear - zFar);

        projection.c0.w = 0f;
        projection.c1.w = 0f;
        projection.c2.w = handednessScale;
        projection.c3.w = 0f;

        return projection;
    }

    private ScaleAndOffset createNDCscaleAndOffset(FovPort tanHalfFov) {

        float projXscale = 2f / (tanHalfFov.LeftTan + tanHalfFov.RightTan);
        float projXoffset = (tanHalfFov.LeftTan - tanHalfFov.RightTan) * projXscale * .5f;
        float projYscale = 2f / (tanHalfFov.UpTan + tanHalfFov.DownTan);
        float projYoffset = (tanHalfFov.UpTan - tanHalfFov.DownTan) * projYscale * .5f;

        return new ScaleAndOffset(new Vec2(projXscale, projYscale), new Vec2(projXoffset, projYoffset));
    }
    
    private class ScaleAndOffset {

        public Vec2 scale;
        public Vec2 offset;

        public ScaleAndOffset(Vec2 scale, Vec2 offset) {

            this.scale = scale;
            this.offset = offset;
        }
    }

    public int getLightingUBB() {
        return lightingUBB;
    }

    public int[] getLightingUBO() {
        return lightingUBO;
    }

    private void checkError(GL3 gl3) {

        int error = gl3.glGetError();

        if (error != 0) {
            System.out.println("Error " + error + " !");
        }
    }

    public LitTexture getLitTexture() {
        return litTexture;
    }

    public LitSolid getLitSolid() {
        return litSolid;
    }
}
