package pl.amu.edu.trebuh_divoolej.aocprojekt;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.vuforia.RajawaliVuforiaRenderer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by hubert on 31.05.15.
 */
public class AOCRenderer extends RajawaliVuforiaRenderer {
    private AOCActivity activity;
    private DirectionalLight light;
    private Object3D nesController;

    public AOCRenderer(Context context) {
        super(context);
        activity = (AOCActivity)context;
    }

    @Override
    protected void foundFrameMarker(int id, Vector3 vector3, Quaternion quaternion) {
        if(id == 420) {
            nesController.setVisible(true);
            nesController.setPosition(vector3);
            nesController.setOrientation(quaternion);
        }
    }

    @Override
    protected void foundImageMarker(String s, Vector3 vector3, Quaternion quaternion) {
    }

    @Override
    public void noFrameMarkersFound() {

    }

    @Override
    public void onRenderFrame(GL10 gl) {
        hideAllModels();
        super.onRenderFrame(gl);
    }

    private void hideAllModels() {
        nesController.setVisible(false);
    }

    @Override
    protected void initScene() {
        light = new DirectionalLight(.1f, 0, -1.0f);
        light.setColor(1.0f, 1.0f, 0.8f);
        light.setPower(1);

        getCurrentScene().addLight(light);

        try {
            // Load the NES controller model
            // (by ozzlennon: http://www.blendswap.com/blends/view/78487)

            LoaderOBJ objParser = new LoaderOBJ(this, R.raw.nes);
            objParser.parse();
            nesController.setScale(2);

            getCurrentScene().addChild(nesController);
            nesController.setVisible(false);

        } catch (ParsingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onOffsetsChanged(float v, float v1, float v2, float v3, int i, int i1) {

    }

    @Override
    public void onTouchEvent(MotionEvent motionEvent) {

    }
}
