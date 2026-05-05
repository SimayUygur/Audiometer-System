package serial;

import com.fazecast.jSerialComm.SerialPort;

public class SerialHandler {

    private SerialPort port;

    public void connect() {
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("No ports found");
            return;
        }

        port = ports[0];
        port.openPort();
    }

    public void send(String data) {
        if (port != null && port.isOpen()) {
            byte[] bytes = (data + "\n").getBytes();
            port.writeBytes(bytes, bytes.length);
        }
    }

    public String read() {
        if (port != null && port.bytesAvailable() > 0) {
            byte[] buffer = new byte[port.bytesAvailable()];
            port.readBytes(buffer, buffer.length);
            return new String(buffer);
        }
        return null;
    }
}