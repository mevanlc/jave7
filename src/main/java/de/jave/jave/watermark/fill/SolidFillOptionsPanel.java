package de.jave.jave.tool.fill;

import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.plate.MouseCharacterPanel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class SolidFillOptionsPanel implements IComponentContainer {
   private final JComponent content;

   public SolidFillOptionsPanel(MouseCharacterModel mouseCharacterModel) {
      Ensure.ensureArgumentNotNull(mouseCharacterModel);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new MouseCharacterPanel(mouseCharacterModel).getContent());
      panel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
