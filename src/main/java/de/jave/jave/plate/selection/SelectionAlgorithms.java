package de.jave.jave.plate.selection;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.Selection;
import de.jave.jave.browser.IJaveDocumentTypeVisitor;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayDeque;
import java.util.Deque;

public class SelectionAlgorithms {
   public static void pasteAsNewSelection(IDocumentEditor editor, CharacterPlate content, Point location) {
      Plate plate = editor.getPlate();
      dropSelectionIfAny(editor);
      plate.getSelection().set(location, content);
      plate.repaint();
   }

   public static void pasteAsNewSelection(IDocumentEditor editor, JaveClipboardSelection sel, Point location) {
      Plate plate = editor.getPlate();
      dropSelectionIfAny(editor);
      plate.getSelection().set(location, sel);
      plate.repaint();
   }

   public static void expandSelection(IDocumentEditor editor) {
      Plate plate = editor.getPlate();
      Rectangle re = plate.getSelectionRegion();
      dropSelection(editor);
      re.x--;
      re.y--;
      re.width += 2;
      re.height += 2;
      plate.setSelection(re);
   }

   public static void shrinkSelection(IDocumentEditor editor) {
      Plate plate = editor.getPlate();
      CharacterPlate ch = plate.getSelectionContent();
      Insets insets = ch.getEmptyInsets();
      int w = ch.getWidth();
      int h = ch.getHeight();
      if (insets.top + insets.bottom < h && insets.right + insets.left < w && (h != 1 || w != 1 || ch.get(0, 0) != ' ')) {
         int w2 = w - insets.left - insets.right;
         int h2 = h - insets.bottom - insets.top;
         CharacterPlate chNew = new CharacterPlate(w2, h2);

         for (int y = 0; y < h2; y++) {
            for (int x = 0; x < w2; x++) {
               chNew.set(x, y, ch.get(x + insets.left, y + insets.top));
            }
         }

         Point p = plate.getSelection().getLocation();
         plate.getSelection().set(new Point(p.x + insets.left, p.y + insets.top), chNew);
         plate.repaint();
      } else {
         dropSelection(editor);
      }
   }

   public static void selectAll(IDocumentEditor editor) {
      dropSelectionIfAny(editor);
      Plate plate = editor.getPlate();
      Dimension documentSize = plate.getDocumentSize();
      Rectangle region = new Rectangle(0, 0, documentSize.width, documentSize.height);
      plate.setSelection(region);
   }

   public static boolean selectConnected(IDocumentEditor editor, Point location) {
      Plate plate = editor.getPlate();
      if (location == null || !plate.isInside(location)) {
         return false;
      }

      dropSelectionIfAny(editor);
      Rectangle region = findConnectedRegion(plate.getContent(), location);
      if (region == null) {
         return false;
      }

      plate.setSelection(region);
      return true;
   }

   static Rectangle findConnectedRegion(CharacterPlate content, Point location) {
      if (location == null || !content.contains(location.x, location.y) || content.get(location.x, location.y) == ' ') {
         return null;
      }

      boolean[][] visited = new boolean[content.getWidth()][content.getHeight()];
      Deque<Point> pending = new ArrayDeque<>();
      pending.add(location);
      int minX = location.x;
      int maxX = location.x;
      int minY = location.y;
      int maxY = location.y;

      while (!pending.isEmpty()) {
         Point current = pending.removeFirst();
         if (!content.contains(current.x, current.y) || visited[current.x][current.y]) {
            continue;
         }

         visited[current.x][current.y] = true;
         if (content.get(current.x, current.y) == ' ') {
            continue;
         }

         minX = Math.min(minX, current.x);
         maxX = Math.max(maxX, current.x);
         minY = Math.min(minY, current.y);
         maxY = Math.max(maxY, current.y);
         pending.add(new Point(current.x + 1, current.y));
         pending.add(new Point(current.x - 1, current.y));
         pending.add(new Point(current.x, current.y + 1));
         pending.add(new Point(current.x, current.y - 1));
      }

      return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
   }

   public static void dropSelection(IDocumentEditor editor) {
      Plate plate = editor.getPlate();
      if (!plate.hasSelection()) {
         throw new RuntimeException("No Selection to drop in Plate.dropSelection()!");
      } else {
         Selection selection = plate.getSelection();
         if (isAutoExpandEnabledFor(plate.getPlatePreferences(), editor.getType())) {
            PlateDocument document = plate.getDocument();
            Insets expansion = getRequiredExpansionToDrop(document.getSize(), selection.getRegion());
            if (expansion != null) {
               expandDocument(editor, expansion);
            }
         }

         selection.paste();
         plate.unselect();
      }
   }

   private static boolean isAutoExpandEnabledFor(final PlatePreferences preferences, JaveDocumentType documentType) {
      return documentType.accept(new IJaveDocumentTypeVisitor<Boolean>() {
         public Boolean visitText(JaveDocumentType type) {
            return preferences.isAutoResizeOnDropForTextEditor();
         }

         public Boolean visitGame(JaveDocumentType type) {
            return Boolean.FALSE;
         }

         public Boolean visitAnimation(JaveDocumentType type) {
            return preferences.isAutoResizeOnDropForAnimationEditor();
         }
      });
   }

   private static void expandDocument(final IDocumentEditor editor, final Insets expansion) {
      final Plate plate = editor.getPlate();
      final PlateDocument document = plate.getDocument();
      final Selection selection = plate.getSelection();
      final Point p = selection.getLocation();
      editor.getType().accept(new IJaveDocumentTypeVisitor<Void>() {
         public Void visitText(JaveDocumentType type) {
            selection.setLocation(p.x + expansion.left, p.y + expansion.top);
            // Layers: auto-expand still grows the document layer only. Secondary
            // layer position/content expansion needs a deliberate model method.
            CharacterPlate content = document.getContent();
            content.expand(expansion);
            plate.handleDocumentSizeChanged();
            return null;
         }

         public Void visitGame(JaveDocumentType type) {
            return null;
         }

         public Void visitAnimation(JaveDocumentType type) {
            selection.setLocation(p.x + expansion.left, p.y + expansion.top);
            AnimationDocumentEditor animationEditor = (AnimationDocumentEditor)editor;
            AnimationEditorModel model = animationEditor.getModel();

            // Layers: animation documents are intentionally not layer-compatible
            // in MVP1; keep this frame-oriented path separate from text layers.
            for (int frameIndex = 0; frameIndex < model.getAnimationFile().getFrameCount(); frameIndex++) {
               JaveAnimationFrame frame = model.getAnimationFile().getFrame(frameIndex);
               CharacterPlate characterPlate = new CharacterPlate(frame.getContent());
               characterPlate.expand(expansion);
               frame.setContent(characterPlate);
            }

            CharacterPlate content = document.getContent();
            content.expand(expansion);
            plate.handleDocumentSizeChanged();
            return null;
         }
      });
   }

   private static Insets getRequiredExpansionToDrop(Dimension documentSize, Rectangle selectionRectangle) {
      Point p = selectionRectangle.getLocation();
      int left;
      if (p.x < 0) {
         left = -p.x;
      } else {
         left = 0;
      }

      int top;
      if (p.y < 0) {
         top = -p.y;
      } else {
         top = 0;
      }

      int right;
      if (p.x + selectionRectangle.width > documentSize.width) {
         right = p.x + selectionRectangle.width - documentSize.width;
      } else {
         right = 0;
      }

      int bottom;
      if (p.y + selectionRectangle.height > documentSize.height) {
         bottom = p.y + selectionRectangle.height - documentSize.height;
      } else {
         bottom = 0;
      }

      return left == 0 && right == 0 && top == 0 && bottom == 0 ? null : new Insets(top, left, bottom, right);
   }

   public static void dropSelectionIfAny(IDocumentEditor editor) {
      if (editor.getPlate().hasSelection()) {
         dropSelection(editor);
      }
   }
}
