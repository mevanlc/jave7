package net.disy.commons.core.text;

public enum TextAlignment {
   CENTER("Center", 0) {
      @Override
      public void accept(ITextAlignmentVisitor visitor) {
         visitor.visitCenter(this);
      }
   },
   LEFT("Left", 2) {
      @Override
      public void accept(ITextAlignmentVisitor visitor) {
         visitor.visitLeft(this);
      }
   },
   RIGHT("Right", 4) {
      @Override
      public void accept(ITextAlignmentVisitor visitor) {
         visitor.visitRight(this);
      }
   };

   private final String description;
   private final int swingValue;

   private TextAlignment(String description, int swingValue) {
      this.description = description;
      this.swingValue = swingValue;
   }

   @Deprecated
   public int getIdValue() {
      return this.getSwingValue();
   }

   public int getSwingValue() {
      return this.swingValue;
   }

   @Override
   public String toString() {
      return this.description;
   }

   public abstract void accept(ITextAlignmentVisitor var1);

   public static TextAlignment[] getAll() {
      return values();
   }

   @Deprecated
   public static TextAlignment getByIdValue(int swingValue) {
      return getBySwingValue(swingValue);
   }

   public static TextAlignment getBySwingValue(int swingValue) {
      for (TextAlignment alignment : getAll()) {
         if (alignment.swingValue == swingValue) {
            return alignment;
         }
      }

      throw new IllegalArgumentException("No TextAlignment for swingValue " + swingValue + " defined");
   }

   @Deprecated
   public static TextAlignment getByDescription(String description) {
      return getById(description);
   }

   public static TextAlignment getById(String id) {
      for (TextAlignment alignment : values()) {
         if (alignment.description.equals(id)) {
            return alignment;
         }
      }

      throw new IllegalArgumentException("No TextAlignment defined for description " + id);
   }

   @Deprecated
   public static TextAlignment getByCaseInsensitiveDescription(String description) {
      return getByCaseInsensitiveId(description);
   }

   public static TextAlignment getByCaseInsensitiveId(String id) {
      String lowerCasedDescription = id.toLowerCase();

      for (TextAlignment alignment : values()) {
         if (alignment.description.toLowerCase().equals(lowerCasedDescription)) {
            return alignment;
         }
      }

      throw new IllegalArgumentException("No TextAlignment defined for id " + id);
   }

   public String getId() {
      return this.description;
   }

   @Deprecated
   public String getDescription() {
      return this.getId();
   }
}
