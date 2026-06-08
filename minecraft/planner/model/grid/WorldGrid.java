package minecraft.planner.model.grid;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import minecraft.planner.gui.java3d.modelfactory.impl.StairModelFactory;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StatisticsChangeListener;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid.GridStatistics.StatisticsUpdateThread;
import minecraft.planner.model.util.Tag;
import minecraft.planner.model.util.Tag$Type;

public class WorldGrid {
   private int width = 0;
   private int height = 0;
   private int maxHeight;
   private int minHeight;
   private int totalBlocks = 0;
   private int visibleBlocks = 0;
   private Array2D<WorldGrid.Cell> grid;
   private WorldGrid.GridStatistics statistics;

   public WorldGrid() {
      this.maxHeight = 0;
      this.minHeight = Integer.MAX_VALUE;
      this.grid = null;
      this.statistics = new WorldGrid.GridStatistics();
   }

   public void initialize(int width, int height) {
      this.initialize(width, height, false);
   }

   public void initialize(int width, int height, boolean includeBufferCells) {
      this.width = width;
      this.height = height;
      if (includeBufferCells) {
         this.width += 2;
         this.height += 2;
      }

      this.totalBlocks = 0;
      this.visibleBlocks = 0;
      this.grid = new Array2D<>(this.width, this.height);
      this.statistics.resetStatistics();
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public WorldGrid.Cell getCell(int x, int y) {
      if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
         WorldGrid.Cell cell = null;

         try {
            cell = this.grid.get(x, y);
            if (cell == null) {
               cell = new WorldGrid.Cell(this, x, y);
               this.grid.set(x, y, cell);
            }
         } catch (Exception e) {
            cell = null;
         }

         return cell;
      } else {
         return null;
      }
   }

   public int getTotalBlocks() {
      return this.totalBlocks;
   }

   public int getVisibleBlocks() {
      return this.visibleBlocks;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public int getMinHeight() {
      return this.minHeight;
   }

   public void updateMetrics(StructureModel<?> model) {
      this.maxHeight = 0;
      this.minHeight = Integer.MAX_VALUE;
      this.totalBlocks = 0;
      this.visibleBlocks = 0;

      for (int xLoop = 0; xLoop < this.width; xLoop++) {
         for (int yLoop = 0; yLoop < this.height; yLoop++) {
            WorldGrid.Cell cell = this.getCell(xLoop, yLoop);
            if (cell != null) {
               this.totalBlocks = this.totalBlocks + cell.getHeight();
               this.visibleBlocks = this.visibleBlocks + cell.getNumberOfBlocks();
               if (cell.getHeight() > this.maxHeight) {
                  this.maxHeight = cell.getHeight();
               }

               if (cell.getHeight() < this.minHeight) {
                  this.minHeight = cell.getHeight();
               }
            }
         }
      }
   }

   public WorldGrid.GridStatistics getStatistics() {
      return this.statistics;
   }

   public void saveAsXML(TextureName defaultTexture, OutputStream os) throws IOException {
      String header = "<grid width='" + this.width + "' " + "height" + "='" + this.height + "' " + "maxheight" + "='" + this.maxHeight + "'>";
      os.write(header.getBytes());

      for (int xLoop = 0; xLoop < this.width; xLoop++) {
         for (int yLoop = 0; yLoop < this.height; yLoop++) {
            WorldGrid.Cell cell = this.grid.get(xLoop, yLoop);
            os.write(cell.toXML(defaultTexture).getBytes());
         }
      }

      String footer = "</grid>";
      os.write(footer.getBytes());
   }

   public void saveAsNBT(TextureName defaultTexture, OutputStream os) throws IOException {
      short height = (short)this.getMaxHeight();
      short width = (short)this.getWidth();
      short length = (short)this.getHeight();
      Tag[] tagArray = new Tag[9];
      tagArray[0] = new Tag(Tag$Type.TAG_Short, "Height", new Short(height));
      tagArray[1] = new Tag(Tag$Type.TAG_Short, "Length", new Short(length));
      tagArray[2] = new Tag(Tag$Type.TAG_Short, "Width", new Short(width));
      tagArray[3] = new Tag("Entities", Tag$Type.TAG_List);
      tagArray[4] = new Tag("TileEntities", Tag$Type.TAG_List);
      tagArray[5] = new Tag(Tag$Type.TAG_String, "Materials", "Alpha");
      int structureSize = width * height * length;
      byte[] structure = new byte[structureSize];
      byte[] data = new byte[structureSize];
      int arrayIndex = 0;
      int layerSize = width * length;

      for (int i = 0; i < structureSize; i++) {
         int x = width - 1 - i % width;
         int y = length - 1 - (int)Math.floor(i % layerSize / width);
         int z = (int)Math.floor(i / layerSize) + 1;
         WorldGrid.Cell cell = this.getCell(x, y);
         WorldGrid.Cell.ZStack stack = null;
         if (cell != null) {
            stack = cell.getStack();
         }

         if (cell != null && stack != null && cell.getStack().containsZValue(z)) {
            WorldGrid.Cell.ZValue value = cell.getStack().getZValue(z);
            TextureName texture = value.getTexture();
            if (texture == TextureName.None) {
               texture = defaultTexture;
            }

            structure[i] = texture.getBlockValue();
            if (texture.hasOrientation()) {
               if (texture.getModelFactoryClass() == StairModelFactory.class) {
                  switch (value.getOrientation()) {
                     case North:
                        data[i] = 1;
                        break;
                     case East:
                        data[i] = 3;
                        break;
                     case South:
                        data[i] = 0;
                        break;
                     case West:
                        data[i] = 2;
                        break;
                     default:
                        data[i] = 0;
                  }
               }
            } else {
               data[i] = texture.getDataValue();
            }
         } else {
            structure[i] = 0;
            data[i] = 0;
         }
      }

      tagArray[6] = new Tag(Tag$Type.TAG_Byte_Array, "Blocks", structure);
      tagArray[7] = new Tag(Tag$Type.TAG_Byte_Array, "Data", data);
      tagArray[8] = new Tag(Tag$Type.TAG_End, null, (Tag[])null);
      new Tag(Tag$Type.TAG_Compound, "Schematic", tagArray).writeTo(os);
   }

   public void trim() {
      this.grid.trim();
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();

      for (int x = 0; x < this.grid.getWidth(); x++) {
         for (int y = 0; y < this.grid.getHeight(); y++) {
            WorldGrid.Cell cell = this.grid.get(x, y);
            cell.setCoordinate(x, y);
         }
      }
   }

   public boolean layerIsEmpty(int z) {
      for (int x = 0; x < this.width; x++) {
         for (int y = 0; y < this.height; y++) {
            if (this.getCell(x, y).getStack().containsZValue(z)) {
               return false;
            }
         }
      }

      return true;
   }

   public void addRow() {
      this.grid.increaseHeight();
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
   }

   public void removeRow() {
      this.grid.decreaseHeight();
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
   }

   public void addColumn() {
      this.grid.increaseWidth();
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
   }

   public void removeColumn() {
      this.grid.decreaseWidth();
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
   }

   public boolean canAddLayer(int layer) {
      return layer <= 128;
   }

   public boolean canRemoveLayer(int layer) {
      return !this.containsZ(layer);
   }

   public void addRow(int row) {
      this.grid.addRow(row);
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
      this.reIndexGrid();
   }

   public void addColumn(int col) {
      this.grid.addColumn(col);
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
      this.reIndexGrid();
   }

   public void addLayer(int layer) {
      for (int x = 0; x < this.width; x++) {
         for (int y = 0; y < this.height; y++) {
            WorldGrid.Cell cell = this.grid.get(x, y);
            if (cell != null) {
               cell.getStack().moveLayers(layer, 1);
            }
         }
      }
   }

   public void removeLayer(int layer) {
      for (int x = 0; x < this.width; x++) {
         for (int y = 0; y < this.height; y++) {
            WorldGrid.Cell cell = this.grid.get(x, y);
            if (cell != null) {
               WorldGrid.Cell.ZStack stack = cell.getStack();
               stack.removeZ(layer);
               stack.moveLayers(layer + 1, -1);
            }
         }
      }
   }

   public void removeRow(int row) {
      this.grid.removeRow(row);
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
      this.reIndexGrid();
   }

   public void removeColumn(int col) {
      this.grid.removeColumn(col);
      this.width = this.grid.getWidth();
      this.height = this.grid.getHeight();
      this.reIndexGrid();
   }

   private void reIndexGrid() {
      for (int x = 0; x < this.grid.getWidth(); x++) {
         for (int y = 0; y < this.grid.getHeight(); y++) {
            WorldGrid.Cell cell = this.grid.get(x, y);
            if (cell != null) {
               cell.setCoordinate(x, y);
            } else {
               this.grid.set(x, y, new WorldGrid.Cell(this, x, y));
            }
         }
      }
   }

   private boolean containsZ(int z) {
      for (int x = 0; x < this.width; x++) {
         for (int y = 0; y < this.height; y++) {
            WorldGrid.Cell cell = this.getCell(x, y);
            if (cell != null && cell.getStack().containsZValue(z)) {
               return true;
            }
         }
      }

      return false;
   }

   public class Cell implements IdentifiesEmptiness {
      private WorldGrid grid;
      private int x;
      private int y;
      private WorldGrid.Cell.ZStack zStack;
      private boolean processed;

      public Cell(WorldGrid grid, int x, int y) {
         this.grid = grid;
         this.x = x;
         this.y = y;
         this.zStack = new WorldGrid.Cell.ZStack(null);
         this.processed = false;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public void setCoordinate(int x, int y) {
         this.x = x;
         this.y = y;
      }

      public WorldGrid.Cell.ZStack getStack() {
         return this.zStack;
      }

      public int getHeight() {
         return this.zStack.getMaxHeight();
      }

      public int getNumberOfBlocks() {
         return this.zStack.getZValues().size();
      }

      public void setProcessed(boolean processed) {
         this.processed = processed;
      }

      public boolean isProcessed() {
         return this.processed;
      }

      public String toXML(TextureName defaultTexture) {
         if (this.getStack().isEmpty()) {
            return "";
         }

         StringBuffer buffer = new StringBuffer();
         buffer.append("<cell x='" + this.getX() + "' " + "y" + "='" + this.getY() + "'>");

         for (WorldGrid.Cell.ZValue z : this.getStack().getZValues()) {
            TextureName texture = z.getTexture();
            if ((texture == null || texture == TextureName.None) && defaultTexture != null && defaultTexture != TextureName.None) {
               texture = defaultTexture;
            }

            buffer.append(
               "<block height='" + z.getHeight() + "' " + "texture" + "='" + texture.toString() + "' " + "orientation" + "='" + z.getOrientation() + "'/>"
            );
         }

         buffer.append("</cell>");
         return buffer.toString();
      }

      @Override
      public boolean isEmpty() {
         return this.getStack().isEmpty();
      }

      public class ZStack {
         private HashMap<Integer, WorldGrid.Cell.ZValue> heights = new HashMap<>();
         private boolean minHeightSet = false;
         private int minHeight = 0;
         private int maxHeight = 0;

         private ZStack() {
         }

         public WorldGrid.Cell.ZValue getZValue(int height) {
            return this.heights.get(height);
         }

         public boolean containsZValue(int height) {
            return this.containsZValue(Cell.this.new ZValue(height));
         }

         public boolean containsZValue(WorldGrid.Cell.ZValue zValue) {
            return this.heights.values().contains(zValue);
         }

         public synchronized void removeZ(int z) {
            if (this.heights.containsKey(z)) {
               WorldGrid.Cell.ZValue removed = this.heights.remove(z);
               Cell.this.grid.getStatistics().decrementCount(removed.getTexture());
               if (z == this.maxHeight || z == this.minHeight) {
                  this.computeMinMax();
               }
            }
         }

         public void addZ(int from, int to) {
            for (int i = from; i <= to; i++) {
               this.addZ(i);
            }
         }

         public void addZ(int z) {
            this.addZ(z, TextureName.None, minecraft.planner.model.grid.attributes.Orientation.None);
         }

         public void addZ(int from, int to, TextureName texture) {
            for (int i = from; i <= to; i++) {
               this.addZ(i, texture, minecraft.planner.model.grid.attributes.Orientation.None);
            }
         }

         public synchronized void addZ(int z, TextureName texture) {
            this.addZ(z, texture, minecraft.planner.model.grid.attributes.Orientation.None);
         }

         public synchronized void addZ(int z, TextureName texture, minecraft.planner.model.grid.attributes.Orientation orientation) {
            if (z > 0) {
               if (this.heights.isEmpty()) {
                  this.minHeight = z;
                  this.maxHeight = z;
               } else {
                  if (!this.minHeightSet || z < this.minHeight) {
                     this.minHeight = z;
                     this.minHeightSet = true;
                  }

                  if (z > this.maxHeight) {
                     this.maxHeight = z;
                  }
               }

               WorldGrid.Cell.ZValue existingZValue = this.heights.get(z);
               if (existingZValue != null) {
                  Cell.this.grid.getStatistics().decrementCount(existingZValue.getTexture());
               }

               this.heights.put(z, Cell.this.new ZValue(z, texture, orientation));
               Cell.this.grid.getStatistics().incrementCount(texture);
            }
         }

         public synchronized void moveLayers(int fromLayer, int increment) {
            Set<WorldGrid.Cell.ZValue> valuesToMove = new HashSet<>();
            Set<Integer> heightsToRemove = new HashSet<>();

            for (int height : this.heights.keySet()) {
               if (height >= fromLayer) {
                  valuesToMove.add(this.heights.get(height));
                  heightsToRemove.add(height);
               }
            }

            for (int height : heightsToRemove) {
               this.heights.remove(height);
            }

            for (WorldGrid.Cell.ZValue value : valuesToMove) {
               value.setHeight(value.getHeight() + increment);
               this.heights.put(value.getHeight(), value);
            }

            this.computeMinMax();
         }

         public synchronized boolean isEmpty() {
            return this.heights.isEmpty();
         }

         public synchronized void mirrorZ(int shift) {
            HashMap<Integer, WorldGrid.Cell.ZValue> newHeights = new HashMap<>(2 * this.heights.size());

            for (WorldGrid.Cell.ZValue value : this.heights.values()) {
               TextureName texture = value.getTexture();
               WorldGrid.Cell.ZValue top = Cell.this.new ZValue(shift + value.getHeight(), texture, value.getOrientation());
               WorldGrid.Cell.ZValue bottom = Cell.this.new ZValue(shift - value.getHeight() + 1, texture, value.getOrientation());
               newHeights.put(top.height, top);
               newHeights.put(bottom.height, bottom);
            }

            this.heights.clear();
            this.heights = newHeights;
            this.computeMinMax();
         }

         private void computeMinMax() {
            this.minHeightSet = false;
            this.minHeight = 0;
            this.maxHeight = 0;

            for (Integer value : this.heights.keySet()) {
               if (!this.minHeightSet || value > 0 && value < this.minHeight) {
                  this.minHeight = value;
                  this.minHeightSet = true;
               }

               if (value > this.maxHeight) {
                  this.maxHeight = value;
               }
            }
         }

         public synchronized boolean zPresent(int z) {
            return this.heights.keySet().contains(z);
         }

         public synchronized void clear() {
            this.heights.clear();
            this.minHeight = 0;
            this.minHeightSet = false;
            this.maxHeight = 0;
         }

         public synchronized int getMinHeight() {
            return this.minHeight;
         }

         public synchronized int getMaxHeight() {
            return this.maxHeight;
         }

         public synchronized Collection<WorldGrid.Cell.ZValue> getZValues() {
            return Collections.unmodifiableCollection(this.heights.values());
         }
      }

      public class ZValue {
         private int height;
         private TextureName texture;
         private minecraft.planner.model.grid.attributes.Orientation orientation;
         private Properties properties;

         public ZValue(int height) {
            this(height, TextureName.None);
         }

         public ZValue(int height, TextureName texture) {
            this(height, texture, minecraft.planner.model.grid.attributes.Orientation.None);
         }

         public ZValue(int height, TextureName texture, minecraft.planner.model.grid.attributes.Orientation orientation) {
            this.height = height;
            this.texture = texture;
            this.orientation = orientation;
            this.properties = null;
         }

         public int getHeight() {
            return this.height;
         }

         protected void setHeight(int height) {
            this.height = height;
         }

         public TextureName getTexture() {
            return this.texture;
         }

         public minecraft.planner.model.grid.attributes.Orientation getOrientation() {
            return this.orientation;
         }

         public void setProperties(Properties properties) {
            this.properties = properties;
         }

         public Properties getProperties() {
            return this.properties;
         }

         public void setTexture(TextureName texture) {
            if (this.texture != texture) {
               Cell.this.grid.getStatistics().decrementCount(this.texture);
            }

            this.texture = texture;
            Cell.this.grid.getStatistics().incrementCount(texture);
         }

         public void setOrientation(minecraft.planner.model.grid.attributes.Orientation orientation) {
            this.orientation = orientation;
         }

         @Override
         public int hashCode() {
            return this.height;
         }

         @Override
         public boolean equals(Object o) {
            if (o instanceof WorldGrid.Cell.ZValue) {
               WorldGrid.Cell.ZValue v = (WorldGrid.Cell.ZValue)o;
               return this.height == v.height;
            } else {
               return Integer.class.isAssignableFrom(o.getClass()) ? this.height == (Integer)o : false;
            }
         }
      }
   }

   public class GridStatistics {
      private Map<TextureName, Integer> blockStatistics;
      private Collection<StatisticsChangeListener> changeListeners = new HashSet<>();

      public GridStatistics() {
         this.blockStatistics = new HashMap<>();
      }

      public void incrementCount(TextureName name) {
         synchronized (this.blockStatistics) {
            if (!this.blockStatistics.containsKey(name)) {
               this.blockStatistics.put(name, 0);
            }

            this.blockStatistics.put(name, this.blockStatistics.get(name) + 1);
         }
      }

      public void decrementCount(TextureName name) {
         synchronized (this.blockStatistics) {
            if (this.blockStatistics.containsKey(name)) {
               int value = this.blockStatistics.get(name);
               if (value > 0) {
                  this.blockStatistics.put(name, value - 1);
               }
            }
         }
      }

      public void resetStatistics() {
         synchronized (this.blockStatistics) {
            this.blockStatistics.clear();
         }
      }

      public int getCount(TextureName name) {
         synchronized (this.blockStatistics) {
            return this.blockStatistics.containsKey(name) ? this.blockStatistics.get(name) : 0;
         }
      }

      public void registerStatisticsChangeListener(StatisticsChangeListener listener) {
         synchronized (this.changeListeners) {
            this.changeListeners.add(listener);
         }
      }

      public void unregisterStatisticsChangeListener(StatisticsChangeListener listener) {
         synchronized (this.changeListeners) {
            this.changeListeners.remove(listener);
         }
      }

      public void fireStatisticsChangedEvent() {
         for (StatisticsChangeListener listener : this.changeListeners) {
            new StatisticsUpdateThread(this, listener).start();
         }
      }
   }
}
