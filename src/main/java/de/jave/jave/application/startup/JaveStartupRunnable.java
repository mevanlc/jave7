package de.jave.jave.application.startup;

import de.jave.gui.splash.IStartupMonitor;
import de.jave.gui.splash.IStartupRunnable;
import de.jave.gui.splash.StartupException;
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
import java.awt.Component;
import net.disy.commons.swing.dialog.core.DialogDefaults;

public class JaveStartupRunnable implements IStartupRunnable {
   public JaveStartupRunnable(String[] arguments) {
   }

   @Override
   public void startUp(IStartupMonitor monitor) throws StartupException {
      JavEApplication jave = null;

      try {
         monitor.beginTask(JaveMessages.StartUp_Task_Starting, -1);
         DialogDefaults.getInstance().setFrameIconImages(JaveIcons.JAVE_ICON_IMAGES);
         monitor.subTask(JaveMessages.StartUp_SubTask_LoadConfig);
         ConfigurationList configurationList = initConfigFiles(monitor.getParentComponent());
         monitor.subTask(JaveMessages.StartUp_SubTask_CreatingUI);
         jave = new JavEApplication(configurationList);
         monitor.subTask(JaveMessages.StartUp_SubTask_HalfwayPoint);
         jave.setTool(0);
         monitor.subTask(JaveMessages.StartUp_SubTask_CrashRecovery);
         boolean recovered = jave.startupRecovery(monitor);
         monitor.subTask(JaveMessages.StartUp_SubTask_CreateMenu);
         jave.startupMenuBar();
         monitor.subTask(JaveMessages.StartUp_SubTask_Cleanup);
         jave.startupFinish2();
         monitor.subTask(JaveMessages.StartUp_SubTask_ToolOptionsDialog);
         jave.startupOptionsDialog();
         MacOsXInitializer.initializeApplicationCallbacks(jave.getMaxOsXApplicationCallbacks());
         monitor.subTask(JaveMessages.StartUp_SubTask_Finished);
         jave.startupFinish3();
         jave.getMainPanel().requestFocus();
         jave.getFrame().toFront();
         if (!recovered) {
            JaveApplicationPreferences applicationPreferences = jave.getApplicationPreferences();
            boolean showQuickStartOnStartup = applicationPreferences.isShowQuickStartOnStartup();
            if (showQuickStartOnStartup) {
               monitor.dispose();
               QuickStartAction quickStartAction = new QuickStartAction(jave);
               quickStartAction.performQuickStart(jave.getFrame());
            }

            if (jave.getDocumentManager().getSize() == 0) {
               jave.doNew();
            }
         }
      } catch (StartupException var8) {
         throw var8;
      } catch (Exception var9) {
         if (jave != null) {
            jave.dispose();
         }

         throw new StartupException(JaveMessages.StartUp_ErrorOccured, var9);
      }
   }

   public static ConfigurationList initConfigFiles(Component parentComponent) throws StartupException {
      MessageDialogConfigurationFileErrorHandler errorHandler = new MessageDialogConfigurationFileErrorHandler(parentComponent);
      JaveConfigurationFileLoader loader = new JaveConfigurationFileLoader(errorHandler);

      try {
         loader.checkConfigurationFolderExistant();
      } catch (ConfigurationException var10) {
         throw new StartupException(JaveMessages.StartUp_ErrorMessage_ProbablyConfigFoldersMissing, var10);
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
}
