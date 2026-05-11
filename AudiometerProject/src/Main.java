/** 
 * * Bağımlılıklar:
 * 1. jSerialComm: Seri haberleşme için.
 * 2. JFreeChart: Odyogram çizimi için.
 */

import gui.AudiometerGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AudiometerGUI();
        });
    }
}