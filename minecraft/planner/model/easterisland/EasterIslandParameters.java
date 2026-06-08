package minecraft.planner.model.easterisland;

import javax.swing.ImageIcon;
import minecraft.planner.model.StructureParameters;

public class EasterIslandParameters extends StructureParameters {
   private ImageIcon characterImage;

   public EasterIslandParameters(ImageIcon characterImage) {
      this.characterImage = characterImage;
   }

   public ImageIcon getCharacterImage() {
      return this.characterImage;
   }
}
