package com.codingnirvana.wordracer.visualizer;

import com.codingnirvana.wordracer.gamerunner.Game;
import com.codingnirvana.wordracer.gamerunner.Tournament;

import java.util.List;

public class ConsoleVisualizer {

    public static void printGameBoard(Game game) {
        System.out.println(String.format("Player 1 (%s)", game.getFirstPlayer().getName()));
        printBoardWithTotal(game, game.getFirstPlayerBoard());

        System.out.println(String.format("Player 2 (%s)", game.getSecondPlayer().getName()));
        printBoardWithTotal(game, game.getSecondPlayerBoard());
    }

    public static void printRankings(List<Tournament.Ranking> rankings) {
        int rank = 1;
        System.out.println("***********************");
        System.out.println("Final Rankings");
        System.out.println("***********************");
        for (Tournament.Ranking ranking : rankings) {
            System.out.println(String.format("%s %s %s %s", rank, ranking.getPlayer().getName(), ranking.getTotalScore(), ranking.getTotalPoints()));
            rank++;
        }
    }

    private static void printBoardWithTotal(Game game, char[][] board) {
        int[][] score = Game.calculateScore(board);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(board[i][j] + "   ");
            }
            System.out.print(score[0][i]);
            System.out.println();
        }

        for (int i = 0; i < 7; i++) {
            System.out.print(String.format("%1$-" + 4 + "s", score[1][i]));
        }
        System.out.println(game.totalScore(board));
    }
}
