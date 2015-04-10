import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

public class Keyframe {

    PApplet parent;
    String nameAnim;
    JSONObject jsonKeyframe;


    Keyframe(String _nameAnim, PApplet _parent){

        parent = _parent;
        nameAnim = _nameAnim;

        jsonKeyframe = parent.loadJSONObject(new File("animations\\TEMPLATE_keyframe.json"));
        jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(0).getString("params").setString("opacity","0");
        jsonKeyframe.setString("species", "Panthera leo");
        jsonKeyframe.setString("name", "Lion");

        parent.saveJSONObject(jsonKeyframe, "animations/" + nameAnim + "/keyframes/keyframe" + ".json");
    }
}
