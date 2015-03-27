import controlP5.*;
import processing.core.PApplet;
import processing.data.JSONObject;
import java.io.File;

public class AnimationsManager {

    PApplet parent;
    ControlP5 cp5;
    PreviewController previewController;
    Animation currentAnim;

    // INTERFACE
    ListBox listAnimations;
    Button buttonNewAnim;
    Button buttonDeleteAnim;
    Textlabel labelNameAnimation;
    Textfield inputNewAnimName;
    Button buttonPlayAnim;
    Button buttonStopAnim;
    Group groupAnimations;

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

        groupAnimations = cp5.addGroup("groupAnimations");
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
                       .setPosition(150,770)
                        .setSize(40,40)
                         .setGroup("groupEditor");

        buttonDeleteAnim.setLabel("       -")
                         .setValue(0)
                          .setPosition(100, 770)
                           .setSize(40, 40)
                            .setGroup("groupEditor");

        labelNameAnimation.setText("Choose Animation")
                           .setPosition(200,40)
                            .moveTo("global")
                             .show();

        inputNewAnimName.setLabel("new animation name")
                         .setPosition(200, 20)
                          .setSize(170, 20)
                           .setFocus(true)
                            .setGroup("groupEditor")
                             .hide();

        listAnimations.getCaptionLabel()
                       .hide();

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
        }
    }

    public void newAnimNameinput(String animName) {

        listAnimations.addItem(animName, getLengthListbox(listAnimations)).setColorBackground(parent.color(0));
        inputNewAnimName.hide();
        labelNameAnimation.show();
        if (getLengthListbox(listAnimations) >= 32) listAnimations.scroll(1);
    }

    public void highlightSelectedAnim(int currentIndex){

        parent.println("highlight");
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
        parent.println("display");
        ListBoxItem item = listAnimations.getItem(index);
        String animName = listAnimations.getItem(index).getName().replaceAll(" ", "_");

        String configFilePath = "animations\\"+animName+"\\config.json";
        JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
        labelNameAnimation.setText(item.getText()+"                 "+configjson.getInt("fps")+" FPS");
        String path = "animations\\" + animName + "\\keyframes\\0.json";
        previewController.displayKeyframe(path);
    }

    public void newAnimation(String name){
        currentAnim =new Animation(name,25,parent);
        newAnimNameinput(name);
        highlightSelectedAnim(getLengthListbox(listAnimations)-1);
    }


    public int getLengthListbox(ListBox list) {
        String[][] tempString = list.getListBoxItems();
        return tempString.length;
    }


    public void deleteAnimation(int id){
        if(getLengthListbox(listAnimations) != 0) {
            String name = listAnimations.getItem(id).getName();
            File animDirectory = new File("animations\\" + name);
            listAnimations.removeItem(name);
            deleteFolder(animDirectory);
            highlightSelectedAnim(id - 1);
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
        //TODO attention au chemin, peut-être different sur OSX,LINUX...
        for (File file : files) {
            if (file.isDirectory()) {
                String configFilePath= "animations\\"+file.getName()+"\\config.json";
                JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
                listAnimations.addItem(configjson.getString("name"), getLengthListbox(listAnimations));
            }
        }
    }
}