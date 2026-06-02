package de.jave.jave.tool.text;

import de.jave.jave.JaveMessages;
import de.jave.jave.icon.JaveIcons;
import javax.swing.Icon;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class CursorMovementUi extends AbstractObjectUi<CursorMovement> {
   public Icon getIcon(CursorMovement value) {
      switch (value) {
         case DIRECTED:
            return JaveIcons.CURSORMOVE_DIRECTED;
         case NONE:
            return JaveIcons.CURSORMOVE_NONE;
         case NORMAL:
            return JaveIcons.CURSORMOVE_NORMAL;
         case TRACK_FOLLOWING:
            return JaveIcons.CURSORMOVE_TRACK_FOLLOWING;
         default:
            throw new IllegalArgumentException();
      }
   }

   public String getLabel(CursorMovement value) {
      switch (value) {
         case DIRECTED:
            return JaveMessages.Tool_Text_CursorMovement_Directed;
         case NONE:
            return JaveMessages.Tool_Text_CursorMovement_None;
         case NORMAL:
            return JaveMessages.Tool_Text_CursorMovement_Normal;
         case TRACK_FOLLOWING:
            return JaveMessages.Tool_Text_CursorMovement_TrackFollowing;
         default:
            throw new IllegalArgumentException();
      }
   }
}
