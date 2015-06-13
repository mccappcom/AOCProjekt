package pl.amu.edu.trebuh_divoolej.aocprojekt;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.vuforia.RajawaliVuforiaRenderer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by hubert on 31.05.15.
 */
public class AOCRenderer extends RajawaliVuforiaRenderer {
    public static final String TAG = "AOCRenderer";

    private AOCActivity activity;
    private DirectionalLight light;
    private Object3D wmiTextObject;
    private Object3D wmiLogoObject;
    private Sphere mSphere;

    public AOCRenderer(Context context) {
        super(context);
        activity = (AOCActivity)context;
        setFrameRate(60);
    }

    @Override
    protected void foundFrameMarker(int id, Vector3 position, Quaternion orientation) {
        // Ta metoda obsługuje frame markery - jest ich 512, png spakowane gdzieś w vuforia sdk.
        // Ten niżej ma id 420, ale tutaj odwołujesz się do nich wg. kolejności dodawania do AOCActivity
        // (w initApplicationAr() )

        if (id == 0) {
            wmiTextObject.setVisible(true);
            wmiTextObject.setPosition(position);
            wmiTextObject.setOrientation(orientation);
        } else if (id == 1) {
            mSphere.setVisible(true);
            mSphere.setPosition(position);
            mSphere.setOrientation(orientation);
        } else if (id == 2) {
            wmiTextObject.setVisible(true);
            wmiTextObject.setPosition(position);
            wmiTextObject.setOrientation(orientation);
        } else if (id == 3) {
            wmiTextObject.setVisible(true);
            wmiTextObject.setPosition(position);
            wmiTextObject.setOrientation(orientation);
        }

    }

    @Override
    protected void foundImageMarker(String s, Vector3 position, Quaternion orientation) {
        // Ta metoda obsługuje obrazek, w bazie danych aocImages.dat jest tylko zdjęcie WMI.
        // Bazy tworzy się na stronie https://developer.vuforia.com/target-manager

        if (s.equals("kampus")) {
            wmiLogoObject.setVisible(true);
            wmiLogoObject.setPosition(position);
            wmiLogoObject.setOrientation(orientation);
        }
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
        if (wmiTextObject != null) wmiTextObject.setVisible(false);
    }

    @Override
    protected void initScene() {
        light = new DirectionalLight(.1f, 0, -1.0f);
        light.setColor(1.0f, 1.0f, 0.8f);
        light.setPower(1);

        Material material = new Material();
        try {
            material.addTexture(new Texture("textura", R.drawable.textura));
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        mSphere = new Sphere(1, 24, 24);
        mSphere.setMaterial(material);

        getCurrentScene().addChild(mSphere);
        getCurrentScene().addLight(light);

        try {
            // Load the WMI text thing.
            final LoaderOBJ wmiTextParser = new LoaderOBJ(this, R.raw.wmitext_obj);
            wmiTextParser.parse();

            wmiTextObject = wmiTextParser.getParsedObject();
            wmiTextObject.setScale(50);

            getCurrentScene().addChild(wmiTextObject);
            wmiTextObject.setVisible(false);

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
