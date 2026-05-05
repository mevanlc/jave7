package net.disy.commons.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.icon.IBaseIconProvider;
import net.disy.commons.swing.icon.IconImageIcon;
import net.disy.commons.swing.label.internal.MnemonicLabel;
import net.disy.commons.swing.label.internal.MnemonicLabelParser;
import net.disy.commons.swing.util.GuiUtilities;
import net.disy.commons.swing.util.IEnableable;

public abstract class AbstractDisyAction extends AbstractAction implements IEnableable {
   public static final String BASE_ICON = "BaseIcon";
   private Component explicitParentComponent;

   public AbstractDisyAction() {
      this(new ActionConfiguration());
   }

   public AbstractDisyAction(String name) {
      this(new ActionConfiguration(name));
   }

   public AbstractDisyAction(String name, Icon icon) {
      this(new ActionConfiguration(name, icon));
   }

   public AbstractDisyAction(Icon icon) {
      this(null, icon);
   }

   public AbstractDisyAction(IActionConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.setName(configuration.getName());
      this.setIcon(configuration.getIcon());
      this.setToolTipText(configuration.getToolTipText());
   }

   public IActionConfiguration getActionConfiguration() {
      return new ActionConfiguration(this.getName(), this.getIcon(), this.getToolTipText());
   }

   public void setExplicitParentComponent(Component explicitParentComponent) {
      this.explicitParentComponent = explicitParentComponent;
   }

   public void setName(String name) {
      if (name != null) {
         MnemonicLabel mnemonicLabel = MnemonicLabelParser.parse(name);
         this.putValue("Name", mnemonicLabel.getPlainText());
         if (mnemonicLabel.getMnemonicCharacter() != null) {
            this.setMnemonic(mnemonicLabel.getMnemonicCharacter());
         }
      } else {
         this.putValue("Name", name);
      }
   }

   protected Component getParentComponent(ActionEvent e) {
      return this.explicitParentComponent == null ? GuiUtilities.getWindowFor(e) : this.explicitParentComponent;
   }

   public final void setMnemonic(int keyCode) {
      this.putValue("MnemonicKey", keyCode);
   }

   public final void setMnemonic(char character) {
      char ch = Character.toUpperCase(character);
      if (!isLetter(ch) && !isDigit(ch)) {
         throw new IllegalArgumentException("Unsupported mnemonic character'" + character + "'.");
      } else {
         this.setMnemonic((int)ch);
      }
   }

   public final void setAcceleratorKey(KeyStroke keyStroke) {
      this.putValue("AcceleratorKey", keyStroke);
   }

   private static boolean isDigit(char ch) {
      return ch >= '0' && ch <= '9';
   }

   private static boolean isLetter(char ch) {
      return Character.isLetter(ch);
   }

   public final String getName() {
      return (String)this.getValue("Name");
   }

   public final void setIcon(Icon icon) {
      Icon baseIcon = getBaseIcon(icon);
      if (!(icon instanceof ImageIcon) && icon != null) {
         ImageIcon imageIcon = new IconImageIcon(icon);
         this.putValue("SmallIcon", imageIcon);
      } else {
         this.putValue("SmallIcon", icon);
      }
      this.putValue(BASE_ICON, baseIcon);
   }

   private static Icon getBaseIcon(Icon icon) {
      return icon instanceof IBaseIconProvider ? ((IBaseIconProvider)icon).getBaseIcon() : icon;
   }

   public final void setToolTipText(String shortDescription) {
      this.putValue("ShortDescription", shortDescription);
   }

   public final String getToolTipText() {
      return (String)this.getValue("ShortDescription");
   }

   public final Icon getIcon() {
      return (Icon)this.getValue("SmallIcon");
   }

   public final Icon getBaseIcon() {
      return (Icon)this.getValue(BASE_ICON);
   }
}
