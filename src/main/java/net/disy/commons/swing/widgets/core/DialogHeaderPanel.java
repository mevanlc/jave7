package net.disy.commons.swing.dialog.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogIconResources;
import net.disy.commons.swing.dialog.core.message.DialogMessageModel;
import net.disy.commons.swing.dialog.core.message.DialogMessagePanel;
import net.disy.commons.swing.icon.CompositeIcon;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.widgets.HorizontalLine;

public class DialogHeaderPanel {
   private final DialogMessagePanel messagePanel;
   private final JLabel descriptionLabel;
   private final ObjectModel<String> descriptionModel;
   private final Icon largeDialogHeaderIcon;
   private JComponent content;

   public DialogHeaderPanel(DialogMessageModel messageModel, ObjectModel<String> descriptionModel, Icon largeDialogHeaderIcon) {
      Ensure.ensureArgumentNotNull(messageModel);
      Ensure.ensureArgumentNotNull(descriptionModel);
      this.descriptionModel = descriptionModel;
      this.largeDialogHeaderIcon = largeDialogHeaderIcon;
      this.messagePanel = new DialogMessagePanel(messageModel);
      this.descriptionLabel = new JLabel("!Dialog.description!", 2);
      this.descriptionLabel.setForeground(IDialogConstants.HEADER_TEXT_COLOR);
      this.descriptionLabel.setFont(IDialogConstants.HEADER_TITLE_FONT);
      descriptionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            DialogHeaderPanel.this.updateDescription();
         }
      });
      this.updateDescription();
   }

   public JComponent getContent() {
      if (this.content == null) {
         JPanel innerPanel = new JPanel(new BorderLayout(LayoutUtilities.getDpiAdjusted(2), LayoutUtilities.getDpiAdjusted(2)));
         innerPanel.add(Box.createRigidArea(new Dimension(1, 22)), "East");
         innerPanel.add(this.messagePanel.getContent(), "Center");
         innerPanel.add(this.descriptionLabel, "North");
         innerPanel.setBorder(new EmptyBorder(LayoutUtilities.getDpiAdjusted(2), LayoutUtilities.getDpiAdjusted(4), 0, LayoutUtilities.getDpiAdjusted(2)));
         innerPanel.setBackground(IDialogConstants.HEADER_BACKGROUND_COLOR);
         Icon icon;
         if (this.largeDialogHeaderIcon != null) {
            icon = new CompositeIcon(DisyCommonsSwingDialogIconResources.DIALOG_HEADER_ICON_BACKGROUND, this.largeDialogHeaderIcon);
         } else {
            icon = new CompositeIcon(DisyCommonsSwingDialogIconResources.DIALOG_HEADER_ICON_BACKGROUND);
         }

         JLabel label = new JLabel(icon);
         JPanel panel = new JPanel(new GridDialogLayout(2, false, 0, 0));
         panel.setBackground(IDialogConstants.HEADER_BACKGROUND_COLOR);
         panel.add(innerPanel, GridDialogLayoutData.FILL_BOTH);
         GridDialogLayoutData iconLayout = new GridDialogLayoutData();
         iconLayout.setVerticalAlignment(GridAlignment.END);
         panel.add(label, iconLayout);
         JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
         contentPanel.add(panel, "Center");
         contentPanel.add(new HorizontalLine(), "South");
         this.content = contentPanel;
      }

      return this.content;
   }

   private void updateDescription() {
      String description = this.descriptionModel.getValue();
      this.descriptionLabel.setText(description);
   }
}
