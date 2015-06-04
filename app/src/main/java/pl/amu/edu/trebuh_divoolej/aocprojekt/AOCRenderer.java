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
    public static final String TAG = "AOCRenderer";

    private AOCActivity activity;
    private DirectionalLight light;
    private Object3D cubeObject;

    public AOCRenderer(Context context) {
        super(context);
        activity = (AOCActivity)context;
    }

    @Override
    protected void foundFrameMarker(int id, Vector3 vector3, Quaternion quaternion) {
        // Ta metoda obsługuje frame markery - jest ich 512, png spakowane gdzieś w vuforia sdk.
        // Ten niżej ma id 420, ale tutaj odwołujesz się do nich wg. kolejności dodawania do AOCActivity
        // (w initApplicationAr() )
        if(id == 0) {
            cubeObject.setVisible(true);
            cubeObject.setPosition(vector3);
            cubeObject.setOrientation(quaternion);
        }
    }

    @Override
    protected void foundImageMarker(String s, Vector3 vector3, Quaternion quaternion) {
        // Ta metoda obsługuje obrazek, w bazie danych aocImages.dat jest tylko zdjęcie WMI.
        // Bazy tworzy się na stronie https://developer.vuforia.com/target-manager
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
        if (cubeObject != null) cubeObject.setVisible(false);
    }

    @Override
    protected void initScene() {
        light = new DirectionalLight(.1f, 0, -1.0f);
        light.setColor(1.0f, 1.0f, 0.8f);
        light.setPower(1);

        getCurrentScene().addLight(light);

        try {
            // Load the Companion Cube model
            // (by GamerFreaq: http://www.blendswap.com/blends/view/4587)

            LoaderOBJ objParser = new LoaderOBJ(this, R.raw.bike_obj);
            objParser.parse();

            cubeObject = objParser.getParsedObject();
            cubeObject.setScale(5);

            getCurrentScene().addChild(cubeObject);
            cubeObject.setVisible(false);

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
