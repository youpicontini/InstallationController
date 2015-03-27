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
        previewController = new PreviewController(cp5, parent);
        animationsManager = new AnimationsManager(cp5, parent, previewController);
    }

    public void setup(){

        animationsManager.setup();
        previewController.setup();
        //timeline.setup();
    }

    public void draw(){
        previewController.draw();
        //timeline.draw();
    }
}
