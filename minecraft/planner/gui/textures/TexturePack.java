package minecraft.planner.gui.textures;

import java.awt.Color;
import java.util.Collection;
import javax.swing.ImageIcon;

public interface TexturePack {
   String getName();

   String getFileName();

   ImageIcon getImage(int var1, int var2);

   ImageIcon getImage(TextureName var1);

   ImageIcon getPlanImage(TextureName var1);

   TextureName findClosestMatchingTexture(Color var1, Collection<TextureName> var2);
}
