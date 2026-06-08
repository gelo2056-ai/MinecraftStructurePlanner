package minecraft.planner.gui.java3d;

import com.sun.j3d.utils.image.TextureLoader;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.vecmath.Color4f;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;

public class TextureCache {
   public static final String RESOURCE_PACKAGE = "/minecraft/planner/resources/";
   private static final Map<String, Appearance> textureCache = new HashMap<>();

   public static Appearance getTexture(TexturePack pack, TextureName texture) {
      Appearance appearance = null;
      String key = pack.getName() + ":" + texture.name();
      synchronized (textureCache) {
         if (textureCache.containsKey(key)) {
            appearance = textureCache.get(key);
         } else {
            appearance = createTexture(pack, texture);
            textureCache.put(key, appearance);
         }

         return appearance;
      }
   }

   private static Appearance createTexture(TexturePack pack, TextureName name) {
      Appearance appearance = null;

      try {
         ImageIcon icon = pack.getImage(name);
         TextureLoader textureLoader = new TextureLoader(icon.getImage(), "RGBA", new Container());
         Texture texture = textureLoader.getTexture();
         texture.setBoundaryModeS(3);
         texture.setBoundaryModeT(3);
         texture.setBoundaryColor(new Color4f(0.0F, 1.0F, 0.0F, 0.0F));
         TextureAttributes textureAttributes = new TextureAttributes();
         textureAttributes.setTextureMode(2);
         appearance = new Appearance();
         appearance.setTexture(texture);
         appearance.setTextureAttributes(textureAttributes);
         appearance.setMaterial(new Material(name.getMatte(), Color.BLACK, name.getMatte(), Color.BLACK, 1.0F));
         PolygonAttributes pa = new PolygonAttributes();
         pa.setCullFace(1);
         appearance.setPolygonAttributes(pa);
         if (name.getTransparency() > 0.0F) {
            TransparencyAttributes ta = new TransparencyAttributes(1, name.getTransparency());
            appearance.setTransparencyAttributes(ta);
         }
      } catch (Exception e) {
         e.printStackTrace();
         appearance = null;
      }

      return appearance;
   }
}
