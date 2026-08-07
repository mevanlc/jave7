package de.jave.jave.rectangle;

import de.jave.gui.CharField;
import de.jave.jave.RectangleAlgorithm;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.plate.MouseCharacterModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.junit.Assert;
import org.junit.Test;

public class RectangleStylePanelTest {
   @Test
   public void selectingAStyleFillsInAllOfItsCharacters() {
      RectangleStylePanel panel = new RectangleStylePanel(new MouseCharacterModel());

      for (RectangleStyle style : RectangleStyle.values()) {
         if (style != RectangleStyle.CHARACTERS) {
            panel.setStyle(style);

            Assert.assertEquals(style, panel.getStyle());
            Assert.assertArrayEquals(
               "characters of " + style, RectangleAlgorithm.getCharsForStyle(style), panel.getCurrentChars()
            );
         }
      }
   }

   @Test
   public void switchingBackAndForthBetweenStylesKeepsTheStyleSelected() {
      RectangleStylePanel panel = new RectangleStylePanel(new MouseCharacterModel());

      panel.setStyle(RectangleStyle.UNICODE_BOLD);
      panel.setStyle(RectangleStyle.NORMAL);
      panel.setStyle(RectangleStyle.UNICODE_BOLD);

      Assert.assertEquals(RectangleStyle.UNICODE_BOLD, panel.getStyle());
      Assert.assertArrayEquals(RectangleAlgorithm.getCharsForStyle(RectangleStyle.UNICODE_BOLD), panel.getCurrentChars());
   }

   @Test
   public void editingACharacterSwitchesToUserDefinedAndKeepsTheEditedCharacter() {
      RectangleStylePanel panel = new RectangleStylePanel(new MouseCharacterModel());
      panel.setStyle(RectangleStyle.ROUND);
      char[] expected = RectangleAlgorithm.getCharsForStyle(RectangleStyle.ROUND).clone();
      expected[0] = '#';

      charFieldsOf(panel).get(0).getModel().setCharacter('#');

      Assert.assertNull(panel.getStyle());
      Assert.assertArrayEquals(expected, panel.getCurrentChars());
      Assert.assertArrayEquals(expected, RectangleAlgorithm.getCharsForStyle(null));
   }

   @Test
   public void theCustomStyleFollowsTheMouseCharacter() {
      MouseCharacterModel mouseCharacterModel = new MouseCharacterModel();
      RectangleStylePanel panel = new RectangleStylePanel(mouseCharacterModel);
      char[] expected = new char[RectangleAlgorithm.CHARACTERS];
      Arrays.fill(expected, '*');

      panel.setStyle(RectangleStyle.CHARACTERS);
      mouseCharacterModel.setCharacter1('*');

      Assert.assertEquals(RectangleStyle.CHARACTERS, panel.getStyle());
      Assert.assertArrayEquals(expected, panel.getCurrentChars());
   }

   private static List<CharField> charFieldsOf(RectangleStylePanel panel) {
      JComponent content = panel.getContent();
      Container charFieldPanel = (Container)((BorderLayout)content.getLayout()).getLayoutComponent(BorderLayout.CENTER);
      List<CharField> charFields = new ArrayList<>();

      for (Component component : charFieldPanel.getComponents()) {
         if (component instanceof CharField) {
            charFields.add((CharField)component);
         }
      }

      Assert.assertEquals(RectangleAlgorithm.CHARACTERS, charFields.size());
      return charFields;
   }
}
