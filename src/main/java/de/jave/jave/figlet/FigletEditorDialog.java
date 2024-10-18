package de.jave.jave.figlet;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.swing.application.JFigletEditor;
import de.jave.figlet.swing.preferences.JFigletPreferences;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.JaveGlobalRessources;
import de.jave.lib.gui.GuiUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;

public class FigletEditorDialog {
   private final JDialog dialog;
   private final JFigletEditor editor;
   private boolean isCanceled = false;

   public FigletEditorDialog(Component parentComponent, JFigletPreferences preferences, FontModel displayFontModel, IFigDriver figDriver) {
      Ensure.ensureArgumentNotNull(figDriver);
      this.editor = new JFigletEditor(figDriver, displayFontModel, preferences, JaveGlobalRessources.TMP_FOLDER);
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      buttonPanelBuilder.add(new SmartAction("&OK") {
         @Override
         protected void execute(Component parent) {
            FigletEditorDialog.this.performOk();
         }
      });
      buttonPanelBuilder.add(new SmartAction("&Cancel") {
         @Override
         protected void execute(Component parent) {
            FigletEditorDialog.this.performCancel();
         }
      });
      this.dialog = JDialogFactory.createJDialog(parentComponent, "FIGlet Editor", true);
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            FigletEditorDialog.this.performCancel();
         }
      });
      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(this.editor.getContent(), "Center");
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      this.dialog.pack();
      GuiUtilities.centerToParent(this.dialog);
   }

   private void performOk() {
      this.dialog.dispose();
   }

   private void performCancel() {
      this.isCanceled = true;
      this.dialog.dispose();
   }

   public void show() {
      this.isCanceled = false;
      this.editor.requestFocus();
      this.dialog.setVisible(true);
   }

   public String getResultText() {
      return this.editor.getResultText();
   }

   public boolean isCanceled() {
      return this.isCanceled;
   }
}
