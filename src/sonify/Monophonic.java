/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonify;

import java.io.IOException;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

/**
 *
 * @author RuiSa
 */
public class Monophonic extends Processor{
    private int timbre;
    private int initialLoudnessIndex;
    private int initialLengthIndex;
    
    public Monophonic(String imgPath) throws IOException {
        super(imgPath);
    }
    
    @Override
    protected void prepareData() {
        Double dTimbre = new Double(image.getHeight() * image.getHeight() + rnd.nextInt(128));
        timbre = dTimbre.intValue() % Constants.INSTRUMENTS_SIZE;
        Double dInitialLengthIndex = new Double(image.getHeight() * image.getHeight() + rnd.nextInt(128));
        initialLoudnessIndex = (int) ((double)(image.getHeight() * image.getHeight() + rnd.nextInt()) % Constants.LOUDNESS_SIZE);
        initialLengthIndex = dInitialLengthIndex.intValue() % Constants.LENGTH_SIZE;
    }
    
    @Override
    protected void translateData() {
        int color, pitch;
        double time = 0.0;
        Part p = new Part("Sonified", Constants.Instruments[timbre], 0);
        Note n;
        
        int lastPitch = -1, lastPitchCount = 0;
        
        for(int x = 0; x < image.getWidth(); x++) {
            Phrase phr = new Phrase("Sonified Phrase " + x, time);
            for(int y = 0; y < image.getHeight(); y++) {
                color = image.getRGB(x, y);
                
                if(color < 0) {
                    color *= -1;
                    color += MAX_PITCH;
                }
                
                pitch = (int)((double)color % MAX_PITCH);
                if(lastPitchCount < 0) { //evitar muitas repetições de pitch
                    lastPitch = pitch;
                    lastPitchCount++;
                } else {
                    if(pitch == lastPitch) {
                        lastPitchCount++;
                        if(lastPitchCount > MAX_PITCH_REPETITIONS) {
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
}
