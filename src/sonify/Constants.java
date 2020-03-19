/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonify;

import static jm.constants.Articulations.*;
import static jm.constants.Durations.*;
import static jm.constants.ProgramChanges.*;
import static jm.constants.Volumes.*;

/**
 *
 * @author Rui
 */
public class Constants {
    public static final int Loudness[] = {
        SILENT,
        PPP,
        PP,
        P,
        MP,
        MF,
        F,
        FF,
        FFF
    };
    public static final int LOUDNESS_SIZE = Loudness.length;
    
    public static final double Length[] = {
        EIGHTH_NOTE_TRIPLET,
        SIXTEENTH_NOTE,
        DOTTED_SIXTEENTH_NOTE,
        SIXTEENTH_NOTE_TRIPLET,
        THIRTYSECOND_NOTE,
        THIRTYSECOND_NOTE_TRIPLET
    };
    public static final int LENGTH_SIZE = Length.length;
    
    public static final double Duration[] = {
        STACCATISSIMO,
        STACCATO
    };
    public static final int DURATION_SIZE = Duration.length;
    
    public static final int Instruments[] = {
        PIANO,
        HARP,
        ACOUSTIC_GUITAR
    };
    public static final int INSTRUMENTS_SIZE = Instruments.length;
}
