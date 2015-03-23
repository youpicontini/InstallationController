import controlP5.*;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Ledstripe {
    String id;
    PGraphics canvas;
    int x;
    int y;
    ControlP5 cp5;
    PApplet parent;

    Button ledButton;

    Ledstripe(String _id, PGraphics _canvas, int _x, int _y, ControlP5 _cp5, PApplet _parent){
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
                .setSize(200,5)
                .setColor(color)
                .setSwitch(true)
                .moveTo("editor");
    }

    void draw(){
        canvas.rect(x, y, 200, 5);
    }
}
