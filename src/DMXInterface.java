import dmxP512.*;
import processing.core.PApplet;

public class DMXInterface {

    PApplet parent;
    DmxP512 dmxOutput;
    AppController appController;

    int universeSize;

    String DMXPRO_PORT;

    int DMXPRO_BAUDRATE = 115000;

    int nbChannel;
    float[] currentKeyframeValues;

    DMXInterface(PApplet _parent, AppController _appController, int _universeSize) {
        parent = _parent;
        appController = _appController;
        //fix douzette transistor cassé "+1"
        universeSize = _universeSize+1;

    }

    void setup() {

        if(System.getProperty("os.name").equals("Mac OS X")) {
            DMXPRO_PORT = "/dev/tty.usbserial-EN168019";//case matters! on windows port must be upper cased.
        }
        else{
            DMXPRO_PORT = "COM3";
        }

        dmxOutput = new DmxP512(parent, universeSize, false);

        try {
            dmxOutput.setupDmxPro(DMXPRO_PORT, DMXPRO_BAUDRATE);
        }
        catch(Exception e){
            parent.println("NO DMX DEVICE CONNECTED");
        }

        nbChannel = appController.editor.animationsManager.nb_elements;
    }

    void draw() {
        currentKeyframeValues = appController.player.getCurrentKeyframeValues();
        for(int i=0; i<nbChannel; i++){
            dmxOutput.set(i+1,Math.round(parent.map(currentKeyframeValues[i], 0, 1, 0, 255)));
        }
        //fix douzette transistor cassé
        dmxOutput.set(nbChannel+1,Math.round(parent.map(currentKeyframeValues[1], 0, 1, 0, 255)));
        // fin fix
    }
}