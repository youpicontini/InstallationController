import processing.core.*;
import controlP5.*;
import dmxP512.*;
import promidi.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;




public class InstallationController extends PApplet {

    ControlP5 cp5;
    AppController appController;
    //DMXInterface dmxInterface;
    MidiIO midiIO;
    //MIDIController midiController;
    public static final String APPNAME="InstallationController";
    public static final String PROJECTNAME="Crystal Net";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String args[]) {
        PApplet.main("InstallationController");
    }

	public void setup() {
        size(1440,900);
        frame.setTitle(APPNAME);

        cp5 = new ControlP5(this);
        appController = new AppController(cp5, this);
        appController.setup();
//        dmxInterface = new DMXInterface(this, appController);
//        dmxInterface.setup();
//        midiController = new MIDIController(this, appController);
//        midiController.setup();

        // get an instance of midiIO
        midiIO = MidiIO.getInstance(this);

        // print a list of all devices
        midiIO.printDevices();

        //plug all methods to handle midievents
        midiIO.plug(this,"noteOn",0,0);
        midiIO.plug(this,"noteOff",0,0);
        midiIO.plug(this,"controllerIn",0,0);
        midiIO.plug(this,"programChange",0,0);
	}

	public void draw() {
        background(100);
        appController.draw();
//        dmxInterface.draw();
        //print(appController.editor.previewController.currentLedStripe.id);
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
                //println(appController.editor.previewController.currentLedStripe.severalSelected);
            }
        } else {
            if (key == 'q') {
                if (appController.editor.animationsManager.currentAnim.currentKeyframeIndex != 0) {
                    appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.currentKeyframeIndex--;
                    appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                    appController.editor.previewController.unselectDevices();
                }
            }
            if (key == 'd') {
                if (appController.editor.animationsManager.currentAnim.currentKeyframeIndex != (appController.editor.animationsManager.currentAnim.keyframeNumber - 1)) {
                    appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.currentKeyframeIndex++;
                    appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                    appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                    appController.editor.previewController.unselectDevices();
                }
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
        if(appController.editor.animationsManager instanceof AnimationsManager && appController.player instanceof Player) {
            if (e.name().equals("inputNewAnimName")) {
                appController.editor.animationsManager.currentAnimName = appController.editor.animationsManager.inputNewAnimName.getText();
                appController.editor.animationsManager.labelNameAnimation.setText(appController.editor.animationsManager.currentAnimName);
                appController.editor.animationsManager.labelNameAnimation.show();
                appController.editor.animationsManager.inputNewAnimName.hide();
                appController.editor.animationsManager.inputNewAnimFPS.show();
                appController.editor.animationsManager.inputNewAnimFPS.setFocus(true);
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
                if (appController.editor.animationsManager.buttonPlayAnim.isOn()) {
                    appController.editor.animationsManager.buttonPlayAnim.setOff();
                    tempPlay = true;
                }
                appController.editor.animationsManager.setCurrentAnimIndex(currentIndex);
                appController.editor.animationsManager.updateCurrentAnim(currentIndex);
                appController.editor.animationsManager.highlightSelectedAnim(currentIndex);
                appController.editor.animationsManager.displayAnimation(currentIndex);
                appController.editor.animationsManager.currentAnim.kfHasChanged = false;
                appController.editor.previewController.animation = true;
                appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.buttonPlayAnim.show();
                appController.editor.animationsManager.buttonNewKeyframe.show();
                appController.editor.animationsManager.buttonDeleteKeyframe.show();
                appController.editor.animationsManager.buttonResetKeyframe.show();
                appController.editor.animationsManager.labelKeyframeName.show();
                appController.editor.animationsManager.labelKeyframeName.setText("FIRST");
                appController.editor.animationsManager.buttonNewAnim.show();
                appController.editor.animationsManager.buttonDeleteAnim.show();
                appController.editor.animationsManager.sliderDeviceOpacity.show();
                if (tempPlay)
                    appController.editor.animationsManager.buttonPlayAnim.setOn();

            }
            if (e.isTab() && e.getTab().getName() == "default" && appController.editor.animationsManager.inputNewAnimName.isVisible()) {
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
                    appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
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
                appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                appController.editor.animationsManager.sliderDeviceOpacity.setValue(0);
                appController.editor.animationsManager.currentAnim.kfHasChanged = true;
            }

            if (e.name().equals("buttonPlayAnim")) {
                if (appController.editor.animationsManager instanceof AnimationsManager) {
                    if (appController.editor.animationsManager.buttonPlayAnim.isOn()) {
                        appController.editor.animationsManager.buttonNewKeyframe.hide();
                        appController.editor.animationsManager.buttonDeleteKeyframe.hide();
                        appController.editor.animationsManager.buttonResetKeyframe.hide();
                        appController.editor.animationsManager.buttonNewAnim.hide();
                        appController.editor.animationsManager.buttonDeleteAnim.hide();
                        appController.editor.animationsManager.sliderDeviceOpacity.hide();
                        appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                        appController.editor.animationsManager.playAnimation();
                    } else {
                        appController.editor.animationsManager.buttonNewKeyframe.show();
                        appController.editor.animationsManager.buttonDeleteKeyframe.show();
                        appController.editor.animationsManager.buttonResetKeyframe.show();
                        appController.editor.animationsManager.buttonNewAnim.show();
                        appController.editor.animationsManager.buttonDeleteAnim.show();
                        appController.editor.animationsManager.sliderDeviceOpacity.show();
                        appController.editor.animationsManager.stopAnimation();
                    }
                }
            }
            if (e.name().equals("default")) {
                appController.editor.animationsManager.listAnimations.show();
                appController.editor.previewController.editor = true;
                appController.editor.previewController.offsetOpacity = 0;
                appController.player.sliderMasterOpacity.setValue(1);
                appController.player.buttonNoise.setOff();
                appController.player.buttonStrobe.setOff();
            }
            if (e.name().equals("tabPlayer")) {
                appController.editor.animationsManager.listAnimations.show();
                appController.player.sliderMasterOpacity.show();
                appController.player.buttonNoise.show();
                appController.player.buttonStrobe.show();
                appController.editor.previewController.editor = false;
                appController.player.sliderMasterOpacity.setValue(1);
            }
            if (e.name().equals("sliderDeviceOpacity")) {
                appController.editor.animationsManager.currentAnim.currentKeyframe.currentOpacity = appController.editor.animationsManager.sliderDeviceOpacity.getValue();
                appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                appController.editor.animationsManager.currentAnim.kfHasChanged = true;
            }
            if (e.name().equals("sliderMasterOpacity")) {
                appController.player.adaptOpacityFromMaster(appController.player.sliderMasterOpacity.getValue());
            }
            if (e.name().equals("buttonNoise")) {
                appController.editor.previewController.noise = appController.player.buttonNoise.getBooleanValue();
            }

            if (e.name().equals("buttonStrobe")) {
                appController.editor.previewController.strobe = appController.player.buttonStrobe.getBooleanValue();
            }
        }
    }


    public void noteOn(Note note){
        int pit = note.getPitch();
        switch (pit){
            case 39: if(appController.editor.previewController.noise)
                        appController.player.buttonNoise.setOff();
                    else
                        appController.player.buttonNoise.setOn();
                break;
            case 48: if(appController.editor.animationsManager.buttonPlayAnim.isOn())
                        appController.editor.animationsManager.buttonPlayAnim.setOff();
                    else
                        appController.editor.animationsManager.buttonPlayAnim.setOn();
                break;
            case 38: if(appController.player.buttonStrobe.isOn())
                        appController.player.buttonStrobe.setOff();
                    else
                        appController.player.buttonStrobe.setOn();
                break;
        }
    }

    public void noteOff(Note note){
        int pit = note.getPitch();
    }

    public void controllerIn(promidi.Controller controller){
        int num = controller.getNumber();
        int val = controller.getValue();

        switch (num){
            case 25: appController.player.sliderMasterOpacity.setValue(map(val, 0, 127, 0, 1));
                break;
            case 24: appController.editor.previewController.periodStrobe=(long)map(val, 0, 127, 1, 20);
                break;
            case 3: appController.editor.animationsManager.currentAnim.fps= (int)map(val, 0, 127, 1, 20);
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
