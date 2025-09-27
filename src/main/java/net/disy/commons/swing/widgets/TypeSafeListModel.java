package net.disy.commons.swing.list;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

public class TypeSafeListModel<T> extends AbstractListModel {
   private final List<T> elements;

   public TypeSafeListModel() {
      this(new ArrayList<>());
   }

   public TypeSafeListModel(List<T> elements) {
      this.elements = elements;
   }

   @Override
   public int getSize() {
      return this.elements.size();
   }

   @Override
   public T getElementAt(int index) {
      return this.elements.get(index);
   }

   public void addElement(T element) {
      this.elements.add(element);
      this.fireIntervalAdded(this, this.elements.size() - 1, this.elements.size() - 1);
   }
}
