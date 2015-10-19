import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.lang.System;
import java.util.ArrayList;

import controlP5.*;
import processing.core.PShape;
import processing.data.JSONArray;

public class PreviewController {

    PGraphics of;
    ArrayList<LedStripe> LedStripesArray;
    PApplet parent;
    ControlP5 cp5;
    int nb_elements;
    float[] currentKeyframeValues;
    LedStripe currentLedStripe,currentLedStripeHover;

    boolean editor;
    boolean animation;

    PreviewController (ControlP5 _cp5, PApplet _parent, int _nb_elements) {
        parent = _parent;
        cp5 = _cp5;
        nb_elements = _nb_elements;

        LedStripesArray = new ArrayList<LedStripe>();
        editor=false;
        animation = false;

    }

    void setup(){
        of = parent.createGraphics(1050, 750);
        JSONArray currentCoordinates;
        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/setup.json";
        else {
            System.out.println(System.getProperty("user.dir"));
            tempPath = System.getProperty("user.dir") + "\\installations\\CrystalNet\\setup.json";
            }
        for(int i = 0; i < nb_elements; i++){
            currentCoordinates =  parent.loadJSONObject(tempPath).getJSONArray("coordinates").getJSONArray(i).getJSONObject(0).getJSONArray("line");
            LedStripesArray.add(new LedStripe(Integer.toString(i), of, currentCoordinates, cp5, parent, this));
        }
        currentLedStripe = LedStripesArray.get(0);
        currentKeyframeValues = new float[nb_elements];
    }

    void setCurrentKeyframeValuesDisplayed(float[] val){
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
        of.endShape();
        of.endDraw();
        parent.image(of, 200, 60);
    }

    void setCurrentLedStripe(LedStripe ls) {
        currentLedStripe = ls;
    }
    void setCurrentLedStripeHover(LedStripe ls) {
        currentLedStripeHover = ls;
    }
}
