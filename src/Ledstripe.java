import controlP5.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONArray;

public class LedStripe {
    String id;
    PGraphics canvas;
    ControlP5 cp5;
    PApplet parent;
    PreviewController pc;

    float opacity;
    int transparent;
    int white;
    boolean selected=false;
    boolean severalSelected=false;
    JSONArray coordinates;

    boolean ol;
    PVector a, b, c, pj;
    float closeness = 10;

    LedStripe(String _id, PGraphics _canvas,JSONArray _coordinates, ControlP5 _cp5, PApplet _parent, PreviewController _pc){
        id = _id;
        canvas = _canvas;
        cp5 = _cp5;
        parent = _parent;
        coordinates = _coordinates;
        pc = _pc;

        transparent = parent.color(0, 0, 0, 1);
        white = parent.color(255, 255, 255, 255);


        a = new PVector(coordinates.getInt(0), coordinates.getInt(1));
        b = new PVector(coordinates.getInt(2), coordinates.getInt(3));
        c = new PVector();
        pj = new PVector();

    }

    void display(float op){
        opacity = parent.map(op,0,1,1,255);

        // Offset the cursor so we can see what is happening
        ol = isOnLine(a, b, c, pj);

        canvas.pushStyle();
        canvas.strokeWeight(5);
        canvas.stroke(255, 255, 255, opacity);
        canvas.line(coordinates.getInt(0), coordinates.getInt(1), coordinates.getInt(2), coordinates.getInt(3));
        c.set(parent.mouseX - 200, parent.mouseY - 60);
        canvas.stroke(0, 255, 0);
        if(pc.editor && pc.animation) {
            if (ol) {
                pc.setCurrentLedStripeHover(this);
                canvas.stroke(0, 0, 255);
            }
            if (selected) {
                pc.setCurrentLedStripe(this);
                canvas.stroke(255, 0, 0);
            }
        }
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

    String getId(){
        return id;
    }
}
