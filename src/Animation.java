import controlP5.ControlP5;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;


public class Animation {

    Keyframe currentKeyframe;
    float[] currentValues = new float[4];
    int currentKeyframeIndex;

    String name;
    String idAnim;
    int fps;

    ControlP5 cp5;
    PApplet parent;
    JSONObject jsonAnimation;
    PreviewController previewController;

    Animation (String _name, int _fps,ControlP5 _cp5, PApplet _parent, PreviewController _previewController) {
        name = _name;
        idAnim = name.replaceAll(" ","_");
        fps = _fps;
        cp5 = _cp5;
        parent = _parent;
        previewController = _previewController;

        jsonAnimation = new JSONObject();
        jsonAnimation.setString("id", idAnim);
        jsonAnimation.setString("name", name);
        jsonAnimation.setInt("fps", fps);
        parent.saveJSONObject(jsonAnimation, "animations\\" + idAnim + "\\config.json");

        currentKeyframeIndex=0;

        loadKeyframe(0);
    }

    void addKeyframe(int currentIndex){
        parent.print("adding kf n°"+currentIndex);
        currentKeyframeIndex = currentIndex;
        saveKeyframe(currentIndex);
        currentValues = new float[4];
        currentKeyframe = new Keyframe(parent, currentValues);
        currentKeyframeIndex++;
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

    public void loadKeyframe(int currentIndex){
        JSONObject jsonKeyframe;
        jsonKeyframe = parent.loadJSONObject(new File("animations\\" + idAnim + "\\keyframes\\"+ currentIndex + ".json"));
        for (int i=0; i<currentValues.length;i++) {
            currentValues[i] = jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(i).getJSONObject("params").getFloat("opacity");
        }
        currentKeyframe = new Keyframe(parent, currentValues);
    }

    void updateCurrentValues(){
        currentValues[currentKeyframe.currentDevice] = currentKeyframe.currentOpacity;
        previewController.setCurrentKeyframeValues(currentValues);
    }
}