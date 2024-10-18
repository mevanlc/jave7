package net.disy.commons.swing.showhide;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;

public class ShowHideButton {
   private static final Icon HIDE_ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("showhide/hide_arrows.gif");
   private static final Icon SHOW_ICON = DisyCommonsSwingImageProvider.getInstance().getImageIcon("showhide/show_arrows.gif");
   private final JButton button;
   private final BooleanModel model;
   private final String hideToolTipText;
   private final String showToolTipText;

   public ShowHideButton(BooleanModel model) {
      this(model, null, null);
   }

   public ShowHideButton(final BooleanModel model, String hideToolTipText, String showToolTipText) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.showToolTipText = showToolTipText;
      this.hideToolTipText = hideToolTipText;
      this.button = new JButton();
      this.button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setValue(!model.getValue());
         }
      });
      this.button.setFocusPainted(false);
      this.button.setPreferredSize(new Dimension(17, 17));
      this.button.setMargin(new Insets(0, 0, 0, 0));
      this.button.setBorder(new EmptyBorder(4, 4, 4, 4));
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ShowHideButton.this.updateView();
         }
      });
      this.updateView();
   }

   private void updateView() {
      this.button.setIcon(this.model.getValue() ? HIDE_ICON : SHOW_ICON);
      this.button.setToolTipText(this.model.getValue() ? this.hideToolTipText : this.showToolTipText);
   }

   public JComponent getContent() {
      return this.button;
   }
}
