/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mastermind;

import cz.gyarab.util.game.mastermind.Board;
import cz.gyarab.util.game.mastermind.Board.Strategy;
import cz.gyarab.util.game.mastermind.Tuples;
import cz.gyarab.util.game.mastermind.Verdict;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author vavra
 */
public class KnuthAlgorithm implements Strategy {

    private Board board;

    private final List<Tuples> impossible = new LinkedList<>();
    private final Set<Tuples> possible;

    public KnuthAlgorithm(Board board) {
        this.board = board;
        this.possible = new HashSet(Arrays.asList(board.guessing()));
    }

    public void adjustSets(Verdict verdict, Tuples response) {
        Iterator<Tuples> iterator = possible.iterator();
        while (iterator.hasNext()) {
            Tuples i = iterator.next();
            if (!board.getVerdict(i, response).equals(verdict)) {
                impossible.add(i);
                iterator.remove();
            }
        }
    }
    
    @Override
    public void start(){
        board.addGuess(board.guessing()[7]);
    }

    @Override
    public Tuples guess() {
        this.adjustSets(board.getVerdict(), board.getGuess());
        int minimumEliminated = -1;
        Tuples bestGuess = null;
        List<Tuples> unused = new LinkedList<>(possible);
        unused.addAll(impossible);
        for (Tuples a : unused) {
            int[][] minMaxTable = new int[board.getColumns() + 1][board.getColumns() + 1];
            for (Tuples b : possible) {
                Verdict abKey = board.getVerdict(a, b);
                minMaxTable[abKey.getCorrect()][abKey.getWrong()]++;
            }
            int mostHits = -1;
            for (int[] row : minMaxTable) {
                for (int i : row) {
                    mostHits = Integer.max(i, mostHits);
                }
            }
            int score = possible.size() - mostHits;
            if (score > minimumEliminated) {
                minimumEliminated = score;
                bestGuess = a;
            }
        }
        return bestGuess;
    }

    public static void main(String[] args) {
        Board board = Board.builderSuper().create();
        Strategy knuth = new KnuthAlgorithm(board);
        board.showWindow();
        board.newGame();
        board.showWindow();
        board.uncover();
        knuth.start();
//        board.testStrategy(knuth);
        for(int i = 0; i < board.getRows(); i++){
            if(board.addGuess(knuth.guess()).isDone()){
                break;
            }
        }
//        for (int i = 0; i < board.getRows(); i++) {
//            board.addGuess(knuth.guess());
//        }
//        HashSet<Tuples> t = new HashSet(Arrays.asList(board.guessing()));
//        System.out.println(t);
//        for(int i = 0; i < t.length; i+=30){
//            for(int j = 0; j < 30; j++){
//                System.out.print(t[i+ j]+" ");
//            }
//            System.out.println("");
//        }
    }
}
