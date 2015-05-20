import controlP5.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

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
    JSONArray coordinates;

    PVector a, b, c, pj;
    float closeness = 100;


    Button ledButton;

    LedStripe(String _id, PGraphics _canvas,JSONArray _coordinates, ControlP5 _cp5, PApplet _parent){
        id = _id;
        canvas = _canvas;
        cp5 = _cp5;
        parent = _parent;
        coordinates = _coordinates;

        transparent = parent.color(0, 0, 0, 1);
        white = parent.color(255, 255, 255, 255);

//        CColor color = new CColor(transparent, transparent, transparent, transparent, transparent);
//        ledButton = cp5.addButton(id)
//                        .setValue(0)
//                        .setPosition(200 + x, 60 + y)
//                        .setSize(rectWidth, rectHeight)
//                        .setColor(color)
//                        .setSwitch(true)
//                        .moveTo("default");

        // cursor(cross);
        a = new PVector(coordinates.getInt(0), coordinates.getInt(1));
        b = new PVector(coordinates.getInt(2), coordinates.getInt(3));
        parent.println(a,b);
        c = new PVector();
        pj = new PVector();

    }

    void display(float op){
        opacity = parent.map(op,0,1,1,255);
        canvas.pushStyle();
        canvas.strokeWeight(5);
        canvas.stroke(255, 255, 255, opacity);
        canvas.line(coordinates.getInt(0), coordinates.getInt(1), coordinates.getInt(2), coordinates.getInt(3));
        if(selected)
            canvas.stroke(255,0,0);
        else
            canvas.stroke(0, 255, 0);

        canvas.strokeWeight(1);
        canvas.line(coordinates.getInt(0), coordinates.getInt(1), coordinates.getInt(2), coordinates.getInt(3));

        canvas.popStyle();

    }

    boolean isOnLine(PVector v0, PVector v1, PVector p, PVector vp) {
        // Return minimum distance between line segment vw and point p
        PVector line = PVector.sub(v1, v0);
        float l2 = line.magSq();  // i.e. |w-v|^2 -  avoid a sqrt
        if (l2 == 0.0) {
            vp.set(v0);
            return false;
        }
        PVector pv0_line = PVector.sub(p, v0);
        float t = pv0_line.dot(line)/l2;
        pv0_line.normalize();
        vp.set(line);
        vp.mult(t);
        vp.add(v0);
        float d = PVector.dist(p, vp);
        if (t >= 0 && t <= 1 && d <= closeness)
            return true;
        else
            return false;
    }
}
