import controlP5.*;
import processing.core.PApplet;

public class Player {

    ControlP5 cp5;
    PApplet parent;
    Editor editor;
    SoundSpectrum soundSpectrum;

    Slider sliderMasterOpacity;
    Button buttonNoise;
    Button buttonStrobe;
    Button buttonGlow;
    Button buttonSoundReactive;
    Button buttonPlayAnim;

    int nb_device;
    float[] currentKeyframeValues;
    boolean playing;
    int fps;
    boolean fpsTick;

    boolean strobe,glow,noise,soundReactive;
    boolean strobbing;
    long periodStrobe;
    Thread strobeThread;
    Thread fpsThread;
    float masterOpacity;

    Player(ControlP5 _cp5, PApplet _parent, Editor _editor, SoundSpectrum _soundSpectrum, int _nb_device){
        cp5 = _cp5;
        parent = _parent;
        editor = _editor;
        soundSpectrum = _soundSpectrum;
        nb_device = _nb_device;

        sliderMasterOpacity = cp5.addSlider("sliderMasterOpacity");
        buttonNoise = cp5.addButton("buttonNoise");
        buttonStrobe = cp5.addButton("buttonStrobe");
        buttonGlow = cp5.addButton("buttonGlow");
        buttonSoundReactive = cp5.addButton("buttonSoundReactive");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");

        playing = false;
    }

    public void setup(){

        sliderMasterOpacity.setLabel("Master opacity")
                .setPosition(1260, 80)
                .setGroup("groupPlayer")
                .setSize(100, 40)
                .setRange(0,1)
                .setValue(1.0f)
                .hide();

        buttonNoise.setLabel("Noise")
                .setPosition(1260, 130)
                .setGroup("groupPlayer")
                .setSize(100, 40)
                .setSwitch(true)
                .hide();

        buttonStrobe.setLabel("Strobe")
                .setPosition(1260, 180)
                .setGroup("groupPlayer")
                .setSize(100, 40)
                .setSwitch(true)
                .hide();

        buttonGlow.setLabel("Glow")
                .setPosition(1260, 230)
                .setGroup("groupPlayer")
                .setSize(100, 40)
                .setSwitch(true)
                .hide();

        buttonSoundReactive.setLabel("Sound Reactive")
                .setPosition(1260, 280)
                .setGroup("groupPlayer")
                .setSize(100, 40)
                .setSwitch(true)
                .hide();

        buttonPlayAnim.setLabel("play")
                .setPosition(1260, 770)
                .moveTo("global")
                .setSize(170, 40)
                .setSwitch(true)
                .hide();

        playing=false;
        noise=false;
        strobe=false;
        strobbing = false;
        periodStrobe = 1;
        fps = 1;
        currentKeyframeValues= new float[nb_device];

    }

    void draw(){
        if(playing) {
            strobeTick();
            FPSTick();
            if (strobe) {
                if (strobbing) {
                    for (int i = 0; i < currentKeyframeValues.length; i++) {
                        currentKeyframeValues[i] = 1 * masterOpacity;
                    }
                } else {
                    for (int i = 0; i < currentKeyframeValues.length; i++) {
                        currentKeyframeValues[i] = 0;
                    }
                }
            }
            if (noise) {
                for (int i = 0; i < currentKeyframeValues.length; i++) {
                    currentKeyframeValues[i] = parent.noise(parent.random(0, parent.width), parent.random(0, parent.height)) * masterOpacity;
                }
            }
            else {
                if (soundReactive) {
                    if (soundSpectrum.beat.isOnset()) {
                        editor.animationsManager.currentAnim.play();
                        setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());
                        for (int i = 0; i < currentKeyframeValues.length; i++) {
                            currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
                        }
                    }
                }
                else {
                    if (fpsTick) {
                        editor.animationsManager.currentAnim.play();
                        setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());
                        for (int i = 0; i < currentKeyframeValues.length; i++) {
                            currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
                        }
                        fpsTick=false;
                    }
                }
            }
        }
        else{
            if (editor.animationsManager.currentAnim instanceof Animation)
                setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());
        }
        if(glow) {
            for (int i = 0; i < currentKeyframeValues.length; i++) {
                currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
            }
        }
        editor.previewController.setCurrentKeyframeValuesDisplayed(currentKeyframeValues);
    }

    void setCurrentKeyframeValues(float[] val){
        currentKeyframeValues = val;
    }

    float[] getCurrentKeyframeValues(){
        return currentKeyframeValues;
    }

    public void setMasterOpacity(float f){
        masterOpacity = f;
    }

    void strobeTick(){
        if(!(strobeThread instanceof Thread)) {
            strobeThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if(strobbing) strobbing = false;
                            else strobbing = true;
                            Thread.sleep(1000 / periodStrobe);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            strobeThread.start();
        }
    }
    void FPSTick(){
        if(!(fpsThread instanceof Thread)) {
            fpsThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if(fpsTick) fpsTick = false;
                            else fpsTick = true;
                            Thread.sleep(fps);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            fpsThread.start();
        }
    }
}
