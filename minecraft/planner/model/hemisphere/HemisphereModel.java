package minecraft.planner.model.hemisphere;

import minecraft.planner.gui.JStatusBar$Mode;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class HemisphereModel extends StructureModel<HemisphereParameters> {
   public static final String NAME = "Hemispherical Dome";

   private boolean isWithinDome(int origin, int radius, int x, int y) {
      int xSquared = (x - origin) * (x - origin);
      int ySquared = (y - origin) * (y - origin);
      double distance = Math.sqrt(xSquared + ySquared);
      return Math.round(distance) <= radius;
   }

   private int heightOfDome(int origin, int radius, int x, int y, int baseHeight) {
      if (this.isWithinDome(origin, radius, x, y)) {
         int xSquared = (x - origin) * (x - origin);
         int ySquared = (y - origin) * (y - origin);
         int rSquared = radius * radius;
         return 1 + (int)Math.round(Math.sqrt(rSquared - xSquared - ySquared)) + baseHeight;
      } else {
         return 0;
      }
   }

   public void generate(HemisphereParameters parameters) {
      super.generate(parameters);

      try {
         StructurePlanner.setStatusMode(JStatusBar$Mode.Percent);
         WorldGrid grid = this.getWorldGrid();
         int radius = parameters.getRadius();
         int maxVertical = parameters.getMaxVertical();
         int baseHeight = parameters.getBaseHeight();
         int gridDimension = radius * 2 + 1;
         grid.initialize(gridDimension, gridDimension);
         double verticalModifier = 0.0;
         boolean verticalModifierApplied = false;
         if (maxVertical > 0) {
            verticalModifier = (double)(maxVertical - 1) / radius;
            verticalModifierApplied = true;
         }

         int origin = (int)Math.floor(gridDimension / 2.0);
         double total = gridDimension * gridDimension * 2;
         double count = 0.0;

         for (int xLoop = 0; xLoop < gridDimension; xLoop++) {
            for (int yLoop = 0; yLoop < gridDimension; yLoop++) {
               int cellHeight = this.heightOfDome(origin, radius, xLoop, yLoop, baseHeight);
               if (verticalModifierApplied) {
                  cellHeight = (int)Math.round(cellHeight * verticalModifier);
               }

               grid.getCell(xLoop, yLoop).getStack().addZ(cellHeight);
               count++;
               StructurePlanner.setPercent(count / total);
            }
         }

         for (int xLoop = 0; xLoop < gridDimension; xLoop++) {
            for (int yLoop = 0; yLoop < gridDimension; yLoop++) {
               WorldGrid$Cell cell = this.grid.getCell(xLoop, yLoop);
               int cellHeight = cell.getStack().getMaxHeight();
               if (cellHeight > 0) {
                  int visibleHeight = this.calculateVisibleHeight(cell);
                  cell.getStack().addZ(visibleHeight, cellHeight);
                  count++;
                  StructurePlanner.setPercent(count / total);
               }
            }
         }
      } finally {
         StructurePlanner.setStatusMode(JStatusBar$Mode.Text);
      }

      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   @Override
   public String getStructureName() {
      return "Hemispherical Dome";
   }
}
