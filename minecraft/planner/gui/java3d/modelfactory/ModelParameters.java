package minecraft.planner.gui.java3d.modelfactory;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.grid.attributes.Orientation;

public class ModelParameters {
   private float x;
   private float y;
   private float z;
   private TextureName texture;
   private Orientation orientation;

   public ModelParameters() {
      this(TextureName.None, Orientation.None);
   }

   public ModelParameters(TextureName texture) {
      this(texture, Orientation.None);
   }

   public ModelParameters(TextureName texture, Orientation orientation) {
      this.texture = texture;
      this.orientation = orientation;
   }

   public void setTexture(TextureName texture) {
      this.texture = texture;
   }

   public void setOrientation(Orientation orientation) {
      this.orientation = orientation;
   }

   public TextureName getTexture() {
      return this.texture;
   }

   public Orientation getOrientation() {
      return this.orientation;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y;
   }

   public void setZ(float z) {
      this.z = z;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getZ() {
      return this.z;
   }

   public String key() {
      return this.x + "|" + this.y + "|" + this.z + "|" + this.texture.name() + "|" + this.orientation.name();
   }

   @Override
   public int hashCode() {
      return this.key().hashCode();
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ModelParameters)) {
         return false;
      }

      ModelParameters p = (ModelParameters)o;
      return this.x == p.x && this.y == p.y && this.z == p.z && this.texture == p.texture && this.orientation == p.orientation;
   }
}
