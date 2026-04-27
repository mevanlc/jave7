package de.jave.jave.actions.preferences;

import de.jave.jave.actions.JaveKeyBindings;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.layout.cardlayout.CardPanel;
import net.disy.commons.swing.layout.cardlayout.CardPanelKey;
import net.disy.commons.swing.list.ListSelectionMode;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public final class JavePreferencesAction extends SmartAction {
   private final JaveApplicationPreferences preferences;
   private final PlatePreferences platePreferences;

   public JavePreferencesAction(JaveApplicationPreferences preferences, PlatePreferences platePreferences) {
      super("Preferences...");
      Ensure.ensureArgumentNotNull(preferences);
      Ensure.ensureArgumentNotNull(platePreferences);
      this.preferences = preferences;
      this.platePreferences = platePreferences;
      this.setAcceleratorKey(JaveKeyBindings.PREFERENCES);
   }

   @Override
   protected void execute(Component parentComponent) {
      performShowPreferencesDialog(parentComponent, this.preferences, this.platePreferences);
   }

   public static void performShowPreferencesDialog(Component parent, JaveApplicationPreferences preferences, PlatePreferences platePreferences) {
      final IJavePreferencesPanel[] panels = new IJavePreferencesPanel[]{
         new GeneralPreferencesPanel(preferences),
         new AuthorPreferencesPanel(preferences),
         new DisplayPreferencesPanel(platePreferences),
         new TextEditorPreferencesPanel(preferences, platePreferences),
         new AnimationEditorPreferencesPanel(preferences, platePreferences),
         new TextToolPreferencesPanel(preferences),
         new SelectionPreferencesPanel(preferences),
         new AdvancedPreferencesPanel(preferences)
      };
      AbstractDialogPage dialogPage = new AbstractDialogPage("") {
         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            final JList list = new JList<>(panels);
            list.setSelectionMode(ListSelectionMode.SINGLE_SELECTION.getListSelectionMode());
            list.setSelectedIndex(0);
            list.setCellRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<IJavePreferencesPanel>() {
               public String getLabel(IJavePreferencesPanel value) {
                  return value.getTitle();
               }
            }));
            JScrollPane leftPanel = new JScrollPane(list);
            final CardPanel cardPanel = new CardPanel();

            for (IJavePreferencesPanel panel : panels) {
               cardPanel.add(new PreferencesDetailsPanel(panel).getContent(), new CardPanelKey(panel.getClass().getName()));
            }

            list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                  int index = list.getSelectedIndex();
                  if (index != -1) {
                     IJavePreferencesPanel panel = panels[index];
                     cardPanel.setSelectedSubPanel(new CardPanelKey(panel.getClass().getName()));
                  }
               }
            });
            JSplitPane splitPane = new JSplitPane(1, leftPanel, cardPanel.getContent());
            splitPane.setBorder(null);
            return splitPane;
         }

         @Override
         public String getTitle() {
            return "Preferences";
         }
      };
      UserDialog dialog = new UserDialog(parent, new DefaultDialogConfiguration<IDialogPage>(dialogPage) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }
      });
      IDialogResult result = dialog.show();
      if (!result.isCanceled()) {
         for (IJavePreferencesPanel panel : panels) {
            panel.savePreferences();
         }

         preferences.flush();
         platePreferences.flush();
      } else {
         for (IJavePreferencesPanel panel : panels) {
            panel.revert();
         }
      }
   }
}
