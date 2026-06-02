package de.jave.asciimation.export.applet;

import de.jave.asciimation.AnimationOutputOptionsConfiguration;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.DefaultAnimationOutputOptionsPage;
import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.swing.layout.grid.GridAlignment;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;
import net.dizzy.commons.swing.message.MessageTypeUi;
import net.dizzy.commons.swing.widgets.AutoWrappingLabel;

public class AppletAnimationOutputOptionsPage extends DefaultAnimationOutputOptionsPage {
   public AppletAnimationOutputOptionsPage(AnimationExportWizardModel model) {
      super(model, new AnimationOutputOptionsConfiguration(new ExtensionFileFilter[]{ExtensionFileFilters.HTML}, true, true));
   }

   @Override
   protected void addAdditionalOptionsComponents(GridDialogPanelBuilder dialogPanel) {
      dialogPanel.add(
         new IDialogComponent() {
            @Override
            public void fillInto(JPanel panel, int columnCount) {
               JPanel content = new JPanel(new GridDialogLayout(2, false));
               GridDialogLayoutData labelData = new GridDialogLayoutData();
               labelData.setVerticalAlignment(GridAlignment.BEGINNING);
               content.add(new JLabel(MessageTypeUi.infoIcon), labelData);
               content.add(
                  new AutoWrappingLabel(
                        "Along with the web site there will be a jar and a jmov file copied to the output folder, containing the player and the animation content."
                     )
                     .getContent(),
                  GridDialogLayoutData.FILL_HORIZONTAL
               );
               GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
               data.setHorizontalSpan(columnCount);
               panel.add(content, data);
            }

            @Override
            public int getColumnCount() {
               return 1;
            }
         }
      );
   }
}
