package de.jave.jave.application.about;

import de.jave.gui.GHyperlink;
import de.jave.gui.layout.CenterLayout;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.jave.version.JaveVersion;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.LayoutUtilities;
import net.dizzy.commons.swing.widgets.AutoWrappingLabel;
import net.dizzy.commons.swing.widgets.HorizontalLine;

public class JaveAboutDialog {
   public static void showAboutDialog(Component parentComponent) {
      AbstractDialogPage dialogPage = new AbstractDialogPage("") {
         @Override
         public String getTitle() {
            return "JavE " + JaveVersion.getFullVersionNumber();
         }

         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            JPanel content = new JPanel();
            content.setLayout(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentGroupsSpacing()));
            content.add(JaveAboutDialog.createTitle(), "North");
            content.add(JaveAboutDialog.createDetailsPanel(), "Center");
            return content;
         }
      };
      UserDialog dialog = new UserDialog(
         parentComponent, new DefaultDialogConfiguration<IDialogPage>(dialogPage, DialogButtonConfigurationFactory.createCloseOnly()) {
            @Override
            public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
               return DialogHeaderPanelConfiguration.createInvisible();
            }
         }
      );
      dialog.show();
   }

   private static JComponent createTitle() {
      JLabel title = new JLabel(JaveTitleProvider.TITLE, 0);
      title.setFont(JaveGlobalRessources.FONT_BOLD);
      return title;
   }

   private static JComponent createDetailsPanel() {
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("JavE Homepage:"), GridDialogLayoutData.RIGHT);
      panel.add(new GHyperlink("http://www.jave.de", 0));
      panel.add(new JLabel("Version:"), GridDialogLayoutData.RIGHT);
      panel.add(createSelectableLabel(JaveVersion.getFullVersionNumber() + " " + JaveVersion.getBuildDate()));
      panel.add(new JLabel("Author:"), GridDialogLayoutData.RIGHT);
      panel.add(createSelectableLabel("Markus Gebhard"));
      panel.add(new JLabel("Author E-mail:"), GridDialogLayoutData.RIGHT);
      panel.add(new GHyperlink("mailto:markus@jave.de?subject=jave", "markus@jave.de", 0));
      GridDialogLayoutData twoColumnsLayoutData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      twoColumnsLayoutData.setHorizontalSpan(2);
      panel.add(new HorizontalLine(), twoColumnsLayoutData);
      panel.add(new JLabel("Operating System:"), GridDialogLayoutData.RIGHT);
      panel.add(createSelectableLabel(getOptionalSystemProperty("os.name")));
      panel.add(new JLabel("Java Vendor:"), GridDialogLayoutData.RIGHT);
      panel.add(createSelectableLabel(getOptionalSystemProperty("java.vendor")));
      panel.add(new JLabel("Java Version:"), GridDialogLayoutData.RIGHT);
      panel.add(createSelectableLabel(getOptionalSystemProperty("java.version")));
      panel.add(new JLabel("Codebase:"), GridDialogLayoutData.RIGHT);
      panel.add(createSelectableLabel(JaveGlobalRessources.codeBase.getAbsolutePath()));
      JPanel detailsPanel = new JPanel(new CenterLayout());
      detailsPanel.add(panel);
      return detailsPanel;
   }

   private static String getOptionalSystemProperty(String propertyName) {
      try {
         String value = System.getProperty(propertyName);
         return value == null ? "n/a" : value;
      } catch (Exception var2) {
         return "n/a";
      }
   }

   private static JComponent createSelectableLabel(String text) {
      return new AutoWrappingLabel(text, 150).getContent();
   }
}
