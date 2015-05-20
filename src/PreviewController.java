import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;
import controlP5.*;
import processing.core.PVector;
import processing.data.JSONArray;

public class PreviewController {

    PGraphics of;
    ArrayList<LedStripe> LedStripesArray;
    PApplet parent;
    ControlP5 cp5;
    int nb_elements = 50;
    float[] currentKeyframeValues = new float[nb_elements];

    PreviewController (ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        LedStripesArray = new ArrayList<LedStripe>();
    }

    void setup(){
        of = parent.createGraphics(1050, 750);
        JSONArray currentCoordinates;
        for(int i = 0; i < nb_elements; i++){
            currentCoordinates =  parent.loadJSONObject("installation\\setup.json").getJSONArray("coordinates").getJSONArray(i).getJSONObject(0).getJSONArray("line");
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
            LedStripe currentLedStripe =  LedStripesArray.get(i) ;
            currentLedStripe.display(currentKeyframeValues[i]);
            parent.pushStyle();
            // Offset the cursor so we can see what is happening
            boolean ol = currentLedStripe.isOnLine(currentLedStripe.a, currentLedStripe.b, currentLedStripe.c, currentLedStripe.pj);
            if (ol) {
                parent.cursor(PConstants.HAND);
                parent.println(ol);
            }
            else {
                parent.cursor(PConstants.ARROW);
            }
        }
        of.endDraw();
        parent.image(of, 200, 60);

    }
}
