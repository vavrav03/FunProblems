/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HanoiTower;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Stack;
import javax.swing.*;

/**
 *
 * @author vavral
 */
public class GUI extends JPanel {

   private Pillar[] pillars;
   private Block[] blocks;

   public GUI(int width, int height) {
      this.pillars = new Pillar[3];
      this.setMinimumSize(new Dimension(width, height));
      for (int i = 0; i < pillars.length; i++) {
         this.pillars[i] = new Pillar(width / 6 + (i) * width / 3);
      }
      this.setBackground(Color.GRAY);
      blocks = new Block[5];
      Random random = new Random();
      for (int i = 0; i < blocks.length; i++) {
         blocks[i] = new Block(i + 1, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)),
               width / (3 * blocks.length) * (i + 1), this.getMinimumSize().height / 30);
         pillars[0].add(blocks[i]);
      }
      solve();
   }

   @Override
   protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, this.getWidth(), this.getHeight());
      //ground
      int groundHeight = this.getHeight() / 18;
      g.setColor(new Color(41, 40, 38));
      g.fillRect(0, this.getHeight() - groundHeight, this.getWidth(), groundHeight);
      double widthRatio = (float) this.getWidth() / this.getMinimumSize().width;
      double heightRatio = (float) this.getHeight() / this.getMinimumSize().height;
      paintPillars(g2, this.getHeight() - groundHeight, widthRatio, heightRatio);
      paintBlocks(g2, this.getHeight() - groundHeight, widthRatio, heightRatio);
   }

   private void paintPillars(Graphics2D g, int groundHeight, double widthRatio, double heightRatio) {
      g.setColor(new Color(249, 211, 66));
      for (Pillar p : pillars) {
         Rectangle r = new Rectangle(new Point((int) (p.getX() * widthRatio), (int) (Pillar.Y * heightRatio)));
         r.add((int) (p.getX() * widthRatio + Pillar.width * widthRatio), groundHeight);
         g.fill(r);
      }
   }

   private void paintBlocks(Graphics2D g, int groundHeight, double widthRatio, double heightRatio) {
      for (int i = 0; i < pillars.length; i++) {
         for (int x = 0; x < pillars[i].size(); x++) {
            g.setColor(pillars[i].get(x).getColor());
            Rectangle r = new Rectangle(
                  new Point(
                        (int) ((pillars[i].getX() - 0.5 * (pillars[i].get(Math.abs(pillars[i].size() - x - 1)).width))
                              * widthRatio),
                        (int) ((groundHeight - (x + 1) * pillars[i].get(x).height * heightRatio))));
            r.add((pillars[i].getX() + 0.5 * (pillars[i].get(Math.abs(pillars[i].size() - x - 1)).width) + Pillar.width)
                  * widthRatio, (int) ((groundHeight - x * pillars[i].get(x).height * heightRatio)));
            g.fill(r);
         }
      }
   }

   private void transport(int start, int end) {

   }

   private void solve() {
      repaint();
   }

   private class Pillar extends Stack<Block> {

      private int X;
      public static final int Y = 50;
      public static final int width = 20;

      public Pillar(int X) {
         this.X = X;
      }

      public int getX() {
         return X;
      }
   }

   private class Block {

      private int value;
      private Color color;
      private int width;
      public final int height;

      public Block(int value, Color color, int width, int height) {
         this.value = value;
         this.color = color;
         this.width = width;
         this.height = height;
      }

      public Color getColor() {
         return color;
      }

      public int getWidth() {
         return width;
      }

      public int getValue() {
         return value;
      }
   }

   public static void main(String[] args) {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setMinimumSize(new Dimension(600, 700));
      frame.setLayout(new GridLayout(1, 1, 0, 0));
      frame.getContentPane().add(new GUI(frame.getWidth() - 18, frame.getHeight() - 45));
      SwingUtilities.invokeLater(
            () -> frame.setVisible(true));
   }
}
