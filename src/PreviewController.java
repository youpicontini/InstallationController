import processing.core.PApplet;
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
    float[] currentKeyframeValuesDisplayed;
    LedStripe currentLedStripe,currentLedStripeHover;

    boolean editor;
    boolean animation;

    float offsetOpacity;
    boolean noise;
    boolean strobe;
    boolean strobbing;
    boolean flashing;
    long periodStrobe;
    int savedTime;
    Thread strobeThread;

    PreviewController (ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        LedStripesArray = new ArrayList<LedStripe>();

        editor = false;
        animation = false;

        offsetOpacity=1;
        noise=false;
        strobe=false;
        strobbing = false;
        flashing = false;
        periodStrobe = 1;
    }

    void setup(){
        of = parent.createGraphics(1050, 750);
        JSONArray currentCoordinates;
        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/setup.json";
        else
            tempPath = "installations\\CrystalNet\\setup.json";
        for(int i = 0; i < nb_elements; i++){
            currentCoordinates =  parent.loadJSONObject(tempPath).getJSONArray("coordinates").getJSONArray(i).getJSONObject(0).getJSONArray("line");
            LedStripesArray.add(new LedStripe(Integer.toString(i), of, currentCoordinates, cp5, parent, this));
        }
        currentLedStripe = LedStripesArray.get(0);
        savedTime = parent.millis();
        currentKeyframeValuesDisplayed = new float[nb_elements];

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
        if(noise){
            for (int i = 0; i < currentKeyframeValues.length; i++) {
                currentKeyframeValuesDisplayed[i] = parent.noise(parent.random(0,parent.width),parent.random(0,parent.height)) * offsetOpacity;
                LedStripesArray.get(i).display(currentKeyframeValuesDisplayed[i]);
            }
        }
        else if(strobe){
                strobeTick();
                if(strobbing){
                    for (int i = 0; i < currentKeyframeValues.length; i++) {
                        currentKeyframeValuesDisplayed[i] = 1 * offsetOpacity;
                        LedStripesArray.get(i).display(currentKeyframeValuesDisplayed[i]);
                    }
                }
                else{
                    for (int i = 0; i < currentKeyframeValues.length; i++) {
                        currentKeyframeValuesDisplayed[i] = 0;
                        LedStripesArray.get(i).display(currentKeyframeValuesDisplayed[i]);
                    }
                }

        }
        else {
            for (int i = 0; i < currentKeyframeValues.length; i++) {
                currentKeyframeValuesDisplayed[i] = currentKeyframeValues[i] * offsetOpacity;
                LedStripesArray.get(i).display(currentKeyframeValuesDisplayed[i]);
            }
        }
        of.endDraw();
        parent.image(of, 200, 60);
    }

    void setCurrentLedStripe(LedStripe ls) {
        currentLedStripe = ls;
    }
    void setCurrentLedStripeHover(LedStripe ls) {
        currentLedStripeHover = ls;
    }

    void strobeTick(){
        if(!(strobeThread instanceof Thread)) {
            strobeThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if(strobbing) strobbing = false;
                            else strobbing = true;
                            Thread.sleep(1000 / periodStrobe);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            strobeThread.start();
        }
    }

}
