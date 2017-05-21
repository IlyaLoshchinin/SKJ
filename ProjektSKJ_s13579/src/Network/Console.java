package Network;


import java.io.*;
import java.net.Socket;
//-------------klient-----------
public class Console extends Thread {

    String ipConsole = null;
    int portConsole;

    public Console(String ip, int port) {
        ipConsole = ip;
        portConsole = port;
    }

    @Override
    public void run() {

        Socket socket = null;

       if (ipConsole.length() == 0) {
            System.out.println("IP Console == null");
            System.exit(-1);
        }

        System.out.println("C::Connecting to... " + ipConsole);
        try {
            socket = new Socket(ipConsole, portConsole);//то что передал в файле создаем подключение
            System.out.println("C::Connected to Client1!");
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;

            while(true){
                System.out.println("C::Write message:");
                line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                if(line.equalsIgnoreCase("closeConsole")){
                    break;

                }
                out.writeUTF(line);
                //System.out.println("C::Sanded to client1: " + line);
                out.flush();

                line = in.readUTF(); // wait() ждем текста от сервера
                System.out.println("C::Client recive: " + line);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
