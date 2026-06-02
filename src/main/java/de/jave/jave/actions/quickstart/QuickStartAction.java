package de.jave.jave.actions.quickstart;

import de.jave.gui.listpanel.IClickHandler;
import de.jave.gui.listpanel.MouseActiveListPanelBuilder;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.preferences.JaveApplicationPreferences;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.widgets.AutoWrappingLabel;

public final class QuickStartAction extends SmartAction {
   private final JavEApplication application;

   public QuickStartAction(JavEApplication application) {
      super(JaveMessages.QuickStart_ActionName, QuickStartIcons.ACTION_ICON);
      this.setToolTipText(JaveMessages.QuickStart_ActionToolTip);
      Ensure.ensureArgumentNotNull(application);
      this.application = application;
   }

   @Override
   protected void execute(Component parentComponent) {
      this.performQuickStart(parentComponent);
   }

   public void performQuickStart(Component parentComponent) {
      final IQuickStartItem[] items = new IQuickStartItem[]{
         new TextEditorQuickStartItem(),
         new Image2AsciiQuickStartItem(),
         new FigletEditorQuickStartItem(),
         new WatermarkQuickStartItem(),
         new AnimationEditorQuickStartItem()
      };
      final ObjectModel<IQuickStartItem> selectionModel = new ObjectModel<>();
      IDialogPage page = new AbstractDialogPage(JaveMessages.QuickStart_DialogHeaderText) {
         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            MouseActiveListPanelBuilder<IQuickStartItem> builder = new MouseActiveListPanelBuilder<>(new IClickHandler<IQuickStartItem>() {
               public void handleClick(IQuickStartItem item) {
                  selectionModel.setValue(item);
                  fireRequestFinish();
               }
            });

            for (IQuickStartItem item : items) {
               builder.add(this.createItemPanel(item), item);
            }

            return builder.createComponent();
         }

         private JComponent createItemPanel(IQuickStartItem item) {
            JLabel nameLabel = new JLabel(item.getName());
            nameLabel.setFont(new Font("SansSerif", 1, 13));
            JPanel rightPanel = new JPanel(new GridDialogLayout(1, false));
            rightPanel.add(nameLabel);
            AutoWrappingLabel descriptionLabel = new AutoWrappingLabel(item.getDescription());
            descriptionLabel.setEnabled(false);
            rightPanel.add(descriptionLabel.getContent(), GridDialogLayoutData.FILL_BOTH);
            JPanel panel = new JPanel(new GridDialogLayout(2, false));
            panel.add(new JLabel(item.getIcon()));
            panel.add(rightPanel, GridDialogLayoutData.FILL_BOTH);
            return panel;
         }

         @Override
         public String getTitle() {
            return JaveMessages.QuickStart_DialogTitle;
         }
      };
      UserDialog dialog = new UserDialog(
         parentComponent, new DefaultDialogConfiguration<IDialogPage>(page, DialogButtonConfigurationFactory.createCloseOnly()) {
            @Override
            public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
               return DialogHeaderPanelConfiguration.createVisibleWithIcon(QuickStartIcons.LARGE_DIALOG_ICON);
            }

            @Override
            public JComponent createOptionalButtonPanelLeftComponent() {
               return QuickStartAction.createShowQuickStartCheckBox(QuickStartAction.this.application.getApplicationPreferences());
            }
         }
      );
      dialog.show();
      IQuickStartItem item = selectionModel.getValue();
      if (item != null) {
         item.perform(this.application);
      }
   }

   public static JCheckBox createShowQuickStartCheckBox(final JaveApplicationPreferences preferences) {
      final JCheckBox checkBox = new JCheckBox(JaveMessages.QuickStart_ShowOnStartupCheckBoxLabel, preferences.isShowQuickStartOnStartup());
      checkBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            preferences.setShowQuickStartOnStartup(checkBox.isSelected());
         }
      });
      return checkBox;
   }
}
