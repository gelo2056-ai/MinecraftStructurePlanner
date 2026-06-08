package minecraft.planner.gui.bitmap;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import minecraft.planner.model.bitmap.BitmapModel;

public class BitmapDisplayPanel extends BitmapStructureDisplayPanel implements Printable {
   public BitmapDisplayPanel(BitmapModel model) {
      super(model);
   }

   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      return this.getLayerPanel().print(graphics, pageFormat, pageIndex);
   }
}
