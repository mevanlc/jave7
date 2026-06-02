package net.dizzy.commons.core.message;

public interface IBasicMessage {
   MessageType getType();

   String getText();

   String getTitle();

   default boolean isErrorMessage() {
      return getType() == MessageType.ERROR;
   }
}
