import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {

    private byte[] buffer = new byte[256];
    private int portNumber;
    private String IP ;
    private String sendString = "";
    private InetAddress inetAdress;
    private DatagramPacket inPacket;
    private DatagramSocket inSocket;

    public Client(String IP,int port) throws UnknownHostException {
        this.IP = IP;
        portNumber = port;
        inetAdress = InetAddress.getByName(IP);
    }

    public void sendMessage(String str) throws IOException  {
        buffer = str.getBytes();
        inPacket = new DatagramPacket(buffer,buffer.length,inetAdress,portNumber); // poluczenie i otpravlenije
        inSocket = new DatagramSocket(); // sluszet
        inSocket.send(inPacket);
    }

    public void recieveMessage() throws IOException {
        byte[] buff = new byte[256];
        DatagramSocket outSocket = new DatagramSocket(portNumber);
        DatagramPacket outPacket = new DatagramPacket(buff,buff.length);
        outSocket.receive(outPacket);
        String reciveData = new String(outPacket.getData(),0,outPacket.getLength());
        System.out.println("Recieve message:" + reciveData);
    }



}
