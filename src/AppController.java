import controlP5.*;
import processing.core.PApplet;

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

        projectName = cp5.addTextlabel("projectName");
        framerateProject = cp5.addTextlabel("framerateProject");
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
    }

    public void draw(){
        framerateProject.setText(""+Math.round(parent.frameRate)).setColor(0xff000000);
        editor.draw();
        player.draw();
    }
}
