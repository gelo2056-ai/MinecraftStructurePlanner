package minecraft.planner.model.viaduct;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class ViaductModel extends StructureModel<ViaductParameters> {
   public static final String NAME = "Viaduct";

   public synchronized void generate(ViaductParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      grid.initialize(parameters.getViaductSpan(), parameters.getViaductDepth());
      int archDiameter = 2 * parameters.getArchRadius() - 1;
      int widthPerSpan = archDiameter + parameters.getTowerWidth();
      int deckHeight = parameters.getTowerHeight() + parameters.getArchHeight() + 1;
      int numberOfArches = (int)Math.floor(parameters.getViaductSpan() / widthPerSpan);
      int numberOfTowers = numberOfArches + 1;
      int remainderSpan = parameters.getViaductSpan() - numberOfArches * widthPerSpan - parameters.getTowerWidth();
      if (remainderSpan <= 1) {
         numberOfArches--;
         numberOfTowers--;
         remainderSpan = parameters.getViaductSpan() - numberOfArches * widthPerSpan - parameters.getTowerWidth();
      }

      int inset = remainderSpan / 2;
      this.generateViaductDeck(
         parameters.getViaductSpan(),
         deckHeight,
         parameters.getDeckThickness(),
         parameters.getViaductDepth(),
         parameters.getViaductTexture(),
         parameters.getWallHeight(),
         parameters.getWallTexture()
      );
      this.generateViaductTowers(
         numberOfTowers,
         inset,
         widthPerSpan,
         parameters.getTowerHeight() + parameters.getArchHeight(),
         parameters.getTowerWidth(),
         parameters.getViaductDepth(),
         parameters.getViaductTexture()
      );
      this.generateArches(
         numberOfArches,
         inset,
         parameters.getTowerHeight() + 1,
         parameters.getTowerWidth(),
         parameters.getViaductDepth(),
         widthPerSpan,
         parameters.getArchRadius(),
         parameters.getArchHeight(),
         parameters.getViaductTexture()
      );
      this.generateLeftEndArch(
         inset,
         parameters.getTowerHeight() + 1,
         parameters.getArchRadius(),
         parameters.getArchHeight(),
         inset,
         parameters.getViaductDepth(),
         parameters.getViaductTexture()
      );
      int rightEndArchX = numberOfArches * widthPerSpan + parameters.getTowerWidth() + inset;
      int rightEndArchSpan = parameters.getViaductSpan() - rightEndArchX;
      this.generateRightEndArch(
         rightEndArchX,
         parameters.getTowerHeight() + 1,
         parameters.getArchRadius(),
         parameters.getArchHeight(),
         rightEndArchSpan,
         parameters.getViaductDepth(),
         parameters.getViaductTexture()
      );
      grid.updateMetrics(this);
      super.removeHiddenVoxels();
      super.generated();
   }

   private void generateArches(
      int numberOfArches, int spanInset, int towerHeight, int towerWidth, int towerDepth, int widthPerSpan, int archRadius, int archHeight, TextureName texture
   ) {
      for (int arch = 1; arch <= numberOfArches; arch++) {
         int archStart = spanInset + arch * widthPerSpan - archRadius;
         this.generateArch(archStart, towerHeight, archRadius, archHeight, towerDepth, texture);
      }
   }

   private void generateLeftEndArch(int archEndX, int archY, int archRadius, int archHeight, int archSpan, int archDepth, TextureName texture) {
      WorldGrid grid = this.getWorldGrid();
      double step = (Math.PI / 2) / archRadius;

      for (int x = 0; x < archSpan; x++) {
         int height = (int)Math.ceil(archY + Math.sin(x * step) * archHeight);

         for (int y = 0; y < archDepth; y++) {
            grid.getCell(archEndX - x, y).getStack().addZ(height, archY + archHeight, texture);
         }
      }
   }

   private void generateRightEndArch(int archEndX, int archY, int archRadius, int archHeight, int archSpan, int archDepth, TextureName texture) {
      WorldGrid grid = this.getWorldGrid();
      double step = (Math.PI / 2) / archRadius;

      for (int x = 0; x < archSpan; x++) {
         int height = (int)Math.ceil(archY + Math.sin(x * step) * archHeight);

         for (int y = 0; y < archDepth; y++) {
            grid.getCell(archEndX + x - 1, y).getStack().addZ(height, archY + archHeight, texture);
         }
      }
   }

   private void generateArch(int archX, int archY, int archRadius, int archHeight, int archDepth, TextureName texture) {
      WorldGrid grid = this.getWorldGrid();
      double step = (Math.PI / 2) / archRadius;

      for (int x = 0; x < archRadius; x++) {
         int height = (int)Math.ceil(archY + Math.sin(step * x) * archHeight);

         for (int y = 0; y < archDepth; y++) {
            grid.getCell(archX - archRadius + x + 1, y).getStack().addZ(height, archY + archHeight, texture);
            grid.getCell(archX + (archRadius - x - 1), y).getStack().addZ(height, archY + archHeight, texture);
         }
      }
   }

   private void generateViaductTowers(int numberOfTowers, int spanInset, int widthPerSpan, int height, int width, int depth, TextureName texture) {
      WorldGrid grid = this.getWorldGrid();

      for (int tower = 0; tower < numberOfTowers; tower++) {
         int startOfTower = spanInset + tower * widthPerSpan;

         for (int x = startOfTower; x < startOfTower + width; x++) {
            if (x != startOfTower && x != startOfTower + width - 1) {
               grid.getCell(x, 0).getStack().addZ(1, height, texture);
               grid.getCell(x, depth - 1).getStack().addZ(1, height, texture);
            } else {
               for (int y = 0; y < depth; y++) {
                  grid.getCell(x, y).getStack().addZ(1, height, texture);
               }
            }
         }
      }
   }

   private void generateViaductDeck(int span, int height, int thickness, int depth, TextureName deckTexture, int wallHeight, TextureName wallTexture) {
      WorldGrid grid = this.getWorldGrid();
      int deckTop = height + thickness - 1;

      for (int x = 0; x < span; x++) {
         for (int y = 0; y < depth; y++) {
            grid.getCell(x, y).getStack().addZ(height, deckTop, deckTexture);
            if (wallHeight > 0 && (y == 0 || y == depth - 1)) {
               grid.getCell(x, y).getStack().addZ(deckTop + 1, deckTop + wallHeight, wallTexture);
            }
         }
      }
   }

   @Override
   public boolean includeVisibleBlocksParameter() {
      return false;
   }

   @Override
   public String getStructureName() {
      return "Viaduct";
   }

   @Override
   public int calculateVisibleHeight(WorldGrid$Cell cell) {
      return 0;
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }
}
