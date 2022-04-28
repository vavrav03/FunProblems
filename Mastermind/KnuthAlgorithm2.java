/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mastermind;

import cz.gyarab.util.Utils;
import cz.gyarab.util.game.mastermind.Board;
import cz.gyarab.util.game.mastermind.Board.Strategy;
import cz.gyarab.util.game.mastermind.Tuples;

/**
 *
 * @author vavra
 */
public class KnuthAlgorithm2 implements Board.Strategy {

    public KnuthAlgorithm2(Board board) {
    }

    @Override
    public void start() {
        Strategy.super.start();

    }

    @Override
    public Tuples guess() {
        throw new UnsupportedOperationException();
    }

//     
//
//    this.removeInconsistentCodes(this.consistentCodes,  
//     
//    this.lastGuess,
//                answer);
//        List<Code> bestGuesses = new ArrayList<Code>();
//
//    bestGuesses.add (
//    
//    this.consistentCodes.get(0));
//        int maxMinimum = 0;
//    for (Code code : this.allCodes
//
//    
//        ) {
//            int minimum = Integer.MAX_VALUE;
//        for (Answer a : this.allAnswers) {
//            int removedCodesSize = getConsistentCodes(
//                    this.consistentCodes, code, a).size();
//            minimum = Math.min(removedCodesSize, minimum);
//        }
//        if (minimum == maxMinimum && minimum > 0) {
//            bestGuesses.add(code);
//        }
//        if (minimum > maxMinimum) {
//            maxMinimum = minimum;
//            bestGuesses.clear();
//            bestGuesses.add(code);
//        }
//    }
//    // Use, if possible, consistent codes
//    List<Code> consistentBestGuesses = getConsistentCodes(
//            bestGuesses, this.lastGuess, answer);
//
//    if (!consistentBestGuesses.isEmpty () 
//        ) {
//            bestGuesses = consistentBestGuesses;
//    }
//    // Use a code with the shortest travel distance
//
//     
//    this.lastGuess  = this.getShortestCode(bestGuesses);
//
//     
//    this.lastButton  = this.lastGuess.get(this.CODE_LENGTH - 1);
//    return this.lastGuess ;
    public static void main(String[] args) {
        Board board = Board.builderOriginal().create();
        board.showWindow();
        board.newGame();
        board.uncover();
        for (int i = 0; i < board.getRows(); i++) {
            if (board.addGuess(board.random()).isDone()) {
                System.out.println("Won: " + board.getCurrent());
                break;
            }
            Utils.sleep(50);
        }
    }
}
