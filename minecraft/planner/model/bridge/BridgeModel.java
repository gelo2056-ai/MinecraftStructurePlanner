package minecraft.planner.model.bridge;

import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class BridgeModel extends StructureModel<BridgeParameters> {
   public static final String NAME = "Arch Bridge";

   public void generate(BridgeParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      int width = parameters.getWidth();
      int span = parameters.getSpan();
      int maxHeight = parameters.getMaxHeight();
      int minStepWidth = parameters.getMinStepWidth();
      int wallHeight = parameters.getWallHeight();
      boolean fullySupported = parameters.isFullySupported();
      boolean inverted = parameters.isInverted();
      grid.initialize(span, width);
      int previousHeight = 0;

      for (int xLoop = 0; xLoop < span; xLoop++) {
         double sineVerticalModifier = Math.sin(Math.PI * ((xLoop + 0.5) / span));
         int maxHeightForSpan = (int)Math.ceil(sineVerticalModifier * maxHeight);

         for (int yLoop = 0; yLoop < width; yLoop++) {
            WorldGrid$Cell cell = grid.getCell(xLoop, yLoop);
            if (wallHeight > 0 && (yLoop == 0 || yLoop == width - 1)) {
               cell.getStack().addZ(maxHeightForSpan, maxHeightForSpan + wallHeight);
               WorldGrid$Cell supportCell = null;
               if (xLoop >= 1 && previousHeight < maxHeightForSpan) {
                  supportCell = grid.getCell(xLoop - 1, yLoop);
               } else if (previousHeight > maxHeightForSpan) {
                  supportCell = grid.getCell(xLoop, yLoop);
               }

               if (supportCell != null) {
                  supportCell.getStack().addZ(supportCell.getHeight() + 1);
               }
            } else {
               cell.getStack().addZ(maxHeightForSpan);
            }

            if (fullySupported && previousHeight > 0) {
               WorldGrid$Cell supportCell = null;
               if (previousHeight < maxHeightForSpan) {
                  supportCell = grid.getCell(xLoop, yLoop);
                  if (supportCell != null) {
                     supportCell.getStack().addZ(previousHeight);
                  }
               } else if (previousHeight > maxHeightForSpan) {
                  supportCell = grid.getCell(xLoop - 1, yLoop);
                  if (supportCell != null) {
                     supportCell.getStack().addZ(maxHeightForSpan);
                  }
               }
            }
         }

         previousHeight = maxHeightForSpan;
      }

      super.generated();
   }

   @Override
   public boolean includeVisibleBlocksParameter() {
      return false;
   }

   @Override
   public String getStructureName() {
      return "Arch Bridge";
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
