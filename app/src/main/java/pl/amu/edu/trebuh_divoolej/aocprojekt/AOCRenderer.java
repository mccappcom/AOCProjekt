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
import org.rajawali3d.materials.textures.AnimatedGIFTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
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
    private Object3D bulbasaurObject;
    private Plane plane;
    private AnimatedGIFTexture nyanCatTexture;

    public AOCRenderer(Context context) {
        super(context);
        activity = (AOCActivity)context;
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
            plane.setVisible(true);
            plane.setPosition(position);
            plane.setOrientation(orientation);
            try {
                nyanCatTexture.update();
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        } else if (id == 2) {
            bulbasaurObject.setVisible(true);
            bulbasaurObject.setPosition(position);
            bulbasaurObject.setOrientation(orientation);
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
            bulbasaurObject.setVisible(true);
            bulbasaurObject.setPosition(position);
            bulbasaurObject.setOrientation(orientation);
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
        if (plane != null) plane.setVisible(false);
        if (bulbasaurObject != null) bulbasaurObject.setVisible(false);
    }

    @Override
    protected void initScene() {
        light = new DirectionalLight(.1f, 0, -1.0f);
        light.setColor(1.0f, 1.0f, 1.0f);
        light.setPower(1);

        Material nyanCatMaterial = new Material();
        nyanCatTexture = new AnimatedGIFTexture("textura", R.drawable.nyan);
        try {
            nyanCatMaterial.addTexture(nyanCatTexture);
            nyanCatMaterial.setColorInfluence(0);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        plane = new Plane(24, 24, 1, 1, Vector3.Axis.Y);
        plane.setScale(2.6);
        plane.setMaterial(nyanCatMaterial);

        getCurrentScene().addChild(plane);
        getCurrentScene().addLight(light);

        try {
            // Load the WMI text thing.
            final LoaderOBJ wmiTextParser = new LoaderOBJ(this, R.raw.wmitext_obj);
            wmiTextParser.parse();

            wmiTextObject = wmiTextParser.getParsedObject();
            wmiTextObject.setScale(50);

            getCurrentScene().addChild(wmiTextObject);
            wmiTextObject.setVisible(false);

            // Load the Bulbasaur:
            final LoaderOBJ bulbaParser = new LoaderOBJ(this, R.raw.bulba_obj);
            bulbaParser.parse();

            bulbasaurObject = bulbaParser.getParsedObject();
            bulbasaurObject.setScale(7);

            Material bulbaMaterial = new Material();
            bulbaMaterial.setColorInfluence(0);
            bulbaMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
            bulbaMaterial.addTexture(new Texture("bulbaTexture", R.drawable.dif_bulbasaur_01));

            bulbasaurObject.setMaterial(bulbaMaterial);

            getCurrentScene().addChild(bulbasaurObject);
            bulbasaurObject.setVisible(false);

        } catch (ParsingException | ATexture.TextureException e) {
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
