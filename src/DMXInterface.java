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
        for(int i=1; i<nbChannel; i++){
            dmxOutput.set(i,Math.round(parent.map(appController.editor.previewController.currentKeyframeValuesDisplayed[i], 0, 1, 0, 255)));
        }
    }
}