import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.data.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;

public class Animation {

    Keyframe currentKeyframe;
    int nb_elements;
    float[] currentValues,nextValues;
    float[][] currentKeyframesValues;
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
    AnimationsManager AM;

    Animation (String _name, int _fps, int _nb_elements, ControlP5 _cp5, PApplet _parent, PreviewController _previewController, AnimationsManager _AM) {
        name = _name;
        idAnim = name.replaceAll(" ","_");
        fps = _fps;
        nb_elements = _nb_elements;
        cp5 = _cp5;
        parent = _parent;
        previewController = _previewController;
        AM = _AM;

        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/" + idAnim + "/config.json";
        }
        else {
            tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\" + idAnim + "\\config.json";
        }
        if(!(new File(tempPath).exists())) {
            jsonAnimation = new JSONObject();
            jsonAnimation.setString("id", idAnim);
            jsonAnimation.setString("name", name);
            jsonAnimation.setInt("fps", fps);
            parent.saveJSONObject(jsonAnimation, tempPath);
        }
        currentKeyframeIndex=0;
        AnimPlaying = false;
        currentValues = new float[nb_elements];
        nextValues = new float[nb_elements];
        setInitialKeyframeNumber();
        currentKeyframesValues = new float[keyframeNumber][nb_elements];
        setKeyframes();
    }

    void addKeyframe(int currentIndex){

        saveKeyframe(currentIndex);
        int temp = currentIndex+1;
        currentKeyframeIndex = currentIndex;
        renameFollowingKeyframesFiles(keyframeNumber - 1, currentIndex);
        kfHasChanged = true;
        saveKeyframe(temp);
        currentKeyframeIndex++;
        keyframeNumber++;
    }

    void addFirstKeyframe(){
        currentValues = new float[nb_elements];
        kfHasChanged = true;
        saveKeyframe(0);
        currentKeyframeIndex = 0;
        keyframeNumber = 1;
    }

    int getFps(){
        return fps;
    }

    void removeKeyframe(int currentIndex){
        DecimalFormat formatter = new DecimalFormat("0000");
        String indexFormatted = formatter.format(currentIndex);
        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/" + idAnim +"/keyframes/"+indexFormatted+".json";
        else
            tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\" + idAnim +"\\keyframes\\"+indexFormatted+".json";
        File f = new File(tempPath);
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
        renameFollowingKeyframesFiles(currentIndex,keyframeNumber-1);
        keyframeNumber--;
        currentKeyframeIndex--;
    }

    public void saveKeyframe(int currentIndex){
        if(kfHasChanged) {
            JSONObject jsonKeyframe;
            String tempPath;
            if(System.getProperty("os.name").equals("Mac OS X"))
                tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/TEMPLATE_keyframe.json";
            else
                tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\TEMPLATE_keyframe.json";
            jsonKeyframe = parent.loadJSONObject(new File(tempPath));
            for (int i = 0; i < currentValues.length; i++) {
                jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(i).getJSONObject("params").setFloat("opacity", currentValues[i]);
            }
            DecimalFormat formatter = new DecimalFormat("0000");
            String indexFormatted = formatter.format(currentIndex);
            if(System.getProperty("os.name").equals("Mac OS X"))
                tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/" + idAnim + "/keyframes/" + indexFormatted + ".json";
            else
                tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\" + idAnim + "\\keyframes\\" + indexFormatted + ".json";
            parent.saveJSONObject(jsonKeyframe, tempPath);
            kfHasChanged = false;
        }
    }

    public void loadKeyframe(int currentIndex){
        JSONObject jsonKeyframe;
        DecimalFormat formatter = new DecimalFormat("0000");
        String indexFormatted = formatter.format(currentIndex);
        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/" + idAnim + "/keyframes/"+ indexFormatted + ".json";
        else
            tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\" + idAnim + "\\keyframes\\"+ indexFormatted + ".json";
        jsonKeyframe = parent.loadJSONObject(new File(tempPath));
        if (currentIndex == 0)
            AM.labelKeyframeName.setText("FIRST");
        else if(currentIndex == keyframeNumber-1)
                AM.labelKeyframeName.setText("LAST");
            else
                AM.labelKeyframeName.setText("keyframe n°" +Integer.toString(currentIndex));
        for (int i = 0; i<currentValues.length;i++) {
            currentValues[i] = jsonKeyframe.getJSONArray("outputs").getJSONObject(0).getJSONArray("objects").getJSONObject(i).getJSONObject("params").getFloat("opacity");
        }
        currentKeyframe = new Keyframe(parent, currentValues);
    }

    float[] getCurrentValues() {
        if (currentKeyframe instanceof Keyframe)
            currentValues[currentKeyframe.currentDevice] = currentKeyframe.currentOpacity;
        return currentValues;
    }

    float[] getNextValues(){
        return nextValues;
    }

    void setCurrentValues(float[] val){
        for(int i = 0; i < val.length; i++) {
            currentValues[i] = val[i];
        }
    }
    void setNextValues(float[] val){
        for(int i = 0; i < val.length; i++) {
            nextValues[i] = val[i];
        }
    }



    void renameFollowingKeyframesFiles(int startIndex, int endIndex){
        if(startIndex != endIndex) {
            boolean renamed;
            parent.cursor(PConstants.WAIT);
            DecimalFormat formatter = new DecimalFormat("0000");
            String tempPath;
            if(System.getProperty("os.name").equals("Mac OS X"))
                tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/" + idAnim + "/keyframes/";
            else
                tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\" + idAnim + "\\keyframes\\";
            int i = startIndex;
            if(startIndex>endIndex) { //add
                while (i > endIndex ) {
                    String iFormatted = formatter.format(i);
                    try{
                        //File oldFile = new File(tempPath + iFormatted + ".json");
                        //iFormatted = formatter.format(i+1);
                        renamed =  new File(tempPath + formatter.format(i) + ".json").renameTo(new File(tempPath + formatter.format(i+1) + ".json"));
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
//                        File oldFile = new File(tempPath + iFormatted + ".json");
//                        iFormatted = formatter.format(i);
                        renamed = new File(tempPath + formatter.format(i+1) + ".json").renameTo(new File(tempPath + formatter.format(i) + ".json"));
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

        String tempPath;
        if(System.getProperty("os.name").equals("Mac OS X"))
            tempPath = System.getProperty("user.dir")+"/installations/CrystalNet/animations/" + idAnim + "/keyframes/";
        else
            tempPath = System.getProperty("user.dir")+"\\installations\\CrystalNet\\animations\\" + idAnim +"\\keyframes";
        File folder = new File(tempPath);
        if (folder.exists())
            keyframeNumber = folder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return !f.isHidden();
                }
            }).length;
        else
            keyframeNumber = 1;
    }

    void setKeyframes(){
        for (int i = 0; i < keyframeNumber; i++) {
            loadKeyframe(i);
            for (int j = 0; j < nb_elements; j++) {
                currentKeyframesValues[i][j] = currentValues[j];
            }
        }
    }

    void play(){
        setCurrentValues(currentKeyframesValues[currentKeyframeIndex]);
        /*for (int j = 0; j < nb_elements; j++) {
            System.out.print("[" + j + "]" + currentValues[j] + "\t");
        }
        System.out.println("ok");
        System.out.println("ok");*/
        if(currentKeyframeIndex != (keyframeNumber-1)) {
            setNextValues(currentKeyframesValues[currentKeyframeIndex+1]);
        }
        else {
            setNextValues(currentKeyframesValues[0]);
        }
        currentKeyframeIndex = (currentKeyframeIndex+1) % (keyframeNumber);
    }
}