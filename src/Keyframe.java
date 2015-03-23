import processing.core.PApplet;
import processing.data.JSONObject;
import java.util.ArrayList;

public class Keyframe {

    PApplet parent;
    String nameAnim;
    JSONObject jsonKeyframe;


    Keyframe(String _nameAnim, PApplet _parent){

        parent = _parent;
        nameAnim = _nameAnim;

        jsonKeyframe = new JSONObject();
        jsonKeyframe.setInt("id", 0);
        jsonKeyframe.setString("species", "Panthera leo");
        jsonKeyframe.setString("name", "Lion");

        parent.saveJSONObject(jsonKeyframe, "animations/" + nameAnim + "/keyframes/keyframe" + ".json");
    }
}
