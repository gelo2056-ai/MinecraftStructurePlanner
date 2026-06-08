package minecraft.planner.gui.freeform;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import minecraft.planner.model.freeform.FreeformModel;

public class FreeformDisplayPanel extends FreeformStructureDisplayPanel implements Printable {
   public FreeformDisplayPanel(FreeformModel model) {
      super(model);
   }

   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      return this.getLayerPanel().print(graphics, pageFormat, pageIndex);
   }
}
