/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonify;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author Rui
 */
abstract public class Processor {
    protected Score score;
    protected final String outputName;
    protected BufferedImage image;
    protected final Random rnd;
    protected final int MAX_DIM = 256;
    protected final int MAX_PITCH = Note.MAX_PITCH + 1;
    protected final int MAX_PITCH_REPETITIONS = 4;

    public Processor(String imgPath) throws IOException {
        rnd = new Random(System.currentTimeMillis());
        File f = new File(imgPath);
        if (!f.isDirectory() && f.exists()) {
            outputName = f.getName().substring(0, f.getName().lastIndexOf(".")) + "_mono.mid";
            image = ImageIO.read(f);
            if(image.getWidth() > MAX_DIM || image.getHeight() > MAX_DIM) {
                image = Thumbnails.of(image).scale(calculateScaleFactor()).asBufferedImage();
            }
        } else {
            throw new IOException("Invalid image path.");
        }
        score = new Score(outputName);
    }

    public void Run() {
        prepareData();
        translateData();
        save();
    }
    
    abstract protected void prepareData();
    
    abstract protected void translateData();
    
    private void save() {
        System.out.println("Outputting midi file...");
        Write.midi(score, outputName);
    }
    
    private double calculateScaleFactor() {
        double scale;
        
        if(image.getHeight() >= image.getWidth()) {
            scale = (double) MAX_DIM / image.getHeight();
        } else {
            scale = (double) MAX_DIM / image.getWidth();
        }
        System.out.println("Scaling factor: " + scale);
        return scale;
    }
}
