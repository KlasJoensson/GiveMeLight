package joensson.klas.givemelight;

import android.hardware.Camera;

import java.io.IOException;

/**
 * Created by klas on 2015-04-05.
 * The class android.hardware.Camera is deprecated in api level 21, this might not run on newer devises!
 */
public class Flashlight {

    private Camera camera;
    private Camera.Parameters parameters;
    private boolean isFlashOn = false;
    private static Flashlight myInstance;

    public static Flashlight getInstance() throws IOException {
        if (myInstance == null) {
            myInstance = new Flashlight();
        }

        return myInstance;
    }

    private Flashlight() throws IOException {
       activateCamera();
    }

    public void turnOnFlash() throws IOException {
        if (camera == null || parameters == null) {
            activateCamera();
        }

        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        isFlashOn = true;
    }

    public void turnOffFlash() throws IOException {
        if (camera == null || parameters == null) {
            activateCamera();
        }

        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
        isFlashOn = false;
    }

    public void activateCamera() throws IOException {
        try {
            camera = Camera.open();
            parameters = camera.getParameters();
            if  (parameters.getFlashMode() == Camera.Parameters.FLASH_MODE_TORCH) {
                isFlashOn = true;
            } else {
                isFlashOn = false;
            }
        } catch (Exception e) {
            throw new IOException("Could not access camera");
        }
    }

    public void closeCamera() {
        if (camera != null)
            camera.release();
    }
}
