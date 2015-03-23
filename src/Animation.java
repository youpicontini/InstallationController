import processing.core.PApplet;

import java.util.ArrayList;

public class Animation {

    Keyframe keyframe;
    int indexKeyframe=0;
    String name;
    PApplet parent;

    ArrayList<Keyframe> keyframesArray;

    Animation (String _name, PApplet _parent) {
        name = _name;
        parent = _parent;

        keyframesArray = new ArrayList<Keyframe>();
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
