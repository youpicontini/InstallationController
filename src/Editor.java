import controlP5.*;
import processing.core.PApplet;

public class Editor {

    ControlP5 cp5;
    PApplet parent;

    //Timeline timeline;
    AnimationsManager animationsManager;
    PreviewController previewController;

    int nb_device;

    Editor(ControlP5 _cp5, PApplet _parent, int _nb_device) {
        cp5 = _cp5;
        parent = _parent;
        nb_device =_nb_device;

        //timeline = new Timeline(parent);
        previewController = new PreviewController(cp5, parent, nb_device);
        animationsManager = new AnimationsManager(cp5, parent, previewController, nb_device);
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
