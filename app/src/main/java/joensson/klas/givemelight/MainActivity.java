package joensson.klas.givemelight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    private NumberPicker redNumberPicker;
    private NumberPicker greenNumberPicker;
    private NumberPicker blueNumberPicker;
    private NumberPicker alphaNumberPicker;
    private SurfaceView colorPreview;
    private ToggleButton flashlightButton;
    private ImageView flashlightImg;
    private Flashlight flashlight;

    private static final int START_COLOR_AND_ALPHA_VALUE = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redNumberPicker = (NumberPicker) findViewById(R.id.redNumberPicker);
        setUpNumberPicker(redNumberPicker);
        greenNumberPicker =(NumberPicker) findViewById(R.id.greenNumberPicker);
        setUpNumberPicker(greenNumberPicker);
        blueNumberPicker = (NumberPicker) findViewById(R.id.blueNumberPicker);
        setUpNumberPicker(blueNumberPicker);
        alphaNumberPicker = (NumberPicker) findViewById(R.id.alphaNumberPicker);
        setUpNumberPicker(alphaNumberPicker);

        colorPreview = (SurfaceView) findViewById(R.id.colorPreview);
        colorPreview.setBackgroundColor(getColor());

        Button showLightBoardButton = (Button) findViewById(R.id.openLightBoardButton);
        showLightBoardButton.setOnClickListener(openLightBoardListener);

        ImageView div = (ImageView) findViewById(R.id.dividerImage);
        div.setImageResource(R.drawable.chain);

        flashlightImg = (ImageView) findViewById(R.id.flashlightImage);
        flashlightImg.setImageResource(R.drawable.flashlight_off);
        flashlightButton = (ToggleButton) findViewById(R.id.flashOnOffButton);
        flashlightButton.setOnClickListener(flashlightListener);
    }

    /**
     * Sets the max, min and default value on the number pickers. Also adds a listener.
     *
     * @param numberPicker The number picker to be set up.
     */
    private void setUpNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMaxValue(255);
        numberPicker.setMinValue(0);
        numberPicker.setValue(START_COLOR_AND_ALPHA_VALUE);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                changeColorPreview();
            }
        });
    }

    /**
     * Changes the color on the light board preview,
     */
    private void changeColorPreview() {
        int color = getColor();
        colorPreview.setBackgroundColor(color);
    }

    /**
     * Figure out what color that has been set by the user via number pickers.
     *
     * @return An integer corresponding to the selected color.
     */
    private int getColor() {
        int red, green, blue, alpha;
        if (redNumberPicker != null)
            red = redNumberPicker.getValue();
        else
            red = START_COLOR_AND_ALPHA_VALUE;
        if (greenNumberPicker != null)
            green = greenNumberPicker.getValue();
        else
            green = START_COLOR_AND_ALPHA_VALUE;
        if (blueNumberPicker != null)
            blue = blueNumberPicker.getValue();
        else
            blue = START_COLOR_AND_ALPHA_VALUE;
        if (alphaNumberPicker != null)
            alpha = alphaNumberPicker.getValue();
        else
            alpha = START_COLOR_AND_ALPHA_VALUE;

        return Color.argb(alpha,red,green,blue);
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            if (flashlight == null) {
                flashlight = Flashlight.getInstance();
            }

        } catch (IOException e) {
            Log.e("On start","Could not find the camera: " + e.getMessage());
            createFlashErrorDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if (flashlight == null) {
                flashlight = Flashlight.getInstance();
            }
            flashlight.activateCamera();
            flashlightButton.setChecked(flashlight.isFlashOn());
            if (flashlight.isFlashOn()) {
                flashlightImg.setImageResource(R.drawable.flashlight_on);
            } else {
                flashlightImg.setImageResource(R.drawable.flashlight_off);
            }
        } catch (IOException e) {
            Log.e("On resume","Could not find the camera: " + e.getMessage());
            createFlashErrorDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

        if (flashlight != null) {
            flashlight.closeCamera();
            flashlightImg.setImageResource(R.drawable.flashlight_off);
            flashlightButton.setChecked(false);
        }

    }

    /**
     * Creates and views the error dialog used when the flash isn't supported by the device.
     */
    private void createFlashErrorDialog() {
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setTitle("Error");
        alert.setMessage(getString(R.string.flash_error_message));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // If there's no support for a flash light, let's disable the button
                flashlightButton.setEnabled(false);
            }
        });
        alert.show();
    }

    /**
     * The listener that listen to the show-light-board-button and controls the actions taken when
     * it's pressed.
     */
    private View.OnClickListener openLightBoardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent showLBIntent = new Intent(MainActivity.this, LightBoard.class);
            showLBIntent.putExtra("color", getColor());

            startActivity(showLBIntent);
        }
    };

    /**
     * The listener that listen to the flashlight button and controls the action taken when it's
     * pressed.
     */
    private View.OnClickListener flashlightListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (flashlightButton.isChecked()) {
                // Flashlight is off, let's turn it on
                flashlightImg.setImageResource(R.drawable.flashlight_on);
                try {
                    flashlight.turnOnFlash();
                } catch (IOException e) {
                    Log.e("Flash error: ", e.getMessage());
                }
            } else {
                // Flashlight is on, let's turn it off
                flashlightImg.setImageResource(R.drawable.flashlight_off);
                try {
                    flashlight.turnOffFlash();
                } catch (IOException e) {
                    Log.e("Flash error: ", e.getMessage());
                }
            }
        }
    };
}
