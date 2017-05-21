package Network;

import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

    static String urlToFileConfig;
    static String urlToHTMLFile;
    static String argIP;
    static String argPort;

    public static void main(String[] args) {
        if(args.length == 4){
            urlToFileConfig = args[0];
            urlToHTMLFile = args[1];
            argIP = args[2];
            argPort = args[3];
        }

        String configFile = readConfigFileForClient(urlToFileConfig);
        System.out.println(configFile);
        Client client = createClient(configFile);
        //Client client = new Client("172.0.0.1",1000);

        client.create(Integer.parseInt(argPort));//будет аля сервер
        client.start();

        if(client.getNameClient().equalsIgnoreCase("agent 1")){//консоль для первого главного клиента
        Console c = new Console(client.getIP(),client.getPort()); // работает как клиент, будет отправлять инфу client
        c.start();
        }
    }

    public static void openGrafOnBrowser(String urlToHTMLFile) {
        File fileHTML = new File(urlToHTMLFile);
        try {
            Desktop.getDesktop().browse(fileHTML.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Client createClient(String configFile) {
        int value = 0;
        Client client = null;
        Label lastLabelAdd = null;
        Label linkLabelLevel1 = null;
        Matcher m = Pattern.compile("((?!\")[\\s\\w\\d.]*?(?=[ ]*\"))|(\\()|([\\)])").matcher(configFile);
        while (m.find()) {
            switch (value) {
                case 0:
                    client = new Client(m.group(1));
                    System.out.println(client.getNameClient() + " - level 0");

                    break;
                case 1:
                    client.addLabel(m.group(1));
                    System.out.println(client.getLabels().get(client.getLabels().size() - 1).getValue() + " - level 1");
                    linkLabelLevel1 = client.getLabels().get(client.getLabels().size() - 1);

                    break;
                case 2:
                    linkLabelLevel1.setChild(new Label(m.group(1)));
                    System.out.println(linkLabelLevel1.getChild().getValue() + " - level 2");
                    lastLabelAdd = linkLabelLevel1.getChild();

                    break;
                case 3:
                    lastLabelAdd.setChild(new Label(m.group(1)));
                    System.out.println(lastLabelAdd.getChild().getValue() + " - level 3");

                    break;
                case 4:
                    lastLabelAdd.getChild().setChild(new Label(m.group(1)));
                    System.out.println(lastLabelAdd.getChild().getChild().getValue() + " - level 4");

                    break;
            }

            m.find();
            if (m.group().equals("(")) {
                value++;
            }
            if (m.group().equals(")")) {
                value--;
                int i = m.start();
                while (configFile.charAt(i) == configFile.charAt(i + 1)) {
                    value--;
                    m.find();
                    if (i == configFile.length() - 2) {
                        break;
                    }
                    i++;
                }
            }


        }
        if (client != null) {
            //client.addLinesOfGraf(linesOfGraf);
        }
        return client;
    }


    public static void drawGraf(String urlHTML, LinesOfGraf lines) {
        File fileHTML = new File(urlHTML);
        File fileToDotExe = new File("graphviz-2.38/release/bin/dot.exe");
        GraphViz gv = null;
        try {
            gv = new GraphViz(fileToDotExe.getCanonicalPath(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        gv.addln(gv.start_graph());
        for (String line : lines) {
            gv.addln(line);
        }
        gv.addln(gv.end_graph());
        //System.out.println(gv.getDotSource());

        gv.increaseDpi();   // 249 dpi

        String type = "png";
        String repesentationType = "dot";

        File out = new File("graf." + type);    // Windows
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out.getAbsolutePath());

        try {
            FileWriter fileWriter = new FileWriter(fileHTML.getAbsolutePath());
            fileWriter.write("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Graf globalnej wiedzy</title><style >body,html{margin: 0px;padding: 0px;height: 100%;width: 100%;}#main{height: 100%;background: url(" + out + ") top center; background-repeat: no-repeat;-webkit-background-size: 100%; -moz-background-size: 100%; -o-background-size: 100%; background-size: 100%; -webkit-background-size: contain; -moz-background-size: contain; -o-background-size: contain; background-size: contain;}</style></head><body><div id=\"main\"><div/></body></html>");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readConfigFileForClient(String urlFile) {
        String line;
        StringBuffer stringBuffer = null;
        try {
            File fileConfig = new File(urlFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileConfig.getAbsoluteFile()));
            if (bufferedReader.ready()) {
                stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
