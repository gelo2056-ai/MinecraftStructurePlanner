package minecraft.planner.model.grid;

import java.util.LinkedList;
import java.util.List;

public class Array2D<T extends IdentifiesEmptiness> {
   private int width;
   private int height;
   private LinkedList<LinkedList<T>> xList;

   public Array2D(int width, int height) {
      this.width = width;
      this.height = height;
      this.xList = new LinkedList<>();

      for (int x = 0; x < width; x++) {
         this.xList.add(this.createList(height));
      }
   }

   public synchronized int getWidth() {
      return this.width;
   }

   public synchronized int getHeight() {
      return this.height;
   }

   public synchronized T get(int x, int y) {
      if (x < 0 || x >= this.width) {
         throw new IndexOutOfBoundsException("x value falls outside of Array2D width range");
      } else if (y >= 0 && y < this.height) {
         return this.xList.get(x).get(y);
      } else {
         throw new IndexOutOfBoundsException("y value falls outside of Array2D height range");
      }
   }

   public synchronized void set(int x, int y, T object) {
      if (x < 0 || x >= this.width) {
         throw new IndexOutOfBoundsException("x value falls outside of Array2D width range");
      }

      if (y >= 0 && y < this.height) {
         this.xList.get(x).set(y, object);
      } else {
         throw new IndexOutOfBoundsException("y value falls outside of Array2D height range");
      }
   }

   public synchronized boolean increaseWidth() {
      this.xList.add(this.createList(this.height));
      this.width++;
      return true;
   }

   public synchronized boolean increaseHeight() {
      for (List<T> yList : this.xList) {
         yList.add(null);
      }

      this.height++;
      return true;
   }

   public synchronized boolean decreaseWidth() {
      int columnIndex = this.width - 1;
      if (!this.columnContainsData(columnIndex)) {
         this.xList.remove(columnIndex);
         this.width--;
         return true;
      } else {
         return false;
      }
   }

   public synchronized boolean decreaseHeight() {
      int rowIndex = this.height - 1;
      if (this.rowContainsData(rowIndex)) {
         return false;
      }

      for (List<T> column : this.xList) {
         column.remove(rowIndex);
      }

      this.height--;
      return true;
   }

   public synchronized void addRow(int row) {
      for (LinkedList<T> column : this.xList) {
         column.add(row, null);
      }

      this.height++;
   }

   public synchronized void addColumn(int col) {
      if (col == this.xList.size()) {
         this.xList.addLast(this.createList(this.getHeight()));
      } else {
         this.xList.add(col, this.createList(this.getHeight()));
      }

      this.width++;
   }

   public synchronized void removeRow(int row) {
      for (LinkedList<T> column : this.xList) {
         column.remove(row);
      }

      this.height--;
   }

   public synchronized void removeColumn(int col) {
      this.xList.remove(col);
      this.width--;
   }

   public synchronized void trim() {
      this.trimColumns();
      this.trimRows();
   }

   public synchronized void trimColumns() {
      while (!this.xList.isEmpty() && !this.columnContainsData(0)) {
         this.xList.removeFirst();
         this.width--;
      }

      while (!this.xList.isEmpty() && !this.columnContainsData(this.width - 1)) {
         this.xList.removeLast();
         this.width--;
      }
   }

   public synchronized void trimRows() {
      while (!this.xList.isEmpty() && this.height > 0 && !this.rowContainsData(0)) {
         for (LinkedList<T> column : this.xList) {
            column.removeFirst();
         }

         this.height--;
      }

      while (!this.xList.isEmpty() && this.height > 0 && !this.rowContainsData(this.height - 1)) {
         for (LinkedList<T> column : this.xList) {
            column.removeLast();
         }

         this.height--;
      }
   }

   public synchronized boolean columnContainsData(int x) {
      if (x >= 0 && x < this.width) {
         for (T element : this.xList.get(x)) {
            if (element != null && !element.isEmpty()) {
               return true;
            }
         }

         return false;
      } else {
         throw new IndexOutOfBoundsException("x value falls outside of Array2D width range");
      }
   }

   public synchronized boolean rowContainsData(int y) {
      if (y >= 0 && y < this.height) {
         for (List<T> column : this.xList) {
            if (column.get(y) != null && !column.get(y).isEmpty()) {
               return true;
            }
         }

         return false;
      } else {
         throw new IndexOutOfBoundsException("y value falls outside of Array2D height range");
      }
   }

   private LinkedList<T> createList(int size) {
      LinkedList<T> list = new LinkedList<>();

      for (int i = 0; i < size; i++) {
         list.add(null);
      }

      return list;
   }
}
