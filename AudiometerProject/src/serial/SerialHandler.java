package serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class SerialHandler {

    private SerialPort port;
    private StringBuilder messageBuffer = new StringBuilder();
    private Consumer<String> onDataReceived; 

    public void setOnDataReceived(Consumer<String> callback) {
        this.onDataReceived = callback;
    }

    // BAĞLANTI DURUMU DÜZELTMESİ: GUI'ye bağlandığını veya hata sebebini bildiren metot
    public boolean connect(Consumer<String> onError) {
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            onError.accept("Sistemde COM portu bulunamadı!");
            return false;
        }

        for (SerialPort p : ports) {
            if (p.getSystemPortName().equalsIgnoreCase("COM2")) {
                port = p;
                break;
            }
        }
        
        if (port == null) {
            onError.accept("COM2 portu bulunamadı. Donanım bağlı değil!");
            return false; 
        }

        port.setBaudRate(9600);
        if (port.openPort()) {
            port.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    byte[] newData = new byte[port.bytesAvailable()];
                    port.readBytes(newData, newData.length);
                    messageBuffer.append(new String(newData));

                    int index;
                    String target = "RESPONSE";
                    while ((index = messageBuffer.indexOf(target)) != -1) {
                        if (onDataReceived != null) {
                            onDataReceived.accept(target);
                        }
                        messageBuffer.delete(0, index + target.length());
                    }
                }
            });
            return true;
        } else {
            onError.accept("COM2 portu var ama AÇILAMADI (Başka bir uygulama kullanıyor olabilir).");
            return false;
        }
    }

    public boolean isOpen() {
        return port != null && port.isOpen();
    }

    public void send(String data) {
        if (isOpen()) {
            port.writeBytes((data + "\n").getBytes(), (data + "\n").length());
        }
    }
}