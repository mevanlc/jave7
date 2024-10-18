package net.disy.commons.swing.color;

import java.awt.Color;
import javax.swing.UIManager;

public class SwingColors {
   public static Color getControlColor() {
      return UIManager.getColor("control");
   }

   public static Color getControlDkShadowColor() {
      return UIManager.getColor("controlDkShadow");
   }

   public static Color getControlHighlightColor() {
      return UIManager.getColor("controlHighlight");
   }

   public static Color getControlLtHighlightColor() {
      Color ltHighlightColor = UIManager.getColor("controlLtHighlight");
      return ltHighlightColor == null ? getControlHighlightColor() : ltHighlightColor;
   }

   public static Color getControlShadowColor() {
      return UIManager.getColor("controlShadow");
   }

   public static Color getPanelBackgroundColor() {
      return UIManager.getColor("Panel.background");
   }

   public static Color getPanelForegroundColor() {
      return UIManager.getColor("Panel.foreground");
   }

   public static Color getTableFocusCellBackgroundColor() {
      return UIManager.getColor("Table.focusCellBackground");
   }

   public static Color getTableFocusCellForegroundColor() {
      return UIManager.getColor("Table.focusCellForeground");
   }

   public static Color getTableHeaderBackgroundColor() {
      return UIManager.getColor("TableHeader.background");
   }

   public static Color getTableHeaderForegroundColor() {
      return UIManager.getColor("TableHeader.foreground");
   }

   public static Color getTableSelectionBackgroundColor() {
      return UIManager.getColor("Table.selectionBackground");
   }

   public static Color getTableSelectionForegroundColor() {
      return UIManager.getColor("Table.selectionForeground");
   }

   public static Color getTreeLineColor() {
      return UIManager.getColor("Tree.line");
   }

   public static Color getTreeSelectionBackgroundColor() {
      return UIManager.getColor("Tree.selectionBackground");
   }

   public static Color getTreeSelectionBorderColor() {
      return UIManager.getColor("Tree.selectionBorderColor");
   }

   public static Color getTreeSelectionForegroundColor() {
      return UIManager.getColor("Tree.selectionForeground");
   }

   public static Color getTreeTextBackgroundColor() {
      return UIManager.getColor("Tree.textBackground");
   }

   public static Color getTreeTextForegroundColor() {
      return UIManager.getColor("Tree.textForeground");
   }

   public static Color getTextAreaForegroundColor() {
      return UIManager.getColor("TextArea.foreground");
   }

   public static Color getTextAreaBackgroundColor() {
      return UIManager.getColor("TextArea.background");
   }

   public static Color getTextFieldInactiveBackgroundColor() {
      return UIManager.getColor("TextField.inactiveBackground");
   }

   public static Color getTextAreaCaretForegroundColor() {
      return UIManager.getColor("TextArea.caretForeground");
   }

   public static Color getTextAreaSelectionBackgroundColor() {
      return UIManager.getColor("TextArea.selectionBackground");
   }

   public static Color getTextAreaSelectionForegroundColor() {
      return UIManager.getColor("TextArea.selectionForeground");
   }

   public static Color getTextAreaInactiveForegroundColor() {
      return UIManager.getColor("TextArea.inactiveForeground");
   }

   public static Color getLabelForegroundColor() {
      return UIManager.getColor("Label.foreground");
   }

   public static Color getLabelBackgroundColor() {
      return UIManager.getColor("Label.background");
   }

   public static Color getHyperlinkColor() {
      return Color.BLUE;
   }
}
