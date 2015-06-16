package pl.amu.edu.trebuh_divoolej.aocprojekt;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.IDiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.AnimatedGIFTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.vuforia.RajawaliVuforiaRenderer;

import javax.microedition.khronos.opengles.GL10;

public class AOCRenderer extends RajawaliVuforiaRenderer {
    public static final String TAG = "AOCRenderer";

    private AOCActivity activity;
    private DirectionalLight light;
    private Object3D wmiTextObject;
    private Object3D bulbasaurObject;
    private Object3D companionCubeObject;
    private Object3D landscapeObject;
    private Object3D macObject;
    private Plane nyanCatPlane;
//    private Plane pythonPlane;
//    private Plane rubyPlane;
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
            landscapeObject.setVisible(true);
            landscapeObject.setPosition(position);
            landscapeObject.setOrientation(orientation);
        } else if (id == 1) {
            nyanCatPlane.setVisible(true);
            nyanCatPlane.setPosition(position);
            nyanCatPlane.setOrientation(orientation);
            try {
                nyanCatTexture.update();
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        } else if (id == 2) {
            wmiTextObject.setVisible(true);
            wmiTextObject.setPosition(position);
            wmiTextObject.setOrientation(orientation);
        } else if (id == 3) {
            companionCubeObject.setVisible(true);
            companionCubeObject.setPosition(position);
            companionCubeObject.setOrientation(orientation);
        } else if (id == 4) {
            macObject.setVisible(true);
            macObject.setPosition(position);
            macObject.setOrientation(orientation);
        }

    }

    @Override
    protected void foundImageMarker(String s, Vector3 position, Quaternion orientation) {
        // Ta metoda obsługuje obrazek, w bazie danych aocImages.dat jest tylko zdjęcie WMI.
        // Bazy tworzy się na stronie https://developer.vuforia.com/target-manager

        if (s.equals("kampus")) {
            wmiTextObject.setVisible(true);
            wmiTextObject.setPosition(position);
            wmiTextObject.setOrientation(orientation);
        }
        if (s.equals("grass")) {
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
        if (nyanCatPlane != null) nyanCatPlane.setVisible(false);
        if (bulbasaurObject != null) bulbasaurObject.setVisible(false);
        if (companionCubeObject != null) companionCubeObject.setVisible(false);
        if (landscapeObject != null) landscapeObject.setVisible(false);
        if (macObject != null) macObject.setVisible(false);
//        if (pythonPlane != null) pythonPlane.setVisible(false);
//        if (rubyPlane != null) rubyPlane.setVisible(false);
    }

    @Override
    protected void initScene() {
        light = new DirectionalLight(.1f, 0, -1.0f);
        light.setColor(1.0f, 1.0f, 1.0f);
        light.setPower(1);

        // Load the Nyan Cat stuff:

        Material nyanCatMaterial = new Material();
        nyanCatTexture = new AnimatedGIFTexture("textura", R.drawable.nyan);
        try {
            nyanCatMaterial.addTexture(nyanCatTexture);
            nyanCatMaterial.setColorInfluence(0);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        nyanCatPlane = new Plane(24, 24, 1, 1, Vector3.Axis.Y);
        nyanCatPlane.setScale(2.6);
        nyanCatPlane.setMaterial(nyanCatMaterial);
        getCurrentScene().addChild(nyanCatPlane);

//        Material pythonMaterial = new Material();
//        try {
//            pythonMaterial.addTexture(new Texture("python", R.drawable.python));
//        } catch (ATexture.TextureException e) {
//            e.printStackTrace();
//        }
//
//        pythonPlane = new Plane(24, 24, 1, 1, Vector3.Axis.Y);
//        pythonPlane.setScale(2.6);
//        pythonPlane.setMaterial(pythonMaterial);
//        getCurrentScene().addChild(pythonPlane);
//
//        Material rubyMaterial = new Material();
//        try {
//            rubyMaterial.addTexture(new Texture("ruby", R.drawable.ruby));
//        } catch (ATexture.TextureException e) {
//            e.printStackTrace();
//        }
//
//        rubyPlane = new Plane(24, 24, 1, 1, Vector3.Axis.Y);
//        rubyPlane.setScale(2.6);
//        rubyPlane.setMaterial(rubyMaterial);
//        getCurrentScene().addChild(rubyPlane);


        getCurrentScene().addLight(light);

        // Load all 3D objects:

        try {
            IDiffuseMethod diffuseMethod = new DiffuseMethod.Lambert();

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
            bulbasaurObject.setScale(12);

            Material bulbaMaterial = new Material();
            bulbaMaterial.setColorInfluence(0);
            bulbaMaterial.setDiffuseMethod(diffuseMethod);
            bulbaMaterial.addTexture(new Texture("bulbaTexture", R.drawable.dif_bulbasaur_01));

            bulbasaurObject.setMaterial(bulbaMaterial);

            getCurrentScene().addChild(bulbasaurObject);
            bulbasaurObject.setVisible(false);

            // Load the Companion cube:
            final LoaderOBJ cubeParser = new LoaderOBJ(this, R.raw.companioncube_obj);
            cubeParser.parse();

            companionCubeObject = cubeParser.getParsedObject();
            companionCubeObject.setScale(36);

            Material cubeMaterial = new Material();
            cubeMaterial.setColorInfluence(0);
            cubeMaterial.setDiffuseMethod(diffuseMethod);
            cubeMaterial.addTexture(new Texture("companionCubeTexture", R.drawable.metal_box_skin001));

            companionCubeObject.setMaterial(cubeMaterial);

            getCurrentScene().addChild(companionCubeObject);
            companionCubeObject.setVisible(false);

            // Load the low poly landscape:
            final LoaderOBJ landscapeParser = new LoaderOBJ(this, R.raw.landscape_obj);
            landscapeParser.parse();

            landscapeObject = landscapeParser.getParsedObject();
            landscapeObject.setScale(15.5);

            Material landscapeMaterial = new Material();
            landscapeMaterial.setColorInfluence(0);
            landscapeMaterial.setDiffuseMethod(diffuseMethod);
            landscapeObject.setMaterial(landscapeMaterial);

            getCurrentScene().addChild(landscapeObject);
            landscapeObject.setVisible(false);

            // Load the Mac model:
            final LoaderOBJ macParser = new LoaderOBJ(this, R.raw.lampa_obj);
            macParser.parse();

            macObject = macParser.getParsedObject();
            macObject.setScale(5);

            Material macMaterial = new Material();
            macMaterial.setColorInfluence(0);
            macMaterial.setDiffuseMethod(diffuseMethod);
//            macMaterial.addTexture(new Texture("MacTexture", R.drawable.imac2012));
            macObject.setMaterial(macMaterial);

            getCurrentScene().addChild(macObject);
            macObject.setVisible(false);


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
