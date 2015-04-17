import controlP5.Button;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;


public class Animation {

    Keyframe currentKeyframe;
    float[] currentValues = new float[4];

    String name;
    String id;
    String idAnim;
    int fps;

    ControlP5 cp5;
    PApplet parent;
    JSONObject jsonAnimation;

    //
    Animation (String _name, int _fps,ControlP5 _cp5, PApplet _parent) {
        name = _name;
        id = name.replaceAll(" ","_");
        fps = _fps;
        cp5 = _cp5;
        parent = _parent;

        jsonAnimation = new JSONObject();
        jsonAnimation.setString("id", id);
        jsonAnimation.setString("name", name);
        jsonAnimation.setInt("fps", fps);
        parent.saveJSONObject(jsonAnimation, "animations\\" + id + "\\config.json");

        currentKeyframe = new Keyframe(parent);
        saveKeyframe(0);
        updateCurrentValues();
    }

    void addKeyframe(int currentIndex){
        parent.print("adding kf n°"+currentIndex);
        saveKeyframe(currentIndex);
        currentKeyframe = new Keyframe(parent);
//        keyframesArray.add(keyframe);
//        indexKeyframe++;
    }

    void removeKeyframe(int currentIndex){
        parent.print("removing kf n°"+currentIndex);
//        keyframesArray.remove(index);
//        indexKeyframe--;
    }

    public void saveKeyframe(int currentIndex){
        JSONObject jsonKeyframe;
        jsonKeyframe = parent.loadJSONObject(new File("animations\\TEMPLATE_keyframe.json"));
        for (int i=0; i<currentValues.length;i++){
            jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(i).getJSONObject("params").setFloat("opacity", currentValues[i]);
        }
        parent.saveJSONObject(jsonKeyframe, "animations\\" + idAnim + "\\keyframes\\"+ currentIndex + ".json");
    }

    void updateCurrentValues(){
        currentValues[currentKeyframe.currentDevice] = currentKeyframe.currentOpacity;
    }
}