package minecraft.planner.gui.freeform.Clipboard;

import minecraft.planner.gui.textures.TextureName;

public class CopyCell {
   private int x;
   private int y;
   private TextureName texture;

   public CopyCell(int x, int y, TextureName texture) {
      this.x = x;
      this.y = y;
      this.texture = texture;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public TextureName getTexture() {
      return this.texture;
   }

   @Override
   public int hashCode() {
      return this.x + this.y;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof CopyCell)) {
         return false;
      }

      CopyCell c = (CopyCell)o;
      return c.x == this.x && c.y == this.y && c.texture == this.texture;
   }
}
