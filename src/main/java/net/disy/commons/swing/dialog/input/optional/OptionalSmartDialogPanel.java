package net.disy.commons.swing.dialog.input.optional;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.IObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.component.Gap;
import net.disy.commons.swing.dialog.input.AbstractSmartDialogPanel;
import net.disy.commons.swing.dialog.input.text.IUpdatableSmartDialogPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.util.ToggleComponentEnabler;

public class OptionalSmartDialogPanel<T> extends AbstractSmartDialogPanel implements IOptionalSmartDialogPanel {
   private final JCheckBox enabledCheckBox;
   private final IUpdatableSmartDialogPanel delegate;
   private final IObjectModel<T> model;
   private final IObjectModel<T> fieldModel;
   private final boolean mandatory;

   public OptionalSmartDialogPanel(IObjectModel<T> model, IUpdatableSmartDialogPanel field, IObjectModel<T> fieldModel, boolean mandatory) {
      this(model, null, field, fieldModel, mandatory);
   }

   protected OptionalSmartDialogPanel(
      IObjectModel<T> model, String checkBoxLabel, IUpdatableSmartDialogPanel delegate, IObjectModel<T> fieldModel, boolean mandatory
   ) {
      this.model = model;
      this.delegate = delegate;
      this.fieldModel = fieldModel;
      this.mandatory = mandatory;
      this.enabledCheckBox = new JCheckBox(checkBoxLabel);
      this.enabledCheckBox.setSelected(mandatory || model.getValue() != null);
      if (!mandatory) {
         this.enabledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               OptionalSmartDialogPanel.this.updateModel();
            }
         });
      }

      this.updateView();
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            OptionalSmartDialogPanel.this.updateView();
         }
      });
      delegate.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            OptionalSmartDialogPanel.this.updateModel();
         }
      });
   }

   protected void updateModel() {
      this.model.setValue(this.enabledCheckBox.isSelected() ? this.fieldModel.getValue() : null);
   }

   protected void updateView() {
      if (this.model.getValue() != null) {
         this.fieldModel.setValue(this.model.getValue());
      }
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.delegate.addChangeListener(listener);
   }

   @Override
   public void requestFocus() {
      this.delegate.requestFocus();
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      if (this.mandatory) {
         this.fillMandatory(panel, columnCount);
      } else {
         this.fillOptional(panel, columnCount);
      }
   }

   private void fillOptional(JPanel panel, int columnCount) {
      if (this.isCheckboxWithoutLabel()) {
         panel.add(this.enabledCheckBox);
      } else {
         GridDialogLayoutData allColumnsData = new GridDialogLayoutData();
         allColumnsData.setHorizontalSpan(columnCount);
         panel.add(this.enabledCheckBox, allColumnsData);
         panel.add(Box.createHorizontalStrut(LayoutUtilities.getDpiAdjusted(20)), -1);
      }

      this.delegate.fillInto(panel, columnCount - 2);
      ToggleComponentEnabler.connect(this.enabledCheckBox, this.delegate);
   }

   private void fillMandatory(JPanel panel, int columnCount) {
      if (this.isCheckboxWithoutLabel()) {
         panel.add(new Gap());
      } else {
         GridDialogLayoutData allColumnsData = new GridDialogLayoutData();
         allColumnsData.setHorizontalSpan(columnCount);
         panel.add(new Gap(), allColumnsData);
         panel.add(Box.createHorizontalStrut(LayoutUtilities.getDpiAdjusted(20)), -1);
      }

      this.delegate.fillInto(panel, columnCount - 2);
   }

   private boolean isCheckboxWithoutLabel() {
      return this.enabledCheckBox.getText() == null || this.enabledCheckBox.getText().isEmpty();
   }

   @Override
   public int getColumnCount() {
      return this.delegate.getColumnCount() + 2;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return this.delegate.createOptionalCurrentMessage();
   }

   protected JCheckBox getCheckbox() {
      return this.enabledCheckBox;
   }

   @Override
   public void update() {
      this.delegate.update();
   }

   @Override
   public JComponent[] getComponents() {
      return this.delegate.getComponents();
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.delegate.setEnabled(enabled);
   }
}
