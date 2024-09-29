package main;

import java.io.*;

public class Data {
    int coolDown = 180;
    GamePanel gp;
    String dataFolder = System.getProperty("user.home") + "\\AppData\\Roaming\\.tank";
    String dataFolderBin = System.getProperty("user.home") + "\\AppData\\Roaming\\.tank\\bin";
    String dataFolderSave = System.getProperty("user.home") + "\\AppData\\Roaming\\.tank\\bin\\saves";
    String dataFolderContent = System.getProperty("user.home") + "\\AppData\\Roaming\\.tank\\content";
    File dir = new File(dataFolder);
    File dirBin = new File(dataFolderBin);
    File dirSave = new File(dataFolderSave);
    File dirContent = new File(dataFolderContent);
    File data = new File(dataFolderSave + "\\save01.txt");
    File optionsConfig = new File(dataFolderBin + "\\config.txt");

    public Data(GamePanel gp) {
        this.gp = gp;
    }

    public void loadData() throws IOException {
        checkFiles();

        BufferedReader br = new BufferedReader(new FileReader(data));

        String st;
        while ((st = br.readLine()) != null)

            if (st != null) {
                if (st.startsWith("p:")) {
                    gp.points = Integer.parseInt(st.substring(2));
                }
            }
        br.close();
    }

    public void saveData() throws IOException {
        checkFiles();

        PrintWriter pw = new PrintWriter(data);
        pw.print("");
        pw.print("p:" + gp.points);
        pw.println();

        pw.close();
    }

    public void loadOptionConfig() throws IOException {
        checkFiles();
        BufferedReader br = new BufferedReader(new FileReader(optionsConfig));

        String st;
        while ((st = br.readLine()) != null)

            if (st != null) {
                if (st.startsWith("fs:")) {
                    gp.isFullScreenOn = Boolean.parseBoolean(st.substring(3));
                }
                if (st.startsWith("vol:")) {
                    gp.volumeValue = Float.parseFloat(st.substring(4));
                }
                if (st.startsWith("buffs:")) {
                    gp.spawnBuffs = Boolean.parseBoolean(st.substring(6));
                }
            }
        br.close();
    }

    public void saveOptionConfig() throws IOException {
        checkFiles();

        PrintWriter pw = new PrintWriter(optionsConfig);
        pw.print("");
        pw.print("fs:" + gp.isFullScreenOn + "\nvol:" + gp.volumeValue + "\nbuffs:" + gp.spawnBuffs);
        pw.println();

        pw.close();
    }


    void checkFiles() throws IOException {
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!dirBin.exists()) {
            dirBin.mkdir();
        }
        if (!dirContent.exists()) {
            dirContent.mkdir();
        }
        if (!dirSave.exists()) {
            dirSave.mkdir();
        }
        if (!data.exists()) {
            data.createNewFile();
        }
        if (!optionsConfig.exists()) {
            optionsConfig.createNewFile();
        }
    }
}
