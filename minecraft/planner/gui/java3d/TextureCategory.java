package minecraft.planner.gui.java3d;

public enum TextureCategory {
   AlwaysAvailable("Blocks which are always available"),
   RegularWorld("Blocks from the regular game world"),
   NetherWorld("Blocks from the Nether"),
   Importable("Blocks only available through importing to the game world"),
   Gatherable("Blocks which can be gathered"),
   Craftable("Blocks which can be crafted"),
   Plant("Plant blocks"),
   Wool("Wool blocks"),
   Ore("Ore blocks"),
   Refined("Refined ore blocks");

   private String description;

   TextureCategory(String description) {
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }
}
