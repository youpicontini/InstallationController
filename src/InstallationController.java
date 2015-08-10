import processing.core.*;
import controlP5.*;
import promidi.*;

import java.awt.event.MouseWheelEvent;
import javax.sound.midi.Sequencer.SyncMode;



public class InstallationController extends PApplet {

    ControlP5 cp5;
    AppController appController;
    MidiIO midiIO;
    DMXInterface dmxInterface;
    //MIDIController midiController;
    SoundSpectrum soundSpectrum;

    int nb_device;

    public static final String APPNAME="InstallationController";
    public static final String PROJECTNAME="Crystal Net";

    public static void main(String args[]) {
        PApplet.main(/*new String[] {"--present",*/"InstallationController"/*}*/);
    }

	public void setup() {
        size(1440,900);
        frame.setTitle(APPNAME);

        cp5 = new ControlP5(this);


        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/setup.json";
        }
        else {
            tempPath = "installations\\CrystalNet\\setup.json";
        }

        nb_device = loadJSONObject(tempPath).getInt("nb_elements");


        soundSpectrum = new SoundSpectrum(this);
        appController = new AppController(cp5, soundSpectrum, nb_device, this);
        dmxInterface = new DMXInterface(this, appController, nb_device);


        appController.setup();
        dmxInterface.setup();
//        midiController = new MIDIController(this, appController);
//        midiController.setup();
        soundSpectrum.setup();
        // MIDI
        midiIO = MidiIO.getInstance(this);
        midiIO.printDevices();

        //plug all methods to handle midievents
        try {
            midiIO.plug(this, "noteOn", 0, 0);
            midiIO.plug(this, "noteOff", 0, 0);
            midiIO.plug(this, "controllerIn", 0, 0);
            midiIO.plug(this, "programChange", 0, 0);
        }
        catch (Exception e){
            println("NO MIDI CONTROLLER IS PLUGGED");
        }
	}

	public void draw() {
        background(100);
        appController.draw();
        dmxInterface.draw();
        soundSpectrum.draw();
	}

    public void mouseClicked(){
        if (mouseY >60 && mouseY <810 && mouseX<1250 && mouseX>200) {
            if(appController.editor.previewController.editor && appController.editor.previewController.animation)
                if(appController.editor.previewController.currentLedStripeHover.ol) {
                    appController.editor.previewController.currentLedStripe.selected = false;
                    appController.editor.previewController.currentLedStripe = appController.editor.previewController.currentLedStripeHover;
                    appController.editor.previewController.currentLedStripe.selected = true;
                    int tempIndex = Integer.parseInt(appController.editor.previewController.currentLedStripe.id);
                    appController.editor.animationsManager.currentAnim.currentKeyframe.currentDevice = tempIndex;
                    appController.editor.animationsManager.sliderDeviceOpacity.setValue(appController.editor.animationsManager.currentAnim.currentValues[tempIndex]);
                    appController.editor.animationsManager.currentAnim.kfHasChanged=false;
                }
                else {
                    appController.editor.previewController.currentLedStripe.selected = false;
                }
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == SHIFT) {
                appController.editor.previewController.currentLedStripe.severalSelected = true;
            }
        } else {
            if (key == 'a') {
                if (appController.editor.animationsManager.currentAnim.currentKeyframeIndex != 0) {
                    appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.currentKeyframeIndex--;
                    appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                    appController.editor.previewController.unselectDevices();
                }
            }
            if (key == 'd') {
                if (appController.editor.animationsManager.currentAnim.currentKeyframeIndex != (appController.editor.animationsManager.currentAnim.keyframeNumber - 1)) {
                    appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.currentKeyframeIndex++;
                    appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                    appController.editor.previewController.unselectDevices();
                }
            }
            if(key == 'x'){
                if(appController.editor.animationsManager.currentAnim.currentKeyframe.currentOpacity == 0)appController.editor.animationsManager.currentAnim.currentKeyframe.currentOpacity = 1;
                else appController.editor.animationsManager.currentAnim.currentKeyframe.currentOpacity = 0;
                appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                appController.editor.animationsManager.currentAnim.kfHasChanged = true;

            }
            if(key == 'p'){
                int tempDiv = appController.player.fpsDivider;
                switch (tempDiv){
                    case 1:appController.player.fpsDivider = 2;
                        break;
                    case 2:appController.player.fpsDivider = 4;
                        break;
                    case 4:appController.player.fpsDivider = 1;
                        break;
                }
                println(appController.player.fpsDivider);
            }
        }
    }

    public void keyReleased(){
        if (key == CODED) {
            if (keyCode == SHIFT) {
                appController.editor.previewController.currentLedStripe.severalSelected = false;
            }
        }
    }

    public void mouseWheel(MouseWheelEvent event) {
        println(event.getWheelRotation());
    }

    public void controlEvent(ControlEvent e) {
            if (e.name().equals("inputNewAnimName")) {
                appController.editor.animationsManager.currentAnimName = appController.editor.animationsManager.inputNewAnimName.getText();
                appController.editor.animationsManager.labelNameAnimation.setText(appController.editor.animationsManager.currentAnimName);
                appController.editor.animationsManager.labelNameAnimation.show();
                appController.editor.animationsManager.inputNewAnimName.hide();
                appController.editor.animationsManager.inputNewAnimFPS.show();
                //appController.editor.animationsManager.inputNewAnimFPS.setFocus(true);
            }
            if (e.name().equals("buttonNewAnim")) {
                if (appController.editor.animationsManager instanceof AnimationsManager)
                    appController.editor.animationsManager.toggleVisibilityInputNewAnimation();
            }
            if (e.name().equals("inputNewAnimFPS")) {
                appController.editor.animationsManager.newAnimation(appController.editor.animationsManager.currentAnimName, Integer.parseInt(appController.editor.animationsManager.inputNewAnimFPS.getText()));
                background(100);
                appController.editor.animationsManager.inputNewAnimFPS.hide();
                //appController.editor.animationsManager.show();
            }
            if (e.name().equals("listAnimations")) {
                int currentIndex = (int) e.group().value();
                boolean tempPlay = false;
                if (appController.player.buttonPlayAnim.isOn()) {
                    appController.player.buttonPlayAnim.setOff();
                    tempPlay = true;
                }
                appController.editor.animationsManager.setCurrentAnimIndex(currentIndex);
                appController.editor.animationsManager.updateCurrentAnim(currentIndex);
                appController.editor.animationsManager.highlightSelectedAnim(currentIndex);
                appController.editor.animationsManager.displayAnimation(currentIndex);
                appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                appController.editor.animationsManager.currentAnim.kfHasChanged = false;
                appController.editor.previewController.animation = true;
                appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.player.buttonPlayAnim.show();
                appController.editor.animationsManager.buttonNewKeyframe.show();
                appController.editor.animationsManager.buttonDeleteKeyframe.show();
                appController.editor.animationsManager.buttonResetKeyframe.show();
                appController.editor.animationsManager.labelKeyframeName.show();
                appController.editor.animationsManager.labelKeyframeName.setText("FIRST");
                appController.editor.animationsManager.buttonNewAnim.show();
                appController.editor.animationsManager.buttonDeleteAnim.show();
                appController.editor.animationsManager.sliderDeviceOpacity.show();
                if(!appController.player.liveMode)appController.player.fps = appController.editor.animationsManager.currentAnim.getFps();
                if (tempPlay) {
                    appController.player.buttonPlayAnim.setOn();
                }

            }
            if (e.isTab() && e.getTab().getName().equals("default") && appController.editor.animationsManager.inputNewAnimName.isVisible()) {
                appController.editor.animationsManager.labelNameAnimation.show();
                appController.editor.animationsManager.inputNewAnimName.hide();
            }
            if (e.name().equals("buttonDeleteAnim")) {
                if (appController.editor.animationsManager instanceof AnimationsManager)
                    appController.editor.animationsManager.deleteAnimation(appController.editor.animationsManager.selectedIndex);
            }
            if (e.name().equals("buttonNewKeyframe")) {
                if (appController.editor.animationsManager instanceof AnimationsManager) {
                    appController.editor.animationsManager.currentAnim.addKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                    appController.editor.previewController.unselectDevices();
                    appController.editor.animationsManager.currentAnim.kfHasChanged = false;
                }
            }
            if (e.name().equals("buttonDeleteKeyframe")) {
                if (appController.editor.animationsManager instanceof AnimationsManager)
                    appController.editor.animationsManager.currentAnim.removeKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.kfHasChanged = false;
            }
            if (e.name().equals("buttonResetKeyframe")) {
                if (appController.editor.animationsManager instanceof AnimationsManager)
                    appController.editor.animationsManager.currentAnim.currentValues = new float[appController.editor.animationsManager.currentAnim.nb_elements];
                appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                appController.editor.animationsManager.sliderDeviceOpacity.setValue(0);
                appController.editor.animationsManager.currentAnim.kfHasChanged = true;
            }
            if (e.name().equals("buttonPlayAnim")) {
                if (appController.editor.animationsManager instanceof AnimationsManager) {
                    if (appController.player.buttonPlayAnim.isOn()) {
                        appController.editor.animationsManager.buttonNewKeyframe.hide();
                        appController.editor.animationsManager.buttonDeleteKeyframe.hide();
                        appController.editor.animationsManager.buttonResetKeyframe.hide();
                        appController.editor.animationsManager.buttonNewAnim.hide();
                        appController.editor.animationsManager.buttonDeleteAnim.hide();
                        appController.editor.animationsManager.sliderDeviceOpacity.hide();
                        appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                        appController.player.playing=true;
                    } else {
                        appController.editor.animationsManager.buttonNewKeyframe.show();
                        appController.editor.animationsManager.buttonDeleteKeyframe.show();
                        appController.editor.animationsManager.buttonResetKeyframe.show();
                        appController.editor.animationsManager.buttonNewAnim.show();
                        appController.editor.animationsManager.buttonDeleteAnim.show();
                        appController.editor.animationsManager.sliderDeviceOpacity.show();
                        appController.player.playing=false;
                    }
                }
            }
            if (e.name().equals("default")) {
                appController.editor.animationsManager.listAnimations.show();
                appController.editor.previewController.editor = true;
                appController.player.masterOpacity = 0;
                appController.player.buttonPlayAnim.show();
                appController.player.sliderMasterOpacity.setValue(1);
                appController.player.buttonNoise.setOff();
                appController.player.buttonStrobe.setOff();
                appController.player.buttonGlow.setOff();
                appController.player.buttonSoundReactive.setOff();
            }
            if (e.name().equals("tabPlayer")) {
                appController.editor.animationsManager.listAnimations.show();
                appController.player.sliderMasterOpacity.show();
                appController.player.buttonNoise.show();
                appController.player.buttonStrobe.show();
                appController.player.buttonGlow.show();
                appController.player.buttonSoundReactive.show();
                appController.player.buttonLiveMode.show();
                appController.player.buttonInvert.show();
                appController.player.buttonBongoMode.show();
                appController.player.buttonPlayAnim.show();
                appController.editor.previewController.editor = false;
                appController.player.sliderMasterOpacity.setValue(1);
            }
            if (e.name().equals("sliderDeviceOpacity")) {
                appController.editor.animationsManager.currentAnim.currentKeyframe.currentOpacity = appController.editor.animationsManager.sliderDeviceOpacity.getValue();
                appController.player.setCurrentKeyframeValues(appController.editor.animationsManager.currentAnim.getCurrentValues());
                appController.editor.animationsManager.currentAnim.kfHasChanged = true;
            }
            if (e.name().equals("sliderMasterOpacity")) {
                appController.player.setMasterOpacity(appController.player.sliderMasterOpacity.getValue());
            }
            if (e.name().equals("buttonNoise")) {
                appController.player.noise = appController.player.buttonNoise.getBooleanValue();
            }

            if (e.name().equals("buttonStrobe")) {
                appController.player.strobe = appController.player.buttonStrobe.getBooleanValue();
            }

            if (e.name().equals("buttonSoundReactive")) {
                appController.player.soundReactive = appController.player.buttonSoundReactive.getBooleanValue();
            }

            if (e.name().equals("buttonLiveMode")) {
                appController.player.liveMode = appController.player.buttonLiveMode.getBooleanValue();
                appController.player.fps=2;
            }
            if (e.name().equals("buttonBongoMode")) {
                appController.player.bongoMode = appController.player.buttonBongoMode.getBooleanValue();
            }

            if (e.name().equals("buttonInvert")) {
                appController.player.invert = appController.player.buttonInvert.getBooleanValue();
            }

            if (e.name().equals("buttonGlow")) {
                appController.player.glow = appController.player.buttonGlow.getBooleanValue();
            }
    }

    public void noteOn(Note note){
        int pit = note.getPitch();
        float tempKfValues[] = new float[nb_device];
        println(pit);
        switch (pit) {
            case 48:
                if (appController.player.buttonPlayAnim.isOn())
                    appController.player.buttonPlayAnim.setOff();
                else
                    appController.player.buttonPlayAnim.setOn();
                break;
            case 36:
                if (appController.player.noise)
                    appController.player.buttonNoise.setOff();
                else {
                    appController.player.buttonInvert.setOff();
                    appController.player.buttonStrobe.setOff();
                    appController.player.buttonGlow.setOff();
                    appController.player.buttonNoise.setOn();
                }
                break;
            case 37:
                if (appController.player.buttonStrobe.isOn())
                    appController.player.buttonStrobe.setOff();
                else {
                    appController.player.buttonInvert.setOff();
                    appController.player.buttonNoise.setOff();
                    appController.player.buttonGlow.setOff();
                    appController.player.buttonStrobe.setOn();
                }
                break;
            case 38:
                if (appController.player.buttonGlow.isOn())
                    appController.player.buttonGlow.setOff();
                else {
                    appController.player.buttonInvert.setOff();
                    appController.player.buttonNoise.setOff();
                    appController.player.buttonStrobe.setOff();
                    appController.player.buttonGlow.setOn();
                }
                break;
            case 39:
                if (appController.player.buttonInvert.isOn())
                    appController.player.buttonInvert.setOff();
                else {
                    appController.player.buttonStrobe.setOff();
                    appController.player.buttonNoise.setOff();
                    appController.player.buttonGlow.setOff();
                    appController.player.buttonInvert.setOn();
                }
                break;

            case 49:
                if(appController.player.bongoMode){
                    for(int i=10; i<16;i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 50:
                if(appController.player.bongoMode){
                    for(int i=15; i<21;i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 44:
                if(appController.player.bongoMode) {
                    for (int i = 0; i < 6; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 45:
                if(appController.player.bongoMode) {
                    for (int i = 5; i < 11; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 46:
                if(appController.player.bongoMode) {
                    for (int i = 20; i < 26; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 47:
                if(appController.player.bongoMode) {
                    for (int i = 25; i < 31; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 40:
                if(appController.player.bongoMode) {
                    for (int i = 44; i < 49; i++) {
                        tempKfValues[i] = 1;
                    }
                    tempKfValues[7] = 1;
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 41:
                if(appController.player.bongoMode) {
                    for (int i = 40; i < 45; i++) {
                        tempKfValues[i] = 1;
                    }

                    tempKfValues[22] = 1;
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 42:
                if(appController.player.bongoMode) {
                    for (int i = 35; i < 41; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 43:
                if(appController.player.bongoMode) {
                    for (int i = 30; i < 36; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 51:
                int tempDiv = appController.player.fpsDivider;
                switch (tempDiv){
                    case 1:appController.player.fpsDivider = 2;
                        break;
                    case 2:appController.player.fpsDivider = 4;
                        break;
                    case 4:appController.player.fpsDivider = 1;
                        break;
                }
                println(appController.player.fpsDivider);
        }
    }

    public void noteOff(Note note){

            /*
        int pit = note.getPitch();
        float tempKfValues[] = new float[nb_device];
        switch (pit){
            case 49:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 50:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 44:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 45:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 46:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 47:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 40:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 41:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 42:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
            case 43:
                if(appController.player.bongoMode) {
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                break;
        }
                */
    }

    public void controllerIn(promidi.Controller controller){
        int num = controller.getNumber();
        int val = controller.getValue();

        switch (num){
            case 25: appController.player.sliderMasterOpacity.setValue(map(val, 0, 127, 0, 1));
                break;
            case 21: appController.player.periodStrobe=(long)map(val, 0, 127, 1, 15);
                break;
            case 20:if(appController.player.liveMode) appController.player.fps = (int)map(val, 0, 127, 1000, 200);
                break;
        }
    }

    public void programChange(ProgramChange programChange){
        int num = programChange.getNumber();
    }

//    public void mousePressed() {
//        if ((mouseY > (appController.editor.timeline.y_timeline - appController.editor.timeline.upTick)) && (mouseY < (appController.editor.timeline.y_timeline + appController.editor.timeline.upTick))) {
//            print("Pressed");
//            appController.editor.timeline.mDifX = mouseX - appController.editor.timeline.mX;
//            cursor(HAND);
//        }
//    }
//
//    public void mouseReleased() {
//        if ((mouseY > (appController.editor.timeline.y_timeline - appController.editor.timeline.upTick)) && (mouseY < (appController.editor.timeline.y_timeline + appController.editor.timeline.upTick))){
//            println("Released");
//            cursor(ARROW);
//        }
//    }
//
//    public void mouseDragged() {
//        if ((mouseY > (appController.editor.timeline.y_timeline - appController.editor.timeline.upTick)) && (mouseY < (appController.editor.timeline.y_timeline + appController.editor.timeline.upTick))){
//            print("dragged");
//            appController.editor.timeline.mX = mouseX - appController.editor.timeline.mDifX;
//            println(appController.editor.timeline.mX);
//        }
//    }
}
