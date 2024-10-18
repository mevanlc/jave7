package net.jmge.gif.encode;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;

public class GifPixelsEncoder {
   private static final int EOF = -1;
   private final int imgW;
   private final int imgH;
   private final byte[] pixAry;
   private final boolean wantInterlaced;
   private final int initCodeSize;
   private int countDown;
   private int xCur;
   private int yCur;
   private int curPass;
   private static final int BITS = 12;
   private static final int HSIZE = 5003;
   private int n_bits;
   private final int maxbits = 12;
   private int maxcode;
   private final int maxmaxcode = 4096;
   private final int[] htab = new int[5003];
   private final int[] codetab = new int[5003];
   private final int hsize = 5003;
   private int free_ent = 0;
   private boolean clear_flg = false;
   private int g_init_bits;
   private int ClearCode;
   private int EOFCode;
   private int cur_accum = 0;
   private int cur_bits = 0;
   private final int[] masks = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535};
   private int a_count;
   private final byte[] accum = new byte[256];

   public GifPixelsEncoder(Dimension size, byte[] pixels, boolean interlaced, int color_depth) {
      this.imgW = size.width;
      this.imgH = size.height;
      this.pixAry = pixels;
      this.wantInterlaced = interlaced;
      this.initCodeSize = Math.max(2, color_depth);
   }

   public void encode(OutputStream os) throws IOException {
      os.write(this.initCodeSize);
      this.countDown = this.imgW * this.imgH;
      this.xCur = this.yCur = this.curPass = 0;
      this.compress(this.initCodeSize + 1, os);
      os.write(0);
   }

   private void bumpPosition() {
      this.xCur++;
      if (this.xCur == this.imgW) {
         this.xCur = 0;
         if (!this.wantInterlaced) {
            this.yCur++;
         } else {
            switch (this.curPass) {
               case 0:
                  this.yCur += 8;
                  if (this.yCur >= this.imgH) {
                     this.curPass++;
                     this.yCur = 4;
                  }
                  break;
               case 1:
                  this.yCur += 8;
                  if (this.yCur >= this.imgH) {
                     this.curPass++;
                     this.yCur = 2;
                  }
                  break;
               case 2:
                  this.yCur += 4;
                  if (this.yCur >= this.imgH) {
                     this.curPass++;
                     this.yCur = 1;
                  }
                  break;
               case 3:
                  this.yCur += 2;
            }
         }
      }
   }

   private int nextPixel() {
      if (this.countDown == 0) {
         return -1;
      } else {
         this.countDown--;
         byte pix = this.pixAry[this.yCur * this.imgW + this.xCur];
         this.bumpPosition();
         return pix & 0xFF;
      }
   }

   private final int MAXCODE(int n_bits) {
      return (1 << n_bits) - 1;
   }

   private void compress(int init_bits, OutputStream outs) throws IOException {
      this.g_init_bits = init_bits;
      this.clear_flg = false;
      this.n_bits = this.g_init_bits;
      this.maxcode = this.MAXCODE(this.n_bits);
      this.ClearCode = 1 << init_bits - 1;
      this.EOFCode = this.ClearCode + 1;
      this.free_ent = this.ClearCode + 2;
      this.char_init();
      int ent = this.nextPixel();
      int hshift = 0;

      for (int fcode = 5003; fcode < 65536; fcode *= 2) {
         hshift++;
      }

      hshift = 8 - hshift;
      int hsize_reg = 5003;
      this.cl_hash(hsize_reg);
      this.output(this.ClearCode, outs);

      int c;
      label42:
      while ((c = this.nextPixel()) != -1) {
         int var10 = (c << 12) + ent;
         int i = c << hshift ^ ent;
         if (this.htab[i] == var10) {
            ent = this.codetab[i];
         } else {
            if (this.htab[i] >= 0) {
               int disp = hsize_reg - i;
               if (i == 0) {
                  disp = 1;
               }

               do {
                  if ((i -= disp) < 0) {
                     i += hsize_reg;
                  }

                  if (this.htab[i] == var10) {
                     ent = this.codetab[i];
                     continue label42;
                  }
               } while (this.htab[i] >= 0);
            }

            this.output(ent, outs);
            ent = c;
            if (this.free_ent < 4096) {
               this.codetab[i] = this.free_ent++;
               this.htab[i] = var10;
            } else {
               this.cl_block(outs);
            }
         }
      }

      this.output(ent, outs);
      this.output(this.EOFCode, outs);
   }

   private void output(int code, OutputStream outs) throws IOException {
      this.cur_accum = this.cur_accum & this.masks[this.cur_bits];
      if (this.cur_bits > 0) {
         this.cur_accum = this.cur_accum | code << this.cur_bits;
      } else {
         this.cur_accum = code;
      }

      for (this.cur_bits = this.cur_bits + this.n_bits; this.cur_bits >= 8; this.cur_bits -= 8) {
         this.char_out((byte)(this.cur_accum & 0xFF), outs);
         this.cur_accum >>= 8;
      }

      if (this.free_ent > this.maxcode || this.clear_flg) {
         if (this.clear_flg) {
            this.maxcode = this.MAXCODE(this.n_bits = this.g_init_bits);
            this.clear_flg = false;
         } else {
            this.n_bits++;
            if (this.n_bits == 12) {
               this.maxcode = 4096;
            } else {
               this.maxcode = this.MAXCODE(this.n_bits);
            }
         }
      }

      if (code == this.EOFCode) {
         while (this.cur_bits > 0) {
            this.char_out((byte)(this.cur_accum & 0xFF), outs);
            this.cur_accum >>= 8;
            this.cur_bits -= 8;
         }

         this.flush_char(outs);
      }
   }

   private void cl_block(OutputStream outs) throws IOException {
      this.cl_hash(5003);
      this.free_ent = this.ClearCode + 2;
      this.clear_flg = true;
      this.output(this.ClearCode, outs);
   }

   private void cl_hash(int hsize) {
      for (int i = 0; i < hsize; i++) {
         this.htab[i] = -1;
      }
   }

   private void char_init() {
      this.a_count = 0;
   }

   private void char_out(byte c, OutputStream outs) throws IOException {
      this.accum[this.a_count++] = c;
      if (this.a_count >= 254) {
         this.flush_char(outs);
      }
   }

   private void flush_char(OutputStream outs) throws IOException {
      if (this.a_count > 0) {
         outs.write(this.a_count);
         outs.write(this.accum, 0, this.a_count);
         this.a_count = 0;
      }
   }
}
