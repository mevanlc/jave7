package net.disy.commons.swing.dialog.core;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.preferences.IDialogPreferences;
import net.disy.commons.swing.dialog.userdialog.IDialogCloseHandler;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IGridDialogLayoutData;
import net.disy.commons.swing.util.GuiUtilities;
import net.disy.commons.swing.widgets.HorizontalLine;

public abstract class AbstractDialog {
   private static final String INITIAL_DIALOG_TITLE = "!Dialog.title!";
   private final WindowAdapter cancelingWindowListener = new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
         Window parentComponent = GuiUtilities.getWindowFor(e);
         AbstractDialog.this.performCancel(parentComponent);
      }
   };
   private final ISwingFrameOrDialog dialog;
   private final IGenericDialogConfiguration dialogConfiguration;
   private final Component parent;
   private boolean canceled = false;
   private final DialogPagePanel dialogPagePanel;
   private IDialogCloseHandler closeHandler = IDialogCloseHandler.NULL_HANDLER;

   public AbstractDialog(Component parent, IGenericDialogConfiguration dialogConfiguration) {
      Ensure.ensureArgumentNotNull(dialogConfiguration);
      this.parent = parent;
      this.dialogConfiguration = dialogConfiguration;
      this.dialogPagePanel = new DialogPagePanel(dialogConfiguration.getHeaderPanelConfiguration());
      this.dialog = createFrameOrDialog(parent);
      this.dialog.setModal(true);
      this.dialog.getContentPane().setLayout(new GridDialogLayout(1, true, 0, 0));
      this.dialog.setDefaultCloseOperation(0);
      this.dialog.addWindowListener(this.cancelingWindowListener);
      this.adjustToPreferences(this.dialog, dialogConfiguration.getPreferences());
   }

   private void adjustToPreferences(ISwingFrameOrDialog dialog, IDialogPreferences preferences) {
      if (preferences != null) {
         Rectangle bounds = preferences.getBounds();
         if (bounds != null) {
            dialog.getWindow().setBounds(bounds);
         }
      }
   }

   private void storePereferences(ISwingFrameOrDialog dialog, IDialogPreferences preferences) {
      if (preferences != null) {
         preferences.setBounds(dialog.getWindow().getBounds());
      }
   }

   protected boolean isMainContentGrabVerticalSpace() {
      return true;
   }

   private static ISwingFrameOrDialog createFrameOrDialog(Component parent) {
      Window window = GuiUtilities.getWindowFor(parent);
      if (window != null && window.isVisible()) {
         return new SwingDialog(GuiUtilities.createDialog(parent, "!Dialog.title!"));
      } else {
         JFrame frame = new JFrame("!Dialog.title!");
         if (window != null) {
            List<Image> originalIconImages = window.getIconImages();
            if (!originalIconImages.isEmpty()) {
               frame.setIconImages(originalIconImages);
            } else {
               frame.setIconImages(DialogDefaults.getInstance().getFrameIconImages());
            }
         }

         DialogDefaults dialogDefaults = DialogDefaults.getInstance();
         if (frame.getIconImages().isEmpty()) {
            frame.setIconImages(dialogDefaults.getFrameIconImages());
         }

         return new SwingFrame(frame);
      }
   }

   public final Component getParent() {
      return this.parent;
   }

   public final void performCancel(Component parentComponent) {
      boolean success = this.cancelPressed(parentComponent);
      if (success) {
         this.canceled = true;
         this.closeDialog();
         this.closeHandler.handleDialogClose(new DialogResult(true));
      }
   }

   @Deprecated
   public final boolean isCanceled() {
      return this.canceled;
   }

   protected final IGenericDialogConfiguration getGenericDialog() {
      return this.dialogConfiguration;
   }

   protected final void initializeContent() {
      IGridDialogLayoutData mainContentLayoutData = this.isMainContentGrabVerticalSpace()
         ? GridDialogLayoutData.FILL_BOTH
         : GridDialogLayoutData.FILL_HORIZONTAL;
      this.dialog.getContentPane().add(this.dialogPagePanel.createPanel(), mainContentLayoutData);
      this.dialog.getContentPane().add(new HorizontalLine(), GridDialogLayoutData.FILL_HORIZONTAL);
      this.dialog.getContentPane().add(this.createButtonBar(), GridDialogLayoutData.FILL_HORIZONTAL);
      JComponent belowButtonsPanel = this.createOptionalBelowButtonsPanel();
      if (belowButtonsPanel != null) {
         this.dialog.getContentPane().add(belowButtonsPanel, GridDialogLayoutData.FILL_BOTH);
      }
   }

   protected JComponent createOptionalBelowButtonsPanel() {
      return null;
   }

   protected abstract JComponent createButtonBar();

   protected abstract boolean cancelPressed(Component var1);

   protected void closeDialog() {
      this.storePereferences(this.dialog, this.dialogConfiguration.getPreferences());
      this.dialog.dispose();
      this.dialog.removeWindowListener(this.cancelingWindowListener);
   }

   public final void updateSize() {
      if (this.getContent() == null) {
         this.dialog.pack();
      } else {
         Dimension preferredSize = this.dialogPagePanel.getPreferredSize();
         Dimension actualSize = this.dialogPagePanel.getSize();
         if (preferredSize.width > actualSize.width || preferredSize.height > actualSize.height) {
            GuiUtilities.repack(this.dialog.getWindow());
         }
      }
   }

   protected final void setContent(JComponent content) {
      this.dialogPagePanel.setContent(content);
   }

   protected final JComponent getContent() {
      return this.dialogPagePanel.getContent();
   }

   protected final void setMessage(IBasicMessage message) {
      this.dialogPagePanel.setMessage(message);
      this.updateSize();
   }

   protected final void setDescription(String description) {
      this.dialogPagePanel.setDescription(description);
   }

   protected final void setTitle(String title) {
      this.dialog.setTitle(title);
   }

   protected final void setDefaultButton(JButton button) {
      this.dialog.getRootPane().setDefaultButton(button);
   }

   public final ISwingFrameOrDialog getDialog() {
      return this.dialog;
   }

   protected final void setCloseHandler(IDialogCloseHandler dialogCloseHandler) {
      Ensure.ensureArgumentNotNull(dialogCloseHandler);
      this.closeHandler = dialogCloseHandler;
   }

   public IDialogCloseHandler getCloseHandler() {
      return this.closeHandler;
   }
}
