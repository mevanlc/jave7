package de.jave.figlet.swing.action;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.swing.fontchooser.FontListView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import net.dizzy.commons.swing.action.SmartAction;

public class FontSelectionActions {
   private final List<Action> actions = new ArrayList<>();
   private final SmartAction infoAction;
   private final FontListView view;

   public FontSelectionActions(final FontListView view, IFigDriver figDriver) {
      this.view = view;
      this.infoAction = new FontInfoAction(figDriver) {
         @Override
         protected String getSelectedFont() {
            return view.getSelectedFont();
         }
      };
      this.add(this.infoAction);
   }

   public JPopupMenu getPopupMenu() {
      if (this.actions.size() == 0) {
         return null;
      } else {
         JPopupMenu menu = new JPopupMenu();

         for (int i = 0; i < this.actions.size(); i++) {
            menu.add(this.actions.get(i));
         }

         return menu;
      }
   }

   public void updateEnabled() {
      this.infoAction.setEnabled(this.view.getSelectedFont() != null);
   }

   public void add(Action action) {
      this.actions.add(action);
   }

   public SmartAction getFontInfoAction() {
      return this.infoAction;
   }
}
