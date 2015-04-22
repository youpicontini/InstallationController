import controlP5.*;
import processing.core.PApplet;
import processing.core.PGraphics;

public class LedStripe {
    String id;
    PGraphics canvas;
    int x;
    int y;
    ControlP5 cp5;
    PApplet parent;

    int rectWidth = 200;
    int rectHeight = 5;
    float opacity;
    int transparent;
    int white;
    boolean selected=false;


    Button ledButton;

    LedStripe(String _id, PGraphics _canvas, int _x, int _y, ControlP5 _cp5, PApplet _parent){
        id = _id;
        canvas = _canvas;
        x = _x;
        y = _y;
        cp5 = _cp5;
        parent = _parent;

        transparent = parent.color(0, 0, 0, 1);
        white = parent.color(255, 255, 255, 255);

        CColor color = new CColor(transparent, transparent, transparent, transparent, transparent);

        ledButton = cp5.addButton(id)
                        .setValue(0)
                        .setPosition(200 + x, 60 + y)
                        .setSize(rectWidth, rectHeight)
                        .setColor(color)
                        .setSwitch(true)
                        .moveTo("default");
    }

    void display(float op){
        opacity = parent.map(op,0,1,1,255);
        canvas.pushStyle();
        if(selected){
            canvas.stroke(255,0,0);
        }
        canvas.fill(255, 255, 255, opacity);
        canvas.rect(x, y, rectWidth, rectHeight);
        canvas.popStyle();
    }
}
