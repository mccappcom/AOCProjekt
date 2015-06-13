package pl.amu.edu.trebuh_divoolej.aocprojekt;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.rajawali3d.vuforia.RajawaliVuforiaActivity;


public class AOCActivity extends RajawaliVuforiaActivity {
    public static final String TAG = "AOCActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aoc);
//        useCloudRecognition(true);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        // logo stuff

        addContentView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        startVuforia();
    }

    @Override
    protected void setupTracker() {
        int result = initTracker(TRACKER_TYPE_MARKER);
        if (result == 1) {
            result = initTracker(TRACKER_TYPE_IMAGE);
            if (result == 1)
                super.setupTracker();
            else
                Log.e(TAG, "Couldn't initialize image tracker");
        }
        else
            Log.e(TAG, "Couldn't initialize marker tracker");
    }

    @Override
    protected void initApplicationAR() {
        super.initApplicationAR();

        createFrameMarker(420, "MarkerUAM_420", 50, 50);
        createFrameMarker(430, "MarkerUAM_430", 50, 50);
        createFrameMarker(440, "MarkerUAM_440", 50, 50);
        createFrameMarker(450, "MarkerUAM_450", 50, 50);

        createImageMarker("aocImages.xml");
    }

    @Override
    protected void initRajawali() {
        AOCRenderer renderer = new AOCRenderer(this);
        setRenderer(renderer);

        super.initRajawali();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aoc, menu);
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
