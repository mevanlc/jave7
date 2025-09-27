package de.jave.jave.browser;

public interface IJaveDocumentTypeVisitor<R> {
   R visitText(JaveDocumentType var1);

   R visitAnimation(JaveDocumentType var1);

   R visitGame(JaveDocumentType var1);
}
