import processing.core.*;
import controlP5.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class InstallationController extends PApplet {

    ControlP5 cp5;
    AppController appController;

    public static final String APPNAME="InstallationController";
    public static final String PROJECTNAME="Crystal Net";

    public static void main(String args[]) {
        PApplet.main("InstallationController");
    }

	public void setup() {
        size(1440,900);
        frame.setTitle(APPNAME);

        cp5 = new ControlP5(this);
        appController = new AppController(cp5, this);
        appController.setup();
	}

	public void draw() {
        background(100);
        appController.draw();
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
                    print(tempIndex);
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
        if (key == 'q' ){
            if(appController.editor.animationsManager.currentAnim.currentKeyframeIndex != 0) {
                println("----");
                println("previous");
                appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.currentKeyframeIndex--;
                appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                appController.editor.previewController.unselectDevices();
                String tempname ="keyframe n°"+ Integer.toString(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.labelKeyframeName.setText(tempname);
                println("current kf" + appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                println(appController.editor.animationsManager.currentAnim.currentValues);
            }
            else {
                String tempname ="first";
                appController.editor.animationsManager.labelKeyframeName.setText(tempname);
                println("first");
            }
        }
        if (key == 'd' ){
            if(appController.editor.animationsManager.currentAnim.currentKeyframeIndex != (appController.editor.animationsManager.currentAnim.keyframeNumber-1)) {
                println("----");
                println("next");
                appController.editor.animationsManager.currentAnim.saveKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.currentKeyframeIndex++;
                appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                appController.editor.previewController.unselectDevices();
                String tempname ="keyframe n°"+ Integer.toString(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.labelKeyframeName.setText(tempname);
                println("current kf" + appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                println(appController.editor.animationsManager.currentAnim.currentValues);
            }
            else {
                String tempname ="last";
                appController.editor.animationsManager.labelKeyframeName.setText(tempname);
                println("last");
            }
        }
    }

    public void mouseWheel(MouseWheelEvent event) {
        println(event.getWheelRotation());
    }

    public void controlEvent(ControlEvent e) {
        println(e);
        if(e.name().equals("inputNewAnimName")){
            appController.editor.animationsManager.currentAnimName=appController.editor.animationsManager.inputNewAnimName.getText();
            println(appController.editor.animationsManager.currentAnimName);
            appController.editor.animationsManager.labelNameAnimation.setText(appController.editor.animationsManager.currentAnimName);
            appController.editor.animationsManager.labelNameAnimation.show();
            appController.editor.animationsManager.inputNewAnimName.hide();
            appController.editor.animationsManager.inputNewAnimFPS.show();
            appController.editor.animationsManager.inputNewAnimFPS.setFocus(true);
        }
        if(e.name().equals("buttonNewAnim")){
            if (appController.editor.animationsManager instanceof AnimationsManager)
                appController.editor.animationsManager.toggleVisibilityInputNewAnimation();
        }
        if(e.name().equals("inputNewAnimFPS")){
            appController.editor.animationsManager.newAnimation(appController.editor.animationsManager.currentAnimName, Integer.parseInt(appController.editor.animationsManager.inputNewAnimFPS.getText()));
            background(100);
            appController.editor.animationsManager.inputNewAnimFPS.hide();
            //appController.editor.animationsManager.show();
        }
        if(e.name().equals("listAnimations")){
            int currentIndex = (int)e.group().value();
            appController.editor.animationsManager.setCurrentAnimIndex(currentIndex);
            appController.editor.animationsManager.updateCurrentAnim(currentIndex);
            appController.editor.animationsManager.highlightSelectedAnim(currentIndex);
            appController.editor.animationsManager.displayAnimation(currentIndex);
            appController.editor.animationsManager.currentAnim.kfHasChanged=false;
            appController.editor.previewController.animation=true;
            appController.editor.animationsManager.buttonPlayAnim.show();
            appController.editor.animationsManager.buttonStopAnim.show();
            appController.editor.animationsManager.buttonNewKeyframe.show();
            appController.editor.animationsManager.buttonDeleteKeyframe.show();
            appController.editor.animationsManager.buttonNewAnim.show();
            appController.editor.animationsManager.buttonDeleteAnim.show();
            appController.editor.animationsManager.sliderDeviceOpacity.show();
        }
        if (e.isTab() && e.getTab().getName()=="default" && appController.editor.animationsManager.inputNewAnimName.isVisible()) {
            appController.editor.animationsManager.labelNameAnimation.show();
            appController.editor.animationsManager.inputNewAnimName.hide();
        }
        if(e.name().equals("buttonDeleteAnim")) {
            if (appController.editor.animationsManager instanceof AnimationsManager)
                appController.editor.animationsManager.deleteAnimation(appController.editor.animationsManager.selectedIndex);
        }
        if(e.name().equals("buttonNewKeyframe")) {
            if (appController.editor.animationsManager instanceof AnimationsManager) {
                appController.editor.animationsManager.currentAnim.addKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.loadKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
                appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
                appController.editor.previewController.unselectDevices();
                appController.editor.animationsManager.currentAnim.kfHasChanged=false;
            }
        }
        if(e.name().equals("buttonDeleteKeyframe")) {
            if (appController.editor.animationsManager instanceof AnimationsManager)
                appController.editor.animationsManager.currentAnim.removeKeyframe(appController.editor.animationsManager.currentAnim.currentKeyframeIndex);
            appController.editor.animationsManager.currentAnim.kfHasChanged=false;
        }

        if(e.name().equals("buttonPlayAnim")){
            if (appController.editor.animationsManager instanceof AnimationsManager) {
                appController.editor.animationsManager.buttonNewKeyframe.hide();
                appController.editor.animationsManager.buttonDeleteKeyframe.hide();
                appController.editor.animationsManager.buttonNewAnim.hide();
                appController.editor.animationsManager.buttonDeleteAnim.hide();
                appController.editor.animationsManager.sliderDeviceOpacity.hide();
                appController.editor.animationsManager.playAnimation();
            }
        }
        if(e.name().equals("buttonStopAnim")){
            if (appController.editor.animationsManager instanceof AnimationsManager) {
                appController.editor.animationsManager.buttonNewKeyframe.show();
                appController.editor.animationsManager.buttonDeleteKeyframe.show();
                appController.editor.animationsManager.buttonNewAnim.show();
                appController.editor.animationsManager.buttonDeleteAnim.show();
                appController.editor.animationsManager.sliderDeviceOpacity.show();
                appController.editor.animationsManager.stopAnimation();
            }
        }
        if(e.name().equals("default")){
            appController.editor.animationsManager.listAnimations.show();
            appController.editor.previewController.editor=true;
        }
        if(e.name().equals("tabPlayer")){
            appController.editor.animationsManager.listAnimations.show();
            appController.editor.previewController.editor=false;
        }
        if(e.name().equals("sliderDeviceOpacity")){
            appController.editor.animationsManager.currentAnim.currentKeyframe.currentOpacity=appController.editor.animationsManager.sliderDeviceOpacity.getValue();
            appController.editor.animationsManager.currentAnim.sendCurrentValuesToPreviewController();
            appController.editor.animationsManager.currentAnim.kfHasChanged=true;
            println(appController.editor.animationsManager.currentAnim.currentValues);
        }
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
