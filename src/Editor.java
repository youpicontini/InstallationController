import controlP5.*;
import processing.core.PApplet;


public class Editor {

    ControlP5 cp5;
    PApplet parent;

    Timeline timeline;
    AnimationsManager animationsManager;
    PreviewController previewController;

    Editor(ControlP5 _cp5, PApplet _parent) {
        cp5 = _cp5;
        parent = _parent;

        timeline = new Timeline(parent);
        animationsManager = new AnimationsManager(cp5, parent);
        previewController = new PreviewController(cp5, parent);
    }

    public void setup(){

        animationsManager.setup();
        previewController.setup();
    }

    public void draw(){
        previewController.draw();
    }
}
