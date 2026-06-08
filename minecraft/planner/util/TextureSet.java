package minecraft.planner.util;

import java.util.ArrayList;
import java.util.List;
import minecraft.planner.gui.textures.TextureName;

public class TextureSet implements Comparable<TextureSet> {
   private boolean isEditable;
   private String name;
   private List<TextureName> textures;

   public TextureSet(String name, TextureName[] textures, boolean isEditable) {
      this(name, new ArrayList<>(textures.length), isEditable);

      for (int index = 0; index < textures.length; index++) {
         TextureName texture = textures[index];
         if (texture != TextureName.None && texture.isTexturable()) {
            this.textures.add(texture);
         }
      }

      this.isEditable = isEditable;
   }

   public TextureSet(String name, List<TextureName> textures, boolean isEditable) {
      if (name != null && name.length() != 0) {
         this.name = name;
         this.textures = textures;
         this.isEditable = isEditable;
      } else {
         throw new IllegalArgumentException("NULL or empty name passed to TextureSet constructor");
      }
   }

   public String getName() {
      return this.name;
   }

   public List<TextureName> getTextures() {
      return this.textures;
   }

   public boolean isEditable() {
      return this.isEditable;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setTextures(List<TextureName> textures) {
      this.textures = textures;
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }

   @Override
   public boolean equals(Object o) {
      return !(o instanceof TextureSet) ? false : this.name.equals(((TextureSet)o).name);
   }

   @Override
   public String toString() {
      return this.name;
   }

   public int compareTo(TextureSet o) {
      return this.name.compareTo(o.name);
   }
}
