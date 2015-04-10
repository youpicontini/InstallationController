import controlP5.Button;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.data.JSONObject;
import java.util.ArrayList;

public class Animation {

    Keyframe keyframe;
    int indexKeyframe=0;
    String name;
    String id;
    int fps;


    Button buttonNewKeyframe;
    Button buttonDeleteKeyframe;

    ControlP5 cp5;
    PApplet parent;
    JSONObject jsonAnimation;

    ArrayList<Keyframe> keyframesArray;

    Animation (String _name, int _fps,ControlP5 _cp5, PApplet _parent) {
        name = _name;
        id = name.replaceAll(" ","_");
        fps = _fps;
        cp5 = _cp5;
        parent = _parent;

        buttonNewKeyframe = cp5.addButton("buttonNewKeyframe");
        buttonDeleteKeyframe = cp5.addButton("buttonDeleteKeyframe");

        buttonNewKeyframe.setLabel("       +")
                .setValue(0)
                .setPosition(1390,720)
                .setSize(40,40)
                .setGroup("groupEditor");

        buttonDeleteKeyframe.setLabel("       -")
                .setValue(0)
                .setPosition(1340, 720)
                .setSize(40, 40)
                .setGroup("groupEditor");

        keyframesArray = new ArrayList<Keyframe>();

        jsonAnimation = new JSONObject();
        jsonAnimation.setString("id", id);
        jsonAnimation.setString("name", name);
        jsonAnimation.setInt("fps", fps);
        parent.saveJSONObject(jsonAnimation, "animations/"+id+"/config.json");
    }

    void addKeyframe(int currentIndex){
        parent.print("adding kf n°"+currentIndex);
        keyframe = new Keyframe(String.valueOf(currentIndex+1), parent);
//        keyframesArray.add(keyframe);
//        indexKeyframe++;
    }

    void removeKeyframe(int currentIndex){
        parent.print("removing kf n°"+currentIndex);
//        keyframesArray.remove(index);
//        indexKeyframe--;
    }
}
