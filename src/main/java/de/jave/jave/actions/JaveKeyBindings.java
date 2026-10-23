package de.jave.jave.actions;

import de.jave.maxosx.MacOsXInitializer;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 * Central registry for JavE keyboard shortcuts.
 *
 * CMD_OR_CTRL selects Command on macOS and Control on Windows/Linux for
 * command shortcuts. Swing menu mnemonics use their own platform-specific
 * modifiers (Ctrl+Option on macOS).
 */
public final class JaveKeyBindings {
   private static final int CMD_OR_CTRL = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
   private static final int CMD_OR_CTRL_SHIFT = CMD_OR_CTRL | InputEvent.SHIFT_DOWN_MASK;
   private static final int CTRL = InputEvent.CTRL_DOWN_MASK;
   private static final int CTRL_SHIFT = CTRL | InputEvent.SHIFT_DOWN_MASK;

   public static final KeyStroke NEW_DOCUMENT = KeyStroke.getKeyStroke(KeyEvent.VK_N, CMD_OR_CTRL);
   public static final KeyStroke OPEN = KeyStroke.getKeyStroke(KeyEvent.VK_O, CMD_OR_CTRL);
   public static final KeyStroke BROWSE = KeyStroke.getKeyStroke(KeyEvent.VK_B, CMD_OR_CTRL);
   public static final KeyStroke CLOSE = KeyStroke.getKeyStroke(KeyEvent.VK_W, CMD_OR_CTRL);
   public static final KeyStroke SAVE = KeyStroke.getKeyStroke(KeyEvent.VK_S, CMD_OR_CTRL);
   public static final KeyStroke SAVE_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_S, CMD_OR_CTRL_SHIFT);

   public static final KeyStroke UNDO = KeyStroke.getKeyStroke(KeyEvent.VK_Z, CMD_OR_CTRL);
   public static final KeyStroke REDO = KeyStroke.getKeyStroke(KeyEvent.VK_Y, CMD_OR_CTRL);
   public static final KeyStroke CUT = KeyStroke.getKeyStroke(KeyEvent.VK_X, CMD_OR_CTRL);
   public static final KeyStroke COPY = KeyStroke.getKeyStroke(KeyEvent.VK_C, CMD_OR_CTRL);
   public static final KeyStroke PASTE_AS_NEW_SELECTION = KeyStroke.getKeyStroke(KeyEvent.VK_V, CMD_OR_CTRL);
   public static final KeyStroke PASTE_INTO_SELECTION = KeyStroke.getKeyStroke(KeyEvent.VK_V, CMD_OR_CTRL_SHIFT);
   public static final KeyStroke SELECT_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_A, CMD_OR_CTRL);
   public static final KeyStroke SELECT_CONNECTED = KeyStroke.getKeyStroke(KeyEvent.VK_A, CMD_OR_CTRL_SHIFT);
   public static final KeyStroke REPLACE = ReplaceKeyBinding.create(MacOsXInitializer.isMacOs(), CMD_OR_CTRL);
   public static final KeyStroke CROP = KeyStroke.getKeyStroke(KeyEvent.VK_R, CMD_OR_CTRL);

   public static final KeyStroke TOGGLE_GRID = KeyStroke.getKeyStroke(KeyEvent.VK_G, CMD_OR_CTRL);

   public static final KeyStroke ZOOM_IN = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, CMD_OR_CTRL);
   public static final KeyStroke ZOOM_OUT = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, CMD_OR_CTRL);
   public static final KeyStroke RESET_ZOOM = KeyStroke.getKeyStroke(KeyEvent.VK_0, CMD_OR_CTRL);

   public static final KeyStroke SEARCH_MENUS = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, CMD_OR_CTRL_SHIFT);

   public static final KeyStroke PREFERENCES = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, CMD_OR_CTRL);
   public static final KeyStroke EXIT = KeyStroke.getKeyStroke(KeyEvent.VK_Q, CTRL);
   public static final KeyStroke UNICODE_PICKER = KeyStroke.getKeyStroke(KeyEvent.VK_U, CTRL);
   public static final KeyStroke BOX_DRAWING_PICKER = KeyStroke.getKeyStroke(KeyEvent.VK_U, CTRL_SHIFT);

   public static final KeyStroke SELECT_TOOL = KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL);
   public static final KeyStroke FREEHAND_SELECT_TOOL = KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL_SHIFT);
   public static final KeyStroke TEXT_TOOL = KeyStroke.getKeyStroke(KeyEvent.VK_T, CTRL);

   private JaveKeyBindings() {}
}
