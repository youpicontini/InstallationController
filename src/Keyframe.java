import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

public class Keyframe {

    PApplet parent;
    float currentOpacity;
    int currentDevice;
    float[] currentValues = new float[4];

    Keyframe(PApplet _parent, float[] _currentValues){

        parent = _parent;

        currentOpacity = 0.0f;
        currentDevice = 0;
        currentValues = _currentValues;
    }



    public float getCurrentOpacity() {
        return currentOpacity;
    }

    public void setCurrentOpacity(float currentOpacity) {
        this.currentOpacity = currentOpacity;
    }
}
