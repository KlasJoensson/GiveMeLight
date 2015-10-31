package joensson.klas.givemelight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;


public class LightBoard extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_board);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        Intent myIntent = this.getIntent();
        int color = myIntent.getIntExtra("color", Color.WHITE);

        RelativeLayout lightBoard = (RelativeLayout) findViewById(R.id.light_board);
        lightBoard.setBackgroundColor(color);
    }

}
