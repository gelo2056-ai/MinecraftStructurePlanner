package minecraft.planner.model.bitmap;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureModel;

public class BitmapModel extends StructureModel<BitmapParameters> {
   public static final String NAME = "Pixel Art";

   public void generate(BitmapParameters parameters) {
      super.generate(parameters);
      this.grid.initialize(parameters.getWidth(), parameters.getHeight(), false);
      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   public void fireModelChangedEvent() {
      this.notifyListenersOfChange(this);
   }

   @Override
   public String getStructureName() {
      return "Pixel Art";
   }

   @Override
   public TextureName getDefaultTexture() {
      return null;
   }
}
