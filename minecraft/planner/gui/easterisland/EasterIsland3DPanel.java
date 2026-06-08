package minecraft.planner.gui.easterisland;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import minecraft.planner.gui.displaypanels.View3DPanel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.bitmap.BitmapModel;
import minecraft.planner.model.bitmap.BitmapParameters;

public class EasterIsland3DPanel extends View3DPanel<BitmapParameters> implements StructureModelChangeListener {
   private boolean needsUpdate;
   private boolean hasFocus = false;

   public EasterIsland3DPanel(BitmapModel model) {
      super(model, false, true);
      this.needsUpdate = true;
      this.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent arg0) {
            EasterIsland3DPanel.this.hasFocus = true;
            EasterIsland3DPanel.this.structureModelChanged(this);
         }

         @Override
         public void focusLost(FocusEvent arg0) {
            EasterIsland3DPanel.this.hasFocus = false;
         }
      });
   }

   @Override
   public void structureModelChanged(Object originator) {
      if (this.hasFocus) {
         if (this.needsUpdate) {
            super.structureModelChanged(originator);
            this.needsUpdate = false;
         }
      } else {
         this.needsUpdate = true;
      }
   }

   @Override
   public void forceUpdate() {
      this.needsUpdate = true;
   }
}
