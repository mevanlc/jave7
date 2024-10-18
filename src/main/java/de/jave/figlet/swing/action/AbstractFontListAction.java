package de.jave.figlet.swing.action;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.util.FigException;
import java.awt.Component;
import javax.swing.JOptionPane;
import net.disy.commons.swing.action.SmartAction;

public abstract class AbstractFontListAction extends SmartAction {
   private final IFigDriver figlet;

   public AbstractFontListAction(IFigDriver figlet, String name) {
      super(name);
      this.figlet = figlet;
   }

   protected IFigDriver getFiglet() {
      return this.figlet;
   }

   @Override
   protected void execute(Component parentComponent) {
      String fontName = this.getSelectedFont();

      FigFont font;
      try {
         font = this.figlet.getFont(fontName);
      } catch (FigException var5) {
         var5.printStackTrace();
         JOptionPane.showMessageDialog(null, "Error loading font '" + fontName + "'", "Error (" + var5.getLocalizedMessage() + ")", 0);
         return;
      }

      this.execute(font, parentComponent);
   }

   protected abstract String getSelectedFont();

   protected abstract void execute(FigFont var1, Component var2);
}
