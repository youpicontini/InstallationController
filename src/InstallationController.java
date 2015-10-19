import processing.core.*;
import controlP5.*;
import themidibus.*;

import java.awt.event.MouseWheelEvent;


public class InstallationController extends PApplet {

    ControlP5 cp5;
    AppController appController;
    //DMXInterface dmxInterface;
    MIDIController midiController;
    GUIManager guiManager;
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
            tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\setup.json";
            //System.out.println("tempPath");
        }

        nb_device = loadJSONObject(tempPath).getInt("nb_elements");

        soundSpectrum = new SoundSpectrum(this);
        appController = new AppController(cp5, soundSpectrum, nb_device, this);
        //dmxInterface = new DMXInterface(this, appController, nb_device);
        midiController = new MIDIController(this, appController, nb_device);
        guiManager = new GUIManager(this, appController);

        appController.setup();
        //dmxInterface.setup();
        midiController.setup();
        soundSpectrum.setup();
	}

	public void draw() {
        background(100);
        appController.draw();
        //dmxInterface.draw();
        soundSpectrum.draw();
	}


    // GUI
    public void mouseClicked(){
        if (mouseY >60 && mouseY <810 && mouseX<1250 && mouseX>200) {
            guiManager.mouseClicked();
        }
    }

    public void keyPressed() {
        guiManager.keyPressed(key);
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
        guiManager.controlEvent(e);
    }

    //MIDI
    public void noteOn(Note note){
        midiController.noteOn(note);
    }

    public void controllerChange(ControlChange change){
        midiController.controllerChange(change);
    }

    public void rawMidi(byte[] data) {
        midiController.rawMidi(data);
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
