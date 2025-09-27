package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import java.awt.Font;
import net.disy.commons.core.text.font.FontDescription;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.builder.DialogConfigurationBuilder;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.font.FontFactory;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class FontChooserDialog {
   private final Component parentComponent;
   private final FontModel fontModel;
   private final FontChooserPanel fontChooserPanel;
   private boolean canceled;

   public FontChooserDialog(Component parentComponent, Font font) {
      this(parentComponent, FontFactory.createFontDescription(font));
   }

   public FontChooserDialog(Component parentComponent, FontDescription fontDescription) {
      this(parentComponent, new FontModel(fontDescription));
   }

   public FontChooserDialog(Component parentComponent) {
      this(parentComponent, new FontModel());
   }

   public FontChooserDialog(Component parentComponent, FontModel fontModel) {
      this.fontModel = fontModel;
      this.parentComponent = parentComponent;
      this.fontChooserPanel = new FontChooserPanel(fontModel);
   }

   public FontChooserPanel getFontChooserPanel() {
      return this.fontChooserPanel;
   }

   public IDialogResult show() {
      UserDialog dialog = this.createDialog();
      IDialogResult result = dialog.show();
      this.canceled = result.isCanceled();
      return result;
   }

   public IDialogResult show(String title) {
      UserDialog dialog = this.createDialog(title);
      IDialogResult result = dialog.show();
      this.canceled = result.isCanceled();
      return result;
   }

   public UserDialog createDialog() {
      return this.createDialog(DisyCommonsSwingFontChooserMessages.getString("FontChooserDialog.title"));
   }

   private UserDialog createDialog(String title) {
      IDialogPage page = new FontChooserDialogPage(this.fontChooserPanel, title);
      return new UserDialog(this.parentComponent, new DialogConfigurationBuilder().invisibleHeaderPanel().create(page));
   }

   public FontModel getFontModel() {
      return this.fontModel;
   }

   public Font getFont() {
      return this.getFontModel().getFont();
   }

   @Deprecated
   public final boolean isCanceled() {
      return this.canceled;
   }

   public void setFont(FontDescription fontDescription) {
      this.fontModel.setFont(fontDescription);
   }

   public void setFont(Font font) {
      this.fontModel.setFont(font);
   }
}
