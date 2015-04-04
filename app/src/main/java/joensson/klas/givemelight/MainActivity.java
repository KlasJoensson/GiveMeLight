package joensson.klas.givemelight;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.NumberPicker;


public class MainActivity extends ActionBarActivity {

    private NumberPicker redNumberPicker;
    private NumberPicker greenNumberPicker;
    private NumberPicker blueNumberPicker;
    private NumberPicker alphaNumberPicker;
    private SurfaceView colorPreview;
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
    }

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

    private void changeColorPreview() {
        int color = getColor();
        colorPreview.setBackgroundColor(color);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
