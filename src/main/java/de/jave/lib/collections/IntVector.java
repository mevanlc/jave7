package de.jave.lib.collections;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class IntVector implements Cloneable, Serializable {
   private int[] elementData;
   private int elementCount;
   private final int capacityIncrement;

   public IntVector(int initialCapacity, int capacityIncrement) {
      if (initialCapacity < 0) {
         throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
      } else {
         this.elementData = new int[initialCapacity];
         this.capacityIncrement = capacityIncrement;
      }
   }

   public IntVector(int initialCapacity) {
      this(initialCapacity, 0);
   }

   public IntVector() {
      this(10);
   }

   public synchronized void trimToSize() {
      int oldCapacity = this.elementData.length;
      if (this.elementCount < oldCapacity) {
         int[] oldData = this.elementData;
         this.elementData = new int[this.elementCount];
         System.arraycopy(oldData, 0, this.elementData, 0, this.elementCount);
      }
   }

   public synchronized void ensureCapacity(int minCapacity) {
      this.ensureCapacityHelper(minCapacity);
   }

   private void ensureCapacityHelper(int minCapacity) {
      int oldCapacity = this.elementData.length;
      if (minCapacity > oldCapacity) {
         int[] oldData = this.elementData;
         int newCapacity = this.capacityIncrement > 0 ? oldCapacity + this.capacityIncrement : oldCapacity * 2;
         if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
         }

         this.elementData = new int[newCapacity];
         System.arraycopy(oldData, 0, this.elementData, 0, this.elementCount);
      }
   }

   public synchronized void setSize(int newSize) {
      if (newSize > this.elementCount) {
         this.ensureCapacityHelper(newSize);
      } else {
         for (int i = newSize; i < this.elementCount; i++) {
            this.elementData[i] = 0;
         }
      }

      this.elementCount = newSize;
   }

   public int capacity() {
      return this.elementData.length;
   }

   public int size() {
      return this.elementCount;
   }

   public boolean isEmpty() {
      return this.elementCount == 0;
   }

   public Enumeration elements() {
      return new Enumeration() {
         int count = 0;

         @Override
         public boolean hasMoreElements() {
            return this.count < IntVector.this.elementCount;
         }

         @Override
         public Object nextElement() {
            synchronized (IntVector.this) {
               if (this.count < IntVector.this.elementCount) {
                  return IntVector.this.elementData[this.count++];
               }
            }

            throw new NoSuchElementException("IntVector Enumeration");
         }
      };
   }

   public boolean contains(int elem) {
      return this.indexOf(elem, 0) >= 0;
   }

   public int indexOf(int elem) {
      return this.indexOf(elem, 0);
   }

   public synchronized int indexOf(int elem, int index) {
      for (int i = index; i < this.elementCount; i++) {
         if (elem == this.elementData[i]) {
            return i;
         }
      }

      return -1;
   }

   public int lastIndexOf(int elem) {
      return this.lastIndexOf(elem, this.elementCount - 1);
   }

   public synchronized int lastIndexOf(int elem, int index) {
      if (index >= this.elementCount) {
         throw new IndexOutOfBoundsException(index + " >= " + this.elementCount);
      } else {
         for (int i = index; i >= 0; i--) {
            if (elem == this.elementData[i]) {
               return i;
            }
         }

         return -1;
      }
   }

   public synchronized int elementAt(int index) {
      if (index >= this.elementCount) {
         throw new ArrayIndexOutOfBoundsException(index + " >= " + this.elementCount);
      } else {
         try {
            return this.elementData[index];
         } catch (ArrayIndexOutOfBoundsException var3) {
            throw new ArrayIndexOutOfBoundsException(index + " < 0");
         }
      }
   }

   public synchronized int firstElement() {
      if (this.elementCount == 0) {
         throw new NoSuchElementException();
      } else {
         return this.elementData[0];
      }
   }

   public synchronized int lastElement() {
      if (this.elementCount == 0) {
         throw new NoSuchElementException();
      } else {
         return this.elementData[this.elementCount - 1];
      }
   }

   public synchronized void setElementAt(int obj, int index) {
      if (index >= this.elementCount) {
         throw new ArrayIndexOutOfBoundsException(index + " >= " + this.elementCount);
      } else {
         this.elementData[index] = obj;
      }
   }

   public synchronized void removeElementAt(int index) {
      if (index >= this.elementCount) {
         throw new ArrayIndexOutOfBoundsException(index + " >= " + this.elementCount);
      } else if (index < 0) {
         throw new ArrayIndexOutOfBoundsException(index);
      } else {
         int j = this.elementCount - index - 1;
         if (j > 0) {
            System.arraycopy(this.elementData, index + 1, this.elementData, index, j);
         }

         this.elementCount--;
         this.elementData[this.elementCount] = 0;
      }
   }

   public synchronized void insertElementAt(int obj, int index) {
      if (index >= this.elementCount + 1) {
         throw new ArrayIndexOutOfBoundsException(index + " > " + this.elementCount);
      } else {
         this.ensureCapacityHelper(this.elementCount + 1);
         System.arraycopy(this.elementData, index, this.elementData, index + 1, this.elementCount - index);
         this.elementData[index] = obj;
         this.elementCount++;
      }
   }

   public synchronized void addElement(int obj) {
      this.ensureCapacityHelper(this.elementCount + 1);
      this.elementData[this.elementCount++] = obj;
   }

   public synchronized boolean removeElement(int obj) {
      int i = this.indexOf(obj);
      if (i >= 0) {
         this.removeElementAt(i);
         return true;
      } else {
         return false;
      }
   }

   public synchronized void removeAllElements() {
      for (int i = 0; i < this.elementCount; i++) {
         this.elementData[i] = 0;
      }

      this.elementCount = 0;
   }

   @Override
   public synchronized Object clone() {
      try {
         IntVector v = (IntVector)super.clone();
         v.elementData = new int[this.elementCount];
         System.arraycopy(this.elementData, 0, v.elementData, 0, this.elementCount);
         return v;
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public synchronized int[] toArray() {
      int[] result = new int[this.elementCount];
      System.arraycopy(this.elementData, 0, result, 0, this.elementCount);
      return result;
   }

   public synchronized int[] toArray(int[] a) {
      if (a.length < this.elementCount) {
         a = new int[this.elementCount];
      }

      System.arraycopy(this.elementData, 0, a, 0, this.elementCount);
      if (a.length > this.elementCount) {
         a[this.elementCount] = 0;
      }

      return a;
   }

   public synchronized int get(int index) {
      if (index >= this.elementCount) {
         throw new ArrayIndexOutOfBoundsException(index);
      } else {
         return this.elementData[index];
      }
   }

   public synchronized int set(int index, int element) {
      if (index >= this.elementCount) {
         throw new ArrayIndexOutOfBoundsException(index);
      } else {
         int oldValue = this.elementData[index];
         this.elementData[index] = element;
         return oldValue;
      }
   }

   public synchronized boolean add(int o) {
      this.ensureCapacityHelper(this.elementCount + 1);
      this.elementData[this.elementCount++] = o;
      return true;
   }

   public void add(int index, int element) {
      this.insertElementAt(element, index);
   }

   public synchronized int remove(int index) {
      if (index >= this.elementCount) {
         throw new ArrayIndexOutOfBoundsException(index);
      } else {
         int oldValue = this.elementData[index];
         int numMoved = this.elementCount - index - 1;
         if (numMoved > 0) {
            System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
         }

         this.elementData[--this.elementCount] = 0;
         return oldValue;
      }
   }

   public void clear() {
      this.removeAllElements();
   }

   protected void removeRange(int fromIndex, int toIndex) {
      int numMoved = this.elementCount - toIndex;
      System.arraycopy(this.elementData, toIndex, this.elementData, fromIndex, numMoved);
   }

   @Override
   public String toString() {
      StringBuffer result = new StringBuffer("{");

      for (int i = 0; i < this.size(); i++) {
         result.append(this.get(i));
         if (i < this.size() - 1) {
            result.append(';');
         }
      }

      result.append('}');
      return result.toString();
   }
}
