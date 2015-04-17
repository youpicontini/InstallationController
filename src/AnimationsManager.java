import controlP5.*;
import processing.core.PApplet;
import processing.data.JSONObject;
import java.io.File;

public class AnimationsManager {

    PApplet parent;
    ControlP5 cp5;
    PreviewController previewController;
    Animation currentAnim;
    String currentAnimName ="";

    // INTERFACE
    ListBox listAnimations;
    Button buttonNewAnim;
    Button buttonDeleteAnim;
    Textlabel labelNameAnimation;
    Textfield inputNewAnimName;
    Textfield inputNewAnimFPS;
    Button buttonPlayAnim;
    Button buttonStopAnim;
    Group groupAnimations;

    Button buttonNewKeyframe;
    Button buttonDeleteKeyframe;
    Slider sliderDeviceOpacity;

    int colorBG;
    int colorSelected;
    int selectedIndex;//no selection initially



    AnimationsManager(ControlP5 _cp5, PApplet _parent, PreviewController _previewController) {
        parent = _parent;
        cp5 = _cp5;
        previewController = _previewController;


        // INTERFACE
        listAnimations = cp5.addListBox("listAnimations");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");
        buttonStopAnim = cp5.addButton("buttonStopAnim");
        buttonNewAnim = cp5.addButton("buttonNewAnim");
        buttonDeleteAnim = cp5.addButton("buttonDeleteAnim");
        labelNameAnimation = cp5.addTextlabel("labelNameAnimation");
        inputNewAnimName = cp5.addTextfield("inputNewAnimName");
        inputNewAnimFPS = cp5.addTextfield("inputNewAnimFPS");
        groupAnimations = cp5.addGroup("groupAnimations");

        buttonNewKeyframe = cp5.addButton("buttonNewKeyframe");
        buttonDeleteKeyframe = cp5.addButton("buttonDeleteKeyframe");

        sliderDeviceOpacity = cp5.addSlider("sliderDeviceOpacity");


    }

    public void setup(){
        selectedIndex = -1;
        colorBG = parent.color(0);
        colorSelected = parent.color(150);

        listAnimations.setPosition(20, 80)
                       .setSize(170, 710)
                        .setItemHeight(20)
                         .setBarHeight(20)
                          .moveTo("global")
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

        buttonNewAnim.setLabel("       +")
                      .setValue(0)
                       .setPosition(150, 770)
                        .setSize(40, 40)
                         .setGroup("groupEditor");

        buttonDeleteAnim.setLabel("       -")
                         .setValue(0)
                          .setPosition(100, 770)
                           .setSize(40, 40)
                            .setGroup("groupEditor");

        labelNameAnimation.setText("Choose Animation")
                           .setPosition(200, 40)
                            .moveTo("global")
                             .show();

        inputNewAnimName.setLabel("new animation name")
                         .setPosition(200, 20)
                          .setSize(170, 20)
                           .setFocus(true)
                            .setGroup("groupEditor")
                             .hide();

        inputNewAnimFPS.setLabel("FPS")
                         .setPosition(380, 20)
                          .setSize(30, 20)
                           .setFocus(true)
                            .setGroup("groupEditor")
                             .setInputFilter(ControlP5.INTEGER)
                              .hide();

        listAnimations.getCaptionLabel()
                       .hide();

        buttonNewKeyframe.setLabel("       +")
                          .setValue(0)
                           .setPosition(1390, 720)
                            .setSize(40, 40)
                             .setGroup("groupEditor");

        buttonDeleteKeyframe.setLabel("       -")
                             .setValue(0)
                              .setPosition(1340, 720)
                               .setSize(40, 40)
                                .setGroup("groupEditor");

        sliderDeviceOpacity.setLabel("opacity")
                            .setPosition(1260, 80)
                             .setSize(130, 20)
                              .setRange(0,1)
                               .setValue(1.0f);
        loadAnimations();
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
            inputNewAnimFPS.hide();
        }
    }

    public void newAnimNameinput(String animName) {

        listAnimations.addItem(animName, getLengthListbox(listAnimations)).setColorBackground(parent.color(0));
        inputNewAnimName.hide();
        labelNameAnimation.show();
        if (getLengthListbox(listAnimations) >= 32) listAnimations.scroll(1);
    }

    public void highlightSelectedAnim(int currentIndex){

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
    void displayAnimation(int index){
        ListBoxItem item = listAnimations.getItem(index);
        String animName = listAnimations.getItem(index).getName().replaceAll(" ", "_");
        String configFilePath;
        JSONObject configjson;
        String path;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            configFilePath = "animations/"+animName+"/config.json";
            path = "animations/" + animName + "/keyframes/0.json";
        }
        else {
            configFilePath = "animations\\"+animName+"\\config.json";
            path = "animations\\" + animName + "\\keyframes\\0.json";
        }
        configjson = parent.loadJSONObject(new File(configFilePath));
        labelNameAnimation.setText(item.getText()+"                 "+configjson.getInt("fps")+" FPS");
        previewController.displayKeyframe(path);
        updateCurrentAnim(index);
    }


    public void newAnimation(String name, int fps){
        currentAnim = new Animation(name, fps, cp5, parent);
        newAnimNameinput(name);
        highlightSelectedAnim(getLengthListbox(listAnimations)-1);
    }

    public void updateCurrentAnim(int id){
        String name = listAnimations.getItem(id).getName();
        String configFilePath = "animations\\"+name.replaceAll(" ","_")+"\\config.json";
        JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
        currentAnim = new Animation(name,configjson.getInt("fps"), cp5, parent);
    }

    public int getLengthListbox(ListBox list) {
        String[][] tempString = list.getListBoxItems();
        return tempString.length;
    }

    public void deleteAnimation(int index){
        if(getLengthListbox(listAnimations) != 0) {
            String name = listAnimations.getItem(index).getName();
            String id = name.replaceAll(" ","_");
            File animDirectory;
            if(System.getProperty("os.name").equals("Mac OS X")) {
                animDirectory = new File("animations/" + id);
            }
            else{
                animDirectory = new File("animations\\" + id);
            }
            listAnimations.removeItem(name);
            deleteFolder(animDirectory);
            selectedIndex--;
            highlightSelectedAnim(getLengthListbox(listAnimations) - 1);
        }
    }

    public void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    parent.println(f);
                    deleteFile(f);
                }
            }
        }
        parent.println(folder);
        deleteFile(folder);
    }

    void deleteFile(File file) {
        try {
            boolean isDeleted = file.delete();
            if(isDeleted) {
                parent.println("Success");
            } else {
                parent.println("Fail");
            }
        } catch (SecurityException e) {
            parent.println("File : "+e.getMessage());
        }
    }

    void loadAnimations() {
        File[] files = new File("animations").listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String configFilePath;
                if(System.getProperty("os.name").equals("Mac OS X")) {
                    configFilePath = "animations/" + file.getName() + "/config.json";
                }
                else {
                    configFilePath= "animations\\" + file.getName() + "\\config.json";
                }
                JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
                listAnimations.addItem(configjson.getString("name"), getLengthListbox(listAnimations));
            }
        }
    }
}
