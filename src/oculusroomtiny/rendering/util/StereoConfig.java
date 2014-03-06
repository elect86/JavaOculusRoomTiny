/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oculusroomtiny.rendering.util;

import oculusroomtiny.rendering.GlViewer;
import oculusroomtiny.rendering.GlViewer.StereoEye;
import oculusroomtiny.rendering.GlViewer.StereoMode;

/**
 *
 * @author gbarbieri
 */
public class StereoConfig {

    private StereoMode mode;
    private Viewport fullView;
    private float interpupillaryDistance;
    private float aspectMultiplier;
    private boolean dirtyFlag;
    private boolean ipdOverride;
    private float yFov;
    private float aspect;
    private float projectionCenterOffset;
    private float orthoPixelOffset;
    private StereoEyeParams[] eyeRenderParams;
    private HMDinfo hmd;

    public StereoConfig() {

        this(StereoMode.none, new Viewport(0, 0, 1280, 800));
    }

    public StereoConfig(StereoMode stereoMode, Viewport viewport) {

        mode = stereoMode;
        fullView = viewport;

        interpupillaryDistance = 0.064f;
        aspectMultiplier = 1f;
        dirtyFlag = true;
        ipdOverride = false;
        yFov = 0f;
        aspect = (float) viewport.w / (float) viewport.h;
        projectionCenterOffset = 0f;
        orthoPixelOffset = 0f;

        hmd = new HMDinfo();
        hmd.hResolution = 1280;
        hmd.vResolution = 800;
        hmd.hScreenSize = 0.14976f;
        hmd.vScreenSize = hmd.hScreenSize / (1280f / 800f);
        hmd.interpupillaryDistance = interpupillaryDistance;
        hmd.lensSeparationDistance = 0.0635f;
        hmd.eyeToScreenDistance = 0.041f;
    }

    public StereoEyeParams getEyeRenderParams(StereoEye eye) {

        updateIfDirty();
        
        switch(eye){
            
            case right:
                return eyeRenderParams[1];
                
            default:
                return eyeRenderParams[0];
        }
    }

    private void updateIfDirty() {

        if (dirtyFlag) {

            updateComputedState();
        }
    }

    private void updateComputedState() {

        aspect = (float) fullView.w / (float) fullView.h;
        aspect *= (mode == StereoMode.none) ? 1f : 0.5f;
        aspect *= aspectMultiplier;

        if (mode == StereoMode.none) {

            yFov = (float) Math.toRadians(80);
        }
    }
}
