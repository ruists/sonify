/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonify;

import java.io.IOException;

/**
 *
 * @author Rui
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong syntax usage.");
        } else {
            try {
                Processor p = new Processor(args[0]);
                p.Run();
            } catch (IOException ex) {
                System.out.println("An error occurred when opening the image file.");
            }
        }
    }
}
