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
        listAnimations = cp5.addListBox("listAnimations");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");
        buttonNewAnim = cp5.addButton("buttonNewAnim");
        labelNameAnimation = cp5.addTextlabel("labelNameAnimation");
        inputNewAnimName = cp5.addTextfield("inputNewAnimName");
    }

    public void setup(){
        indexAnim = 0;
        selectedIndex = -1;
        colorBG = parent.color(0);
        colorSelected = parent.color(150);

        listAnimations.setPosition(20, 80)
                        .setSize(170, 710)
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
                .setPosition(20,770)
                .setSize(170,40)
                .setGroup("groupEditor");

        labelNameAnimation.setText("Choose Animation")
                           .setPosition(200,40);

        inputNewAnimName.setLabel("new animation name")
                .setPosition(200, 20)
                .setSize(170, 20)
                .setFocus(true)
                .setGroup("groupEditor")
                .hide();
    }

    public void toggleVisibilityInputNewAnimation() {
        if (labelNameAnimation.isVisible()){
            labelNameAnimation.hide();
            inputNewAnimName.show();
            inputNewAnimName.setFocus(true);
        }
        else {
            labelNameAnimation.show();
            inputNewAnimName.hide();
        }
    }

    public void newAnimNameinput(String animName) {
        listAnimations.addItem(animName, indexAnim).setColorBackground(parent.color(0));
        indexAnim++;
        inputNewAnimName.hide();
        labelNameAnimation.show();
        if (indexAnim >= 32) listAnimations.scroll(1);
    }

    public void highlightSelectedAnim(int currentIndex){
        ListBoxItem item = listAnimations.getItem(currentIndex);
        labelNameAnimation.setText(item.getText());
        if(selectedIndex >= 0){//if something was previously selected
            ListBoxItem previousItem = listAnimations.getItem(selectedIndex);//get the item
            previousItem.setColorBackground(colorBG);//and restore the original bg colours
        }
        selectedIndex = currentIndex;//update the selected index
        listAnimations.getItem(selectedIndex).setColorBackground(colorSelected);//and set the bg colour to be the active/'selected one'...until a new selection is made and resets this, like above
    }

    void playAnimation(){
        System.out.print("play");
    }
    public void draw(){

    }

    public void newAnimation(String name){
        parent.print(name);
        newAnimNameinput(name);
        highlightSelectedAnim(indexAnim-1);

    }
}
