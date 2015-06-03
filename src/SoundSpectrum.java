import ddf.minim.analysis.*;
import ddf.minim.*;
import processing.core.PApplet;


public class SoundSpectrum {

    PApplet parent;

    Minim minim;
    AudioInput in;
    FFT fft;
    BeatDetect beat;

    int w;


    SoundSpectrum(PApplet _parent){
        parent = _parent;
    }

    public void setup(){
        minim = new Minim(this);
        in = minim.getLineIn(Minim.STEREO,512);
        fft = new FFT(in.bufferSize(), in.sampleRate());
        fft.logAverages(60,7);
        //parent.stroke(255);
        w = parent.width/(6*fft.avgSize());
        parent.strokeWeight(w);
        parent.strokeCap(parent.SQUARE);

        beat = new BeatDetect();
    }

    public void draw() {
        fft.forward(in.mix);
        parent.pushMatrix();
        parent.translate(1062,59);
        beat.detect(in.mix);
        if (beat.isOnset()) {
            parent.pushStyle();
            parent.stroke(255);
            parent.strokeWeight(10);
            parent.line(0, 5, 187, 5);
            parent.popStyle();
        }
        for(int i = 0; i < fft.avgSize(); i++){
            parent.line((i*w), 0, (i*w), 0-fft.getAvg(i)*4);
        }
        parent.popMatrix();
    }
}
