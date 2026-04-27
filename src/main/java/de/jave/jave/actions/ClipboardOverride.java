package de.jave.jave.actions;

/**
 * Per-component clipboard handler installed via the JComponent client
 * property {@link #CLIENT_PROPERTY}. {@link FocusedTextClipboardDelegate}
 * walks from the focus owner up the component hierarchy looking for one
 * of these and dispatches the matching op when it finds it. Lets a
 * non-text widget (e.g. the Fill tool's pattern preview) receive
 * Cmd/Ctrl+X/C/V instead of the canvas.
 */
public interface ClipboardOverride {
   String CLIENT_PROPERTY = "jave.clipboardOverride";

   default void cut() {}

   default void copy() {}

   default void paste() {}
}
