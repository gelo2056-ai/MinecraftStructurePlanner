package minecraft.planner.model.suspensionbridge;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureParameters;

public class SuspensionBridgeParameters extends StructureParameters {
   private int towerHeight;
   private int deckHeight;
   private int deckWidth;
   private int bridgeSpan;
   private int towerInset;
   private TextureName deckTexture;
   private TextureName towerTexture;
   private TextureName cableTexture;
   private int minimumCableHeight;
   private int numberOfCrossBraces;
   private int crossBraceSpacing;

   public SuspensionBridgeParameters(
      int towerHeight,
      int deckHeight,
      int deckWidth,
      int bridgeSpan,
      int towerInset,
      int minimumCableHeight,
      int numberOfCrossBraces,
      int crossBraceSpacing,
      TextureName deckTexture,
      TextureName towerTexture,
      TextureName cableTexture
   ) {
      this.towerHeight = towerHeight;
      this.deckHeight = deckHeight;
      this.deckWidth = deckWidth;
      this.bridgeSpan = bridgeSpan;
      this.towerInset = towerInset;
      this.minimumCableHeight = minimumCableHeight;
      this.numberOfCrossBraces = numberOfCrossBraces;
      this.crossBraceSpacing = crossBraceSpacing;
      this.deckTexture = deckTexture;
      this.towerTexture = towerTexture;
      this.cableTexture = cableTexture;
   }

   public int getTowerHeight() {
      return this.towerHeight;
   }

   public int getDeckHeight() {
      return this.deckHeight;
   }

   public int getDeckWidth() {
      return this.deckWidth;
   }

   public int getBridgeSpan() {
      return this.bridgeSpan;
   }

   public int getTowerInset() {
      return this.towerInset;
   }

   public int getMinimumCableHeight() {
      return this.minimumCableHeight;
   }

   public TextureName getDeckTexture() {
      return this.deckTexture;
   }

   public TextureName getTowerTexture() {
      return this.towerTexture;
   }

   public TextureName getCableTexture() {
      return this.cableTexture;
   }

   public int getNumberOfCrossBraces() {
      return this.numberOfCrossBraces;
   }

   public int getCrossBraceSpacing() {
      return this.crossBraceSpacing;
   }
}
