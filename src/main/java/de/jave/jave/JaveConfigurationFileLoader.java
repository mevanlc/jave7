package de.jave.jave;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IConfigurationFileErrorHandler;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.File;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;

public class JaveConfigurationFileLoader {
   public static final String CONFIGURATION_FOLDER_NAME = "config";
   private final IConfigurationFileErrorHandler errorHandler;

   public JaveConfigurationFileLoader(IConfigurationFileErrorHandler errorHandler) {
      Ensure.ensureArgumentNotNull(errorHandler);
      this.errorHandler = errorHandler;
   }

   public <T> T initConfigFile(IJaveInitializable<T> confy) {
      IJavaInitializationContext context = new IJavaInitializationContext() {
         @Override
         public File getConfigurationFile(String fileName) {
            File file = new File(JaveGlobalRessources.codeBase, fileName);
            if (!file.exists()) {
               file = new File(JaveGlobalRessources.codeBase, file.getName());
            }

            if (!file.exists()) {
               file = new File(fileName);
            }

            if (file.exists()) {
               return file;
            } else {
               Message message = new Message(
                  JaveMessages.JavE,
                  "Error loading configuration from file:\n\t'" + file.getAbsolutePath() + "'\n" + "The file does not exist.",
                  MessageType.ERROR
               );
               JaveConfigurationFileLoader.this.errorHandler.handleError(message);
               return null;
            }
         }
      };

      try {
         return confy.initialize(context);
      } catch (Exception var5) {
         Message message = new Message(JaveMessages.JavE, "Error loading configuration.\n" + var5.getMessage(), var5);
         this.errorHandler.handleError(message);
         return null;
      }
   }

   public void checkConfigurationFolderExistant() throws ConfigurationException {
      File configurationFolder = new File(JaveGlobalRessources.codeBase, "config");
      if (!configurationFolder.exists()) {
         File fallbackFolder = new File("config");
         if (fallbackFolder.exists() && fallbackFolder.canRead()) {
            return;
         }
      }

      if (!configurationFolder.exists()) {
         throw new ConfigurationException("Configuration folder '" + configurationFolder.getAbsolutePath() + "' does not exist.");
      } else if (!configurationFolder.isDirectory()) {
         throw new ConfigurationException("Configuration folder '" + configurationFolder.getAbsolutePath() + "' is not a directory.");
      }
   }
}
