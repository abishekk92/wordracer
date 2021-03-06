package com.codingnirvana.wordracer.gamerunner;

import com.codingnirvana.wordracer.Runner;

import java.io.IOException;
import java.util.*;

public class Game {

    private final Player firstPlayer;
    private final Player secondPlayer;
    private int gameNumber;

    private char[][] firstPlayerBoard;
    private char[][] secondPlayerBoard;
    private GameResult gameResult;

    public Game(Player firstPlayer, Player secondPlayer, int gameNumber) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gameNumber = gameNumber;

        this.firstPlayerBoard = new char[7][7];
        this.secondPlayerBoard = new char[7][7];
    }

    public void play() throws IOException, InterruptedException {
        char startingLetter = (char) ('A' + new Random().nextInt(25));

        firstPlayerBoard[3][3] = secondPlayerBoard[3][3] = startingLetter;
        firstPlayer.initGameBoard(startingLetter, true);
        secondPlayer.initGameBoard(startingLetter, false);

        for (int turn = 0; turn < 24; turn++) {
            Result result = firstPlayer.pickLetter();
            firstPlayerBoard[result.position / 7][result.position % 7] = result.letter;

            int position = secondPlayer.pickPosition(result.letter);
            secondPlayerBoard[position / 7][position % 7] = result.letter;

            result = secondPlayer.pickLetter();
            secondPlayerBoard[result.position / 7][result.position % 7] = result.letter;

            position = firstPlayer.pickPosition(result.letter);
            firstPlayerBoard[position / 7][position % 7] = result.letter;
        }

        firstPlayer.endGame();
        secondPlayer.endGame();
        calculateGameResult();
    }

    private void calculateGameResult() {
        int firstPlayerScore = totalScore(firstPlayerBoard);
        int secondPlayerScore = totalScore(secondPlayerBoard);

        if (firstPlayerScore == secondPlayerScore) {
            this.gameResult = GameResult.DRAW;
        } else if (firstPlayerScore > secondPlayerScore) {
            this.gameResult = GameResult.FIRST_PLAYER_WINNER;
        } else {
            this.gameResult = GameResult.SECOND_PLAYER_WINNER;
        }
    }


    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public String getGameResultAsString() {
        return String.format("%s-%s", totalScore(firstPlayerBoard), totalScore(secondPlayerBoard));
    }


    public int totalScore(char[][] firstPlayerBoard) {
        int[][] score = calculateScore(firstPlayerBoard);
        int total = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                total += score[i][j];
            }
        }
        return total;
    }

    public static int[][] calculateScore(char[][] board) {
        int[] lookup = new int[]{0, 0, 1, 2, 3, 5, 8, 13};

        List<String> words = new ArrayList<String>();
        Scanner scanner = new Scanner(Runner.class.getResourceAsStream("/words.dat"));
        while (scanner.hasNext()) {
            words.add(scanner.nextLine());
        }

        int[][] score = new int[2][7];
        boolean[][] isTakenH = new boolean[7][7];
        boolean[][] isTakenV = new boolean[7][7];

        for (int length = 7; length >= 2; length--) {
            for (int row = 0; row < 7; row++)
                for (int start = 0; start < 7; start++) {
                    int end = start + length;
                    if (end > 7) break;

                    boolean quit = false;
                    for (int i = start; i < end; i++) {
                        if (isTakenH[row][i]) {
                            quit = true;
                            break;
                        }
                    }
                    if (quit)
                        continue;

                    String word = "";
                    for (int i = start; i < end; i++)
                        word += board[row][i];

                    if (Collections.binarySearch(words, word) > 0) {
                        for (int i = start; i < end; i++)
                            isTakenH[row][i] = true;
                        score[0][row] += lookup[word.length()];
                    }
                }

            for (int col = 0; col < 7; col++)
                for (int start = 0; start < 7; start++) {
                    int end = start + length;
                    if (end > 7) break;

                    boolean quit = false;
                    for (int i = start; i < end; i++) {
                        if (isTakenV[i][col]) {
                            quit = true;
                            break;
                        }
                    }
                    if (quit)
                        continue;

                    String word = "";
                    for (int i = start; i < end; i++)
                        word += board[i][col];

                    if (Collections.binarySearch(words, word) > 0) {

                        for (int i = start; i < end; i++)
                            isTakenV[i][col] = true;
                        score[1][col] += lookup[word.length()];
                    }
                }
        }

        return score;
    }

    public char[][] getFirstPlayerBoard() {
        return firstPlayerBoard;
    }

    public char[][] getSecondPlayerBoard() {
        return secondPlayerBoard;
    }

    public static enum GameResult {
        DRAW,
        FIRST_PLAYER_WINNER,
        SECOND_PLAYER_WINNER
    }
}
