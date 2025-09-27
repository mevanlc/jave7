package de.jave.jave.actions;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.swing.preferences.JFigletPreferences;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.figlet.FigletEditorDialog;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.CharacterPlate;
import de.jave.preferences.JavePreferences;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;

public class ShowFigletEditorAction extends AbstractJaveAction {
   private final JavEApplication jave;
   private final JavePreferences javePreferences;
   private FigletEditorDialog figletDialog;
   private final IFigDriver figDriver;

   public ShowFigletEditorAction(IFigDriver figDriver, JavEApplication jave, JaveMainPanel mainPanel, JavePreferences javePreferences) {
      super(mainPanel, "FIGlet Editor", FigletIcons.FIGLET_ICON);
      Ensure.ensureArgumentNotNull(figDriver);
      Ensure.ensureArgumentNotNull(javePreferences);
      this.figDriver = figDriver;
      this.javePreferences = javePreferences;
      this.setToolTipText("Show FIGlet Editor");
      this.jave = jave;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.showFigletEditor(parentComponent);
   }

   public void showFigletEditor(Component parentComponent) {
      if (this.figletDialog == null) {
         JaveApplicationPreferences applicationPreferences = this.jave.getApplicationPreferences();
         JFigletPreferences figletPreferences = new JFigletPreferences(this.javePreferences);
         this.figletDialog = new FigletEditorDialog(parentComponent, figletPreferences, applicationPreferences.getDisplayFontModel(), this.figDriver);
      }

      this.figletDialog.show();
      if (!this.figletDialog.isCanceled()) {
         String text = this.figletDialog.getResultText();
         this.jave.pasteAsNewSelection(new CharacterPlate(text));
      }
   }
}
