import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import controlP5.*;

public class PreviewController {

    PGraphics of;
    ArrayList<LedStripe> LedStripesArray;
    PApplet parent;
    ControlP5 cp5;
    int nb_elements = 4;
    float[] currentKeyframeValues = new float[nb_elements];

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

    void setCurrentKeyframeValues(float[] val){
        currentKeyframeValues = val;
    }

    void unselectDevices(){
        for(int i=0; i<nb_elements; i++) {
            LedStripesArray.get(i).selected = false;
        }
    }

    void draw() {
        of.beginDraw();
        of.background(parent.color(125));
        for (int i = 0; i < currentKeyframeValues.length; i++) {
            LedStripesArray.get(i).display(currentKeyframeValues[i]);
        }
        of.endDraw();
        parent.image(of, 200, 60);
    }
}
