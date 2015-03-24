import processing.core.PApplet;
import processing.core.PGraphics;
import java.util.ArrayList;
import controlP5.*;

public class PreviewController {

    PGraphics of;
    ArrayList<LedStripe> LedStripesArray;
    PApplet parent;
    ControlP5 cp5;

    PreviewController (ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        LedStripesArray = new ArrayList<LedStripe>();

    }

    void setup(){
        of = parent.createGraphics(1050, 750);
        for(int i = 0; i < 5; i++){
            //LedStripesArray.add(new LedStripe(Integer.toString(i), of, i*20, i*10, cp5));
        }
    }

    void draw(){
        of.beginDraw();
        of.background(parent.color(125));
        for (int i = 0; i < LedStripesArray.size(); i++) {
            LedStripesArray.get(i).draw();
        }
        of.endDraw();
        parent.image(of, 200, 60);
    }
}
