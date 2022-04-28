package programy;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.sound.sampled.SourceDataLine;

public class ZlodejFinal {

   private Vysledek[] S;
   private int Hm;
   private Item[] items;

   static private class Item {
      public int h;
      public int c;

      public Item(int h, int c) {
         this.h = h;
         this.c = c;
      }

      @Override
      public String toString() {
         return "[h: " + h + ", c: " + c + "]";
      }
   }

   private class Vysledek {
      public int h;
      public int c;
      public long[] vybrane;

      public Vysledek(int h, int c) {
         this.h = h;
         this.c = c;
         this.vybrane = new long[items.length / 64 + 1];
      }

      public void zapisVybrane(int index) {
         vybrane[index / 64] |= (1 << index);
      }

      public String vybraneString() {
         StringBuilder s = new StringBuilder();
         for (int i = 0; i < items.length; i++) {
            if ((vybrane[i / 64] & (1 << (i % 64) )) != 0) {
               s.append((i + 1) + " ");
            }
         }
         return s.toString();
      }

      @Override
      public String toString() {
         return "[h: " + h + ", c: " + c + ", vybrane: " + vybraneString() + "]";
      }
   }

   public ZlodejFinal(int Hm, Item[] items) {
      this.Hm = Hm;
      this.items = items;
      this.S = new Vysledek[Hm + 1];
   }

   public Vysledek run() {
      for (int h = 0; h <= Hm; h++) {
         if (items[0].h > h) {
            S[h] = new Vysledek(0, 0);
         } else {
            S[h] = new Vysledek(items[0].h, items[0].c);
            S[h].zapisVybrane(0);
         }
      }
      for(int x = 1; x < S.length; x++){
         System.out.println(x+": " +S[x]);
      }
      for (int i = 1; i < items.length; i++) {
         System.out.println();
         for (int h = Hm; h >= 0; h--) {
            if (items[i].h > h) {

            } else {
               int zh = h - items[i].h; // zbylá h
               if (S[h].c > items[i].c + S[zh].c) { // nevybereme prvek
               } else if (S[h].c == items[i].c + S[zh].c) {
                  if (S[h].h > items[i].h + S[zh].h) {
                     S[h].c = items[i].c + S[zh].c;
                     S[h].h = items[i].h + S[zh].h;
                     S[h].vybrane = Arrays.copyOf(S[zh].vybrane, S[zh].vybrane.length);
                     S[h].zapisVybrane(i);
                  } else {

                  }
               } else {
                  S[h].c = items[i].c + S[zh].c;
                  S[h].h = items[i].h + S[zh].h;
                  S[h].vybrane = Arrays.copyOf(S[zh].vybrane, S[zh].vybrane.length);
                  S[h].zapisVybrane(i);
               }
            }
         }
         for(int x = 1; x < S.length; x++){
            System.out.println(x+": " +S[x]);
         }
      }
      return S[Hm];
   }

   public static void main(String[] args) {
      // Item[] items = { new Item(10, 200), new Item(8, 150), new Item(30, 300), new Item(15, 400), new Item(20, 150),
      //       new Item(80, 400), new Item(14, 300) };
      // int Hm = 35;
      Item[] items = { new Item(4, 13), new Item(3, 30), new Item(4, 42), new Item(2, 15), new Item(5, 50), new Item(9, 95) };
      int Hm = 10;
      ZlodejFinal zlodej = new ZlodejFinal(Hm, items);
      System.out.println(zlodej.run());
      System.out.println(1 << 1);
   }
}