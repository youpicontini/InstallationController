import controlP5.*;
import controlP5.Button;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.data.JSONArray;
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
    Group groupAnimations;

    Button buttonNewKeyframe;
    Button buttonDeleteKeyframe;
    Textlabel labelKeyframeName;
    Slider sliderDeviceOpacity;

    int colorBG;
    int colorSelected;
    int selectedIndex;//no selection initially

    boolean animations_loaded=false;

    int currentAnimIndex;
    int nb_elements;


    AnimationsManager(ControlP5 _cp5, PApplet _parent, PreviewController _previewController) {
        parent = _parent;
        cp5 = _cp5;
        previewController = _previewController;


        // INTERFACE
        listAnimations = cp5.addListBox("listAnimations");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");
        buttonNewAnim = cp5.addButton("buttonNewAnim");
        buttonDeleteAnim = cp5.addButton("buttonDeleteAnim");
        labelNameAnimation = cp5.addTextlabel("labelNameAnimation");
        inputNewAnimName = cp5.addTextfield("inputNewAnimName");
        inputNewAnimFPS = cp5.addTextfield("inputNewAnimFPS");
        groupAnimations = cp5.addGroup("groupAnimations");

        labelKeyframeName = cp5.addTextlabel("labelKeyframeName");
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
                             .setColorForeground(parent.color(150))
                              .hide();

        buttonPlayAnim.setLabel("play")
                       .setValue(0)
                        .setPosition(1260, 770)
                         .moveTo("global")
                          .setSize(170, 40)
                            .setSwitch(true)
                            .hide();

        buttonNewAnim.setLabel("       +")
                      .setValue(0)
                       .setPosition(150, 770)
                        .setSize(40, 40)
                         .setGroup("groupEditor")
                          .hide();

        buttonDeleteAnim.setLabel("       -")
                .setValue(0)
                .setPosition(100, 770)
                .setSize(40, 40)
                .setGroup("groupEditor")
                             .hide();

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
                             .setGroup("groupEditor")
                            .hide();

        buttonDeleteKeyframe.setLabel("       -")
                             .setValue(0)
                .setPosition(1340, 720)
                .setSize(40, 40)
                .setGroup("groupEditor")
                .hide();

        labelKeyframeName.setLabel("NAME KEYFRAME")
                          .setValue(0)
                .setPosition(1620, 400)
                .setFont(parent.createFont("", 30))
                .setGroup("groupEditor");

        sliderDeviceOpacity.setLabel("opacity")
                .setPosition(1260, 80)
                .setSize(130, 20)
                .setRange(0,1)
                .setValue(1.0f)
                .setGroup("groupEditor")
                .hide();



        loadAnimations();
        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            tempPath = "installations/CrystalNet/setup.json";
        }
        else {
            tempPath = "installations\\CrystalNet\\setup.json";
        }

        nb_elements = parent.loadJSONObject(tempPath).getInt("nb_elements");
        previewController.setCurrentKeyframeValues(new float[nb_elements]);
        setNbElementsPreviewController(nb_elements);
        //createTemplateKeyframe(nb_elements);
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
        currentAnim.play();
        System.out.print("playing..");
    }

    void stopAnimation(){
        currentAnim.stop();
        System.out.print("stop");
    }
    void displayAnimation(int index){
        ListBoxItem item = listAnimations.getItem(index);
        String animName = listAnimations.getItem(index).getName().replaceAll(" ", "_");
        String configFilePath;
        JSONObject configjson;

        if(System.getProperty("os.name").equals("Mac OS X"))
            configFilePath = "installations/CrystalNet/animations/"+animName+"/config.json";

        else
            configFilePath = "installations\\CrystalNet\\animations\\"+animName+"\\config.json";

        configjson = parent.loadJSONObject(new File(configFilePath));
        labelNameAnimation.setText(item.getText()+"                 "+configjson.getInt("fps")+" FPS");
        updateCurrentAnim(index);
        currentAnim.loadKeyframe(0);
        currentAnim.sendCurrentValuesToPreviewController();

    }


    public void newAnimation(String name, int fps){
        currentAnim = new Animation(name, fps, nb_elements, cp5, parent, previewController);
        currentAnim.addFirstKeyframe();
        newAnimNameinput(name);
        highlightSelectedAnim(getLengthListbox(listAnimations)-1);
    }



    public void updateCurrentAnim(int id){
        if(animations_loaded)
            currentAnim.saveKeyframe(currentAnim.currentKeyframeIndex);
        animations_loaded = true;
        String name = listAnimations.getItem(id).getName();

        String configFilePath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            configFilePath = "installations/CrystalNet/animations/"+name.replaceAll(" ","_")+"/config.json";
        else
            configFilePath = "installations\\CrystalNet\\animations\\"+name.replaceAll(" ","_")+"\\config.json";
        JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
        currentAnim = new Animation(name,configjson.getInt("fps"), nb_elements, cp5, parent, previewController);
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
                animDirectory = new File("installations/CrystalNet/animations/" + id);
            }
            else{
                animDirectory = new File("installations\\CrystalNet\\animations\\" + id);
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
            boolean isDeleted = false;
            parent.cursor(PConstants.WAIT);
            while(!isDeleted) {
                isDeleted = file.delete();
                if (isDeleted) {
                    parent.println("Success");
                } else {
                    parent.println("Fail");
                }
            }
            parent.cursor(PConstants.ARROW);
        } catch (SecurityException e) {
            parent.println("File : "+e.getMessage());
        }
    }

    void loadAnimations() {
        File[] files;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            files = new File("installations/CrystalNet/animations").listFiles();
        }
        else {
            files = new File("installations\\CrystalNet\\animations").listFiles();
        }
        for (File file : files) {
            if (file.isDirectory()) {
                String configFilePath;
                if(System.getProperty("os.name").equals("Mac OS X")) {
                    configFilePath = "installations/CrystalNet/animations/" + file.getName() + "/config.json";
                }
                else {
                    configFilePath= "installations\\CrystalNet\\animations\\" + file.getName() + "\\config.json";
                }
                JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
                listAnimations.addItem(configjson.getString("name"), getLengthListbox(listAnimations));
            }
        }
    }

    void setCurrentAnimIndex(int i){
        currentAnimIndex = i;
    }

    void setNbElementsPreviewController(int i){
        previewController.nb_elements = i;
    }

    void createTemplateKeyframe(int nb_elmts){
        String templateKfFilePath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            templateKfFilePath = "installations/CrystalNet/animations/TEMPLATE_keyframe.json";

        else
            templateKfFilePath = "installations\\CrystalNet\\animations\\TEMPLATE_keyframe.json";

        JSONObject templateKfjson = new JSONObject();
        templateKfjson.setString("id", currentAnimName);
        JSONArray values = new JSONArray();
        JSONObject val = new JSONObject();
        val.setString("type","led");
        JSONArray obj = new JSONArray();
        JSONObject ledObj = new JSONObject();
        JSONObject params = new JSONObject();
        params.setFloat("opacity",0);
        ledObj.setJSONObject("params", params);
        for(int i = 0; i <nb_elmts; i++) {
            ledObj.setInt("id", i);
            parent.println(i);
            parent.println(ledObj);
            obj.setJSONObject(i, ledObj);
//            JSONObject templateKfjson = parent.loadJSONObject(new File(templateKfFilePath));
//            templateKfjson.setString("Id",currentAnimName);
//            templateKfjson.setJSONArray("outputs",i).setJSONObject(0).setJSONArray("objects").setJSONObject(i).setInt("id",i).setJSONObject("params").setFloat("opacity", 0);
        }
        val.setJSONArray("objects",obj);
        values.setJSONObject(0,val);
        templateKfjson.setJSONArray("outputs", values);
        parent.println(templateKfjson);
    }
}
