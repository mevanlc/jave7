package de.jave.jave.actions;

import de.jave.jave.plate.ActiveEditorModel;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.preferences.ColorScheme;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class CurrentColorSchemeModel extends AbstractChangeableModel {
   private final ActiveEditorModel activeEditorModel;
   private IDocumentEditor currentActiveEditor;
   private final IChangeListener colorSchemeChangeListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         CurrentColorSchemeModel.this.fireChangeEvent();
      }
   };

   public CurrentColorSchemeModel(final ActiveEditorModel activeEditorModel) {
      Ensure.ensureArgumentNotNull(activeEditorModel);
      this.activeEditorModel = activeEditorModel;
      this.currentActiveEditor = activeEditorModel.getActiveEditor();
      if (this.currentActiveEditor != null) {
         ObjectModel<ColorScheme> colorSchemeModel = this.currentActiveEditor.getPlate().getColorSchemeModel();
         colorSchemeModel.addChangeListener(this.colorSchemeChangeListener);
      }

      activeEditorModel.addChangeListener(
         new IChangeListener() {
            @Override
            public void stateChanged() {
               if (CurrentColorSchemeModel.this.currentActiveEditor != null) {
                  CurrentColorSchemeModel.this.currentActiveEditor
                     .getPlate()
                     .getColorSchemeModel()
                     .removeChangeListener(CurrentColorSchemeModel.this.colorSchemeChangeListener);
               }

               CurrentColorSchemeModel.this.currentActiveEditor = activeEditorModel.getActiveEditor();
               if (CurrentColorSchemeModel.this.currentActiveEditor != null) {
                  ObjectModel<ColorScheme> colorSchemeModel = CurrentColorSchemeModel.this.currentActiveEditor.getPlate().getColorSchemeModel();
                  colorSchemeModel.addChangeListener(CurrentColorSchemeModel.this.colorSchemeChangeListener);
               }

               CurrentColorSchemeModel.this.fireChangeEvent();
            }
         }
      );
   }

   public ColorScheme getColorScheme() {
      IDocumentEditor editor = this.activeEditorModel.getActiveEditor();
      return editor == null ? null : editor.getPlate().getDocument().getColorScheme();
   }
}
