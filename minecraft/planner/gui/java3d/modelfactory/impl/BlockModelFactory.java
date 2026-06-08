package minecraft.planner.gui.java3d.modelfactory.impl;

import com.sun.j3d.utils.geometry.Box;
import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.Appearance;
import minecraft.planner.gui.java3d.TextureCache;
import minecraft.planner.gui.java3d.modelfactory.ModelFactory;
import minecraft.planner.gui.java3d.modelfactory.ModelParameters;
import minecraft.planner.gui.textures.TextureName;

public class BlockModelFactory extends ModelFactory {
   private static final float SIZE_X = 0.5F;
   private static final float SIZE_Y = 0.5F;
   private static final float SIZE_Z = 0.5F;
   private final Map<TextureName, BlockModel> boxMap = new HashMap<>();
   private static final int PRIMITIVE_FLAGS = 67;

   @Override
   public BlockModel getModel(ModelParameters p) {
      TextureName texture = p.getTexture();
      Appearance appearance = TextureCache.getTexture(this.texturePack, texture);
      BlockModel sharedGroup = null;
      synchronized (this.boxMap) {
         if (!this.boxMap.containsKey(texture)) {
            sharedGroup = new BlockModel(p);
            sharedGroup.addChild(new Box(0.5F, 0.5F, 0.5F, 67, appearance));
            this.boxMap.put(texture, sharedGroup);
         } else {
            sharedGroup = this.boxMap.get(texture);
         }

         return sharedGroup;
      }
   }
}
