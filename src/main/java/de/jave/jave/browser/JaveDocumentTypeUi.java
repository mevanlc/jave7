package de.jave.jave.browser;

import de.jave.asciimation.export.FileTypeIcons;
import de.jave.jave.icon.JaveIcons;
import javax.swing.Icon;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class JaveDocumentTypeUi extends AbstractObjectUi<JaveDocumentType> {
   public Icon getIcon(JaveDocumentType documentType) {
      return documentType.accept(new IJaveDocumentTypeVisitor<Icon>() {
         public Icon visitText(JaveDocumentType type) {
            return FileTypeIcons.TEXT_ICON;
         }

         public Icon visitAnimation(JaveDocumentType type) {
            return FileTypeIcons.ANIMATION_ICON;
         }

         public Icon visitGame(JaveDocumentType type) {
            return JaveIcons.ASCTRIS;
         }
      });
   }
}
