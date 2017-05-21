import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Client client = new Client("172.23.66.92",2000);
        client.sendMessage("Привет Санек!");
        //client.recieveMessage();
    }
}
