package net.disy.commons.swing.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.RGBImageFilter;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import net.disy.commons.swing.color.DisabledFilter;

public class ColorFilterGraphics extends Graphics2D {
   private final Graphics2D delegate;
   private final Component imageComponent;
   private final RGBImageFilter rgbFilter;

   public ColorFilterGraphics(Graphics2D delegate, Component imageComponent) {
      this(delegate, imageComponent, new DisabledFilter());
   }

   public ColorFilterGraphics(Graphics2D delegate, Component imageComponent, RGBImageFilter rgbFilter) {
      this.delegate = delegate;
      this.imageComponent = imageComponent;
      this.rgbFilter = rgbFilter;
   }

   private Image disabledImage(Image img) {
      FilteredImageSource filteredImageSource = new FilteredImageSource(img.getSource(), this.rgbFilter);
      return this.imageComponent.createImage(filteredImageSource);
   }

   private Color disabledColor(Color color) {
      return new Color(this.rgbFilter.filterRGB(0, 0, color.getRGB()));
   }

   @Override
   public void addRenderingHints(Map<?, ?> hints) {
      this.delegate.addRenderingHints(hints);
   }

   @Override
   public void clearRect(int x, int y, int width, int height) {
      this.delegate.clearRect(x, y, width, height);
   }

   @Override
   public void clip(Shape s) {
      this.delegate.clip(s);
   }

   @Override
   public void clipRect(int x, int y, int width, int height) {
      this.delegate.clipRect(x, y, width, height);
   }

   @Override
   public void copyArea(int x, int y, int width, int height, int dx, int dy) {
      this.delegate.copyArea(x, y, width, height, dx, dy);
   }

   @Override
   public Graphics create() {
      return this.delegate.create();
   }

   @Override
   public Graphics create(int x, int y, int width, int height) {
      return this.delegate.create(x, y, width, height);
   }

   @Override
   public void dispose() {
      this.delegate.dispose();
   }

   @Override
   public void draw(Shape s) {
      this.delegate.draw(s);
   }

   @Override
   public void draw3DRect(int x, int y, int width, int height, boolean raised) {
      this.delegate.draw3DRect(x, y, width, height, raised);
   }

   @Override
   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      this.delegate.drawArc(x, y, width, height, startAngle, arcAngle);
   }

   @Override
   public void drawBytes(byte[] data, int offset, int length, int x, int y) {
      this.delegate.drawBytes(data, offset, length, x, y);
   }

   @Override
   public void drawChars(char[] data, int offset, int length, int x, int y) {
      this.delegate.drawChars(data, offset, length, x, y);
   }

   @Override
   public void drawGlyphVector(GlyphVector g, float x, float y) {
      this.delegate.drawGlyphVector(g, x, y);
   }

   @Override
   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
      return this.delegate.drawImage(this.disabledImage(img), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this.disabledColor(bgcolor), observer);
   }

   @Override
   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
      return this.delegate.drawImage(this.disabledImage(img), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
   }

   @Override
   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
      return this.delegate.drawImage(this.disabledImage(img), x, y, width, height, this.disabledColor(bgcolor), observer);
   }

   @Override
   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
      return this.delegate.drawImage(this.disabledImage(img), x, y, width, height, observer);
   }

   @Override
   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
      return this.delegate.drawImage(this.disabledImage(img), x, y, this.disabledColor(bgcolor), observer);
   }

   @Override
   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      return this.delegate.drawImage(this.disabledImage(img), x, y, observer);
   }

   @Override
   public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
      return this.delegate.drawImage(this.disabledImage(img), xform, obs);
   }

   @Override
   public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
      throw new UnsupportedOperationException("not yet implemented");
   }

   @Override
   public void drawLine(int x1, int y1, int x2, int y2) {
      this.delegate.drawLine(x1, y1, x2, y2);
   }

   @Override
   public void drawOval(int x, int y, int width, int height) {
      this.delegate.drawOval(x, y, width, height);
   }

   @Override
   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
      this.delegate.drawPolygon(xPoints, yPoints, nPoints);
   }

   @Override
   public void drawPolygon(Polygon p) {
      this.delegate.drawPolygon(p);
   }

   @Override
   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
      this.delegate.drawPolyline(xPoints, yPoints, nPoints);
   }

   @Override
   public void drawRect(int x, int y, int width, int height) {
      this.delegate.drawRect(x, y, width, height);
   }

   @Override
   public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
      this.delegate.drawRenderableImage(img, xform);
   }

   @Override
   public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
      this.delegate.drawRenderedImage(img, xform);
   }

   @Override
   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
      this.delegate.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
   }

   @Override
   public void drawString(String s, float x, float y) {
      this.delegate.drawString(s, x, y);
   }

   @Override
   public void drawString(String str, int x, int y) {
      this.delegate.drawString(str, x, y);
   }

   @Override
   public void drawString(AttributedCharacterIterator iterator, float x, float y) {
      this.delegate.drawString(iterator, x, y);
   }

   @Override
   public void drawString(AttributedCharacterIterator iterator, int x, int y) {
      this.delegate.drawString(iterator, x, y);
   }


   @SuppressWarnings({"EqualsDoesntCheckParameterClass", "EqualsWhichDoesntCheckParameterClass"})
   @Override
   public boolean equals(Object obj) {
      return this.delegate.equals(obj);
   }

   @Override
   public void fill(Shape s) {
      this.delegate.fill(s);
   }

   @Override
   public void fill3DRect(int x, int y, int width, int height, boolean raised) {
      this.delegate.fill3DRect(x, y, width, height, raised);
   }

   @Override
   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      this.delegate.fillArc(x, y, width, height, startAngle, arcAngle);
   }

   @Override
   public void fillOval(int x, int y, int width, int height) {
      this.delegate.fillOval(x, y, width, height);
   }

   @Override
   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
      this.delegate.fillPolygon(xPoints, yPoints, nPoints);
   }

   @Override
   public void fillPolygon(Polygon p) {
      this.delegate.fillPolygon(p);
   }

   @Override
   public void fillRect(int x, int y, int width, int height) {
      this.delegate.fillRect(x, y, width, height);
   }

   @Override
   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
      this.delegate.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
   }

   @Override
   public Color getBackground() {
      return this.delegate.getBackground();
   }

   @Override
   public Shape getClip() {
      return this.delegate.getClip();
   }

   @Override
   public Rectangle getClipBounds() {
      return this.delegate.getClipBounds();
   }

   @Override
   public Rectangle getClipBounds(Rectangle r) {
      return this.delegate.getClipBounds(r);
   }

   @Deprecated
   @Override
   public Rectangle getClipRect() {
      return this.delegate.getClipRect();
   }

   @Override
   public Color getColor() {
      return this.delegate.getColor();
   }

   @Override
   public Composite getComposite() {
      return this.delegate.getComposite();
   }

   @Override
   public GraphicsConfiguration getDeviceConfiguration() {
      return this.delegate.getDeviceConfiguration();
   }

   @Override
   public Font getFont() {
      return this.delegate.getFont();
   }

   @Override
   public FontMetrics getFontMetrics() {
      return this.delegate.getFontMetrics();
   }

   @Override
   public FontMetrics getFontMetrics(Font f) {
      return this.delegate.getFontMetrics(f);
   }

   @Override
   public FontRenderContext getFontRenderContext() {
      return this.delegate.getFontRenderContext();
   }

   @Override
   public Paint getPaint() {
      return this.delegate.getPaint();
   }

   @Override
   public Object getRenderingHint(Key hintKey) {
      return this.delegate.getRenderingHint(hintKey);
   }

   @Override
   public RenderingHints getRenderingHints() {
      return this.delegate.getRenderingHints();
   }

   @Override
   public Stroke getStroke() {
      return this.delegate.getStroke();
   }

   @Override
   public AffineTransform getTransform() {
      return this.delegate.getTransform();
   }

   @Override
   public int hashCode() {
      return this.delegate.hashCode();
   }

   @Override
   public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
      return this.delegate.hit(rect, s, onStroke);
   }

   @Override
   public boolean hitClip(int x, int y, int width, int height) {
      return this.delegate.hitClip(x, y, width, height);
   }

   @Override
   public void rotate(double theta) {
      this.delegate.rotate(theta);
   }

   @Override
   public void rotate(double theta, double x, double y) {
      this.delegate.rotate(theta, x, y);
   }

   @Override
   public void scale(double sx, double sy) {
      this.delegate.scale(sx, sy);
   }

   @Override
   public void setBackground(Color color) {
      this.delegate.setBackground(this.disabledColor(color));
   }

   @Override
   public void setClip(int x, int y, int width, int height) {
      this.delegate.setClip(x, y, width, height);
   }

   @Override
   public void setClip(Shape clip) {
      this.delegate.setClip(clip);
   }

   @Override
   public void setColor(Color color) {
      this.delegate.setColor(this.disabledColor(color));
   }

   @Override
   public void setComposite(Composite comp) {
      this.delegate.setComposite(comp);
   }

   @Override
   public void setFont(Font font) {
      this.delegate.setFont(font);
   }

   @Override
   public void setPaint(Paint paint) {
      this.delegate.setPaint(paint);
   }

   @Override
   public void setPaintMode() {
      this.delegate.setPaintMode();
   }

   @Override
   public void setRenderingHint(Key hintKey, Object hintValue) {
      this.delegate.setRenderingHint(hintKey, hintValue);
   }

   @Override
   public void setRenderingHints(Map hints) {
      this.delegate.setRenderingHints(hints);
   }

   @Override
   public void setStroke(Stroke s) {
      this.delegate.setStroke(s);
   }

   @Override
   public void setTransform(AffineTransform Tx) {
      this.delegate.setTransform(Tx);
   }

   @Override
   public void setXORMode(Color c1) {
      this.delegate.setXORMode(c1);
   }

   @Override
   public void shear(double shx, double shy) {
      this.delegate.shear(shx, shy);
   }

   @Override
   public String toString() {
      return this.delegate.toString();
   }

   @Override
   public void transform(AffineTransform Tx) {
      this.delegate.transform(Tx);
   }

   @Override
   public void translate(double tx, double ty) {
      this.delegate.translate(tx, ty);
   }

   @Override
   public void translate(int x, int y) {
      this.delegate.translate(x, y);
   }
}
