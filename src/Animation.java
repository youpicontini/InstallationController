import processing.core.PApplet;
import processing.data.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;

public class Animation {

    Keyframe keyframe;
    int indexKeyframe=0;
    String name;
    String id;
    int fps;
    PApplet parent;
    JSONObject jsonAnimation;

    ArrayList<Keyframe> keyframesArray;

    Animation (String _name, int _fps, PApplet _parent) {
        name = _name;
        id = name.replaceAll(" ","_");
        fps = _fps;
        parent = _parent;

        keyframesArray = new ArrayList<Keyframe>();

        jsonAnimation = new JSONObject();
        jsonAnimation.setString("id", id);
        jsonAnimation.setString("name", name);
        jsonAnimation.setInt("fps", fps);
        parent.saveJSONObject(jsonAnimation, "animations/"+id+"/config.json");

    }

    void addKeyframe(){
        keyframe = new Keyframe(name, parent);
        keyframesArray.add(keyframe);
        indexKeyframe++;
    }

    void removeKeyframe(int index){
        keyframesArray.remove(index);
        indexKeyframe--;
    }

}
