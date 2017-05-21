package Network;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Client extends Thread {


    private String name;
    private ArrayList<Label> labels = new ArrayList<>();//pierwogo urownia
    private LinesOfGraf linesOfGraf ;


    public Client(String name) {
        this.name = name;
    }

    public void addLabel(String label) {
        labels.add(new Label(label));
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public String getNameClient() {
        return name;
    }


    public LinesOfGraf getLinesOfGraf() {
        return linesOfGraf;
    }

    public String getIP() {
        return labels.get(0).getChild().getValue();
    }

    public int getPort() {
        return Integer.parseInt(labels.get(1).getChild().getValue());
    }

    //----------------------TCP---------server---------------------
    int portServer;
    ServerSocket serverSocket;
    String serverIP;
    Socket socket;


    public void create(int portServer) {
        try {
            serverSocket = new ServerSocket(portServer);
        } catch (IOException e) {
            System.out.println("The port " + portServer + " is closed!");
            System.exit(-1);
        }

        System.out.println("Cl::Server was created!");
    }

    @Override
    public void run() {

        System.out.println("Cl::Wait console!");
        InputStream sin = null;
        OutputStream sout = null;
        try {
            socket = serverSocket.accept(); //wait() console

            System.out.println("Cl::Got a console! Socket is enabled!");
            sin = socket.getInputStream(); // poluczajem czerez eto
            sout = socket.getOutputStream(); // otprawlaem
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);
        String[] str = null;
        String line = null;
        while (true) {
            StringBuffer outLine = new StringBuffer("OK:200 | ");
            try {
                line = in.readUTF(); // ожидаем пока Console пришлет строку текста.
                if (line.equalsIgnoreCase("closeClient")) {
                    out.writeUTF("Closed client!");
                    break;
                }
                if (line.equalsIgnoreCase("openGraf")) {
                    createGraf();
                    main.drawGraf(main.urlToHTMLFile, linesOfGraf);
                    main.openGrafOnBrowser(main.urlToHTMLFile);
                    outLine.append("Opening Local Graf");
                }
                if (line.startsWith("ADD")) {
                    str = splitWith_(line);
                    if (str[1].equalsIgnoreCase("agent 1")) {
                       outLine.append(addNewBranch(str));
                    } else {
                        //otsylayem infu dalshe po sosediam
                    }

                } else if (line.startsWith("MODIFY")) {
                    str = splitWith_(line);
                    if (str[1].equalsIgnoreCase("agent 1")) {// .isAgent1 ?
                        outLine.append(modifyBranch(str));
                    } else {
                        //otsylayem infu dalshe po sosediam
                    }
                } else if (line.startsWith("REMOVE")) {
                    str = splitWith_(line);
                    if (str[1].equalsIgnoreCase("agent 1")) {// .isAgent1 ?
                        outLine.append(removeLabelFromBranch(str));
                    } else {
                        //otsylayem infu dalshe po sosediam
                    }
                }else{

                }

                if(outLine.length() < 10 )
                    outLine.append("Task not correct! Example: (ADD | REMOVE | MODIFY) / openGraf / closeClient ");

                System.out.println("Cl::Console send:  " + line); // пишем то что получили
                out.writeUTF(outLine.toString()); // отправляем клиенту сообщение о доставке сообщения

                out.flush(); // заставляем поток закончить передачу данных.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //ADD agent 1 nowa linia dane taki
    private String removeLabelFromBranch(String[] str) {//REMOVE agent 1 neigb agent port
        Label tmp = null;
        Label parent = null;
        String deleteLabel = str[str.length-1];
        for (Label l : labels) {
            if (l.getValue().equals(str[2])){
                tmp = l;
                if(tmp.getValue().equals(deleteLabel)){
                    labels.remove(tmp);
                    return "Remove done!";
                }
                break;

            }
        } //REMOVE agent 1 neighbours agent 2 port 12345
        //parent = tmp;

        for (int i = 2; i < str.length-1; i++) {
            if (tmp.hasNext()) {
                if (tmp.getCountChild() == 2) {
                    if (tmp.getChilds()[0].getValue().equals(str[i + 1])) {
                        parent = tmp;
                        tmp = tmp.getChilds()[0];
                        if (tmp.getValue().equals(deleteLabel)) {
                            parent.deleteChild(0);
                            return "Remove Done!";
                        }
                    } else if (tmp.getChilds()[1].getValue().equals(str[i + 1])) {
                        parent = tmp;
                        tmp = tmp.getChilds()[1];
                        if (tmp.getValue().equals(deleteLabel)) {
                            parent.deleteChild(1);
                            return "Remove Done!";
                        }
                    }
                } else {
                    parent = tmp;
                    tmp = tmp.next();
                    if (tmp.getValue().equals(deleteLabel)) {
                        parent.deleteChild(0);
                        return "Remove Done!";
                    }
                }
            }
        }
        return "Nie ma takiego zapisu!";
    }

    private LinesOfGraf createGraf(){

        linesOfGraf = new LinesOfGraf();
        int recurrence = 0;

        linesOfGraf.add(0,getNameClient()); //lavel 1
        ArrayDeque<Label> parentLabel = new ArrayDeque<>();
        for (Label label: labels) { // ходим по первому уровню
            //System.out.println(label.getValue());
            linesOfGraf.add(label.getIdOfGraf(),label.getValue(),0,label.getIdOfGraf());//label 2
            while (label.hasNext()){
                    if(label.valueNext == 2 && labels.contains(label)){
                        label.valueNext = 0;
                        break;
                    }
                    if(label.valueNext == 2){
                        label.valueNext = 0;
                    }
                    if(recurrence == 2){
                        recurrence = 0;
                        //parentLabel.pop();
                        label = parentLabel.pop();
                        continue;
                    }
                if(label.getCountChild() == 2){
                    parentLabel.push(label);//LIFO
                    label = label.next();
                    linesOfGraf.add(label.getIdOfGraf(),label.getValue(),parentLabel.peek().getIdOfGraf(),label.getIdOfGraf());

                }else{
                    label = label.next();
                    linesOfGraf.add(label.getIdOfGraf(),label.getValue(),label.getIdOfGraf()-1,label.getIdOfGraf());
                     // если конец ветки возми ссылку родителя
                        if(!parentLabel.isEmpty()){ // если не пустой
                            recurrence++;
                            label = parentLabel.pop(); //переход к родиетлю -1 в стеке



                        }

                }// end IF
            } // end while
            recurrence = 0;
        }

        return linesOfGraf;
    }

    private String modifyBranch(String[] str) { //MODIFY agent 1 measurements voltage 4.2V 10V
        Label tmp = null; //link to label Level1(ip,port,measurements...)
        String modifyLabel = str[str.length-1];


        for (Label l : labels) {
            if (l.getValue().equals(str[2])){ tmp = l; break; }
        }
        String parent = null;
        for (int i = 3; i < str.length-1; i++) {
            if(tmp.hasNext()){
                if(tmp.getCountChild() != 2){ // posiada 1 child'a to...
                   if(tmp.getChild().getValue().equals(str[i])){
                       parent = tmp.getValue();
                       tmp = tmp.next();
                   }
                }else{
                    for (int j = 0; j < 2; j++) {
                        System.out.println(tmp.getChilds()[j].getValue());
                        if(tmp.getChilds()[j].getValue().equals(str[i])){ //porownojemy z kazdym
                            parent = tmp.getValue();
                            tmp = tmp.getChilds()[j];

                            break;
                        }
                    }
                }
            }else{break;}
        }
        if(tmp.getValue().equals(str[str.length-2])){
            String tmpL = tmp.getValue();
            tmp.setValue(str[str.length-1]);
            //int value = linesOfGraf.size();
            /*for (int i = 0;i < value;i++) {
                *//*if(linesOfGraf.get(i).contains(parent)){
                    //int index = linesOfGraf.indexOf(linesOfGraf.get(i));
                    String st = linesOfGraf.get(i+1).replace(tmpL,tmp.getValue());
                    linesOfGraf.set(i+1,st);
                    break;
                }*//*
            }*/

        return "Zmienono " + tmpL + " na " + tmp.getValue();
        }
        return "Not found the branch!";
    }

    private String addNewBranch(String[] str) { //ADD agent 1 nowa linia dane taki
        String returnLine = "";
        //int value = linesOfGraf.size();
        Label tmp = new Label(str[2]);
        labels.add(tmp);//nowa
        returnLine += str[2]+" -> ";
        //linesOfGraf.add(value, str[2], 0, value++);
        for (int i = 3; i < str.length; i++) {

            returnLine += str[i]+" -> ";
           // linesOfGraf.add(value, str[i], value - 1, value++);

                tmp.setChild(new Label(str[i]));
                tmp = tmp.getChild();

        }
        return " Create new branch: " + returnLine.substring(0,returnLine.length()-4);
    }

    public String[] splitWith_(String line) {
        line = line.replaceAll("agent (?=\\d+)", "agent_");
        String[] str = line.split(" ");
        for (int i = 0; i < str.length; i++) {
            str[i] = str[i].replace("agent_","agent ");
        }
        return str;
    }

}
