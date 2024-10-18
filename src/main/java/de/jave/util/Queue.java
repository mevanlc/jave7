package de.jave.util;

public class Queue {
   protected Object[] entries;
   protected int start;
   protected int end;

   public Queue() {
      this(200);
   }

   public Queue(int capacity) {
      this.entries = new Object[capacity];
      this.start = 0;
      this.end = -1;
   }

   public synchronized void put(Object o) {
      if (this.size() == this.capacity()) {
         this.ensureCapacity(2 * this.capacity());
      }

      this.end++;
      this.end = this.end % this.capacity();
      this.entries[this.end] = o;
   }

   public synchronized Object get() {
      if (this.size() == 0) {
         throw new RuntimeException("Tried to get entry from Queue where queue is empty!");
      } else {
         Object result = this.entries[this.start];
         if (this.start == this.end) {
            this.start = 0;
            this.end = -1;
         } else {
            this.start++;
            this.start = this.start % this.capacity();
         }

         return result;
      }
   }

   public int capacity() {
      return this.entries.length;
   }

   public int size() {
      if (this.end == -1) {
         return 0;
      } else {
         return this.end < this.start ? this.capacity() + this.end - this.start + 1 : this.end - this.start + 1;
      }
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   protected synchronized void ensureCapacity(int c) {
      Object[] e2 = new Object[c];
      if (this.end < this.start) {
         System.arraycopy(this.entries, this.start, e2, 0, this.capacity() - this.start);
         System.arraycopy(this.entries, 0, e2, this.capacity() - this.start, this.end + 1);
         this.end = this.capacity() - this.start + 1;
         this.start = 0;
      } else {
         System.arraycopy(this.entries, this.start, e2, 0, this.end - this.start + 1);
         this.end = this.end - this.start;
         this.start = 0;
      }

      this.entries = e2;
   }

   public synchronized void clear() {
      this.start = 0;
      this.end = -1;
   }

   public static void main(String[] args) {
      Queue q = new Queue(5);

      for (int i = 0; i < 20; i++) {
         q.put(i);
      }

      for (int i = 0; i < 5; i++) {
         System.out.println(q.get());
      }

      for (int i = 20; i < 25; i++) {
         q.put(i);
      }

      for (int i = 0; i < 20; i++) {
         System.out.println(q.get());
      }
   }
}
