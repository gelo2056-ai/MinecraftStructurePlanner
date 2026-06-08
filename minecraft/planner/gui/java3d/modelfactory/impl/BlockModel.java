package minecraft.planner.gui.java3d.modelfactory.impl;

import com.sun.j3d.utils.geometry.Primitive;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import minecraft.planner.gui.java3d.TextureCache;
import minecraft.planner.gui.java3d.modelfactory.ModelParameters;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.UserConfigurationManager;

public class BlockModel extends SharedGroup {
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private Set<Primitive> primitives = new HashSet<>();
   private ModelParameters p;

   public BlockModel(ModelParameters p) {
      this.p = p;
   }

   public ModelParameters getModelParameters() {
      return this.p;
   }

   @Override
   public void addChild(Node child) {
      super.addChild(child);
      this.addPrimitivesToSet(child);
   }

   private void addPrimitivesToSet(Node child) {
      if (Primitive.class.isAssignableFrom(child.getClass())) {
         this.primitives.add((Primitive)child);
      } else if (Group.class.isAssignableFrom(child.getClass())) {
         Enumeration<?> children = ((Group)child).getAllChildren();

         while (children.hasMoreElements()) {
            Object groupChild = children.nextElement();
            if (Node.class.isAssignableFrom(groupChild.getClass())) {
               this.addPrimitivesToSet((Node)groupChild);
            }
         }
      }
   }

   public void retexture(TextureName newTexture) {
      for (Primitive primitive : this.primitives) {
         primitive.setAppearance(TextureCache.getTexture(this.texturePack, newTexture));
      }
   }
}
