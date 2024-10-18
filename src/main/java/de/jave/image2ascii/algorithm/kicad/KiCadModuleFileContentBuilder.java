package de.jave.image2ascii.algorithm.kicad;

import de.jave.lib.CharacterPlate;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KiCadModuleFileContentBuilder {
   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
   private final StringBuffer buffer = new StringBuffer();

   public KiCadModuleFileContentBuilder(Date createDate) {
      String date = DATE_FORMAT.format(createDate);
      this.buffer.append("PCBNEW-LibModule-V1 ").append(date).append("\n");
   }

   public void addModuleIndex(String moduleName) {
      this.buffer.append("$INDEX\n");
      this.buffer.append(moduleName);
      this.buffer.append("\n");
      this.buffer.append("$EndINDEX\n");
   }

   public void startModule(String moduleName) {
      this.buffer.append("$MODULE ").append(moduleName).append("\n");
      this.buffer.append("Po 0 0 0 15 4BF6BB79 4BF6B5CB ~~\n");
      this.buffer.append("Li ").append(moduleName).append("\n");
      this.buffer.append("Sc 4BF6B5CB\n");
      this.buffer.append("AR \n");
      this.buffer.append("Op 0 0 0\n");
      this.buffer.append("At VIRTUAL\n");
      this.buffer.append("T0 0 -750 600 600 0 120 N V 21 N\"").append(moduleName).append("\"\n");
      this.buffer.append("T1 0 600 600 600 0 120 N V 21 N\"VAL**\"\n");
   }

   public void addMechanicalPad(int xPosition, int yPosition, int padWidth, int padHeight) {
      this.buffer.append("$PAD\n");
      this.buffer.append("Sh \"\" R ").append(padWidth).append(" ").append(padHeight).append(" ").append("0 0 0\n");
      this.buffer.append("Dr 0 0 0\n");
      this.buffer.append("At MECA N 00088000\n");
      this.buffer.append("Ne 0 \"\"\n");
      this.buffer.append("Po ").append(xPosition).append(" ").append(yPosition).append("\n");
      this.buffer.append(".LocalClearance 10\n");
      this.buffer.append("$EndPAD\n");
   }

   public void endModule(String moduleName) {
      this.buffer.append("$EndMODULE ").append(moduleName).append("\n");
   }

   public void endLibrary() {
      this.buffer.append("$EndLIBRARY");
   }

   public CharacterPlate createCharacterPlate() {
      return new CharacterPlate(this.buffer.toString());
   }
}
