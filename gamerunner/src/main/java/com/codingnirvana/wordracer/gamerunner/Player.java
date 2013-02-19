package com.codingnirvana.wordracer.gamerunner;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Player {

    private String name;
    private String language;
    private String command;
    private String directory;
    private boolean isFirstPlayer;
    private String runDirectory;

    private Process process;
    private PrintWriter writer;
    private BufferedReader reader;


    public Player(HashMap playerMap, String runDirectory) {
        this.name = (String) playerMap.get("name");
        this.language = (String) playerMap.get("language");
        this.command = (String) playerMap.get("command");
        this.runDirectory = runDirectory;
        this.directory = (String) playerMap.get("directory");
    }

    public String getName() {
        return this.name;
    }

    public String getPlayerDirectory() {
       return String.format("%s/%s/", runDirectory, this.directory);
    }

    public void initGameBoard(char letter, boolean isFirstPlayer) throws IOException {
        initializeProcess();

        writer.println(letter);
        writer.flush();
        writer.println(isFirstPlayer ? '1' : '0');
        writer.flush();
    }

    public Result pickLetter() throws IOException {
        String line = reader.readLine();
        String[] split = line.split(" ");

        char letter = split[0].charAt(0);
        int position = Integer.parseInt(split[1]);
        return new Result(position, letter);
    }

    public int pickPosition(char letter) throws IOException {
        writer.println(letter);
        writer.flush();

        String line = reader.readLine();
        return Integer.parseInt(line);
    }

    public void endGame() throws IOException, InterruptedException {
        writer.close();
        process.destroy();
    }

    private void initializeProcess() throws IOException {
        List<String> res = Collections.synchronizedList(new ArrayList<String>());
        ProcessBuilder pb = new ProcessBuilder(this.command.split(" "));
        pb.directory(new File(getPlayerDirectory()));
        IOUtils.copy(Player.class.getResourceAsStream("/words.dat"), new FileOutputStream(getPlayerDirectory() + "/words.dat"));
        process = pb.start();
        writer = new PrintWriter(process.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
}
