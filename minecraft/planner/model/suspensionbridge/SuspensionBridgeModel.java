package minecraft.planner.model.suspensionbridge;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class SuspensionBridgeModel extends StructureModel<SuspensionBridgeParameters> {
   public static final String NAME = "Suspension Bridge";

   public synchronized void generate(SuspensionBridgeParameters parameters) {
      super.generate(parameters);
      WorldGrid grid = this.getWorldGrid();
      grid.initialize(parameters.getBridgeSpan(), parameters.getDeckWidth());
      this.generateDeck(parameters.getBridgeSpan(), parameters.getDeckWidth(), parameters.getDeckHeight(), parameters.getDeckTexture());
      this.generateTowers(
         parameters.getTowerInset(), parameters.getTowerHeight(), parameters.getDeckWidth(), parameters.getBridgeSpan(), parameters.getTowerTexture()
      );
      this.generateCenterCables(
         parameters.getTowerInset(),
         parameters.getTowerHeight(),
         parameters.getDeckWidth(),
         parameters.getDeckHeight(),
         parameters.getBridgeSpan(),
         parameters.getMinimumCableHeight(),
         parameters.getCableTexture()
      );
      this.generateEndCables(
         parameters.getTowerInset(),
         parameters.getTowerHeight(),
         parameters.getDeckWidth(),
         parameters.getDeckHeight(),
         parameters.getBridgeSpan(),
         parameters.getCableTexture()
      );
      this.generateCrossBraces(
         parameters.getTowerInset(),
         parameters.getTowerHeight(),
         parameters.getDeckWidth(),
         parameters.getBridgeSpan(),
         parameters.getNumberOfCrossBraces(),
         parameters.getCrossBraceSpacing(),
         parameters.getTowerTexture()
      );
      super.generated();
   }

   @Override
   public boolean includeVisibleBlocksParameter() {
      return false;
   }

   @Override
   public String getStructureName() {
      return "Suspension Bridge";
   }

   @Override
   public int calculateVisibleHeight(WorldGrid$Cell cell) {
      return 0;
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   private void generateDeck(int deckSpan, int deckWidth, int deckHeight, TextureName deckTexture) {
      WorldGrid grid = this.getWorldGrid();

      for (int width = 0; width < deckWidth; width++) {
         for (int span = 0; span < deckSpan; span++) {
            grid.getCell(span, width).getStack().addZ(deckHeight, deckTexture);
         }
      }
   }

   private void generateTowers(int towerInset, int towerHeight, int deckWidth, int bridgeSpan, TextureName towerTexture) {
      WorldGrid grid = this.getWorldGrid();

      for (int height = 1; height <= towerHeight; height++) {
         grid.getCell(towerInset, 0).getStack().addZ(height, towerTexture);
         grid.getCell(towerInset, deckWidth - 1).getStack().addZ(height, towerTexture);
         grid.getCell(bridgeSpan - towerInset - 1, 0).getStack().addZ(height, towerTexture);
         grid.getCell(bridgeSpan - towerInset - 1, deckWidth - 1).getStack().addZ(height, towerTexture);
      }
   }

   private void generateCenterCables(
      int towerInset, int towerHeight, int deckWidth, int deckHeight, int bridgeSpan, int minimumCableHeight, TextureName cableTexture
   ) {
      int fromWidth = towerInset + 1;
      int toWidth = fromWidth + (bridgeSpan - towerInset - fromWidth - 2) / 2;
      int cableVertical = towerHeight - deckHeight - minimumCableHeight + 1;
      int cableWidth = toWidth - fromWidth;
      double step = Math.PI / (2 * cableWidth);
      double angle = 0.0;

      for (int cable = fromWidth; cable <= toWidth; cable++) {
         boolean hasMinorCable = cable - fromWidth > 0 && (cable - fromWidth) % 2 == 1;
         int cableHeight = this.calculateCableHeight(towerHeight, angle, cableVertical, deckHeight, minimumCableHeight);
         if (cable <= toWidth) {
            int nextCableHeight = this.calculateCableHeight(towerHeight, angle + step, cableVertical, deckHeight, minimumCableHeight);
            if (nextCableHeight < cableHeight) {
               this.addCableSegment(cable, 0, nextCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
               this.addCableSegment(cable, deckWidth - 1, nextCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
               this.addCableSegment(bridgeSpan - cable - 1, 0, nextCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
               this.addCableSegment(bridgeSpan - cable - 1, deckWidth - 1, nextCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
            } else {
               this.addCableSegment(cable, 0, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
               this.addCableSegment(cable, deckWidth - 1, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
               this.addCableSegment(bridgeSpan - cable - 1, 0, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
               this.addCableSegment(bridgeSpan - cable - 1, deckWidth - 1, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            }
         } else {
            if (hasMinorCable) {
               hasMinorCable = (bridgeSpan - 2 * towerInset - 2) % 2 == 1;
            }

            this.addCableSegment(cable, 0, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(cable, deckWidth - 1, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(bridgeSpan - cable + 1, 0, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(bridgeSpan - cable + 1, deckWidth - 1, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
         }

         angle += step;
      }
   }

   private void generateEndCables(int towerInset, int towerHeight, int deckWidth, int deckHeight, int bridgeSpan, TextureName cableTexture) {
      int fromWidth = 0;
      int toWidth = towerInset - 1;
      int cableVertical = towerHeight - deckHeight - 1;
      double step = (double)cableVertical / toWidth;
      int lastCableHeight = deckHeight + 1;

      for (int cable = fromWidth; cable <= toWidth; cable++) {
         int cableHeight = deckHeight + 1 + (int)((cable - fromWidth) * step);
         boolean hasMinorCable = cableHeight - deckHeight > 1 && (towerInset - cable) % 2 == 1;
         if (cableHeight > towerHeight) {
            cableHeight = towerHeight;
         }

         if (cableHeight > lastCableHeight + 1) {
            this.addCableSegment(cable, 0, lastCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(cable, deckWidth - 1, lastCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(bridgeSpan - cable - 1, 0, lastCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(bridgeSpan - cable - 1, deckWidth - 1, lastCableHeight + 1, cableHeight, deckHeight, hasMinorCable, cableTexture);
         } else {
            this.addCableSegment(cable, 0, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(cable, deckWidth - 1, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(bridgeSpan - cable - 1, 0, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
            this.addCableSegment(bridgeSpan - cable - 1, deckWidth - 1, cableHeight, cableHeight, deckHeight, hasMinorCable, cableTexture);
         }

         lastCableHeight = cableHeight;
      }
   }

   private void generateCrossBraces(
      int towerInset, int towerHeight, int deckWidth, int bridgeSpan, int numberOfCrossBraces, int crossBraceSpacing, TextureName towerTexture
   ) {
      if (numberOfCrossBraces > 0) {
         WorldGrid grid = this.getWorldGrid();

         for (int braceY = 1; braceY < deckWidth - 1; braceY++) {
            WorldGrid$Cell.ZStack braceCellStack1 = grid.getCell(towerInset, braceY).getStack();
            WorldGrid$Cell.ZStack braceCellStack2 = grid.getCell(bridgeSpan - towerInset - 1, braceY).getStack();

            for (int braceNumber = 0; braceNumber < numberOfCrossBraces; braceNumber++) {
               braceCellStack1.addZ(towerHeight - braceNumber * crossBraceSpacing, towerTexture);
               braceCellStack2.addZ(towerHeight - braceNumber * crossBraceSpacing, towerTexture);
            }
         }
      }
   }

   private int calculateCableHeight(int towerHeight, double angle, int cableVertical, int deckHeight, int minimumCableHeight) {
      int cableHeight = towerHeight - (int)(Math.sin(angle) * cableVertical);
      if (cableHeight - deckHeight < minimumCableHeight) {
         cableHeight = deckHeight + minimumCableHeight;
      }

      return cableHeight;
   }

   private void addCableSegment(int x, int y, int fromHeight, int toHeight, int deckHeight, boolean hasMinorCable, TextureName cableTexture) {
      WorldGrid grid = this.getWorldGrid();
      grid.getCell(x, y).getStack().addZ(fromHeight, toHeight, cableTexture);
      if (hasMinorCable) {
         grid.getCell(x, y).getStack().addZ(deckHeight + 1, Math.min(fromHeight, toHeight) - 1, TextureName.Fencepost);
      }
   }
}
