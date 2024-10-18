package de.jave.jave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public abstract class AsciiGraphics extends Graphics {
   @Override
   public void dispose() {
   }

   @Override
   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
      return false;
   }

   @Override
   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
      return false;
   }

   @Override
   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
      return false;
   }

   @Override
   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
      return false;
   }

   @Override
   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
      return false;
   }

   @Override
   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      return false;
   }

   @Override
   public void drawString(String str, int x, int y) {
   }

   @Override
   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
   }

   @Override
   public void drawOval(int x, int y, int width, int height) {
   }

   @Override
   public void fillOval(int x, int y, int width, int height) {
   }

   @Override
   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
   }

   @Override
   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
   }

   @Override
   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
   }

   @Override
   public void clearRect(int x, int y, int width, int height) {
   }

   @Override
   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
   }

   @Override
   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
   }
}
