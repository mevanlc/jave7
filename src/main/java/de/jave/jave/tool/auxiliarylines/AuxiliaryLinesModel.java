package de.jave.jave.tool.auxiliarylines;

import de.jave.jave.Point2d;
import java.util.LinkedList;
import java.util.List;
import net.dizzy.commons.core.model.AbstractChangeableModel;

public class AuxiliaryLinesModel extends AbstractChangeableModel {
   private final List<Line2d> lines = new LinkedList<>();

   public void remove(Line2d line) {
      this.lines.remove(line);
      this.fireChangeEvent();
   }

   public void removeAll() {
      if (this.lines.size() != 0) {
         this.lines.clear();
         this.fireChangeEvent();
      }
   }

   public Line2d[] getLines() {
      return this.lines.toArray(new Line2d[0]);
   }

   public Point2d getPointNear(Point2d p) {
      for (int i = this.lines.size() - 1; i >= 0; i--) {
         Line2d line = this.lines.get(i);
         if (line.getStartPoint().getDistance(p) <= 0.5) {
            return line.getStartPoint();
         }

         if (line.getEndPoint().getDistance(p) <= 0.5) {
            return line.getEndPoint();
         }
      }

      return null;
   }

   public Line2d getLineNear(Point2d p) {
      for (int i = this.lines.size() - 1; i >= 0; i--) {
         Line2d line = this.lines.get(i);
         if (p.getDistanceTo(line.getStartPoint(), line.getEndPoint()) <= 0.5
            && p.getDistance(line.getStartPoint()) <= line.length() + 0.5
            && p.getDistance(line.getEndPoint()) <= line.length() + 0.5) {
            return line;
         }
      }

      return null;
   }

   public void add(Line2d line) {
      this.lines.add(line);
      this.fireChangeEvent();
   }

   public boolean isEmpty() {
      return this.lines.isEmpty();
   }
}
