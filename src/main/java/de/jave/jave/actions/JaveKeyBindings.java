package de.jave.jave.actions;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 * Central registry for JavE keyboard shortcuts.
 *
 * The primary modifier is the platform menu-shortcut mask (Cmd on macOS,
 * Ctrl on Windows/Linux), so the same constants render correctly in menus
 * and fire on whichever modifier the host OS expects.
 */
public final class JaveKeyBindings {
   private static final int MENU = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
   private static final int MENU_SHIFT = MENU | InputEvent.SHIFT_DOWN_MASK;
   private static final int CTRL = InputEvent.CTRL_DOWN_MASK;
   private static final int CTRL_SHIFT = CTRL | InputEvent.SHIFT_DOWN_MASK;

   public static final KeyStroke NEW_DOCUMENT = KeyStroke.getKeyStroke(KeyEvent.VK_N, MENU);
   public static final KeyStroke OPEN = KeyStroke.getKeyStroke(KeyEvent.VK_O, MENU);
   public static final KeyStroke BROWSE = KeyStroke.getKeyStroke(KeyEvent.VK_B, MENU);
   public static final KeyStroke CLOSE = KeyStroke.getKeyStroke(KeyEvent.VK_W, MENU);
   public static final KeyStroke SAVE = KeyStroke.getKeyStroke(KeyEvent.VK_S, MENU);
   public static final KeyStroke SAVE_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_S, MENU_SHIFT);

   public static final KeyStroke UNDO = KeyStroke.getKeyStroke(KeyEvent.VK_Z, MENU);
   public static final KeyStroke REDO = KeyStroke.getKeyStroke(KeyEvent.VK_Y, MENU);
   public static final KeyStroke CUT = KeyStroke.getKeyStroke(KeyEvent.VK_X, MENU);
   public static final KeyStroke COPY = KeyStroke.getKeyStroke(KeyEvent.VK_C, MENU);
   public static final KeyStroke PASTE_AS_NEW_SELECTION = KeyStroke.getKeyStroke(KeyEvent.VK_V, MENU);
   public static final KeyStroke PASTE_INTO_SELECTION = KeyStroke.getKeyStroke(KeyEvent.VK_V, MENU_SHIFT);
   public static final KeyStroke SELECT_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_A, MENU);
   public static final KeyStroke REPLACE = KeyStroke.getKeyStroke(KeyEvent.VK_H, MENU);
   public static final KeyStroke CROP = KeyStroke.getKeyStroke(KeyEvent.VK_R, MENU);

   public static final KeyStroke TOGGLE_GRID = KeyStroke.getKeyStroke(KeyEvent.VK_G, MENU);

   public static final KeyStroke ZOOM_IN = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, MENU);
   public static final KeyStroke ZOOM_OUT = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, MENU);

   public static final KeyStroke PREFERENCES = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, MENU);
   public static final KeyStroke EXIT = KeyStroke.getKeyStroke(KeyEvent.VK_Q, CTRL);
   public static final KeyStroke UNICODE_PICKER = KeyStroke.getKeyStroke(KeyEvent.VK_U, CTRL);
   public static final KeyStroke BOX_DRAWING_PICKER = KeyStroke.getKeyStroke(KeyEvent.VK_U, CTRL_SHIFT);

   public static final KeyStroke SELECT_TOOL = KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL);
   public static final KeyStroke FREEHAND_SELECT_TOOL = KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL_SHIFT);
   public static final KeyStroke TEXT_TOOL = KeyStroke.getKeyStroke(KeyEvent.VK_T, CTRL);

   private JaveKeyBindings() {}
}
