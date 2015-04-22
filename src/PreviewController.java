import processing.core.PApplet;
import processing.core.PGraphics;
import java.util.ArrayList;
import java.io.File;
import controlP5.*;
import processing.data.JSONArray;

public class PreviewController {

    PGraphics of;
    ArrayList<LedStripe> LedStripesArray;
    PApplet parent;
    ControlP5 cp5;
    int nb_elements = 4;
    float[] keyframe = new float[nb_elements];

    int currentKeyframe;
    String currentAnimName;

    PreviewController (ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        LedStripesArray = new ArrayList<LedStripe>();
    }

    void setup(){
        of = parent.createGraphics(1050, 750);
        for(int i = 0; i < nb_elements; i++){
            LedStripesArray.add(new LedStripe(Integer.toString(i), of, i*40, i*40, cp5, parent));
        }
    }

    void displayKeyframe() {

        String path;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            path = "animations/" + currentAnimName + "/keyframes/" + currentKeyframe + ".json";
        }
        else {
            path = "animations\\" + currentAnimName + "\\keyframes\\" + currentKeyframe + ".json";
        }
        JSONArray outputs = parent.loadJSONObject(new File(path)).getJSONArray("outputs").getJSONObject(0).getJSONArray("objects");
        float[] out = new float[outputs.size()];
        for (int i=0; i<outputs.size(); i++) {
            out[i] = outputs.getJSONObject(i).getJSONObject("params").getFloat("opacity");
        }
        parent.println("previewcontroller   " + out);
        keyframe=out;
    }

    void setCurrentKeyframe(int i){
        currentKeyframe = i;
    }

    void setCurrentKeyframeValues(float[] val){
        keyframe = val;
    }

    void setCurrentAnimName(String n){
        currentAnimName = n;
    }

    void unselectDevices(){
        for(int i=0; i<nb_elements; i++) {
            LedStripesArray.get(i).selected = false;
        }
    }

    void draw() {
        of.beginDraw();
        of.background(parent.color(125));
        for (int i = 0; i < keyframe.length; i++) {
            LedStripesArray.get(i).display(keyframe[i]);
        }
        of.endDraw();
        parent.image(of, 200, 60);
    }
}
