import controlP5.*;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

public class AppController {
    PApplet parent;
    ControlP5 cp5;

    Editor editor;
    Player player;

    Textlabel projectName;
    Textlabel framerateProject;
    Tab tabEditor;
    Tab tabPlayer;
    Group groupEditor;
    Group groupPlayer;
    ListBox listInstallations;

    SoundSpectrum soundSpectrum;

    int nb_device;

    AppController(ControlP5 _cp5, SoundSpectrum _soundSpectrum, int _nb_device, PApplet _parent) {

        parent = _parent;
        cp5 = _cp5;
        soundSpectrum = _soundSpectrum;
        nb_device = _nb_device;

        editor = new Editor(cp5, parent, nb_device);
        player = new Player(cp5, parent, editor, soundSpectrum, nb_device);


        /* [INTERFACE] TABS*/
        tabEditor = cp5.addTab("default");
        tabPlayer = cp5.addTab("tabPlayer");

        /* [INTERFACE] GROUPS*/
        groupEditor = cp5.addGroup("groupEditor");
        groupPlayer = cp5.addGroup("groupPlayer");


        listInstallations = cp5.addListBox("listInstallations");
        projectName = cp5.addTextlabel("projectName");
        framerateProject = cp5.addTextlabel("framerateProject");
        //loadInstallations();
    }

    public void setup(){

        editor.setup();
        player.setup();

        /* [INTERFACE] */
        tabEditor.activateEvent(true)
                .setLabel("editor")
                .setWidth(70)
                .setHeight(30);

        tabPlayer.activateEvent(true)
                .setLabel("player")
                .setWidth(70)
                .setHeight(30);

        groupEditor.moveTo(tabEditor);
        groupPlayer.moveTo(tabPlayer);

        projectName.setText(InstallationController.PROJECTNAME)
                .setColor(0xff000000)
                .moveTo("global")
                .setPosition(880,20)
                .setFont(parent.createFont("", 30));

        framerateProject.moveTo(tabPlayer)
                        .setPosition(800, 20)
                        .setFont(parent.createFont("", 30));

        /*listInstallations.setPosition(735, 80)
                .setSize(170, 710)
                .setItemHeight(20)
                .setBarHeight(20)
                .moveTo("global")
                .setColorBackground(parent.color(50))
                .setColorActive(parent.color(150))
                .setColorForeground(parent.color(150));*/
    }

    public void draw(){
        framerateProject.setText(""+Math.round(parent.frameRate)).setColor(0xff000000);
        editor.draw();
        player.draw();
    }

    void loadInstallations() {
        File[] files;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            files = new File(System.getProperty("user.dir")+"/installations").listFiles();
        }
        else {
            files = new File("installations").listFiles();
        }
        for (File file : files) {
            if (file.isDirectory()) {
                String configFilePath;
                if(System.getProperty("os.name").equals("Mac OS X")) {
                    configFilePath = System.getProperty("user.dir")+"/installations/" + file.getName() + "/setup.json";
                }
                else {
                    configFilePath= "installations\\" + file.getName() + "\\setup.json";
                }
                JSONObject configjson = parent.loadJSONObject(new File(configFilePath));
                listInstallations.addItem(configjson.getString("name"), getLengthListbox(listInstallations));
            }
        }
    }


    public int getLengthListbox(ListBox list) {
        String[][] tempString = list.getListBoxItems();
        return tempString.length;
    }
}

