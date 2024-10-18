package net.disy.commons.core.object;

import net.disy.commons.core.util.ObjectUtilities;

public class ObjectPair<T, U> {
   private final T firstObject;
   private final U secondObject;

   public ObjectPair(T firstObject, U secondObject) {
      this.firstObject = firstObject;
      this.secondObject = secondObject;
   }

   public final T getFirstObject() {
      return this.firstObject;
   }

   public final U getSecondObject() {
      return this.secondObject;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof ObjectPair)) {
         return false;
      } else {
         ObjectPair<?, ?> other = (ObjectPair<?, ?>)obj;
         return ObjectUtilities.equals(this.firstObject, other.firstObject) && ObjectUtilities.equals(this.secondObject, other.secondObject);
      }
   }

   @Override
   public final int hashCode() {
      return ObjectUtilities.getHashCode(this.firstObject) ^ ObjectUtilities.getHashCode(this.secondObject);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append('(');
      sb.append(this.firstObject);
      sb.append(" . ");
      sb.append(this.secondObject);
      sb.append(')');
      return sb.toString();
   }
}
