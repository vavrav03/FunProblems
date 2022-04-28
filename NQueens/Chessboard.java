/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Queens;

import java.util.Arrays;

/**
 *
 * @author vavra
 */
public class Chessboard {

   private int[] board;
   private int cols;
   int solutions = 0;
   int colsAtOnce;

   public Chessboard(int rows, int cols) {
      board = new int[rows];
      Arrays.fill(board, -1);
      this.cols = cols;
   }

   public int layout(int count) {
      if (this.board.length == cols && cols == count) {
         solve(count + 1, 0, -1);
      } else {
         this.colsAtOnce = cols - count;
         solve2(count + 1, 0, -1);
      }
      return solutions;
   }

   private void solve2(int count, int row, int col) {
      if (col >= cols) {
         return;
      }
      if (checkConsistency(row, col)) {
         board[row] = col;
         for (int i = 0; i < board.length; i++) {
            for (int j = 0; j <= colsAtOnce; j++) {
               solve2(count - 1, i, col + 1 + j);
            }
         }
         if (count == 1) {
            solutions++;
            System.out.println(this.toString());
         }
         board[row] = -1;
      }
   }

   private void solve(int count, int row, int col) {
      if (checkConsistency(row, col)) {
         if (col >= cols) {
            return;
         }
         board[row] = col;
         for (int i = 0; i < board.length; i++) {
            solve(count - 1, i, col + 1);
         }
         if (count == 1) {
            solutions++;
            System.out.println(this.toString());
         }
         board[row] = -1;
      }
   }

   private boolean checkConsistency(int row, int col) {
      if (board[row] != -1) {
         return false;
      }
      int i, j;
      for (i = row, j = col; i < board.length && j >= 0; i++, j--) {
         if (j == board[i]) {
            return false;
         }
      }
      for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
         if (j == board[i]) {
            return false;
         }
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int row = 0; row < board.length; row++) {
         for (int col = 0; col < board[row]; col++) {
            sb.append((row + col) % 2 == 1 ? '\u25A1' : '\u25A0');
            sb.append(' ');
         }
         sb.append("\u2655 ");
         for (int col = board[row] + 1; col < cols; col++) {
            sb.append((row + col) % 2 == 1 ? '\u25A1' : '\u25A0');
            sb.append(' ');
         }
         sb.append('\n');
      }
      return sb.toString();
   }

   public static void main(String[] args) {
      long start = System.currentTimeMillis();
      int rows = 8, cols = 8, queens = 8;
      System.out.println(
            "R = " + rows + ", C = " + cols + ", N = " + queens + ": " + new Chessboard(rows, cols).layout(queens));
      System.out.println(System.currentTimeMillis() - start + " ms");
   }
}
