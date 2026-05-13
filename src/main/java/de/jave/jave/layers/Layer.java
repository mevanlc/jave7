package de.jave.jave.layers;

import de.jave.lib.CharacterPlate;

public interface Layer {
   String getId();

   String getName();

   void setName(String name);

   CharacterPlate getContent();
}
