import controlP5.*;
import processing.core.PApplet;

public class AppController {
    PApplet parent;
    ControlP5 cp5;

    Editor editor;
    Player player;

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
        groupEditor = new Group(cp5,"groupEditor");
        groupPlayer = new Group(cp5,"groupPlayer");
    }
    public void setup(){
        editor.setup();
        player.setup();

        /* [INTERFACE] */
        cp5.getTab("default")
                .activateEvent(true)
                .setLabel("editor")
                .setWidth(70)
                .setHeight(30);

        cp5.getTab("tabPlayer")
                .activateEvent(true)
                .setLabel("player")
                .setWidth(70)
                .setHeight(30);

        groupEditor.moveTo("default");
        groupPlayer.moveTo("tabPlayer");
    }
    public void draw(){
        editor.draw();
        player.draw();
    }
}
