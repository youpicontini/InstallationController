import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

public class Keyframe {

    PApplet parent;
    float currentOpacity;
    int currentDevice;

    Keyframe(PApplet _parent){

        parent = _parent;

        currentOpacity = 0.0f;
        currentDevice = 0;
    }

    public float getCurrentOpacity() {
        return currentOpacity;
    }

    public void setCurrentOpacity(float currentOpacity) {
        this.currentOpacity = currentOpacity;
    }
}
