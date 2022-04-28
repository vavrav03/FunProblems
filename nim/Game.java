
package nim;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vavra
 */
public class Game {

   //Pro více lidí se strategie změní tak, že žádná vítězná strategie není. Není totiž již 100% šance, že vyhrajete. Je ovšem šance, že neprohrajete (mód, kdy poslední, kdo vytáhne prohrál). Ovšem to už se mi nechtělo implementovat a navíc bych na to asi musel jít brute-force nebo minimaxem a to již postrádá smysl.

   private int[] piles;
   private int sumOfPiles;
   private Player[] players;
   private int currentPlayerIndex;
   private int maxRemovesPerTurn;
   private boolean lastWins;

   private Game(Player[] players, int[] piles, int maxRemovesPerTurn, boolean lastWins) {
      if (piles.length == 0 || players.length <= 1 || maxRemovesPerTurn <= 0) {
         throw new IllegalArgumentException();
      }
      this.piles = piles;
      for (int pile : piles) {
         this.sumOfPiles += pile;
      }
      this.players = players;
      this.maxRemovesPerTurn = maxRemovesPerTurn;
      this.lastWins = lastWins;
   }

   public static Game createGame(Player[] players, int[] piles, int maxRemovesPerTurn, boolean lastWins) {
      HashSet<String> set = new HashSet();
      for (Player p : players) {
         set.add(p.toString());
      }
      if (set.size() != players.length) {
         throw new IllegalArgumentException("Hráči musí mít odlišná jména");
      }
      return new Game(players, piles, maxRemovesPerTurn, lastWins);
   }

   public void startGame() {
      while (true) {
         System.out.println("Aktuální stav: " + printState());
         System.out.print(players[currentPlayerIndex] + ": ");
         int[] data = null;
         try {
            data = players[currentPlayerIndex].move();
            if (remove(data[0], data[1])) {
               if (isEnd()) {
                  break;
               }
               currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
            } else {
               System.err.println("Neplatný vstup. Můžete odebírat maximálně " + maxRemovesPerTurn + " jen z "
                     + piles.length + ((piles.length == 1 ? " hromady" : "hromádek")));
            }
         } catch (NumberFormatException e) {
            System.err.println(
                  "Neplatný vstup. Můžete odebírat maximálně. Vámi zadané údaje neodpovídají standardům zápisu tahů");
         }
      }
      if (lastWins) {
         System.out.println("WINNER:" + players[currentPlayerIndex]);
      } else {
         System.out.println(currentPlayerIndex);
         if (players.length == 2) {
            System.out.print("WINNER: ");
         } else {
            System.out.print("WINNERS: ");
         }
         for (int i = 0; i < currentPlayerIndex; i++) {
            System.out.print(players[i] + " ");
         }
         for (int i = currentPlayerIndex + 1; i < players.length; i++) {
            System.out.println(players[i] + " ");
         }
         System.out.println("");
      }
   }

   public int getPilesLength() {
      return piles.length;
   }

   public int getPile(int index) {
      return piles[index];
   }

   public boolean isEnd() {
      return sumOfPiles == 0;
   }

   public int getMaxRemovesPerTurn() {
      return maxRemovesPerTurn;
   }

   public boolean lastWins() {
      return lastWins;
   }

   public String printState() {
      StringBuilder s = new StringBuilder();
      for (int i = 0; i < piles.length; i++) {
         s.append(piles[i]).append(" ");
      }
      return s.toString();
   }

   public boolean remove(int pile, int ammount) {
      if (pile < 0 || pile >= piles.length || ammount <= 0 || piles[pile] < ammount || ammount > maxRemovesPerTurn) {
         return false;
      }
      piles[pile] -= ammount;
      sumOfPiles -= ammount;
      return true;
   }

   public static void main(String[] args) {
      Human vlada = new Human("Vlada");
      AI ava = new AI("Ava");
      AI marvin = new AI("Marvin");
      Game game = Game.createGame(new Player[] { vlada, ava, marvin }, new int[] { 12 }, 3, true);
      OnePile f = new OnePile(game);
      ava.setStrategy(f);
      marvin.setStrategy(f);
      game.startGame();
      Game game2 = Game.createGame(new Player[] { ava, marvin }, new int[] { 7, 5, 3 }, Integer.MAX_VALUE, false);
      MorePiles p = new MorePiles(game2);
      ava.setStrategy(p);
      marvin.setStrategy(p);
      game2.startGame();
   }
}

abstract class Player {

   private final String name;

   public Player(String name) {
      this.name = name;
   }

   public abstract int[] move();

   @Override
   public String toString() {
      return name;
   }

   @Override
   public int hashCode() {
      int hash = 3;
      hash = 19 * hash + Objects.hashCode(this.name);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Player other = (Player) obj;
      return this.name.equals(other.name);
   }
}

class Human extends Player {

   public Human(String name) {
      super(name);
   }

   @Override
   public int[] move() throws NumberFormatException {
      Scanner sc = new Scanner(System.in);
      String[] data = sc.nextLine().split(" ");
      if (data.length == 1) {
         return new int[] { 0, Integer.parseInt(data[0]) };
      }
      return new int[] { Integer.parseInt(data[0]), Integer.parseInt(data[1]) };
   }
}

class AI extends Player {

   private Strategy strategy;
   private Game game;

   public AI(String name) {
      super(name);
   }

   public void setStrategy(Strategy strategy) {
      this.strategy = strategy;
   }

   @Override
   public int[] move() {
      return strategy.move();
   }
}

abstract class Strategy {

   protected Game game;
   protected Random random;

   public Strategy(Game game) {
      this.game = game;
      this.random = new Random();
   }

   public abstract int[] move();
}

class OnePile extends Strategy {

   public OnePile(Game game) {
      super(game);
   }

   @Override
   public int[] move() {
      int a = super.game.lastWins() ? 2 : 1;
      if (game.getPile(0) == 1) {
         return new int[] { 0, 1 };
      }
      int modulo = (game.getPile(0) - a) % (1 + game.getMaxRemovesPerTurn());
      int[] r;
      if (modulo != 0) {
         r = new int[] { 0, modulo };
      } else {
         r = new int[] { 0, random.nextInt(game.getMaxRemovesPerTurn()) + 1 };
      }
      System.out.println(r[1]);
      return r;
   }
}

class MorePiles extends Strategy {

   public MorePiles(Game game) {
      super(game);
   }

   public int[] move() {
      int xor = 0;
      for (int i = 0; i < game.getPilesLength(); i++) {
         xor ^= game.getPile(i);
      }
      int[] r = null;
      if (!game.lastWins()) {
         int number2AndMore = 0;
         int number1 = 0;
         for (int i = 0; i < game.getPilesLength(); i++) {
            if (game.getPile(i) > 1) {
               number2AndMore++;
            } else if (game.getPile(i) == 1) {
               number1++;
            }
         }
         if (number2AndMore == 1) {
            for (int i = 0; i < game.getPilesLength(); i++) {
               if (game.getPile(i) > 1) {
                  r = new int[] { i, number1 % 2 == 0 ? game.getPile(i) - 1 : game.getPile(i) };
                  System.out.println(r[0] + " " + r[1]);
                  return r;
               }
            }
         }
      }
      if (xor == 0) {
         int index;
         do {
            index = random.nextInt(game.getPilesLength());
         } while (game.getPile(index) == 0);
         int x = random.nextInt(game.getPile(index));
         r = new int[] { index, x == 0 ? x + 1 : x };
      } else {
         for (int i = 0; i < game.getPilesLength(); i++) {
            if ((game.getPile(i) ^ xor) < game.getPile(i)) {
               r = new int[] { i, game.getPile(i) - (game.getPile(i) ^ xor) };
            }
         }
      }
      System.out.println(r[0] + " " + r[1]);
      return r;
   }
}