import java.io.*;
import java.net.Socket;
import java.time.format.DateTimeParseException;

class ClientConnection extends Thread {
    private Socket socket;
    private Service service = new Service();

    public ClientConnection(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        InputStream input;
        BufferedReader reader;
        DataOutputStream output;
        try {
            input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("IO Server error " + e.getMessage());
            return;
        }
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                    try {
                        service.scheduleTask(line);
                        output.writeBytes("Command '" + line + " was executed\n\r");
                    } catch (DateTimeParseException e) {
                        output.writeBytes("Invalid date\n\r");
                    } catch (IllegalArgumentException e) {
                        output.writeBytes("Invalid arguments: " + e.getMessage() + "\n\r");
                    } finally {
                        output.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("IO Server error " + e.getMessage());
            }
        }
    }
}
