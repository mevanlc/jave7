package de.jave.jave.configuration;

import net.disy.commons.core.message.IMessage;

public final class RuntimeExceptionThrowingErrorHandler implements IConfigurationFileErrorHandler {
   @Override
   public void handleError(IMessage message) {
      throw new RuntimeException(message.getDetailedText());
   }
}
