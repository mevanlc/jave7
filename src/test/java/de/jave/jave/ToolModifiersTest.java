package de.jave.jave;

import static java.awt.event.InputEvent.*;
import static org.junit.Assert.*;

import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterMatrix;
import de.jave.jave.pixelplate.PixelPlate;
import java.awt.Rectangle;
import javax.swing.Icon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ToolModifiersTest {
   private final ProbeTool tool = new ProbeTool();

   @Before
   public void reset() {
      tool.updateModifiers(0);
      tool.changes = 0;
      tool.repaints = 0;
   }

   @After
   public void cleanup() {
      tool.updateModifiers(0);
   }

   @Test
   public void releasingEitherModifierPreservesTheOther() {
      tool.updateModifiers(CTRL_DOWN_MASK | ALT_DOWN_MASK);
      assertTrue(Tool.controlDown);
      assertTrue(Tool.altDown);
      tool.updateModifiers(ALT_DOWN_MASK);
      assertFalse(Tool.controlDown);
      assertTrue(Tool.altDown);
      tool.updateModifiers(CTRL_DOWN_MASK | ALT_DOWN_MASK);
      tool.updateModifiers(CTRL_DOWN_MASK);
      assertTrue(Tool.controlDown);
      assertFalse(Tool.altDown);
   }

   @Test
   public void updatesAllModifiersBeforeOneNotification() {
      int all = SHIFT_DOWN_MASK | CTRL_DOWN_MASK | ALT_DOWN_MASK | META_DOWN_MASK;
      tool.updateModifiers(all);
      assertEquals(1, tool.changes);
      assertEquals(all, tool.observedModifiers);
      tool.updateModifiers(all | BUTTON1_DOWN_MASK);
      assertEquals("Button state does not change keyboard modifiers", 1, tool.changes);
      tool.updateModifiers(0);
      assertEquals(2, tool.changes);
      assertEquals(0, tool.observedModifiers);
   }

   @Test
   public void modifierChangesRepaintActivePreviewWithoutPointerMovement() {
      tool.markPlate = new PixelPlate(new Rectangle(0, 0, 10, 10), new Rectangle(0, 0, 10, 10), emptyFilter());
      tool.updateModifiers(CTRL_DOWN_MASK);
      tool.updateModifiers(CTRL_DOWN_MASK | ALT_DOWN_MASK);
      tool.updateModifiers(ALT_DOWN_MASK);
      tool.updateModifiers(0);
      assertEquals(4, tool.repaints);
   }

   @Test
   public void stateIsSharedAcrossTools() {
      tool.updateModifiers(SHIFT_DOWN_MASK | ALT_DOWN_MASK);
      ProbeTool next = new ProbeTool();
      next.takeToHand();
      assertTrue(Tool.shiftDown);
      assertTrue(Tool.altDown);
      next.updateModifiers(0);
      assertFalse(Tool.shiftDown);
      assertFalse(Tool.altDown);
   }

   private static Filter emptyFilter() {
      FilterMatrix[] empty = new FilterMatrix[0];
      return new Filter(empty, empty, empty, empty, empty);
   }

   private static class ProbeTool extends Tool {
      int changes;
      int repaints;
      int observedModifiers;

      ProbeTool() { super(null, null, emptyFilter()); }
      @Override public String getName() { return "Modifier probe"; }
      @Override public Icon getIcon() { return null; }
      @Override public void takeToHand() {}
      @Override public void putAside(boolean selection) {}
      @Override public void repaintCursor() { repaints++; }

      @Override
      protected void modifiersChanged() {
         changes++;
         observedModifiers = (shiftDown ? SHIFT_DOWN_MASK : 0) | (controlDown ? CTRL_DOWN_MASK : 0)
            | (altDown ? ALT_DOWN_MASK : 0) | (metaDown ? META_DOWN_MASK : 0);
         super.modifiersChanged();
      }
   }
}
