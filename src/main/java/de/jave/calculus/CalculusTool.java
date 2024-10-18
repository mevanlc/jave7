package de.jave.calculus;

import de.jave.calculus.parser.ASTAddNode;
import de.jave.calculus.parser.ASTConstNode;
import de.jave.calculus.parser.ASTDivNode;
import de.jave.calculus.parser.ASTExpNode;
import de.jave.calculus.parser.ASTFloat;
import de.jave.calculus.parser.ASTFunctionNode;
import de.jave.calculus.parser.ASTInteger;
import de.jave.calculus.parser.ASTModNode;
import de.jave.calculus.parser.ASTMultNode;
import de.jave.calculus.parser.ASTNegativeNode;
import de.jave.calculus.parser.ASTStart;
import de.jave.calculus.parser.ASTSubtractNode;
import de.jave.calculus.parser.ASTXNode;
import de.jave.calculus.parser.Calculus;
import de.jave.calculus.parser.CalculusVisitor;
import de.jave.calculus.parser.Node;
import de.jave.calculus.parser.ParseException;
import de.jave.calculus.parser.SimpleNode;
import de.jave.calculus.parser.TokenMgrError;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.lib.CharacterPlate;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Point;
import java.awt.Rectangle;

public class CalculusTool {
   public static void plotFunction(
      String formula,
      PixelPlateMode plotMode,
      Filter filter,
      int plateWidth,
      int plateHeight,
      double minX,
      double maxX,
      double minY,
      double maxY,
      CharacterPlate cp
   ) throws ParseException {
      PixelPlate plate = new PixelPlate(new Rectangle(plateWidth, plateHeight), filter);
      plate.setMode(plotMode);
      int virtualWidth = plate.getVirtualWidth();
      int virtualHeight = plate.getVirtualHeight();
      double xStep = (maxX - minX) / (double)virtualWidth;
      double littleDX = xStep / 256.0;
      boolean lastValueOk = false;
      Point last = new Point();
      double lastY = 0.0;
      Node tree = null;

      try {
         tree = Calculus.getParseTree(formula);
      } catch (ParseException var36) {
         throw var36;
      } catch (TokenMgrError var37) {
         throw new ParseException();
      }

      for (double x = minX; x <= maxX; x += xStep) {
         double y = 0.0;

         try {
            y = evaluate(tree, x);
            if (y <= maxY && y >= minY) {
               Point p = getPointFor(x, y, minX, maxX, minY, maxY, virtualWidth, virtualHeight);
               if (lastValueOk) {
                  plate.drawLineBresenham(p.x, p.y, last.x, last.y);
               } else if (x == minX) {
                  plate.set(p.x, p.y);
               } else {
                  double tendenzY = 0.0;

                  try {
                     tendenzY = evaluate(tree, x + littleDX);
                  } catch (TokenMgrError var35) {
                     throw new ParseException();
                  }

                  if ((double)p.y > tendenzY) {
                     plate.drawLineBresenham(p.x, p.y, p.x, virtualHeight - 1);
                  } else if ((double)p.y < tendenzY) {
                     plate.drawLineBresenham(p.x, p.y, p.x, 0);
                  }
               }

               lastValueOk = true;
               last.x = p.x;
               last.y = p.y;
               lastY = y;
            } else {
               if (lastValueOk) {
                  double tendenzY = 0.0;

                  try {
                     tendenzY = evaluate(tree, x - xStep + littleDX);
                  } catch (TokenMgrError var34) {
                     throw new ParseException();
                  }

                  if (tendenzY < lastY) {
                     plate.drawLineBresenham(last.x, last.y, last.x, virtualHeight - 1);
                  } else if (tendenzY > lastY) {
                     plate.drawLineBresenham(last.x, last.y, last.x, 0);
                  }
               }

               lastValueOk = false;
               lastY = y;
            }
         } catch (Exception var38) {
            System.out.println(var38);
            lastValueOk = false;
         }
      }

      LocatedCharacterPlate result = plate.convert();
      result.pasteInto(cp);
   }

   private static Point getPointFor(double x, double y, double minX, double maxX, double minY, double maxY, int virtualWidth, int virtualHeight) {
      int xx = (int)((x - minX) / (maxX - minX) * (double)virtualWidth);
      int yy = virtualHeight - (int)((y - minY) / (maxY - minY) * (double)virtualHeight);
      return new Point(xx, yy);
   }

   public static CharacterPlate createAxis(
      PixelPlateMode axisMode, Filter filter, double axisX, double axisY, int plateWidth, int plateHeight, double minX, double maxX, double minY, double maxY
   ) {
      PixelPlate plate = new PixelPlate(new Rectangle(plateWidth, plateHeight), filter);
      plate.setMode(axisMode);
      int virtualWidth = plate.getVirtualWidth();
      int virtualHeight = plate.getVirtualHeight();
      Point p1 = getPointFor(minX, axisY, minX, maxX, minY, maxY, virtualWidth, virtualHeight);
      Point p2 = getPointFor(maxX, axisY, minX, maxX, minY, maxY, virtualWidth, virtualHeight);
      plate.drawLineBresenham(p1.x, p1.y, p2.x, p2.y);
      p1 = getPointFor(axisX, minY, minX, maxX, minY, maxY, virtualWidth, virtualHeight);
      p2 = getPointFor(axisX, maxY, minX, maxX, minY, maxY, virtualWidth, virtualHeight);
      plate.drawLineBresenham(p1.x, p1.y, p2.x, p2.y);
      LocatedCharacterPlate result = plate.convert();
      CharacterPlate cp = new CharacterPlate(plate.getWidth(), plate.getHeight());
      result.pasteInto(cp);
      return cp;
   }

   public static double evaluate(Node n, final double x) {
      Double result = (Double)n.jjtAccept(new CalculusVisitor() {
         @Override
         public Object visit(ASTFloat node, Object data) {
            return node.getValue();
         }

         @Override
         public Object visit(ASTInteger node, Object data) {
            return (double) node.getValue();
         }

         @Override
         public Object visit(ASTFunctionNode node, Object data) {
            int argumentCount = node.getFunction().getArgumentCount();
            double[] argumentValues = new double[argumentCount];

            for (int i = 0; i < argumentValues.length; i++) {
               argumentValues[i] = CalculusTool.evaluate(node.jjtGetChild(i), x);
            }

            return node.getFunction().evaluate(argumentValues);
         }

         @Override
         public Object visit(ASTConstNode node, Object data) {
            return node.getValue();
         }

         @Override
         public Object visit(ASTXNode node, Object data) {
            return data;
         }

         @Override
         public Object visit(ASTNegativeNode node, Object data) {
            return -(Double) data;
         }

         @Override
         public Object visit(ASTExpNode node, Object data) {
            double left = CalculusTool.evaluate(node.jjtGetChild(0), x);
            double right = CalculusTool.evaluate(node.jjtGetChild(1), x);
            return Math.pow(left, right);
         }

         @Override
         public Object visit(ASTModNode node, Object data) {
            double left = CalculusTool.evaluate(node.jjtGetChild(0), x);
            double right = CalculusTool.evaluate(node.jjtGetChild(1), x);
            return left % right;
         }

         @Override
         public Object visit(ASTDivNode node, Object data) {
            double left = CalculusTool.evaluate(node.jjtGetChild(0), x);
            double right = CalculusTool.evaluate(node.jjtGetChild(1), x);
            return left / right;
         }

         @Override
         public Object visit(ASTMultNode node, Object data) {
            double left = CalculusTool.evaluate(node.jjtGetChild(0), x);
            double right = CalculusTool.evaluate(node.jjtGetChild(1), x);
            return left * right;
         }

         @Override
         public Object visit(ASTSubtractNode node, Object data) {
            double left = CalculusTool.evaluate(node.jjtGetChild(0), x);
            double right = CalculusTool.evaluate(node.jjtGetChild(1), x);
            return left - right;
         }

         @Override
         public Object visit(ASTAddNode node, Object data) {
            double left = CalculusTool.evaluate(node.jjtGetChild(0), x);
            double right = CalculusTool.evaluate(node.jjtGetChild(1), x);
            return left + right;
         }

         @Override
         public Object visit(ASTStart node, Object data) {
            return CalculusTool.evaluate(node.jjtGetChild(0), x);
         }

         @Override
         public Object visit(SimpleNode node, Object data) {
            return CalculusTool.evaluate(node.jjtGetChild(0), x);
         }
      }, x);
      return result;
   }
}
