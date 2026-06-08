package minecraft.planner.model.viaduct;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureParameters;

public class ViaductParameters extends StructureParameters {
   private int viaductSpan;
   private int viaductDepth;
   private int archRadius;
   private int archHeight;
   private int towerWidth;
   private int towerHeight;
   private int wallHeight;
   private int deckThickness;
   private TextureName viaductTexture;
   private TextureName wallTexture;

   public ViaductParameters(
      int viaductSpan,
      int viaductDepth,
      int archRadius,
      int archHeight,
      int towerWidth,
      int towerHeight,
      int wallHeight,
      int deckThickness,
      TextureName viaductTexture,
      TextureName wallTexture
   ) {
      this.viaductSpan = viaductSpan;
      this.viaductDepth = viaductDepth;
      this.archRadius = archRadius;
      this.archHeight = archHeight;
      this.towerWidth = towerWidth;
      this.towerHeight = towerHeight;
      this.wallHeight = wallHeight;
      this.deckThickness = deckThickness;
      this.viaductTexture = viaductTexture;
      this.wallTexture = wallTexture;
   }

   public int getViaductSpan() {
      return this.viaductSpan;
   }

   public int getViaductDepth() {
      return this.viaductDepth;
   }

   public int getArchRadius() {
      return this.archRadius;
   }

   public int getArchHeight() {
      return this.archHeight;
   }

   public int getTowerWidth() {
      return this.towerWidth;
   }

   public int getTowerHeight() {
      return this.towerHeight;
   }

   public int getWallHeight() {
      return this.wallHeight;
   }

   public int getDeckThickness() {
      return this.deckThickness;
   }

   public TextureName getViaductTexture() {
      return this.viaductTexture;
   }

   public TextureName getWallTexture() {
      return this.wallTexture;
   }
}
