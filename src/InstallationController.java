import processing.core.*;
import controlP5.*;

public class InstallationController extends PApplet {

    ControlP5 cp5;
    AppController appController;

    public static final String PROJECTNAME="Crystal Net";

    public static void main(String args[]) {
        PApplet.main("InstallationController");
    }

	public void setup() {
        size(1440,900);
        frame.setTitle(PROJECTNAME);

        cp5 = new ControlP5(this);
        appController = new AppController(cp5, this);

        appController.setup();
	}

	public void draw() {
        background(100);
        appController.draw();
	}
}
