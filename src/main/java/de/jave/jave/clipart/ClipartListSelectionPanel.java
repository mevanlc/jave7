package de.jave.jave.clipart;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.gui.listpanel.IClickHandler;
import de.jave.gui.listpanel.MouseActiveListPanelBuilder;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IComponentContainer;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.LayoutUtilities;
import net.dizzy.commons.swing.widgets.AutoWrappingLabel;

public class ClipartListSelectionPanel implements IComponentContainer {
   private static final Font TITLE_FONT = new Font("Dialog", 1, 12);
   private final JPanel panel = new JPanel(new BorderLayout());
   private final AsciiTextAreaProperties properties;
   private final IClickHandler<Clipart> clickHandler;

   public ClipartListSelectionPanel(
      AsciiTextAreaProperties properties, final ObjectModel<ClipartGroup> groupSelectionModel, IClickHandler<Clipart> clickHandler
   ) {
      Ensure.ensureArgumentNotNull(properties);
      Ensure.ensureArgumentNotNull(groupSelectionModel);
      Ensure.ensureArgumentNotNull(clickHandler);
      this.properties = properties;
      this.clickHandler = clickHandler;
      groupSelectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ClipartListSelectionPanel.this.updatePanelForSelectedGroup(groupSelectionModel.getValue());
         }
      });
      this.updatePanelForSelectedGroup(groupSelectionModel.getValue());
   }

   private void updatePanelForSelectedGroup(ClipartGroup group) {
      this.panel.removeAll();
      if (group == null) {
         this.panel.add(new AutoWrappingLabel("No clipart group selected. Please select a clipart group.").getContent());
      } else {
         MouseActiveListPanelBuilder<Clipart> builder = new MouseActiveListPanelBuilder<>(this.clickHandler);

         for (int i = 0; i < group.getClipartCount(); i++) {
            Clipart clipart = group.getClipart(i);
            JComponent component = this.createClipartComponent(clipart);
            builder.add(component, clipart);
         }

         this.panel.add(builder.createComponent(), "Center");
         this.panel.revalidate();
      }
   }

   private JComponent createClipartComponent(Clipart clipart) {
      JLabel label = new JLabel(clipart.getName() + ":");
      label.setFont(TITLE_FONT);
      CharacterPlate content = clipart.getContent();
      Dimension size = new Dimension(Math.min(content.getSize().width, 40), Math.min(content.getSize().height, 15));
      AsciiTextArea textArea = new AsciiTextArea(size, this.properties);
      textArea.setText(content.asString());
      JPanel clipartPanel = new JPanel(new GridDialogLayout(3, false));
      clipartPanel.add(label, GridDialogLayoutData.FILL_HORIZONTAL);
      clipartPanel.add(new JLabel(clipart.getSize().width + " x " + clipart.getSize().height));
      clipartPanel.add(new JLabel(clipart.getAuthor()));
      clipartPanel.add(textArea.getContent(), new GridDialogLayoutData().setHorizontalSpan(3).setHorizontalIndent(LayoutUtilities.getDpiAdjusted(5)));
      return clipartPanel;
   }

   @Override
   public JComponent getContent() {
      return this.panel;
   }
}
