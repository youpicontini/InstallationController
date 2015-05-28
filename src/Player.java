import controlP5.*;
import processing.core.PApplet;

public class Player {

    ControlP5 cp5;
    PApplet parent;
    Editor editor;

    Slider sliderMasterOpacity;
    Button buttonNoise;
    Button buttonStrobe;

    Player(ControlP5 _cp5, PApplet _parent, Editor _editor){
        cp5 = _cp5;
        parent = _parent;
        editor = _editor;


        sliderMasterOpacity = cp5.addSlider("sliderMasterOpacity");
        buttonNoise = cp5.addButton("buttonNoise");
        buttonStrobe = cp5.addButton("buttonStrobe");

    }

    public void setup(){

        sliderMasterOpacity.setLabel("Master opacity")
                .setPosition(1260, 80)
                .setGroup("groupPlayer")
                .setSize(130, 20)
                .setRange(0,1)
                .setValue(1.0f)
                .hide();

        buttonNoise.setLabel("Noise")
                .setPosition(1260, 110)
                .setGroup("groupPlayer")
                .setSize(40, 40)
                .setSwitch(true)
                .hide();

        buttonStrobe.setLabel("Strobe")
                .setPosition(1260, 160)
                .setGroup("groupPlayer")
                .setSize(40, 40)
                .setSwitch(true)
                .hide();

    }

    public void adaptOpacityFromMaster(float f){

        for(int i=0; i< editor.previewController.nb_elements;i++) {
            editor.previewController.offsetOpacity = f;
        }

    }

}
