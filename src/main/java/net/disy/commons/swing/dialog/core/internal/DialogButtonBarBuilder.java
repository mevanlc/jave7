package net.disy.commons.swing.dialog.core.internal;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.button.RolloverButtonFactory;
import net.disy.commons.swing.component.Gap;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogIconResources;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.label.internal.MnemonicLabelParser;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutDirection;
import net.disy.commons.swing.util.IEnableable;

public class DialogButtonBarBuilder {
   private final List<JComponent> leftSideComponents = new ArrayList<>();
   private final List<JComponent> buttons = new ArrayList<>();
   private AbstractButton helpButton;

   public void addLeftSideComponent(JComponent component) {
      this.leftSideComponents.add(component);
   }

   public void addButtons(JComponent... buttonComponents) {
      this.buttons.addAll(Arrays.asList(buttonComponents));
   }

   public void addButtonsCompacted(JComponent... buttonComponents) {
      JPanel compactedButtonPanel = new JPanel(new GridLayout(1, 0, 0, 0));

      for (JComponent component : buttonComponents) {
         compactedButtonPanel.add(component);
      }

      this.addButtons(compactedButtonPanel);
   }

   public IEnableable setHelpHandler(IDialogHelpHandler helpHandler) {
      this.helpButton = createHelpButton(helpHandler);
      return new IEnableable() {
         @Override
         public void setEnabled(boolean enabled) {
            DialogButtonBarBuilder.this.helpButton.setEnabled(enabled);
         }
      };
   }

   public JComponent createButtonBar() {
      ArrayList<JComponent> allLeftComponents = new ArrayList<>();
      if (this.helpButton != null) {
         allLeftComponents.add(new Gap(0, 0));
         allLeftComponents.add(this.helpButton);
      }

      allLeftComponents.addAll(this.leftSideComponents);
      JPanel panel = new JPanel(new GridDialogLayout(allLeftComponents.size() + 1, false));

      for (JComponent component : allLeftComponents) {
         panel.add(component);
      }

      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder(LayoutDirection.HORIZONTAL);

      for (JComponent createdButton : this.buttons) {
         buttonPanelBuilder.add(createdButton);
      }

      JPanel buttonPanel = buttonPanelBuilder.createPanel();
      panel.add(buttonPanel, GridDialogLayoutData.FILL_HORIZONTAL);
      return panel;
   }

   private static AbstractButton createHelpButton(final IDialogHelpHandler helpHandler) {
      SmartAction helpAction = new SmartAction(DisyCommonsSwingDialogIconResources.DIALOG_HELP) {
         @Override
         protected void execute(Component parentComponent) {
            helpHandler.execute(parentComponent);
         }
      };
      String helpText = MnemonicLabelParser.parse(DisyCommonsSwingDialogMessages.HELP).getPlainText();
      helpAction.setToolTipText(helpText);
      return RolloverButtonFactory.createButton(helpAction);
   }
}
