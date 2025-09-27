package de.jave.jave.actions;

import de.jave.asciimation.export.FileTypeIcons;
import de.jave.jave.JavEApplication;
import de.jave.jave.preferences.ColorScheme;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Dimension;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public final class NewAnimationAction extends SmartAction {
   private final JavEApplication jave;

   public NewAnimationAction(JavEApplication jave) {
      super("New Animation", FileTypeIcons.ANIMATION_ICON);
      Ensure.ensureArgumentNotNull(jave);
      this.jave = jave;
      this.setToolTipText("Create a new Animation");
   }

   @Override
   protected void execute(Component parentComponent) {
      this.doCreateNewAnimation();
   }

   public void doCreateNewAnimation() {
      ColorScheme colorScheme = this.jave.getApplicationPreferences().getDefaultColorSchemeModel().getValue();
      JaveAnimationFile animationFile = new JaveAnimationFile();
      animationFile.getProperties().setForegroundColor(colorScheme.getColorText());
      animationFile.getProperties().setBackgroundColor(colorScheme.getColorPlateBackground());
      JaveAnimationFrame animationFrame = new JaveAnimationFrame();
      Dimension size = this.jave.getApplicationPreferences().getDefaultAnimationSize();
      animationFrame.setContent(new CharacterPlate(size));
      animationFile.add(animationFrame);
      this.jave.openJaveAnimation(animationFile);
   }
}
