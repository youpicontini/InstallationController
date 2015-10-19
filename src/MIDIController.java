import processing.core.PApplet;
import themidibus.*;

public class MIDIController {

    PApplet parent;
    AppController appController;
    MidiBus midiBus;
    int nb_device, midiClk;
    int timeDiv;

    MIDIController(PApplet _parent, AppController _appController, int _nb_device){
        parent = _parent;
        appController = _appController;
        nb_device = _nb_device;
    }

    void setup(){
        timeDiv = 24;
        midiBus = new MidiBus(this, 0, 0);
    }

    public void noteOn(Note note){
        int pit = note.pitch();

        float tempKfValues[] = new float[nb_device];
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
                    appController.player.buttonStrobe.setOff();
                    appController.player.buttonNoise.setOn();
                }
                break;
            case 37:
                if (appController.player.buttonStrobe.isOn())
                    appController.player.buttonStrobe.setOff();
                else {
                    appController.player.buttonNoise.setOff();
                    appController.player.buttonStrobe.setOn();
                }
                break;
            case 38:
                if (appController.player.buttonMorph.isOn())
                    appController.player.buttonMorph.setOff();
                else {
                    appController.player.buttonRandom.setOff();
                    appController.player.buttonInvert.setOff();
                    appController.player.buttonMorph.setOn();
                }
                break;
            case 39:
                if (appController.player.buttonInvert.isOn())
                    appController.player.buttonInvert.setOff();
                else {
                    appController.player.buttonRandom.setOff();
                    appController.player.buttonMorph.setOff();
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
                else{
                    timeDiv = 48;
                }
                break;
            case 45:
                if(appController.player.bongoMode) {
                    for (int i = 5; i < 11; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                else{
                    timeDiv = 24;
                }
                break;
            case 46:
                if(appController.player.bongoMode) {
                    for (int i = 20; i < 26; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                else{
                    timeDiv = 12;
                }
                break;
            case 47:
                if(appController.player.bongoMode) {
                    for (int i = 25; i < 31; i++) {
                        tempKfValues[i] = 1;
                    }
                    appController.player.setCurrentKeyframeValues(tempKfValues);
                }
                else{
                    timeDiv = 6;
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
                else{
                    if (appController.player.buttonRandom.isOn())
                        appController.player.buttonRandom.setOff();
                    else {
                        appController.player.buttonInvert.setOff();
                        appController.player.buttonRandom.setOn();
                    }
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
            /*case 51:
                int tempDiv = appController.player.fpsDivider;
                switch (tempDiv){
                    case 1:appController.player.fpsDivider = 2;
                        break;
                    case 2:appController.player.fpsDivider = 4;
                        break;
                    case 4:appController.player.fpsDivider = 1;
                        break;
                }*/
        }
    }

    public void controllerChange(ControlChange change){
        int num = change.number();
        int val = change.value();

        switch (num){
            case 25: appController.player.sliderMasterOpacity.setValue(parent.map(val, 0, 127, 0, 1));
                break;
            case 21: appController.player.periodStrobe=(long)parent.map(val, 0, 127, 1, 15);
                break;
            case 20:if(appController.player.liveMode) appController.player.fps = (int)parent.map(val, 0, 127, 1000, 200);
                break;
        }
    }

    public void rawMidi(byte[] data) {
        if(data[0] == (byte)0xF8) {
            midiClk = (midiClk+1)%48;
            if (midiClk % timeDiv == 0) {
                appController.player.fpsTick = true;
            }
            if(midiClk % (timeDiv/6) == 0){
                appController.player.morphTick = true;
            }
        }
    }
}