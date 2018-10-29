import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private static final int PORT = 3333;

    void start() {
        System.out.println("Starting server");
        ServerSocket serverSocket;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("IO Server error " + e.getMessage());
            return;
        }
        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("Client connected");
            } catch (IOException e) {
                System.err.println("IO Server error " + e.getMessage());
            }
            new ClientConnection(socket).run();
        }
    }
}