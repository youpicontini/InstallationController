import controlP5.*;
import processing.core.PApplet;
import processing.core.PGraphics;

public class LedStripe {
    String id;
    PGraphics canvas;
    int x;
    int y;
    int rectWidth =200;
    int rectHeight =5;
    ControlP5 cp5;
    PApplet parent;

    Button ledButton;

    LedStripe(String _id, PGraphics _canvas, int _x, int _y, ControlP5 _cp5, PApplet _parent){
        id =_id;
        canvas = _canvas;
        x = _x;
        y = _y;
        cp5 = _cp5;
        parent = _parent;

        int c = parent.color(0, 0, 0, 1);
        CColor color = new CColor(c, c, c, c, c);

        ledButton = cp5.addButton(id)
                .setValue(0)
                .setPosition(200+x,60+y)
                .setSize(rectWidth,rectHeight)
                .setColor(color)
                .setSwitch(true)
                .moveTo("default");
    }

    void draw(){
        canvas.rect(x, y, rectWidth, rectHeight);
    }
}
