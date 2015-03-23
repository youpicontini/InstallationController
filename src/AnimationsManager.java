import controlP5.*;
import processing.core.PApplet;
import java.util.ArrayList;

public class AnimationsManager {

    PApplet parent;
    ControlP5 cp5;

    ArrayList<Animation> AnimationsArray;
    
    // INTERFACE
    ListBox listAnimations;
    Button buttonNewAnim;
    Textlabel labelNameAnimation;
    Textfield inputNewAnimName;
    Button buttonPlayAnim;

    int colorBG;
    int colorSelected;
    int indexAnim;
    int selectedIndex;//no selection initially

    AnimationsManager(ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        // INTERFACE
        listAnimations = new ListBox(cp5, "listAnimations");
        buttonPlayAnim = new Button(cp5, "buttonPlayAnim");
        buttonNewAnim = new Button(cp5, "buttonNewAnim");
        labelNameAnimation = new Textlabel(cp5, "labelNameAnimation", 200, 40);
        inputNewAnimName = new Textfield(cp5, "inputNewAnimName");
    }

    public void setup(){
        indexAnim = 0;
        selectedIndex = -1;
        colorBG = parent.color(0);
        colorSelected = parent.color(150);

        listAnimations.setPosition(20, 80)
                        .setSize(170, 740)
                        .setItemHeight(20)
                        .setBarHeight(20)
                        .setColorBackground(parent.color(50))
                        .setColorActive(parent.color(150))
                        .setColorForeground(parent.color(150));


        buttonPlayAnim.setLabel("play (p)")
                        .setValue(0)
                        .setPosition(1260, 770)
                        .setGroup("groupEditor")
                        .setSize(170, 40);

        buttonNewAnim.setLabel("new animation")
                .setValue(0)
                .setPosition(20,830)
                .setSize(170,20)
                .setGroup("groupEditor");

        labelNameAnimation.setText("Choose Animation");

        inputNewAnimName.setLabel("new animation name")
                .setPosition(200,20)
                .setSize(170,20)
                .setFocus(true)
                .setGroup("Animations")
                .hide();
    }

    public void draw(){

    }

    public void newAnimation(){

    }
}
