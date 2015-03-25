import controlP5.*;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.io.File;

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
    Button buttonStopAnim;

    int colorBG;
    int colorSelected;
    int indexAnim;
    int selectedIndex;//no selection initially

    AnimationsManager(ControlP5 _cp5, PApplet _parent) {
        parent = _parent;
        cp5 = _cp5;

        AnimationsArray = new ArrayList<Animation>();

        // INTERFACE
        listAnimations = cp5.addListBox("listAnimations");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");
        buttonStopAnim = cp5.addButton("buttonStopAnim");
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

        buttonStopAnim.setLabel("stop")
                        .setValue(0)
                        .setPosition(1260, 770)
                        .setGroup("groupEditor")
                        .setSize(170, 40)
                        .hide();

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


        loadAnimations();

//        configjson = loadJSONObject("config.json");
//        String nameAnim = json.getString("name");
//        String name = json.getString("name");
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
        buttonPlayAnim.hide();
        buttonStopAnim.show();
        System.out.print("play");
    }

    void stopAnimation(){
        buttonStopAnim.hide();
        buttonPlayAnim.show();
        System.out.print("stop");
    }
    public void draw(){

    }

    public void newAnimation(String name){
        Animation anim = new Animation(name,25,parent);
        AnimationsArray.add(anim);
        newAnimNameinput(name);
        highlightSelectedAnim(indexAnim-1);
    }
    public void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                String configFilePath= "animations\\"+file.getName()+"\\config.json";
                JSONObject configjson = parent.loadJSONObject(configFilePath);
                listAnimations.addItem(configjson.getString("name"),indexAnim);
                parent.println(configFilePath, configjson);
            }
        }
    }
    public void loadAnimations(){
        //TODO attention au chemin, peut etre different sur OSX,LINUX ...
        String path= "animations";
        File[] files = new File(path).listFiles();
        parent.println(System.getProperty(path));
        showFiles(files);
    }
}
