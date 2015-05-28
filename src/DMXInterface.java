//
//import dmxP512.*;
//import processing.core.PApplet;
//import processing.serial.Serial;
//
//public class DMXInterface {
//
//    PApplet parent;
//    DmxP512 dmxOutput;
//    AppController appController;
//
//    int universeSize = 128;
//
//    boolean LANBOX = false;
//    String LANBOX_IP = "192.168.1.77";
//
//    boolean DMXPRO = true;
//    String DMXPRO_PORT = "COM1";//case matters ! on windows port must be upper cased.
//    int DMXPRO_BAUDRATE = 115000;
//
//    int nbChannel;
//    float[] currentValues;
//
//    DMXInterface(PApplet _parent, AppController _appController) {
//        parent = _parent;
//        appController = _appController;
//
//        nbChannel = appController.editor.animationsManager.nb_elements;
//    }
//
//    void setup() {
//
//        Serial myPort;
//        // List all the available serial ports
//        parent.println(Serial.list());
//        dmxOutput=new DmxP512(parent, universeSize, false);
//        if(LANBOX){
//            dmxOutput.setupLanbox(LANBOX_IP);
//        }
//        if(DMXPRO){
//            dmxOutput.setupDmxPro(DMXPRO_PORT,DMXPRO_BAUDRATE);
//        }
//    }
//
//    void draw() {
//        dmxOutput.set(1,255);
//        dmxOutput.set(2,0);
//        dmxOutput.set(3,255);
//        dmxOutput.set(4,0);
//
////        currentValues = appController.editor.animationsManager.currentAnim.currentValues;
////        for(int i=0;i<nbChannel;i++){
////            dmxOutput.set(i,Math.round(parent.map(currentValues[i], 0, 1, 0, 255)));
////        }
//
//    }
//}