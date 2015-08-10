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
    Button buttonBongoMode;
    Button buttonInvert;
    Button buttonSoundReactive;
    Button buttonLiveMode;
    Button buttonPlayAnim;

    int nb_device;
    float[] currentKeyframeValues, nextKeyframeValues;
    boolean playing;
    int fps, fpsDivider;
    boolean fpsTick,morphTick;

    boolean strobe,glow,noise,soundReactive,invert, liveMode, bongoMode;
    boolean strobbing, morpho;
    long periodStrobe;
    Thread strobeThread;
    Thread fpsThread;
    Thread morphThread;
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
        buttonInvert = cp5.addButton("buttonInvert");
        buttonGlow = cp5.addButton("buttonGlow");
        buttonBongoMode = cp5.addButton("buttonBongoMode");
        buttonSoundReactive = cp5.addButton("buttonSoundReactive");
        buttonLiveMode = cp5.addButton("buttonLiveMode");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");

        playing = false;
    }

    public void setup(){

        sliderMasterOpacity.setLabel("Master opacity")
                .setPosition(1260, 60)
                .setGroup("groupPlayer")
                .setSize(110, 20)
                .setRange(0, 1)
                .setValue(1.0f)
                .hide();

        buttonNoise.setLabel("Noise")
                .setPosition(1260, 90)
                .setGroup("groupPlayer")
                .setSize(50, 50)
                .setSwitch(true)
                .hide();

        buttonStrobe.setLabel("Strobe")
                .setPosition(1320, 90)
                .setGroup("groupPlayer")
                .setSize(50, 50)
                .setSwitch(true)
                .hide();

        buttonGlow.setLabel("Glow")
                .setPosition(1260, 150)
                .setGroup("groupPlayer")
                .setSize(50, 50)
                .setSwitch(true)
                .hide();

        buttonInvert.setLabel("Invert")
                .setPosition(1320, 150)
                .setGroup("groupPlayer")
                .setSize(50, 50)
                .setSwitch(true)
                .hide();

        buttonSoundReactive.setLabel("Sound Reactive")
                .setPosition(1260, 210)
                .setGroup("groupPlayer")
                .setSize(110, 50)
                .setSwitch(true)
                .hide();



        buttonLiveMode.setLabel("Live mode")
                .setPosition(1260, 720)
                .setGroup("groupPlayer")
                .setColorActive(parent.color(255,0,0))
                .setSize(80, 40)
                .setSwitch(true)
                .hide();

        buttonBongoMode.setLabel("Bongo mode")
                .setPosition(1350, 720)
                .setGroup("groupPlayer")
                .setColorActive(parent.color(255,0,0))
                .setSize(80, 40)
                .setSwitch(true)
                .hide();

        buttonPlayAnim.setLabel("play")
                .setPosition(1260, 770)
                .moveTo("global")
                .setColorActive(parent.color(0, 255,0))
                .setSize(170, 40)
                .setSwitch(true)
                .hide();

        playing=false;
        noise=false;
        strobe=false;
        strobbing = false;
        liveMode = false;
        periodStrobe = 1;
        fps = fpsDivider = 1;
        currentKeyframeValues= new float[nb_device];
        strobeTick();
        FPSTick();
    }

    void draw(){
        if(playing) {
            if(!bongoMode) {
                if (strobe || noise || glow) {
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
                    } else if (noise) {
                        for (int i = 0; i < currentKeyframeValues.length; i++) {
                            currentKeyframeValues[i] = parent.noise(parent.random(0, parent.width), parent.random(0, parent.height)) * masterOpacity;
                        }
                    } else if (glow) {
                        for (int i = 0; i < currentKeyframeValues.length; i++) {
                            currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
                        }
                    }
                } else {
                    if (soundReactive) {
                        if (soundSpectrum.beat.isOnset()) {
                            editor.animationsManager.currentAnim.play();
                            setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());
                            for (int i = 0; i < currentKeyframeValues.length; i++) {
                                currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
                            }
                        }
                    } else {
                        if (!morpho) {
                            if (fpsTick) {
                                editor.animationsManager.currentAnim.play();
                                setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());
                                for (int i = 0; i < currentKeyframeValues.length; i++) {
                                    if (invert)
                                        currentKeyframeValues[i] = (1 - currentKeyframeValues[i]) * masterOpacity;
                                    else currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
                                }
                                fpsTick = false;
                            }
                        } else {
                            if (morphTick) {
                                editor.animationsManager.currentAnim.play();
                                setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());
                                setNextKeyframeValues(editor.animationsManager.currentAnim.getNextValues());
                                for (int i = 0; i < currentKeyframeValues.length; i++) {
                                    if (invert)
                                        currentKeyframeValues[i] = (1 - currentKeyframeValues[i]) * masterOpacity;
                                    else currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
                                }
                                fpsTick = false;
                            }
                        }
                    }
                }
            }
        }
        else{
            if (editor.animationsManager.currentAnim instanceof Animation)
                setCurrentKeyframeValues(editor.animationsManager.currentAnim.getCurrentValues());

        }
        editor.previewController.setCurrentKeyframeValuesDisplayed(currentKeyframeValues);
    }

    void setCurrentKeyframeValues(float[] val){
        currentKeyframeValues = val;
    }

    void setNextKeyframeValues(float[] val){
        nextKeyframeValues = val;
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
                            if (fpsTick) fpsTick = false;
                            else fpsTick = true;
                            if (liveMode) Thread.sleep(fps/fpsDivider);
                            else Thread.sleep(1000 / fps);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            fpsThread.start();
        }
    }
    void MorphTick(){
        if(!(morphThread instanceof Thread)) {
            morphThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if (morphTick) morphTick = false;
                            else morphTick = true;
                            if (liveMode) Thread.sleep(fps/10);
                            else Thread.sleep(100 / fps);
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
