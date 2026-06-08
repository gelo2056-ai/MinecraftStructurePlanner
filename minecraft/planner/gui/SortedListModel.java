package minecraft.planner.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.AbstractListModel;

public class SortedListModel<T> extends AbstractListModel {
   private final SortedSet<T> model = Collections.synchronizedSortedSet(new TreeSet<>());

   public Collection<T> elements() {
      return new TreeSet<>(this.model);
   }

   @Override
   public Object getElementAt(int index) {
      return index >= 0 && index < this.model.size() ? this.model.toArray()[index] : null;
   }

   @Override
   public int getSize() {
      return this.model.size();
   }

   public void addElement(T element) {
      if (this.model.add(element)) {
         this.fireIntervalAdded(this, 0, this.getSize());
         this.fireContentsChanged(this, 0, this.getSize());
      }
   }

   public void addAll(Collection<T> elements) {
      if (this.model.addAll(elements)) {
         this.fireIntervalAdded(this, 0, this.getSize());
         this.fireContentsChanged(this, 0, this.getSize());
      }
   }

   public void addAll(T[] elements) {
      this.addAll(Arrays.asList((T[])elements));
   }

   public void clear() {
      this.model.clear();
      this.fireIntervalRemoved(this, 0, this.getSize());
      this.fireContentsChanged(this, 0, this.getSize());
   }

   public boolean contains(T element) {
      return this.model.contains(element);
   }

   public T firstElement() {
      return this.model.first();
   }

   public T lastElement() {
      return this.model.last();
   }

   public Iterator<T> iterator() {
      return this.model.iterator();
   }

   public boolean removeElement(T element) {
      boolean removed = this.model.remove(element);
      if (removed) {
         this.fireIntervalRemoved(this, 0, this.getSize());
         this.fireContentsChanged(this, 0, this.getSize());
      }

      return removed;
   }

   public boolean removeAll(Collection<T> elements) {
      boolean removed = this.model.removeAll(elements);
      if (removed) {
         this.fireIntervalRemoved(this, 0, this.getSize());
         this.fireContentsChanged(this, 0, this.getSize());
      }

      return removed;
   }
}
