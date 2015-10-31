package joensson.klas.givemelight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
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
    private ImageView div;
    private Flashlight flashlight;
    private Menu myMenu;
    private boolean rotationLocked;

    private static final int START_COLOR_AND_ALPHA_VALUE = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int[] numberPickerValues = {START_COLOR_AND_ALPHA_VALUE,
                START_COLOR_AND_ALPHA_VALUE,
                START_COLOR_AND_ALPHA_VALUE,
                START_COLOR_AND_ALPHA_VALUE};

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_270) {
            setContentView(R.layout.activity_main);
            setUpCommonViews(numberPickerValues, false);
            div.setImageResource(R.drawable.chain);
        } else {
            setContentView(R.layout.main_activity_landscape);
            setUpCommonViews(numberPickerValues, false);
            div.setImageResource(R.drawable.chain_landscape);
        }

        flashlightButton.setChecked(false);

        rotationLocked = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.myMenu = menu;
        updateMenu();

        return true;
    }

    /**
     * To be called when an action happened that might influence the menu, i.e. the rotation
     * is looked.
     */
    private void updateMenu() {
        MenuItem menuItem, menuItem1;

        if (myMenu != null) {
            menuItem = myMenu.findItem(R.id.action_lock_to_this);
            menuItem.setVisible(!rotationLocked);
            menuItem = myMenu.findItem(R.id.action_enable_both);
            menuItem.setVisible(rotationLocked);

            menuItem = myMenu.findItem(R.id.action_only_landscape);
            menuItem1 = myMenu.findItem(R.id.action_only_portrait);
            if (rotationLocked) {
                switch (getRequestedOrientation()) {
                    case Configuration.ORIENTATION_PORTRAIT:
                        menuItem1.setChecked(true);
                        if (menuItem.isChecked())
                            menuItem.setChecked(false);
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        menuItem.setChecked(true);
                        if (menuItem1.isChecked())
                            menuItem1.setChecked(false);
                        break;
                    default:
                        checkLockedOrientation();
                        break;
                }
            } else {
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                if (menuItem1.isChecked())
                    menuItem1.setChecked(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        rotationLocked = false;

        switch (id) {
            case R.id.action_only_landscape:
                setUpLandscapeView();
                rotationLocked = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            case R.id.action_only_portrait:
                setUpPortraitView();
                rotationLocked = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            case R.id.action_lock_to_this:
                rotationLocked = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                break;

            case R.id.action_enable_both:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;

            default:
                super.onOptionsItemSelected(item);
        }

        updateMenu();

        return true;
    }

    /**
     * Updates the checked menu items when rotation is locked by the option lock to current rotation
     * of the device.
     */
    private void checkLockedOrientation() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        MenuItem menuItemLandscape = myMenu.findItem(R.id.action_only_landscape);
        MenuItem menuItemPortrait = myMenu.findItem(R.id.action_only_portrait);
        if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_270) {
            menuItemPortrait.setChecked(true);
            if (menuItemLandscape.isChecked())
                menuItemLandscape.setChecked(false);
        }else {
            menuItemLandscape.setChecked(true);
            if (menuItemPortrait.isChecked())
                menuItemPortrait.setChecked(false);
        }

    }

    /**
     * Sets the max, min and default value on the number pickers. Also adds a listener.
     *
     * @param numberPicker The number picker to be set up.
     */
    private void setUpNumberPicker(NumberPicker numberPicker, int value) {
        numberPicker.setMaxValue(255);
        numberPicker.setMinValue(0);
        numberPicker.setValue(value);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (rotationLocked) {
            keepOrientation();
        } else {
            if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setUpLandscapeView();
            } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setUpPortraitView();
            }
        }

    }

    /**
     * Keeps the layout when the layout is locked.
     */
    private void keepOrientation() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        switch(orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

    /**
     * Makes the landscape layout look and behave correct
     */
    private void setUpLandscapeView() {
        int[] numberPickerValues = getNumberPickersValues();
        setContentView(R.layout.main_activity_landscape);
        setUpCommonViews(numberPickerValues, true);
        div.setImageResource(R.drawable.chain_landscape);
    }

    /**
     * Makes the portrait layout look and behave correct
     */
    private void setUpPortraitView() {
        int[] numberPickerValues = getNumberPickersValues();
        setContentView(R.layout.activity_main);
        setUpCommonViews(numberPickerValues, true);
        div.setImageResource(R.drawable.chain);
    }

    /**
     * Sets up the views that the both layouts has in common, to not loos the values of the number
     * pickers they hav to be given to the method. This is done with an array of integers, formatted
     * like this: {red value, green value, blue value, alpha value}.
     *
     * @param numberPickerValues The values of the number pickers
     */
    private void setUpCommonViews(int[] numberPickerValues, boolean checkCameraFlashStatus) {
        flashlightImg = (ImageView) findViewById(R.id.flashlightImage);
        flashlightButton = (ToggleButton) findViewById(R.id.flashOnOffButton);
        if (checkCameraFlashStatus) {
            flashlightButton.setChecked(flashlight.isFlashOn());
            if (flashlight.isFlashOn()) {
                flashlightImg.setImageResource(R.drawable.flashlight_on);
            } else {
                flashlightImg.setImageResource(R.drawable.flashlight_off);
            }
        }
        flashlightButton.setOnClickListener(flashlightListener);

        redNumberPicker = (NumberPicker) findViewById(R.id.redNumberPicker);
        setUpNumberPicker(redNumberPicker, numberPickerValues[0]);
        greenNumberPicker =(NumberPicker) findViewById(R.id.greenNumberPicker);
        setUpNumberPicker(greenNumberPicker, numberPickerValues[1]);
        blueNumberPicker = (NumberPicker) findViewById(R.id.blueNumberPicker);
        setUpNumberPicker(blueNumberPicker, numberPickerValues[2]);
        alphaNumberPicker = (NumberPicker) findViewById(R.id.alphaNumberPicker);
        setUpNumberPicker(alphaNumberPicker, numberPickerValues[3]);

        Button showLightBoardButton = (Button) findViewById(R.id.openLightBoardButton);
        showLightBoardButton.setOnClickListener(openLightBoardListener);

        colorPreview = (SurfaceView) findViewById(R.id.colorPreview);
        colorPreview.setBackgroundColor(getColor());

        div = (ImageView) findViewById(R.id.dividerImage);

    }

    /**
     * Reads the values of the number pickers and returns them in an array of integers. If it can't
     * find an number picker it will return the value set in the static variable
     * START_COLOR_AND_ALPHA_VALUE instead.
     * The formart of the returned array is {red value, green value, blue value, alpha value}.
     *
     * @return An array with the values of the number pickers
     */
    private int[] getNumberPickersValues() {
        int[] values = new int[4];
        if (redNumberPicker != null)
            values[0] = redNumberPicker.getValue();
        else
            values[0] = START_COLOR_AND_ALPHA_VALUE;
        if (greenNumberPicker != null)
            values[1] = greenNumberPicker.getValue();
        else
            values[1] = START_COLOR_AND_ALPHA_VALUE;
        if (blueNumberPicker != null)
            values[2] = blueNumberPicker.getValue();
        else
            values[2] = START_COLOR_AND_ALPHA_VALUE;
        if (alphaNumberPicker != null)
            values[3] = alphaNumberPicker.getValue();
        else
            values[3] = START_COLOR_AND_ALPHA_VALUE;

        return values;
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
