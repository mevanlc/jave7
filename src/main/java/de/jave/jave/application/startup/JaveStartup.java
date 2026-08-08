package de.jave.jave.application.startup;

import de.jave.image2ascii.AsciiGreyscaleTableConfigurationInitializable;
import de.jave.jave.CharSetsConfigurationInitializable;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveConfigurationFileLoader;
import de.jave.jave.JaveMessages;
import de.jave.jave.actions.quickstart.QuickStartAction;
import de.jave.jave.algorithm.GeneralAlgorithmConfigurationInitializable;
import de.jave.jave.algorithm.gradient.AsciiGradientInitializable;
import de.jave.jave.algorithm.repair.AsciiRepairAlgorithmConfigurationInitializable;
import de.jave.jave.algorithm.replaceillegal.AsciiReplaceIllegalConfigurationInitializable;
import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJaveInitializable;
import de.jave.jave.configuration.MessageDialogConfigurationFileErrorHandler;
import de.jave.jave.figlet.FigletInitializable;
import de.jave.jave.filter.FilterInitializable;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pattern.PatternListInitializable;
import de.jave.jave.pixelplate.PixelPlateConfigurationInitializable;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.CharacterMergeRulesConfigurationInitializable;
import de.jave.maxosx.MacOsXInitializer;
import net.dizzy.commons.swing.dialog.core.DialogDefaults;

final class JaveStartup {
   public void start() throws JaveStartupException {
      JavEApplication jave = null;

      try {
         DialogDefaults.getInstance().setFrameIconImages(JaveIcons.JAVE_ICON_IMAGES);
         ConfigurationList configurationList = initConfigFiles();
         jave = new JavEApplication(configurationList);
         jave.setTool(jave.getApplicationPreferences().getStartupToolIndex());
         boolean recovered = jave.startupRecovery();
         jave.startupMenuBar();
         jave.startupFinish2();
         MacOsXInitializer.initializeApplicationCallbacks(jave.getMaxOsXApplicationCallbacks());
         jave.startupFinish3();
         jave.getMainPanel().requestFocus();
         jave.getFrame().toFront();
         if (!recovered) {
            JaveApplicationPreferences applicationPreferences = jave.getApplicationPreferences();
            boolean showQuickStartOnStartup = applicationPreferences.isShowQuickStartOnStartup();
            if (showQuickStartOnStartup && !JavEApplication.isDumpModeActive()) {
               QuickStartAction quickStartAction = new QuickStartAction(jave);
               quickStartAction.performQuickStart(jave.getFrame());
            }

            if (jave.getDocumentManager().getSize() == 0) {
               jave.doNew();
            }
         }
      } catch (JaveStartupException exception) {
         throw exception;
      } catch (Exception exception) {
         if (jave != null) {
            jave.dispose();
         }

         throw new JaveStartupException(JaveMessages.StartUp_ErrorOccured, exception);
      }
   }

   private static ConfigurationList initConfigFiles() throws JaveStartupException {
      MessageDialogConfigurationFileErrorHandler errorHandler = new MessageDialogConfigurationFileErrorHandler(null);
      JaveConfigurationFileLoader loader = new JaveConfigurationFileLoader(errorHandler);

      try {
         loader.checkConfigurationFolderExistant();
      } catch (ConfigurationException exception) {
         throw new JaveStartupException(JaveMessages.StartUp_ErrorMessage_ProbablyConfigFoldersMissing, exception);
      }

      ConfigurationList configurationList = new ConfigurationList();
      IJaveInitializable<?>[] initializables = new IJaveInitializable[]{
         new CharSetsConfigurationInitializable(),
         new PixelPlateConfigurationInitializable(),
         new CharacterMergeRulesConfigurationInitializable(),
         new GeneralAlgorithmConfigurationInitializable(),
         new AsciiGradientInitializable(),
         new AsciiGreyscaleTableConfigurationInitializable(),
         new AsciiReplaceIllegalConfigurationInitializable(),
         new AsciiRepairAlgorithmConfigurationInitializable(),
         new FigletInitializable(),
         new PatternListInitializable(),
         new FilterInitializable()
      };

      for (IJaveInitializable<?> initializable : initializables) {
         Object configuration = loader.initConfigFile(initializable);
         configurationList.add(configuration);
      }

      return configurationList;
   }

   private static class JaveStartupException extends Exception {
      JaveStartupException(String message, Throwable cause) {
         super(message, cause);
      }
   }
}
