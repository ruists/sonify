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
import jm.music.tools.Mod;

/**
 *
 * @author RuiSa
 */
public class Polyphonic extends Processor{
    private static final int PIANO_INDEX = 0;
    private static final int HARP_INDEX = 1;
    private int initialLengthIndex;
    private static final int PIANO_TRANSPOSE = 12;
    private static final int HARP_TRANSPOSE = -12;
    
    public Polyphonic(String imgPath) throws IOException {
        super(imgPath);
    }
    
    @Override
    protected void prepareData() {
        Double dInitialLengthIndex = new Double(image.getHeight() * image.getHeight() + rnd.nextInt(128));
        initialLengthIndex = dInitialLengthIndex.intValue() % Constants.LENGTH_SIZE;
    }

    @Override
    protected void translateData() {
        int color, pitch;
        int phrasePCount = 0, phraseHCount = 0;
        double time1 = 0.0, time2 = 4.0;
        int lastPitch, lastPitchCount;
        boolean isPiano;
        
        Part p1 = new Part("Piano", Constants.Instruments[PIANO_INDEX], 0);
        Part p2 = new Part("Harp", Constants.Instruments[HARP_INDEX], 1);
        Note n;
        Phrase phr;
        for(int x = 0; x < image.getWidth(); x++) {
            if(x % 2 == 0) {
                phr = new Phrase("Piano Phrase " + phrasePCount++, time1);
                isPiano = true;
            } else {
                phr = new Phrase("Harp Phrase " + phraseHCount++, time2);
                isPiano = false;
            }
            lastPitch = -1;
            lastPitchCount = 0;
            
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
                
                n = new Note(pitch, 
                        Constants.Length[initialLengthIndex], 
                        Constants.Loudness[6]);
                phr.addNote(n);
                if(isPiano) {
                    time1 += n.getRhythmValue();
                } else {
                    time2 += n.getRhythmValue();
                }
            }
            
            if(isPiano) {
                p1.addPhrase(phr);
            } else {
                p2.addPhrase(phr);
            }
        }
        System.out.println("Finalizing score...");
        Mod.transpose(p1, PIANO_TRANSPOSE);
        score.addPart(p1);
        Mod.transpose(p2, HARP_TRANSPOSE);
        score.addPart(p2);
    }
}
