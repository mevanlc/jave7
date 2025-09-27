package net.disy.commons.swing.component;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;
import net.disy.commons.swing.util.ToggleComponentEnabler;

public class SelectableChangeComponentBuilder {
   private final JPanel container = new JPanel(new GridDialogLayout(3, false));

   public SelectableChangeComponentBuilder() {
      this.container.setBorder(LayoutUtilities.getDefaultEmptyBorder());
   }

   public void addRow(IComponentContainer view, BooleanModel booleanModel) {
      JCheckBox changeBox = this.addCheckBox(booleanModel, GridAlignment.BEGINNING);
      this.addViewContent(view, 2);
      ToggleComponentEnabler.connect(changeBox, view.getContent());
   }

   private void addViewContent(IComponentContainer view, int horizontalSpan) {
      GridDialogLayoutData componentData = new GridDialogLayoutData();
      componentData.setHorizontalSpan(horizontalSpan);
      componentData.setGrabExcessHorizontalSpace(true);
      this.container.add(view.getContent(), componentData);
   }

   public void addRow(ILabelledView view, BooleanModel booleanModel) {
      JCheckBox changeBox = this.addCheckBox(booleanModel, GridAlignment.CENTER);
      this.container.add(view.getLabel(), GridDialogLayoutData.RIGHT);
      this.addViewContent(view, 1);
      ToggleComponentEnabler.connect(changeBox, view.getLabel(), view.getContent());
   }

   private JCheckBox addCheckBox(BooleanModel booleanModel, GridAlignment verticalAlignment) {
      JCheckBox changeBox = ActionWidgetFactory.createCheckBox(new SmartToggleAction(booleanModel));
      changeBox.setToolTipText(DisyCommonsSwingMessages.getString("SelectableChangeComponentBuilder.changeTooltip"));
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setVerticalAlignment(verticalAlignment);
      this.container.add(changeBox, data);
      return changeBox;
   }

   public JComponent getContent() {
      return this.container;
   }
}
