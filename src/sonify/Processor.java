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
public class Processor {
    private final String outputName;
    private BufferedImage image;
    private Score score;
    private int timbre;
    private int initialLoudnessIndex;
    private int initialLengthIndex;
    private final Random rnd;
    private final int MAX_DIM = 256;
    private final int MAX_PITCH_REPETITIONS = 4;
    private final double SCALE_FACTOR = 0.15;

    public Processor(String imgPath) throws IOException {
        rnd = new Random(System.currentTimeMillis());
        File f = new File(imgPath);
        if (!f.isDirectory() && f.exists()) {
            outputName = f.getName().substring(0, f.getName().lastIndexOf(".")) + ".mid";
            image = ImageIO.read(f);
            if(image.getWidth() > MAX_DIM || image.getHeight() > MAX_DIM) {
                image = Thumbnails.of(image).scale(SCALE_FACTOR).asBufferedImage();
            }
        } else {
            throw new IOException("Invalid image path.");
        }
    }

    public void Run() {
        prepareData();
        translateData();
        save();
    }
    
    private void prepareData() {
        score = new Score(outputName);
        Double dTimbre = new Double(image.getHeight() * image.getHeight() + rnd.nextInt(128));
        timbre = dTimbre.intValue() % Constants.INSTRUMENTS_SIZE;
        Double dInitialLengthIndex = new Double(image.getHeight() * image.getHeight() + rnd.nextInt(128));
        initialLoudnessIndex = (int) ((double)(image.getHeight() * image.getHeight() + rnd.nextInt()) % Constants.LOUDNESS_SIZE);
        initialLengthIndex = dInitialLengthIndex.intValue() % Constants.LENGTH_SIZE;
    }
    
    //TODO: evitar o mesmo pitch muitas vezes seguidas
    private void translateData() {
        int color, pitch;
        int maxPitch = Note.MAX_PITCH + 1;
        double time = 0.0;
        Part p = new Part("Sonified", Constants.Instruments[timbre], 0);
        Note n;
        
        int lastPitch = -1, lastPitchCount = 0;
        
        for(int x = 0; x < image.getWidth(); x++) {
            Phrase phr = new Phrase("Sonified Phrase " + x, time);
            for(int y = 0; y < image.getHeight(); y++) {
                color = image.getRGB(x, y);
                if(color < 0) { //evita valores negativos e ALGUNS valores repetidos
                    color *= -2;
                }
                
                pitch = (int)((double)color % maxPitch);
                if(lastPitch < 0) { //evitar muitas repetições de pitch
                    lastPitch = pitch;
                    lastPitchCount++;
                } else {
                    if(pitch == lastPitch) {
                        lastPitch++;
                        if(lastPitch > MAX_PITCH_REPETITIONS) {
                            continue;
                        }
                    } else {
                        lastPitch = pitch;
                        lastPitchCount = 1;
                    }
                }
                
                n = new Note(pitch, Constants.Length[initialLengthIndex], Constants.Loudness[8]);
                phr.addNote(n);
                time += n.getRhythmValue();
            }
            p.addPhrase(phr);
        }
        System.out.println("Finalizing score...");
        score.addPart(p);
    }
    
    private void save() {
        System.out.println("Outputting midi file...");
        Write.midi(score, outputName);
    }
}
