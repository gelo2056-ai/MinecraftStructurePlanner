package minecraft.planner.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import minecraft.planner.gui.JStatusBar$Mode;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.Voxel;
import minecraft.planner.gui.displaypanels.LayerPainter;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.grid.UndoBuffer;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.util.UserConfigurationManager;

public abstract class StructureModel<P extends StructureParameters> implements GenerateListener<P> {
   private Set<StructureModelChangeListener> listeners = new HashSet<>();
   protected boolean initialized = false;
   protected WorldGrid grid = new WorldGrid();
   protected P parameters = (P)null;
   protected TexturePack defaultTexturePack = UserConfigurationManager.getSelectedTexturePack();
   protected TextureName defaultTexture = TextureName.None;
   protected long startTime;
   protected long endTime;
   protected ModelSaveInformation saveInfo = new ModelSaveInformation();
   protected String notes = null;

   protected StructureModel() {
   }

   public ModelSaveInformation getModelSaveInfo() {
      return this.saveInfo;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public WorldGrid getWorldGrid() {
      return this.grid;
   }

   public P getParameters() {
      return this.parameters;
   }

   public boolean includeVisibleBlocksParameter() {
      return true;
   }

   public void registerListener(StructureModelChangeListener listener) {
      this.listeners.add(listener);
   }

   public void unregisterListener(StructureModelChangeListener listener) {
      this.listeners.remove(listener);
   }

   public void notifyListenersOfChange(Object originator) {
      this.grid.updateMetrics(this);
      this.grid.getStatistics().fireStatisticsChangedEvent();

      for (StructureModelChangeListener listener : this.listeners) {
         listener.structureModelChanged(this);
      }
   }

   public abstract void initialize();

   public abstract String getStructureName();

   @Override
   public void generate(P parameters) {
      StructurePlanner.setStatus("Generating " + this.getStructureName() + "...");
      this.parameters = parameters;
      this.startTime = System.currentTimeMillis();
   }

   private static String formatTime(long milliseconds) {
      DecimalFormat formatter = new DecimalFormat("#,##0");
      return formatter.format(milliseconds);
   }

   public void generated() {
      this.endTime = System.currentTimeMillis();
      StructurePlanner.setStatusMode(JStatusBar$Mode.Text);
      StructurePlanner.setStatus(this.getStructureName() + " model generated in " + formatTime(this.endTime - this.startTime) + "ms");
      this.saveInfo.setChanged();
      this.notifyListenersOfChange(this);
   }

   public int calculateVisibleHeight(WorldGrid$Cell cell) {
      int width = this.getWorldGrid().getWidth();
      int height = this.getWorldGrid().getHeight();
      int northHeight = 0;
      int westHeight = 0;
      int southHeight = 0;
      int eastHeight = 0;
      int x = cell.getX();
      int y = cell.getY();
      int thisHeight = cell.getHeight();
      int visibleHeight = 0;
      if (thisHeight > 0) {
         if (x > 0) {
            westHeight = this.getWorldGrid().getCell(x - 1, y).getHeight();
         }

         if (x < width - 1) {
            eastHeight = this.getWorldGrid().getCell(x + 1, y).getHeight();
         }

         if (y > 0) {
            southHeight = this.getWorldGrid().getCell(x, y - 1).getHeight();
         }

         if (y < height - 1) {
            northHeight = this.getWorldGrid().getCell(x, y + 1).getHeight();
         }

         visibleHeight = this.minimumHeight(northHeight, southHeight, eastHeight, westHeight);
      }

      return visibleHeight;
   }

   protected int minimumHeight(int... heights) {
      int minHeight = Integer.MAX_VALUE;
      int[] var6 = heights;
      int var5 = heights.length;

      for (int var4 = 0; var4 < var5; var4++) {
         int height = var6[var4];
         if (height < minHeight) {
            minHeight = height;
         }
      }

      return minHeight;
   }

   protected void removeHiddenVoxels() {
      Set<Voxel> hiddenVoxels = new HashSet<>();

      for (int x = 1; x <= this.grid.getWidth(); x++) {
         for (int y = 1; y <= this.grid.getHeight(); y++) {
            for (int z = 1; z <= this.grid.getMaxHeight(); z++) {
               WorldGrid$Cell cell = this.grid.getCell(x, y);
               if (cell != null && cell.getStack().containsZValue(z) && this.hiddenVoxel(x, y, z)) {
                  hiddenVoxels.add(new Voxel(x, y, z));
               }
            }
         }
      }

      for (Voxel voxel : hiddenVoxels) {
         this.grid.getCell(voxel.getX(), voxel.getY()).getStack().removeZ(voxel.getZ());
      }
   }

   protected boolean hiddenVoxel(int x, int y, int z) {
      WorldGrid$Cell left = this.grid.getCell(x - 1, y);
      WorldGrid$Cell right = this.grid.getCell(x + 1, y);
      WorldGrid$Cell back = this.grid.getCell(x, y + 1);
      WorldGrid$Cell front = this.grid.getCell(x, y - 1);
      WorldGrid$Cell here = this.grid.getCell(x, y);
      return left != null
         && right != null
         && front != null
         && back != null
         && here != null
         && left.getStack().containsZValue(z)
         && right.getStack().containsZValue(z)
         && back.getStack().containsZValue(z)
         && front.getStack().containsZValue(z)
         && here.getStack().containsZValue(z - 1)
         && here.getStack().containsZValue(z + 1);
   }

   public TextureName getDefaultTexture() {
      return this.defaultTexture;
   }

   public void setDefaultTexture(TextureName name) {
      this.defaultTexture = name;
   }

   public String getNotes() {
      return this.notes;
   }

   public void setNotes(String notes) {
      this.notes = notes;
   }

   public boolean undo(UndoBuffer buffer) {
      buffer.undo(this.getWorldGrid());
      this.notifyListenersOfChange(this);
      return true;
   }

   public void saveAsSchematic(File file) throws IOException {
      FileOutputStream outputStream = new FileOutputStream(file);
      this.grid.saveAsNBT(this.getDefaultTexture(), outputStream);
      outputStream.flush();
      outputStream.close();
      this.saveInfo.setFile(file);
   }

   public BufferedImage toImage(int cellSize) {
      WorldGrid grid = this.getWorldGrid();
      LayerPainter<P> painter = new LayerPainter<>(this, this.defaultTexturePack, false, false, false);
      int maxHeight = grid.getMaxHeight();
      int layoutSize = (int)Math.ceil(Math.sqrt(maxHeight));
      int layoutRows = (int)Math.ceil((double)maxHeight / layoutSize);
      int gridPixelWidth = grid.getWidth() * cellSize;
      int gridPixelHeight = grid.getHeight() * cellSize;
      int bufferWidth = (gridPixelWidth + 50) * layoutSize;
      int bufferHeight = (gridPixelHeight + 75) * layoutRows;
      BufferedImage image = new BufferedImage(bufferWidth, bufferHeight, 1);
      Graphics2D g = image.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, bufferWidth, bufferHeight);
      g.setFont(new Font("Dialog", 1, 14));

      for (int layer = 1; layer <= maxHeight; layer++) {
         int layoutX = (layer - 1) % layoutSize;
         int layoutY = (int)Math.floor((layer - 1) / layoutSize);
         int xOffset = layoutX * gridPixelWidth + (layoutX + 1) * 25;
         int yOffset = layoutY * gridPixelHeight + (layoutY + 1) * 50;
         g.setColor(Color.BLACK);
         g.drawString(this.getStructureName() + ": Layer " + layer, xOffset, yOffset - 10);
         painter.paintLayer(g, layer, cellSize, xOffset, yOffset);
      }

      g.dispose();
      return image;
   }

   public void saveAsImage(File file, int cellSize) throws IOException {
      if (ImageIO.write(this.toImage(cellSize), "png", file)) {
         StructurePlanner.setStatus("Successfully exported " + this.getStructureName() + " plans to " + file.getName());
      } else {
         StructurePlanner.setStatus("Failed to exported " + this.getStructureName() + " plans to " + file.getName());
      }
   }

   public void saveAsXML(File file) throws IOException {
      FileOutputStream outputStream = new FileOutputStream(file);
      String header = "<structure name=\"" + this.getStructureName() + "\">";
      outputStream.write(header.getBytes());
      TextureName defaultTexture = this.getDefaultTexture();
      if (defaultTexture == null || defaultTexture == TextureName.None) {
         defaultTexture = TextureName.Dirt;
      }

      this.grid.saveAsXML(defaultTexture, outputStream);
      if (this.getNotes() != null && this.getNotes().trim().length() > 0) {
         StringBuffer notes = new StringBuffer();
         notes.append("<").append("notes").append("><![CDATA[");
         notes.append(this.getNotes());
         notes.append("]]></").append("notes").append(">");
         outputStream.write(notes.toString().getBytes());
      }

      String footer = "</structure>";
      outputStream.write(footer.getBytes());
      outputStream.flush();
      outputStream.close();
      this.saveInfo.setFile(file);
   }
}
