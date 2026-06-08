package minecraft.planner.model.statue;

import minecraft.planner.gui.displaypanels.TextureFilterChangeListener;
import minecraft.planner.model.easterisland.EasterIslandModel;
import minecraft.planner.model.easterisland.EasterIslandParameters;
import minecraft.planner.model.grid.WorldGrid;

public class StatueModel extends EasterIslandModel implements TextureFilterChangeListener {
   public static final String NAME = "Statue";

   @Override
   public void generate(EasterIslandParameters parameters) {
      super.generate(parameters);
   }

   @Override
   protected void generateModel() {
      WorldGrid grid = this.getWorldGrid();
      grid.initialize(16, 16, false);
      this.head(8, 8, 0);
   }

   @Override
   public String getStructureName() {
      return "Statue";
   }
}
