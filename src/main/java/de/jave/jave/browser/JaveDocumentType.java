package de.jave.jave.browser;

public enum JaveDocumentType {
   TEXT {
      @Override
      public <R> R accept(IJaveDocumentTypeVisitor<R> visitor) {
         return visitor.visitText(this);
      }
   },
   ANIMATION {
      @Override
      public <R> R accept(IJaveDocumentTypeVisitor<R> visitor) {
         return visitor.visitAnimation(this);
      }
   },
   GAME {
      @Override
      public <R> R accept(IJaveDocumentTypeVisitor<R> visitor) {
         return visitor.visitGame(this);
      }
   };

   private JaveDocumentType() {
   }

   public abstract <R> R accept(IJaveDocumentTypeVisitor<R> var1);
}
