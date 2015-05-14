import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.data.JSONObject;

import java.io.File;
import java.text.DecimalFormat;


public class Animation {

    Keyframe currentKeyframe;
    float[] currentValues = new float[4];

    int currentKeyframeIndex;

    String name;
    String idAnim;
    int fps;

    int keyframeNumber = 1;
    boolean kfHasChanged = false;
    boolean AnimPlaying;

    ControlP5 cp5;
    PApplet parent;
    JSONObject jsonAnimation;
    PreviewController previewController;

    Animation (String _name, int _fps,ControlP5 _cp5, PApplet _parent, PreviewController _previewController) {
        name = _name;
        idAnim = name.replaceAll(" ","_");
        fps = _fps;
        cp5 = _cp5;
        parent = _parent;
        previewController = _previewController;

        jsonAnimation = new JSONObject();
        jsonAnimation.setString("id", idAnim);
        jsonAnimation.setString("name", name);
        jsonAnimation.setInt("fps", fps);
        parent.saveJSONObject(jsonAnimation, "animations\\" + idAnim + "\\config.json");

        currentKeyframeIndex=0;
        AnimPlaying = false;
        setInitialKeyframeNumber();
    }

    void addKeyframe(int currentIndex){

        saveKeyframe(currentIndex);
        int temp = currentIndex+1;
        currentKeyframeIndex = currentIndex;
        renameFollowingKeyframesFiles(keyframeNumber - 1, currentIndex);
        parent.println("adding kf n°" + temp);
        currentValues = new float[4];
        kfHasChanged = true;
        saveKeyframe(temp);
        //currentKeyframe = new Keyframe(parent, currentValues);
        currentKeyframeIndex++;
        keyframeNumber++;
    }

    void addFirstKeyframe(){
        currentValues = new float[4];
        kfHasChanged = true;
        saveKeyframe(0);
        currentKeyframeIndex = 0;
        keyframeNumber = 1;
        parent.println("1st kf added");
    }

    void removeKeyframe(int currentIndex){
        DecimalFormat formatter = new DecimalFormat("0000");
        String indexFormatted = formatter.format(currentIndex);
        File f = new File("animations\\" + idAnim+"\\keyframes\\"+indexFormatted+".json");

        boolean deleted = false;
        int i=0;
        parent.cursor(PConstants.WAIT);
        while(!deleted){
            try {
                deleted = f.delete();
                parent.println("deleting... wait\t"+i);
            }catch (Exception e){
                parent.println("File : "+e.getMessage());
            }
            i++;
        }
        parent.cursor(PConstants.ARROW);
        parent.println("removed kf n° " + currentIndex);
        renameFollowingKeyframesFiles(currentIndex,keyframeNumber-1);
        keyframeNumber--;
        currentKeyframeIndex--;
    }

    public void saveKeyframe(int currentIndex){
        if(kfHasChanged) {
            parent.println("saving..." + currentKeyframeIndex);
            JSONObject jsonKeyframe;
            jsonKeyframe = parent.loadJSONObject(new File("animations\\TEMPLATE_keyframe.json"));
            parent.println("current values lenght:"+currentValues.length);
            parent.println("saving/");
            parent.println(currentValues);
            parent.println("missing:");
            parent.println(currentValues[0]);
            for (int i = 0; i < currentValues.length; i++) {
                jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(i).getJSONObject("params").setFloat("opacity", currentValues[i]);
            }
            DecimalFormat formatter = new DecimalFormat("0000");
            String indexFormatted = formatter.format(currentIndex);
            parent.saveJSONObject(jsonKeyframe, "animations\\" + idAnim + "\\keyframes\\" + indexFormatted + ".json");
            kfHasChanged = false;
        }
    }

    public void loadKeyframe(int currentIndex){
        JSONObject jsonKeyframe;
        DecimalFormat formatter = new DecimalFormat("0000");
        String indexFormatted = formatter.format(currentIndex);
        jsonKeyframe = parent.loadJSONObject(new File("animations\\" + idAnim + "\\keyframes\\"+ indexFormatted + ".json"));
        for (int i=0; i<currentValues.length;i++) {
            currentValues[i] = jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(i).getJSONObject("params").getFloat("opacity");
        }
        currentKeyframe = new Keyframe(parent, currentValues);
    }

    void sendCurrentValuesToPreviewController(){
        currentValues[currentKeyframe.currentDevice] = currentKeyframe.currentOpacity;
        previewController.setCurrentKeyframeValues(currentValues);
    }

    void renameFollowingKeyframesFiles(int startIndex, int endIndex){
        if(startIndex != endIndex) {
            boolean renamed;
            parent.cursor(PConstants.WAIT);
            DecimalFormat formatter = new DecimalFormat("0000");
            int i = startIndex;
            if(startIndex>endIndex) { //add
                while (i > endIndex ) {
                    String iFormatted = formatter.format(i);
                    try{
                        File oldFile = new File("animations\\" + idAnim + "\\keyframes\\" + iFormatted + ".json");
                        iFormatted = formatter.format(i+1);
                        File newFile = new File("animations\\" + idAnim + "\\keyframes\\" + iFormatted + ".json");
                        renamed = oldFile.renameTo(newFile);
                        int temp=i+1;
                        System.out.println(i+" renamed to "+ temp +">"+ renamed);
                        if(renamed)i--;
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
            else { //delete
                while(i < endIndex) {
                    String iFormatted = formatter.format(i+1);
                    try{
                        File oldFile = new File("animations\\" + idAnim + "\\keyframes\\" + iFormatted + ".json");
                        iFormatted = formatter.format(i);
                        File newFile = new File("animations\\" + idAnim + "\\keyframes\\" + iFormatted + ".json");
                        renamed = oldFile.renameTo(newFile);
                        int temp=i-1;
                        System.out.println(i+" renamed to "+temp +">"+ renamed);
                        if(renamed)i++;
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
            parent.cursor(PConstants.ARROW);
        }
    }

    void setInitialKeyframeNumber(){

        File folder = new File("animations\\" + idAnim +"\\keyframes");
        if (folder.exists())
            keyframeNumber = folder.listFiles().length;
        else
            keyframeNumber = 1;
    }

    void play(){
        AnimPlaying = true;
        Thread t = new Thread(new Runnable() {
            public void run()
            {
                while(AnimPlaying) {
                    for (currentKeyframeIndex = 0; currentKeyframeIndex < keyframeNumber; currentKeyframeIndex++) {
                        parent.println(currentKeyframeIndex+" played");
                        loadKeyframe(currentKeyframeIndex);
                        sendCurrentValuesToPreviewController();
                        try {
                            Thread.sleep(1000 / fps);                 //1000 milliseconds is one second.
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    currentKeyframeIndex = 0;
                }
            }
        });
        t.start();
    }

    void stop(){
        AnimPlaying = false;
    }

}