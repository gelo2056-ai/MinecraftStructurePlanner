package minecraft.planner.model.dome;

import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class DomeModel extends StructureModel<DomeParameters> {
   public static final String NAME = "Domed Roof";

   public void generate(DomeParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      int width = parameters.getWidth();
      int height = parameters.getHeight();
      int maxVertical = parameters.getMaxVertical();
      int baseHeight = parameters.getBaseHeight();
      boolean xInterpolate = parameters.xInterpolate();
      boolean yInterpolate = parameters.yInterpolate();
      boolean squareBase = parameters.squareBase();
      grid.initialize(width, height);

      for (int xLoop = 0; xLoop < width; xLoop++) {
         double sineVerticalModifier = 1.0;
         if (xInterpolate) {
            sineVerticalModifier = Math.sin(Math.PI * ((xLoop + 0.5) / width));
         }

         int maxHeightForColumn = (int)Math.round(sineVerticalModifier * maxVertical);

         for (int yLoop = 0; yLoop < parameters.getHeight(); yLoop++) {
            double sineColumnModifier = 1.0;
            if (yInterpolate) {
               sineColumnModifier = Math.sin(Math.PI * ((yLoop + 0.5) / height));
            }

            int cellHeight = (int)Math.round(sineColumnModifier * maxHeightForColumn);
            if (squareBase || cellHeight > 0) {
               cellHeight += baseHeight;
            }

            grid.getCell(xLoop, yLoop).getStack().addZ(cellHeight);
         }
      }

      for (int xLoop = 0; xLoop < width; xLoop++) {
         for (int yLoop = 0; yLoop < height; yLoop++) {
            WorldGrid$Cell cell = this.grid.getCell(xLoop, yLoop);
            int cellHeight = cell.getStack().getMaxHeight();
            if (cellHeight > 0) {
               int visibleHeight = this.calculateVisibleHeight(cell);
               cell.getStack().addZ(visibleHeight, cellHeight);
            }
         }
      }

      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Domed Roof";
   }
}
