import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    int portServer = 1000;
    ServerSocket serverSocket;
    InetAddress serverIP;

    public Server(int port){
        portServer = port;
    }

    public static void main(String[] args) {
       Server server = new Server(1000);
        try {
            server.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.actualisation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void create() throws IOException {
        serverSocket = new ServerSocket(portServer);
        serverIP = serverSocket.getInetAddress();
        System.out.println("Server was created!");
    }

    public void actualisation() throws IOException {

        Socket socket = serverSocket.accept(); //wait() klienta
        System.out.println("Got a client! Socket is enabled!");
        InputStream sin = socket.getInputStream(); // poluczajem czerez eto
        OutputStream sout = socket.getOutputStream(); // otprawlaem


        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);

        String line = null;
        while(true){
            line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
            System.out.println("Client recive:" + line); // пишем то что получили
            out.writeUTF("I'm got your message: " + line); // отправляем клиенту сообщение о доставке сообщения
            out.flush(); // заставляем поток закончить передачу данных.
            System.out.println();
        }
    }

    public InetAddress getIPServer() {
        return serverIP;
    }
}
