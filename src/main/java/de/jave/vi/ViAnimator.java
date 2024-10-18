package de.jave.vi;

import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import de.jave.lib.collections.IntVector;
import java.awt.Toolkit;
import net.disy.commons.core.util.Ensure;

public class ViAnimator {
   private CharacterPlate plate;
   private final BooleanArea blinkPlate;
   private int counter;
   private int cursorX;
   private int cursorY;
   private int windowHomeX;
   private int windowHomeY;
   private int windowEndX;
   private int windowEndY;
   private int savedCursorX;
   private int savedCursorY;
   private int blinkCounter;
   private boolean screenReversed = false;
   private final boolean warning = true;
   private static final int BLINK_RATE = 2;
   private boolean bold = false;
   private boolean lowIntensity = false;
   private boolean underline = false;
   private boolean blink = false;
   private boolean reverseVideo = false;
   private boolean invisibleText = false;
   private int parseIndex;
   private int parseLength;
   private char[] parseText;
   private final IViOutput output;

   public ViAnimator(IViOutput output) {
      Ensure.ensureArgumentNotNull(output);
      this.output = output;
      this.counter = 0;
      this.plate = new CharacterPlate(80, 30);
      this.blinkPlate = new BooleanArea(80, 30);
      this.resetToInitialState();
   }

   private void resetToInitialState() {
      this.cursorX = 0;
      this.cursorY = 0;
      this.windowHomeX = 0;
      this.windowHomeY = 0;
      this.windowEndX = this.plate.getWidth() - 1;
      this.windowEndY = this.plate.getHeight() - 1;
      this.bold = false;
      this.lowIntensity = false;
      this.underline = false;
      this.blink = false;
      this.reverseVideo = false;
      this.invisibleText = false;
   }

   public void execute(String line, ViPlayMode mode, int pause) {
      this.parseIndex = 0;
      this.parseLength = line.length();
      this.parseText = line.toCharArray();

      while (this.parseIndex < this.parseLength) {
         char ch = this.parseText[this.parseIndex++];
         if (ch == 27) {
            if (this.parseIndex == this.parseLength) {
               System.err.println("Broken line after ESC!");
               return;
            }

            ch = this.parseText[this.parseIndex++];
            if (ch == '[') {
               IntVector nums = this.parseNumbers();
               if (this.parseIndex == this.parseLength) {
                  System.err.println("Broken line after ESC!");
                  return;
               }

               ch = this.parseText[this.parseIndex++];
               if (nums != null && nums.size() != 0) {
                  if (nums.size() == 1) {
                     if (ch == 'K') {
                        this.eraseLine(nums.get(0));
                     } else if (ch == 'J') {
                        this.eraseScreen(nums.get(0));
                     } else if (ch == 'A') {
                        this.CUU(nums.get(0));
                     } else if (ch == 'B') {
                        this.CUD(nums.get(0));
                     } else if (ch == 'C') {
                        this.CUF(nums.get(0));
                     } else if (ch == 'D') {
                        this.CUB(nums.get(0));
                     } else if (ch == 'H') {
                        this.CUP(nums.get(0), 0);
                     } else if (ch == 'm') {
                        this.SGR(nums.get(0));
                     } else {
                        System.err.println("UNKNOWN Command:    ESC [ " + nums.get(0) + ch);
                     }
                  } else if (nums.size() == 2) {
                     if (ch == 'H' || ch == 'f') {
                        this.CUP(nums.get(0), nums.get(1));
                     } else if (ch == 'r') {
                        this.SETWIN(nums.get(0), nums.get(1));
                     } else if (ch == 'm') {
                        this.SGR(nums);
                     } else {
                        System.err.println("UNKNOWN Command:   ESC [ " + nums.get(0) + ";" + nums.get(1) + ch);
                     }
                  } else if (ch == 'm') {
                     this.SGR(nums);
                  } else {
                     System.err.println("UNKNOWN Command >2: ESC [ " + nums.get(0) + "," + nums.get(1) + "..." + ch);
                  }
               } else if (ch == 's') {
                  this.SCP();
               } else if (ch == '=') {
                  nums = this.parseNumbers();
                  if (this.parseIndex == this.parseLength) {
                     System.err.println("Broken line after ESC=...");
                     return;
                  }

                  ch = this.parseText[this.parseIndex++];
                  if (ch == 'h') {
                     System.err.println("Ignored because unknown command EDC=" + nums + "h");
                  } else {
                     System.err.println("Ignored because unknown command EDC=" + nums + ch);
                  }
               } else if (ch == 'u') {
                  this.RCP();
               } else if (ch == 'K') {
                  this.eraseLine(0);
               } else if (ch == 'J') {
                  this.eraseScreen(0);
               } else if (ch == 'm') {
                  this.SGR(0);
               } else if (ch == 's') {
                  this.SCP();
               } else if (ch == 'M') {
                  this.deleteLine();
               } else if (ch == 'L') {
                  this.insertLine();
               } else if (ch == 'A') {
                  this.CUU(1);
               } else if (ch == 'B') {
                  this.CUD(1);
               } else if (ch == 'C') {
                  this.CUF(1);
               } else if (ch == 'D') {
                  this.CUB(1);
               } else if (ch == 'H') {
                  this.CUP(0, 0);
               } else if (ch == '?') {
                  nums = this.parseNumbers();
                  ch = this.parseText[this.parseIndex++];
                  if (ch != 'l' && ch != 'h') {
                     System.err.println("UNKNOWN Command: ESC [ " + nums + ch);
                  } else if (nums.size() == 1) {
                     switch (nums.get(0)) {
                        case 5:
                           this.setScreenReversed(ch == 'h');
                           break;
                        default:
                           System.err.println("IGNORED Screen-Mode-Command: ESC [ ?" + nums + ch);
                     }
                  } else {
                     System.err.println("IGNORED Command: ESC [ ? " + nums + ch);
                  }
               } else {
                  System.err.println("UNKNOWN Command:  ESC [ " + ch);
               }
            } else if (ch == 'M') {
               this.RI();
            } else if (ch == '(') {
               ch = this.parseText[this.parseIndex++];
               this.setCharacterSet(0, ch);
            } else if (ch == ')') {
               ch = this.parseText[this.parseIndex++];
               this.setCharacterSet(1, ch);
            } else if (ch == 'c') {
               this.resetToInitialState();
            } else if (ch == '#') {
               ch = this.parseText[this.parseIndex++];
               this.setLetterHeight(ch);
            } else {
               System.err.println("UNKNOWN COMMAND:    ESC " + ch);
            }
         } else {
            this.writeChar(ch);
         }

         if (mode == ViPlayMode.CHAR) {
            this.updateOutput();
            if (pause > 0) {
               try {
                  Thread.sleep(pause);
               } catch (InterruptedException var6) {
               }
            }
         }
      }
   }

   private void writeChar(char ch) {
      if (ch < ' ') {
         switch (ch) {
            case '\u0000':
               this.NULL();
               break;
            case '\u0001':
            case '\u0002':
            case '\u0003':
            case '\u0004':
            case '\u0005':
            case '\u0006':
            case '\n':
            case '\u000b':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            default:
               System.err.println("####>---> " + ch);
               this.writeChar(' ');
               break;
            case '\u0007':
               this.BELL();
               break;
            case '\b':
               this.backSpace();
               break;
            case '\t':
               this.horizontalTab();
               break;
            case '\f':
               this.lineFeed();
               break;
            case '\r':
               this.carriageReturn();
               break;
            case '\u000e':
               this.shiftOut();
               break;
            case '\u000f':
               this.shiftIn();
               break;
            case '\u0017':
               this.endOfTransmissionBlock();
               break;
            case '\u001f':
               this.unitSeperator();
         }
      } else {
         try {
            this.plate.setForce(this.cursorX, this.cursorY, ch);
            this.blinkPlate.set(this.cursorX, this.cursorY, this.blink);
         } catch (Exception var3) {
            System.err.println(this.cursorX + "," + this.cursorY + " is not on screen!");
         }

         this.cursorX++;
         if (this.cursorX > this.windowEndX) {
            this.cursorY++;
            this.cursorX = this.windowHomeX;
         }
      }
   }

   private void shiftOut() {
   }

   private void shiftIn() {
   }

   private void endOfTransmissionBlock() {
   }

   private void unitSeperator() {
   }

   private void lineFeed() {
      this.cursorY++;
   }

   private void carriageReturn() {
      this.cursorX = 0;
   }

   private void horizontalTab() {
      this.cursorX = (this.cursorX / 6 + 1) * 6;
   }

   private void NULL() {
   }

   private void BELL() {
      Toolkit.getDefaultToolkit().beep();
   }

   private void backSpace() {
      this.cursorX--;
      if (this.cursorX < 0) {
         this.cursorX = this.windowEndX;
         this.cursorY--;
      }
   }

   private void setLetterHeight(char ch) {
      switch (ch) {
         case '3':
            System.err.println("WARNING: setLetterHeight not supported");
            break;
         case '4':
            System.err.println("WARNING: setLetterHeight not supported");
            break;
         case '5':
            System.err.println("WARNING: setLetterHeight not supported");
            break;
         case '6':
            System.err.println("WARNING: setLetterHeight not supported");
            break;
         default:
            System.err.println("Unknown Mode in setLetterHeight: " + ch);
      }
   }

   private void setCharacterSet(int g, char ch) {
      switch (ch) {
         case '0':
            System.err.println("WARNING: setCharacterSet not supported");
            break;
         case '1':
            System.err.println("WARNING: setCharacterSet not supported");
            break;
         case '2':
            System.err.println("WARNING: setCharacterSet not supported");
            break;
         case 'A':
            System.err.println("WARNING: setCharacterSet not supported");
            break;
         case 'B':
            System.err.println("WARNING: setCharacterSet not supported");
            break;
         default:
            System.err.println("Unknown Mode in setCharacterSet: " + g + "," + ch);
      }
   }

   private void SGR(IntVector nums) {
      for (int i = 0; i < nums.size(); i++) {
         this.SGR(nums.get(i));
      }
   }

   private void SETWIN(int num1, int num2) {
      this.windowHomeY = num1;
      this.windowEndY = num2;
      if (this.cursorY < this.windowHomeY) {
         this.cursorY = this.windowHomeY;
      } else if (this.cursorY > this.windowEndY) {
         this.cursorY = this.windowEndY;
      }
   }

   private void CUP(int lineNo, int columnNo) {
      this.cursorX = columnNo;
      this.cursorY = lineNo;
   }

   private void CUU(int num) {
      this.cursorY -= num;
   }

   private void CUD(int num) {
      this.cursorY += num;
   }

   private void CUF(int num) {
      this.cursorX += num;
   }

   private void CUB(int num) {
      this.cursorX -= num;
   }

   private void RM(int num) {
      this.SM(num);
   }

   private void SM(int num) {
      switch (num) {
         case 0:
            this.plate = new CharacterPlate(40, 25);
            break;
         case 1:
            this.plate = new CharacterPlate(40, 25);
            break;
         case 2:
            this.plate = new CharacterPlate(80, 25);
            break;
         case 3:
            this.plate = new CharacterPlate(80, 25);
            break;
         case 4:
            System.err.println("Unable to use Mode in SM: " + num + " =320x200 color");
            this.plate = new CharacterPlate(40, 25);
            break;
         case 5:
            System.err.println("Unable to use Mode in SM: " + num + " =320x200 black & white");
            this.plate = new CharacterPlate(40, 25);
            break;
         case 6:
            System.err.println("Unable to use Mode in SM: " + num + " =640x200 black & white");
            this.plate = new CharacterPlate(40, 25);
            break;
         case 7:
            System.err.println("Unable to use Mode in SM: " + num + " =Wrap at end of line!!!");
            break;
         default:
            System.err.println("Unknown Mode in SM: " + num);
      }
   }

   private void SGR(int num) {
      if (num >= 30 && num <= 39) {
         System.err.println("WARNING: Foreground color not supported");
      } else if (num >= 40 && num <= 47) {
         System.err.println("WARNING: Background color not supported");
      } else {
         switch (num) {
            case 0:
               this.bold = false;
               this.lowIntensity = false;
               this.underline = false;
               this.blink = false;
               this.reverseVideo = false;
               this.invisibleText = false;
               break;
            case 1:
               System.err.println("WARNING: Mode bold not supported");
               this.bold = true;
               break;
            case 2:
               System.err.println("WARNING: Mode lowIntensity not supported");
               this.lowIntensity = true;
               break;
            case 3:
            case 6:
            default:
               System.err.println("Unknown Mode in SGR: " + num);
               break;
            case 4:
               System.err.println("WARNING: Mode underline not supported");
               this.underline = true;
               break;
            case 5:
               this.blink = true;
               break;
            case 7:
               this.reverseVideo = true;
               break;
            case 8:
               System.err.println("WARNING: Mode invisibleText not supported");
               this.invisibleText = true;
         }
      }
   }

   private void RCP() {
      this.cursorX = this.savedCursorX;
      this.cursorY = this.savedCursorY;
   }

   private void insertLine() {
      this.plate.insertLine(this.cursorY);
   }

   private void deleteLine() {
      this.plate.removeLine(this.cursorY);
   }

   private void eraseScreen(int mode) {
      if (mode == 0) {
         this.plate.fill(this.cursorX, this.cursorY, this.plate.getWidth() - this.cursorX, 1, ' ');
         if (this.cursorY < this.plate.getHeight() - 1) {
            this.plate.fill(0, this.cursorY + 1, this.plate.getWidth(), this.plate.getHeight() - this.cursorY - 1, ' ');
         }
      } else if (mode == 1) {
         this.plate.fill(0, this.cursorY, this.cursorX, 1, ' ');
         this.plate.fill(0, 0, this.plate.getWidth(), this.cursorY, ' ');
      } else if (mode == 2) {
         this.plate.clear();
         this.cursorX = 0;
         this.cursorY = 0;
      } else {
         System.err.println("WARNING: Unknown mode in eraseScreen:" + mode);
      }
   }

   private void eraseLine(int mode) {
      if (mode == 0) {
         this.plate.fill(this.cursorX, this.cursorY, this.windowEndX - this.cursorX, 1, ' ');
         this.blinkPlate.fill(this.cursorX, this.cursorY, this.windowEndX - this.cursorX, 1, false);
      } else if (mode == 1) {
         this.plate.fill(this.windowHomeX, this.cursorY, this.cursorX - this.windowHomeX + 1, 1, ' ');
         this.blinkPlate.fill(this.windowHomeX, this.cursorY, this.cursorX - this.windowHomeX + 1, 1, false);
      } else if (mode == 2) {
         this.plate.fill(this.windowHomeX, this.cursorY, this.windowEndX - this.windowHomeX + 1, 1, ' ');
         this.blinkPlate.fill(this.windowHomeX, this.cursorY, this.windowEndX - this.windowHomeX + 1, 1, false);
      } else {
         System.err.println("WARNING: Unknown mode in eraseLine:" + mode);
      }
   }

   private void SCP() {
      this.savedCursorX = this.cursorX;
      this.savedCursorY = this.cursorY;
   }

   private IntVector parseNumbers() {
      IntVector result = null;

      while (true) {
         int num = this.parseNumber();
         if (num == -1 && this.parseIndex < this.parseLength && this.parseText[this.parseIndex] == ';') {
            this.parseIndex++;
         } else {
            if (num == -1) {
               return result;
            }

            if (result == null) {
               result = new IntVector(5);
            }

            result.add(num);
         }
      }
   }

   private int parseNumber() {
      if (this.parseIndex < this.parseLength && this.parseText[this.parseIndex] >= '0' && this.parseText[this.parseIndex] <= '9') {
         int num = 0;

         do {
            num *= 10;
            num += this.parseText[this.parseIndex] - '0';
            this.parseIndex++;
         } while (this.parseIndex < this.parseLength && this.parseText[this.parseIndex] >= '0' && this.parseText[this.parseIndex] <= '9');

         return num;
      } else {
         return -1;
      }
   }

   public void newLine() {
      this.cursorX = this.windowHomeX;
      this.cursorY++;
      if (this.cursorY > this.windowEndY) {
         this.scroll();
         this.cursorY = this.windowEndY;
      }
   }

   public void updateOutput() {
      this.output.setText(this.convertToString());
      this.output.setColorsInverted(this.screenReversed);
      this.output.setStatus("Frame: " + this.counter);
      this.counter++;
   }

   private String convertToString() {
      this.blinkCounter++;
      if (this.blinkCounter % 2 < 1) {
         return this.plate.toString();
      } else if (this.blinkPlate.isEmpty()) {
         return this.plate.toString();
      } else {
         CharacterPlate cp = this.plate.getClone();

         for (int y = 0; y < cp.getHeight(); y++) {
            for (int x = 0; x < cp.getWidth(); x++) {
               if (this.blinkPlate.isSet(x, y)) {
                  cp.setForce(x, y, ' ');
               }
            }
         }

         return cp.toString();
      }
   }

   private void setScreenReversed(boolean what) {
      this.screenReversed = what;
      System.err.println("WARNING: setScreenReversed not supported");
   }

   private void scroll() {
      CharacterPlate moving = this.plate
         .getCopy(this.windowHomeX, this.windowHomeY + 1, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY);
      this.plate.fill(this.windowHomeX, this.windowHomeY, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY + 1, ' ');
      moving.pasteInto(this.plate, this.windowHomeX, this.windowHomeY);
      BooleanArea blinkMoving = this.blinkPlate
         .getCopy(this.windowHomeX, this.windowHomeY + 1, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY);
      this.blinkPlate.fill(this.windowHomeX, this.windowHomeY, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY + 1, false);
      blinkMoving.pasteInto(this.blinkPlate, this.windowHomeX, this.windowHomeY);
   }

   private void RI() {
      CharacterPlate moving = this.plate
         .getCopy(this.windowHomeX, this.windowHomeY, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY);
      this.plate.fill(this.windowHomeX, this.windowHomeY, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY + 1, ' ');
      moving.pasteInto(this.plate, this.windowHomeX, this.windowHomeY + 1);
      BooleanArea blinkMoving = this.blinkPlate
         .getCopy(this.windowHomeX, this.windowHomeY, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY);
      this.blinkPlate.fill(this.windowHomeX, this.windowHomeY, this.windowEndX - this.windowHomeX + 1, this.windowEndY - this.windowHomeY + 1, false);
      blinkMoving.pasteInto(this.blinkPlate, this.windowHomeX, this.windowHomeY + 1);
   }
}
