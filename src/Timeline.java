
import processing.core.PApplet;
import processing.core.PImage;

import java.awt.event.*;

public class Timeline {

    PApplet parent;
    int timeBegin = 0;
    int timeEnd = 125;
    int periodCenter = (timeEnd - timeBegin) / 2;//Point in timeline on which to center view
    int zoomLevel = timeEnd;//25/3
    int upTick = 40;
    PImage img;
    float y_timeline = 850;
    int maxTimeEnd=39;

    //Mouse Dragging
    float mX;
    float mY;
    float mDifX = 0f;
    float yearTick;
    int magicNumber = 200;

    Timeline(PApplet _parent) {

        parent = _parent;
    }
    public void setup() {
        parent.smooth();
        //img = parent.loadImage("img.jpg");
        //CENTER ON SPECIFIED DATE
        mX = parent.map(periodCenter, 0, timeEnd - timeBegin, parent.width / 2 - magicNumber * zoomLevel, magicNumber * zoomLevel)*-1;
        parent.println(mX);
        parent.addMouseWheelListener(new MouseWheelListener() {
                                         public void mouseWheelMoved(MouseWheelEvent mwe) {
                                             mouseWheel(mwe.getWheelRotation());
                                         }
                                     }
        );
    }

    public void draw() {
        parent.pushMatrix();
        parent.strokeWeight(10);
        parent.stroke(100);
        parent.line(0, y_timeline,  parent.width,  y_timeline);
        if( timeEnd>maxTimeEnd){
            mX =  parent.constrain(mX, parent.width / 2 -  magicNumber * zoomLevel, - parent.width / 2 + magicNumber * zoomLevel);
            parent.translate(mX, mY);
        }
        for (int i = 0; i < timeEnd - timeBegin; i++) {
            if( timeEnd>maxTimeEnd){
                yearTick = parent.map(i, 0, timeEnd - timeBegin, parent.width / 2 - magicNumber * zoomLevel, parent.width / 2 + magicNumber * zoomLevel);
            }
            else{
                yearTick = parent.map(i, 0, timeEnd - timeBegin, 0, parent.width);
            }
            parent.strokeWeight(.5f);
            if (i == 0)parent.text("FIRST", yearTick,  y_timeline+ upTick);
            else if (i == timeEnd-1)parent.text("LAST", yearTick,  y_timeline+ upTick);
            else if (timeEnd == 1)parent.text("FIRST", yearTick,  y_timeline+ upTick);
            else parent.text(i, yearTick, y_timeline+ upTick);

            parent.print(yearTick + " ");


            parent.fill(255);
            parent. line(yearTick, y_timeline - upTick, yearTick, y_timeline + upTick);
            parent.noFill();
        }
        parent.println("");
        parent.popMatrix();
    }



    public void mouseWheel(int delta) {
        if ((parent.mouseY > (y_timeline - upTick)) && (parent.mouseY < (y_timeline + upTick)) && (parent.width>upTick)){
            zoomLevel = zoomLevel + delta;
            zoomLevel = parent.constrain(zoomLevel, timeEnd/10, timeEnd/4);
        }
    }
}
