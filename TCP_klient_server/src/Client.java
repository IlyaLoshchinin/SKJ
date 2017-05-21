import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
       private int serverPort = 1500;
        private String serverIP;// это IP-адрес компьютера, где исполняется наша серверная программа.
        // Здесь указан адрес того самого компьютера где будет исполняться и клиент.
        Socket socket;

    public static void main(String[] args) {
        Client client = new Client("172.0.0.1",1000);
        client.createClient();

        try {
            client.actualisation();
        } catch (IOException e) {
            System.out.println("Actualisation failed");
        }

    }

    public Client(String serverIP,int serverPort){
            this.serverPort = serverPort;
            this.serverIP = serverIP;
            createClient();
        }

        private void createClient() {
            try {
                InetAddress ipAddress = InetAddress.getByName(serverIP);
                socket = new Socket(ipAddress,serverPort);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client connected to server!");


        }

        private void actualisation() throws IOException {
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            System.out.println("Write message:\n");
            while(true){
                line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                out.writeUTF(line);
                System.out.println("Sanded to server!");
                out.flush();

                line = in.readUTF(); // wait() ждем текста от сервера
                System.out.println("Server recive" + line);

            }
        }

}
