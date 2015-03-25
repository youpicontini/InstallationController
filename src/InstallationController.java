import processing.core.*;
import controlP5.*;

public class InstallationController extends PApplet {

    ControlP5 cp5;
    AppController appController;

    public static final String APPNAME="InstallationController";
    public static final String PROJECTNAME="Crystal Net";

    public static void main(String args[]) {
        PApplet.main("InstallationController");
    }

	public void setup() {
        size(1440,900);
        frame.setTitle(APPNAME);

        cp5 = new ControlP5(this);
        appController = new AppController(cp5, this);
        appController.setup();
	}

	public void draw() {
        background(100);
        appController.draw();
	}

    boolean pressingP=false;
    boolean pressingS=false;
    boolean pressingCTRL=false;

    public void keyPressed()
    {
        if (key == CODED ){if(keyCode == CONTROL)pressingCTRL=true;}
        if(key=='p')pressingP=true;
        if(key=='s')pressingS=true;

        //if( pressingP && pressingCTRL) appController.playAnim();
    }

    public void keyReleased()
    {
        if(key=='p')pressingP = false;
        if(key=='s')pressingS = false;
        if ((key == CODED )&&(keyCode == CONTROL)) pressingCTRL = false;
    }
    public void controlEvent(ControlEvent e) {
        println(e);
        if(e.name().equals("inputNewAnimName")){
            appController.editor.animationsManager.newAnimation(appController.editor.animationsManager.inputNewAnimName.getText());
        }
        if(e.name().equals("listAnimations")){
            int currentIndex = (int)e.group().value();
            appController.editor.animationsManager.highlightSelectedAnim(currentIndex);
        }
        if (e.isTab() && e.getTab().getName()=="default" && appController.editor.animationsManager.inputNewAnimName.isVisible()) {
            appController.editor.animationsManager.labelNameAnimation.show();
            appController.editor.animationsManager.inputNewAnimName.hide();
        }
        if(e.name().equals("buttonNewAnim")){
            if (appController.editor.animationsManager instanceof AnimationsManager)
                appController.editor.animationsManager.toggleVisibilityInputNewAnimation();
        }

        /*
        if(e.name().equals("keyframeDurationInput")){
            appController.animEditNav.keyframe_duration = Float.parseFloat(appController.animEditNav.keyframe_duration_input.getText());
            print(appController.animEditNav.keyframe_duration);
        }
        */

        if(e.name().equals("buttonPlayAnim")){
            if (appController instanceof AppController)
                appController.editor.animationsManager.playAnimation();
       }
        if(e.name().equals("buttonStopAnim")){
            if (appController instanceof AppController)
                appController.editor.animationsManager.stopAnimation();
       }
    }

    public void mousePressed() {
        if ((mouseY > (appController.editor.timeline.y_timeline - appController.editor.timeline.upTick)) && (mouseY < (appController.editor.timeline.y_timeline + appController.editor.timeline.upTick))) {
            print("Pressed");
            appController.editor.timeline.mDifX = mouseX - appController.editor.timeline.mX;
            cursor(HAND);
        }
    }

    public void mouseReleased() {
        if ((mouseY > (appController.editor.timeline.y_timeline - appController.editor.timeline.upTick)) && (mouseY < (appController.editor.timeline.y_timeline + appController.editor.timeline.upTick))){
            println("Released");
            cursor(ARROW);
        }
    }

    public void mouseDragged() {
        if ((mouseY > (appController.editor.timeline.y_timeline - appController.editor.timeline.upTick)) && (mouseY < (appController.editor.timeline.y_timeline + appController.editor.timeline.upTick))){
            print("dragged");
            appController.editor.timeline.mX = mouseX - appController.editor.timeline.mDifX;
            println(appController.editor.timeline.mX);
        }
    }
}
