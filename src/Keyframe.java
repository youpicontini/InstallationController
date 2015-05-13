import processing.core.PApplet;

public class Keyframe {

    PApplet parent;
    float currentOpacity;
    int currentDevice;
    float[] currentValues = new float[4];

    Keyframe(PApplet _parent, float[] _currentValues){

        parent = _parent;
        currentValues = _currentValues;

        currentDevice = 0;
        currentOpacity = currentValues[0];
    }



    public float getCurrentOpacity() {
        return currentOpacity;
    }

    public void setCurrentOpacity(float currentOpacity) {
        this.currentOpacity = currentOpacity;
    }
}
