package joensson.klas.givemelight;

import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

/**.
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

    /**
     * Turns the flash on.Throws an exception if the camera don't work properly for some reason.
     *
     * @throws IOException
     */
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

    /**
     * Turns the flash off.Throws an exception if the camera don't work properly for some reason.
     *
     * @throws IOException
     */
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

    /**
     * Activates , i.e. opens, the camera. Throws an exception if the camera don't work properly
     * for some reason.
     *
     * @throws IOException
     */
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
            Log.e("In Activate Camera","Error: "+e.getMessage(), e);
            throw new IOException("Could not access camera");
        }
    }

    /**
     * Closes the camera, i.e. releases the camera resource so an other device may use it and if the
     * flashlight is on torn it off.
     */
    public void closeCamera() {
        if (camera != null) {
            if (isFlashOn) {
                try {
                    turnOffFlash();
                } catch (IOException e) {
                    Log.e("Closing camera", "Can't turn of the flash properly", e);
                }
            }

            camera.release();
        }
    }

    /**
     * Returns the status of the flash-light,
     *
     * @return true if the flash-light is on
     */
    public boolean isFlashOn() {
        return isFlashOn;
    }
}
