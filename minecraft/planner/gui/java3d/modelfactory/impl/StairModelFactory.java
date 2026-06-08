package minecraft.planner.gui.java3d.modelfactory.impl;

import com.sun.j3d.utils.geometry.Box;
import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import minecraft.planner.gui.java3d.TextureCache;
import minecraft.planner.gui.java3d.modelfactory.ModelFactory;
import minecraft.planner.gui.java3d.modelfactory.ModelParameters;
import minecraft.planner.gui.java3d.modelfactory.Orientable;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.grid.attributes.Orientation;

public class StairModelFactory extends ModelFactory implements Orientable {
   private final Map<String, BlockModel> modelMap = new HashMap<>();
   private static final int PRIMITIVE_FLAGS = 67;
   private static final float SIZE_X = 0.5F;
   private static final float SIZE_Y = 0.25F;
   private static final float SIZE_Z = 0.5F;

   private String generateKey(ModelParameters p) {
      return p.getTexture().name() + "|" + p.getOrientation().toString();
   }

   @Override
   public BlockModel getModel(ModelParameters p) {
      Appearance appearance = TextureCache.getTexture(this.texturePack, p.getTexture());
      TextureName texture = p.getTexture();
      BlockModel sharedGroup = new BlockModel(p);
      synchronized (this.modelMap) {
         if (!this.modelMap.containsKey(this.generateKey(p))) {
            BranchGroup stairGroup = new BranchGroup();
            TransformGroup lowerStair = new TransformGroup();
            lowerStair.addChild(new Box(0.5F, 0.25F, 0.5F, 67, appearance));
            Transform3D lowerTransform = new Transform3D();
            lowerTransform.setTranslation(new Vector3f(0.0F, -0.25F, 0.0F));
            lowerStair.setTransform(lowerTransform);
            TransformGroup upperStair = new TransformGroup();
            upperStair.addChild(new Box(0.5F, 0.25F, 0.25F, 67, appearance));
            Transform3D upperTransform = new Transform3D();
            upperTransform.setTranslation(new Vector3f(0.0F, 0.25F, -0.25F));
            upperStair.setTransform(upperTransform);
            stairGroup.addChild(lowerStair);
            stairGroup.addChild(upperStair);
            if (p.getOrientation() != Orientation.None && p.getOrientation() != Orientation.North) {
               TransformGroup rotationGroup = new TransformGroup();
               rotationGroup.addChild(stairGroup);
               Transform3D rotation = new Transform3D();
               switch (p.getOrientation()) {
                  case East:
                     rotation.rotY(Math.PI * 3.0 / 2.0);
                     break;
                  case South:
                     rotation.rotY(Math.PI);
                     break;
                  case West:
                     rotation.rotY(Math.PI / 2);
               }

               rotationGroup.setTransform(rotation);
               sharedGroup.addChild(rotationGroup);
               this.modelMap.put(this.generateKey(p), sharedGroup);
            } else {
               sharedGroup.addChild(stairGroup);
               this.modelMap.put(this.generateKey(p), sharedGroup);
            }
         } else {
            sharedGroup = this.modelMap.get(this.generateKey(p));
         }

         return sharedGroup;
      }
   }
}
