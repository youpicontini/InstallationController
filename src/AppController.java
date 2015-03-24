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

    AppController(ControlP5 _cp5, PApplet _parent) {

        parent = _parent;
        cp5 = _cp5;

        editor = new Editor(cp5, parent);
        player = new Player(cp5, parent);


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
