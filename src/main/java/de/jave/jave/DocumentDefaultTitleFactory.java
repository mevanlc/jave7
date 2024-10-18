package de.jave.jave;

import de.jave.core.NLS;
import java.io.File;

public class DocumentDefaultTitleFactory {
   private static int counter = 0;

   public static String createDefaultDocumentTitle() {
      return createDefaultDocumentTitle((String)null);
   }

   public static String createDefaultDocumentTitle(String optionalName) {
      if (optionalName != null) {
         return optionalName;
      } else {
         counter++;
         return NLS.bind(JaveMessages.Editor_DefaultDocumentTitle, counter);
      }
   }

   public static String createDefaultDocumentTitle(File optionalFile) {
      return createDefaultDocumentTitle(optionalFile == null ? null : optionalFile.getName());
   }
}
