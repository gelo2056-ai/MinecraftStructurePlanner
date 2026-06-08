package minecraft.planner.gui.java3d.modelfactory;

import java.util.HashMap;
import java.util.Map;
import minecraft.planner.gui.JErrorDialog;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.java3d.modelfactory.impl.BlockModel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.UserConfigurationManager;

public class ModelFactory {
   private final Map<Class<? extends ModelFactory>, ModelFactory> factoryMap = new HashMap<>();
   protected TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();

   public static float getXSize() {
      return 0.5F;
   }

   public static float getYSize() {
      return 0.5F;
   }

   public static float getZSize() {
      return 0.5F;
   }

   public synchronized ModelFactory getFactory(TextureName texture) {
      try {
         Class<? extends ModelFactory> factoryClass = texture.getModelFactoryClass();
         ModelFactory instance = null;
         if (this.factoryMap.containsKey(factoryClass)) {
            instance = this.factoryMap.get(factoryClass);
         } else {
            instance = factoryClass.newInstance();
            this.factoryMap.put(factoryClass, instance);
         }

         return instance;
      } catch (Throwable t) {
         JErrorDialog error = new JErrorDialog(StructurePlanner.getFrame(), "Failed to construct Model Factory " + texture.getModelFactoryClass().getName(), t);
         error.setVisible(true);
         return null;
      }
   }

   public BlockModel getModel(ModelParameters p) {
      ModelFactory factoryInstance = this.getFactory(p.getTexture());
      return factoryInstance.getModel(p);
   }
}
