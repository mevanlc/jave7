package net.disy.commons.swing.dialog.core.internal;

import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IGenericDialogConfiguration;
import net.disy.commons.swing.dialog.core.preferences.IDialogPreferences;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;

public abstract class AbstractGenericDialogConfiguration implements IGenericDialogConfiguration {
   private boolean headerPanelVisible = true;
   private final IDialogButtonConfiguration buttonConfiguration;
   private final IDialogPreferences preference;

   public AbstractGenericDialogConfiguration(IDialogButtonConfiguration buttonConfiguration) {
      this(buttonConfiguration, null);
   }

   public AbstractGenericDialogConfiguration(IDialogButtonConfiguration buttonConfiguration, IDialogPreferences preference) {
      Ensure.ensureArgumentNotNull(buttonConfiguration);
      this.buttonConfiguration = buttonConfiguration;
      this.preference = preference;
   }

   @Override
   public final IDialogButtonConfiguration getButtonConfiguration() {
      return this.buttonConfiguration;
   }

   @Deprecated
   @Override
   public boolean isHeaderPanelVisible() {
      return this.headerPanelVisible;
   }

   @Deprecated
   public void setHeaderPanelVisible(boolean headerPanelVisible) {
      this.headerPanelVisible = headerPanelVisible;
   }

   @Deprecated
   @Override
   public Icon getLargeDialogIcon() {
      return null;
   }

   @Override
   public IDialogPreferences getPreferences() {
      return this.preference;
   }

   @Override
   public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
      return new IDialogHeaderPanelConfiguration() {
         @Override
         public boolean isHeaderPanelVisible() {
            return AbstractGenericDialogConfiguration.this.isHeaderPanelVisible();
         }

         @Override
         public Icon getLargeDialogIcon() {
            return AbstractGenericDialogConfiguration.this.getLargeDialogIcon();
         }
      };
   }
}
