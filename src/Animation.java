import processing.core.PApplet;
import processing.data.JSONObject;
import java.util.ArrayList;

public class Animation {

    Keyframe keyframe;
    int indexKeyframe=0;
    String name;
    PApplet parent;

    JSONObject jsonAnimation;

    ArrayList<Keyframe> keyframesArray;

    Animation (String _name, PApplet _parent) {
        name = _name;
        parent = _parent;

        keyframesArray = new ArrayList<Keyframe>();
        jsonAnimation = new JSONObject();
        jsonAnimation.setString("name", name);
        jsonAnimation.setInt("fps", 5);

        parent.saveJSONObject(jsonAnimation, "animations/"+name+"/config" + name + ".json");
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
