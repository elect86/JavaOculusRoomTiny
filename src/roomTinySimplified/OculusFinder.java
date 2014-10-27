/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roomTinySimplified;

import com.jogamp.newt.MonitorDevice;
import com.jogamp.newt.MonitorMode;
import com.jogamp.newt.Screen;

/**
 *
 * @author gbarbieri
 */
public class OculusFinder {

    public static int getDk2Id(Screen screen) {

        for (MonitorDevice monitorDevice : screen.getMonitorDevices()) {

            MonitorMode monitorMode = monitorDevice.getSupportedModes().get(0);

            if (monitorMode.getId() == 0
                    && monitorMode.getSizeAndRRate().surfaceSize.getResolution().getWidth() == 1080
                    && monitorMode.getSizeAndRRate().surfaceSize.getResolution().getHeight() == 1920
                    && monitorMode.getSizeAndRRate().surfaceSize.getBitsPerPixel() == 32
                    && monitorMode.getSizeAndRRate().refreshRate == 75.0
                    && MonitorMode.SizeAndRRate.flags2String(monitorMode.getSizeAndRRate().flags).toString().isEmpty()
                    && monitorMode.getRotation() == 0
                    && monitorDevice.getSupportedModes().size() == 440) {

                return screen.getMonitorDevices().indexOf(monitorDevice);
            }
        }
        return -1;
    }

    public static int getDk1Id(Screen screen) {

        for (MonitorDevice monitorDevice : screen.getMonitorDevices()) {

            MonitorMode monitorMode = monitorDevice.getSupportedModes().get(0);

            if (monitorMode.getId() == 0
                    && monitorMode.getSizeAndRRate().surfaceSize.getResolution().getWidth() == 1920
                    && monitorMode.getSizeAndRRate().surfaceSize.getResolution().getHeight() == 1200
                    && monitorMode.getSizeAndRRate().surfaceSize.getBitsPerPixel() == 32
                    && monitorMode.getSizeAndRRate().refreshRate == 60.0
                    && MonitorMode.SizeAndRRate.flags2String(monitorMode.getSizeAndRRate().flags).toString().isEmpty()
                    && monitorMode.getRotation() == 0
                    && monitorDevice.getSupportedModes().size() == 320) {

                return screen.getMonitorDevices().indexOf(monitorDevice);
            }
        }
        return -1;
    }

    public static int getBestHdm(Screen screen) {

        int dk2 = getDk2Id(screen);
        
        if (dk2 != -1) {
            
            return dk2;
        }
        return getDk1Id(screen);
    }
}
