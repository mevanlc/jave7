package net.disy.commons.core.message;

public interface IBasicMessage {
   String getText();

   MessageType getType();

   boolean isErrorMessage();
}
