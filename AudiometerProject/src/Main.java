/** 
 * Projenin çalışması için kütüphaneleri zaten ekli bulunmakta ama olmamaları durumunda aşağıdaki kütüphaneler kurulmalıdır:
 * 1. jSerialComm (v2.11.0 veya üstü): Proteus/Arduino ile seri haberleşme için.
 * 2. JFreeChart (v1.5.4 veya üstü): Odyogram grafiğini çizdirmek için.
*/

import gui.AudiometerGUI;

public class Main {
    public static void main(String[] args) {
        new AudiometerGUI();
    }
}
