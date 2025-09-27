package net.disy.commons.swing.dialog.color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.layout.grid.EndOfLineMarkerComponent;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IDialogComponent;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class ColorPreviewPanel implements IDialogComponent {
   private final ColorIndicator oldColorIndicator;
   private final ColorIndicator currentColorIndicator;

   public ColorPreviewPanel(ColorModel colorModel) {
      this.oldColorIndicator = new ColorIndicator(new ColorModel(colorModel.getColor()));
      this.currentColorIndicator = new ColorIndicator(colorModel);
   }

   @Override
   public int getColumnCount() {
      return 2;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      JLabel oldColorLabel = new JLabel(DisyCommonsSwingMessages.getString("ColorPreviewPanel.OriginalColor"));
      JLabel currentColorLabel = new JLabel(DisyCommonsSwingMessages.getString("ColorPreviewPanel.SelectedColor"));
      panel.add(oldColorLabel, GridDialogLayoutData.RIGHT);
      panel.add(this.oldColorIndicator.getContent());
      panel.add(new EndOfLineMarkerComponent());
      panel.add(currentColorLabel, GridDialogLayoutData.RIGHT);
      panel.add(this.currentColorIndicator.getContent());
      panel.add(new EndOfLineMarkerComponent());
   }
}
