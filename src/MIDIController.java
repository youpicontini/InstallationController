//import processing.core.*;
//import promidi.*;
//import java.applet.*;
//import java.awt.*;
//import java.awt.image.*;
//import java.awt.event.*;
//import java.io.*;
//import java.net.*;
//import java.text.*;
//import java.util.*;
//import java.util.zip.*;
//
//
//public class MIDIController extends PApplet{
//
//    PApplet parent;
//    AppController appController;
//
//    MidiIO midiIO;
//
//    MIDIController(PApplet _parent, AppController _appController) {
//        parent = _parent;
//        appController = _appController;
//    }
//
//    public void setup() {
//
//        // get an instance of midiIO
//        midiIO = MidiIO.getInstance(parent);
//
//        // print a list of all devices
//        midiIO.printDevices();
//
//        //plug all methods to handle midievents
//        midiIO.plug(parent,"noteOn",0,0);
//        midiIO.plug(parent,"noteOff",0,0);
//        midiIO.plug(parent,"controllerIn",0,0);
//        midiIO.plug(parent,"programChange",0,0);
//    }
//
//    void noteOn(Note note){
//        int vel = note.getVelocity();
//        int pit = note.getPitch();
//        parent.println("noteOn: " + pit);
//    }
//
//    void noteOff(Note note){
//        int pit = note.getPitch();
//        parent.println("noteOff: " + pit);
//    }
//
//    void controllerIn(Controller controller){
//        int num = controller.getNumber();
//        int val = controller.getValue();
//        parent.println("controllerIn: " + num + " " + val);
//    }
//
//    void programChange(ProgramChange programChange){
//        int num = programChange.getNumber();
//        parent.println("programChange: "+num);
//    }
//}
//
//
//
////public class promidi_plug extends PApplet {
////
////    MidiIO midiIO;
////
////    public void setup() {
////
////        midiIO = MidiIO.getInstance(this);
////        println("printPorts of midiIO");
////        midiIO.printDevices();
////
////        midiIO.plug(this, "noteOn", 0, 0);
////        midiIO.plug(this, "noteOff", 0, 0);
////        midiIO.plug(this, "controllerIn", 0, 0);
////        midiIO.plug(this, "programChange", 0, 0);
////    }
////
////    public void draw() {
////
////    }
////
////    public void noteOn(Note note, int deviceNumber, int midiChannel) {
////        int vel = note.getVelocity();
////        int pit = note.getPitch();
////    }
////
////    public void noteOff(Note note, int deviceNumber, int midiChannel) {
////        int pit = note.getPitch();
////    }
////
////    public void controllerIn(Controller controller, int deviceNumber, int midiChannel) {
////        int num = controller.getNumber();
////        int val = controller.getValue();
////    }
////
////    public void programChange(ProgramChange programChange, int deviceNumber, int midiChannel) {
////
////        int num = programChange.getNumber();
////
////    }
////}