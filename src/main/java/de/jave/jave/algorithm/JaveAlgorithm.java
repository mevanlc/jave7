package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.CharacterPlate;
import javax.swing.Icon;

@Deprecated
public abstract class JaveAlgorithm {
   public CharacterPlate apply(CharacterPlate plate) {
      JaveSelection sel = new JaveSelection(plate);
      sel = this.apply(sel);
      return sel.getContent();
   }

   public JaveSelection apply(JaveSelection plate) {
      plate.setContent(this.apply(plate.getContent()));
      return plate;
   }

   public abstract String getUndoRedoName();

   public abstract String getMenuItemLabel();

   public Icon getIcon() {
      return null;
   }
}
