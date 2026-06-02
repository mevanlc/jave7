package de.jave.jave.layers;

import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import net.dizzy.commons.core.util.Ensure;

public final class DocumentLayer implements Layer {
   private final String id;
   private String name;
   private CharacterPlate content;

   public DocumentLayer(String id, String name, CharacterPlate content) {
      Ensure.ensureArgumentNotNull(id);
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(content);
      this.id = id;
      this.name = name;
      this.content = content;
   }

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void setName(String name) {
      Ensure.ensureArgumentNotNull(name);
      this.name = name;
   }

   @Override
   public CharacterPlate getContent() {
      return this.content;
   }

   public void setContent(CharacterPlate content) {
      Ensure.ensureArgumentNotNull(content);
      this.content = content;
   }

   public Dimension getSize() {
      return this.content.getSize();
   }

   public boolean isVisible() {
      return true;
   }

   public boolean isOpaque() {
      return true;
   }
}
