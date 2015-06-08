import dmxP512.*;
import processing.core.PApplet;

public class DMXInterface {

    PApplet parent;
    DmxP512 dmxOutput;
    AppController appController;

    int universeSize = 50;

    String DMXPRO_PORT = "/dev/tty.usbserial-EN168019";//case matters! on windows port must be upper cased.
    int DMXPRO_BAUDRATE = 115000;

    int nbChannel;
    float[] currentKeyframeValues;

    DMXInterface(PApplet _parent, AppController _appController) {
        parent = _parent;
        appController = _appController;
    }

    void setup() {
        dmxOutput = new DmxP512(parent, universeSize, false);
        dmxOutput.setupDmxPro(DMXPRO_PORT, DMXPRO_BAUDRATE);
        nbChannel = appController.editor.animationsManager.nb_elements;

    }

    void draw() {
        currentKeyframeValues = appController.player.getCurrentKeyframeValues();
        for(int i=0; i<nbChannel-1; i++){
            dmxOutput.set(i+1,Math.round(parent.map(currentKeyframeValues[i], 0, 1, 0, 255)));
        }
    }
}