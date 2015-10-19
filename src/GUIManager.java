import controlP5.ControlEvent;
import processing.core.PApplet;

public class GUIManager {


    PApplet parent;
    AppController appController;

    GUIManager(PApplet _parent, AppController _appController){
        parent = _parent;
        appController = _appController;
    }

    public void mouseClicked(){
        if(appController.editor.previewController.editor && appController.editor.previewController.animation) {
            if (appController.editor.previewController.currentLedStripeHover.ol) {
                appController.editor.previewController.currentLedStripe.selected = false;
                appController.editor.previewController.currentLedStripe = appController.editor.previewController.currentLedStripeHover;
                appController.editor.previewController.currentLedStripe.selected = true;
                int tempIndex = Integer.parseInt(appController.editor.previewController.currentLedStripe.id);
                appController.editor.animationsManager.currentAnim.currentKeyframe.currentDevice = tempIndex;
                appController.editor.animationsManager.sliderDeviceOpacity.setValue(appController.editor.animationsManager.currentAnim.currentValues[tempIndex]);
                appController.editor.animationsManager.currentAnim.kfHasChanged = false;
            } else {
                appController.editor.previewController.currentLedStripe.selected = false;
            }
        }
    }

    public void keyPressed(char key){

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
        if(key == 'p') {
            int tempDiv = appController.player.fpsDivider;
            switch (tempDiv) {
                case 1:
                    appController.player.fpsDivider = 2;
                    break;
                case 2:
                    appController.player.fpsDivider = 4;
                    break;
                case 4:
                    appController.player.fpsDivider = 1;
                    break;
            }
            parent.println(appController.player.fpsDivider);
        }
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
            parent.background(100);
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
            appController.player.buttonMorph.setOff();
            appController.player.buttonRandom.setOff();
        }
        if (e.name().equals("tabPlayer")) {
            appController.editor.animationsManager.listAnimations.show();
            appController.player.sliderMasterOpacity.show();
            appController.player.buttonNoise.show();
            appController.player.buttonStrobe.show();
            appController.player.buttonRandom.show();
            appController.player.buttonMorph.show();
            appController.player.buttonGlow.show();
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
            //System.out.println("OK");
        }

        if (e.name().equals("buttonStrobe")) {
            appController.player.strobe = appController.player.buttonStrobe.getBooleanValue();
        }

        if (e.name().equals("buttonLiveMode")) {
            if(appController.player.liveMode) {
                appController.player.FPSTick();
                appController.player.MorphTick();
            }
            appController.player.liveMode = appController.player.buttonLiveMode.getBooleanValue();

        }
        if (e.name().equals("buttonBongoMode")) {
            appController.player.bongoMode = appController.player.buttonBongoMode.getBooleanValue();
        }

        if (e.name().equals("buttonInvert")) {
            appController.player.invert = appController.player.buttonInvert.getBooleanValue();
        }
        if (e.name().equals("buttonRandom")) {
            appController.player.random = appController.player.buttonRandom.getBooleanValue();
        }

        if (e.name().equals("buttonMorph")) {
            appController.player.morpho = appController.player.buttonMorph.getBooleanValue();
        }

        if (e.name().equals("buttonGlow")) {
            appController.player.glow = appController.player.buttonGlow.getBooleanValue();
        }

    }
}
