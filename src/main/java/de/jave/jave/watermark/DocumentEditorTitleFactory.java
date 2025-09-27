package de.jave.jave.plate;

import java.io.File;

public class DocumentEditorTitleFactory {
   public static final String createShortEditorTitle(IDocumentEditor editor) {
      File file = editor.getFile();
      return file == null ? editor.getStopGapName() : file.getName();
   }

   public static final String createEditorTabTitle(IDocumentEditor editor) {
      String title = createShortEditorTitle(editor);
      return editor.isModified() ? "*" + title : title;
   }

   public static final String createEditorFrameTitle(IDocumentEditor editor) {
      File file = editor.getFile();
      StringBuffer title = new StringBuffer("[");
      if (file != null) {
         title.append(file);
      } else {
         title.append(editor.getStopGapName());
      }

      if (editor.isModified()) {
         title.append(" *");
      }

      title.append("]");
      return title.toString();
   }
}
