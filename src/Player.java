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
    Button buttonRandom;
    Button buttonMorph;
    Button buttonGlow;
    Button buttonBongoMode;
    Button buttonInvert;
    Button buttonLiveMode;
    Button buttonPlayAnim;

    int nb_device,morphStep;
    float[] currentKeyframeValues, nextKeyframeValues, tempKf, tempKfstep;
    boolean playing;
    int fps, fpsDivider;
    boolean fpsTick,morphTick;

    boolean strobe, noise, invert, liveMode, bongoMode, random, glow;
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
        buttonRandom = cp5.addButton("buttonRandom");
        buttonInvert = cp5.addButton("buttonInvert");
        buttonMorph = cp5.addButton("buttonMorph");
        buttonBongoMode = cp5.addButton("buttonBongoMode");
        buttonLiveMode = cp5.addButton("buttonLiveMode");
        buttonPlayAnim = cp5.addButton("buttonPlayAnim");
        buttonGlow = cp5.addButton("buttonGlow");

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

        buttonMorph.setLabel("Morph")
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

        buttonRandom.setLabel("Random")
                .setPosition(1260, 210)
                .setGroup("groupPlayer")
                .setSize(50, 50)
                .setSwitch(true)
                .hide();


        buttonGlow.setLabel("glow")
                .setPosition(1320, 210)
                .setGroup("groupPlayer")
                .setSize(50, 50)
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
        random=false;
        strobe=false;
        glow = false;
        strobbing = false;
        liveMode = false;
        periodStrobe = 15;
        fps = fpsDivider = 1;
        currentKeyframeValues= new float[nb_device];
        nextKeyframeValues= new float[nb_device];
        tempKf= new float[nb_device];
        tempKfstep= new float[nb_device];
        strobeTick();
        FPSTick();
        MorphTick();
    }

    void draw(){
        if(playing) {
            if(!bongoMode) {
                if (strobe || noise || random) {
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
                    } else if (random){
                        if (fpsTick) {
                            for (int i = 0; i < currentKeyframeValues.length; i++) {
                                currentKeyframeValues[i] = (int) parent.random(0, 2);
                            }
                            fpsTick = false;
                        }
                    }
                } else {
                    if (!morpho) {
                        if (fpsTick) {
                            editor.animationsManager.currentAnim.play();
                            setCurrentKeyframeValues(editor.animationsManager.currentAnim.currentValues);
                            if (invert){
                                for (int i = 0; i < currentKeyframeValues.length; i++) {
                                     currentKeyframeValues[i] = (1-currentKeyframeValues[i]);
                                }
                            }
                            fpsTick = false;
                        }
                    } else {
                        if (fpsTick) {
                            editor.animationsManager.currentAnim.play();
                            setCurrentKeyframeValues(editor.animationsManager.currentAnim.currentValues);
                            setNextKeyframeValues(editor.animationsManager.currentAnim.getNextValues());
                            for (int i = 0; i < currentKeyframeValues.length; i++) {

                                tempKfstep[i] =(nextKeyframeValues[i] - currentKeyframeValues[i]) / 6;
                            }
                            fpsTick = false;
                            morphStep = 0;
                        }
                        if (morphTick) {
                            if(!glow) {
                                for (int i = 0; i < currentKeyframeValues.length; i++) {
                                    if (invert)
                                        tempKf[i] = (1 - (currentKeyframeValues[i] + (morphStep + 1) * tempKfstep[i])) * masterOpacity;
                                    else
                                        tempKf[i] = (currentKeyframeValues[i] + ((morphStep + 1) * tempKfstep[i])) * masterOpacity;
                                }
                                morphTick = false;
                                morphStep = (morphStep + 1) % 6;
                            }
                            else{
                                for (int i = 0; i < currentKeyframeValues.length; i++) {
                                    if (invert)
                                        tempKf[i] = (1 - ((currentKeyframeValues[i]) - ((currentKeyframeValues[i])/6)*(morphStep + 1))) * masterOpacity;
                                    else
                                        tempKf[i] = (currentKeyframeValues[i] - ((currentKeyframeValues[i])/6)*(morphStep + 1)) * masterOpacity;
                                }
                                morphTick = false;
                                morphStep = (morphStep + 1) % 6;
                            }
                            editor.previewController.setCurrentKeyframeValuesDisplayed(tempKf);
                        }
                    }
                }
            }
        }
        else{
            if (editor.animationsManager.currentAnim instanceof Animation)
                setCurrentKeyframeValues(editor.animationsManager.currentAnim.currentValues);
        }
        if (!morpho){
            for (int i = 0; i < currentKeyframeValues.length; i++) {
                currentKeyframeValues[i] = currentKeyframeValues[i] * masterOpacity;
            }
            editor.previewController.setCurrentKeyframeValuesDisplayed(currentKeyframeValues);
        }
    }

    void setCurrentKeyframeValues(float[] val){
        for(int i = 0; i < val.length; i++) {
            currentKeyframeValues[i] = val[i];
        }
    }

    void setNextKeyframeValues(float[] val){
        for(int i = 0; i < val.length; i++) {
            nextKeyframeValues[i] = val[i];
        }
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
                    while (!liveMode) {
                        try {
                            if (fpsTick) fpsTick = false;
                            else fpsTick = true;
                            Thread.sleep(1000 / fps);
                        }
                        catch (InterruptedException ex) {
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
                    while (!liveMode) {
                        try {
                            if (morphTick) morphTick = false;
                            else morphTick = true;
                            Thread.sleep((1000 / fps)/6);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            morphThread.start();
        }
    }
}
