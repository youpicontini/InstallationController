import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;
import controlP5.*;
import processing.data.JSONArray;

public class PreviewController {

    PGraphics of;
    ArrayList<LedStripe> LedStripesArray;
    PApplet parent;
    ControlP5 cp5;
    int nb_elements;
    float[] currentKeyframeValues;
    LedStripe currentLedStripe;

    boolean clicked;

    PreviewController (ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        LedStripesArray = new ArrayList<LedStripe>();
    }

    void setup(){
        of = parent.createGraphics(1050, 750);
        JSONArray currentCoordinates;
        for(int i = 0; i < nb_elements; i++){
            currentCoordinates =  parent.loadJSONObject("installations\\CrystalNet\\setup.json").getJSONArray("coordinates").getJSONArray(i).getJSONObject(0).getJSONArray("line");
            LedStripesArray.add(new LedStripe(Integer.toString(i), of, currentCoordinates, cp5, parent));
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
            currentLedStripe =  LedStripesArray.get(i) ;
            currentLedStripe.display(currentKeyframeValues[i]);
            setCursor();

        }
        of.endDraw();
        parent.image(of, 200, 60);
    }

    void setCursor(){
        currentLedStripe.c.set(parent.mouseX - 200, parent.mouseY - 60);
    }
}
