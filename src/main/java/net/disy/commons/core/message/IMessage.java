package net.disy.commons.core.message;

public interface IMessage extends IBasicMessage {
   String getTitle();

   Throwable getThrowable();

   String getDetail();

   String getDetailedText();
}
