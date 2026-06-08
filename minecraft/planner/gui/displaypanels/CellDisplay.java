package minecraft.planner.gui.displaypanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import minecraft.planner.model.grid.WorldGrid$Cell;

public class CellDisplay extends JButton {
   private WorldGrid$Cell cell;

   public CellDisplay(WorldGrid$Cell cell) {
      this.cell = cell;
      this.setBorder(BorderFactory.createBevelBorder(0));
      this.setHorizontalAlignment(0);
      this.setHorizontalTextPosition(0);
      this.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            CellDisplay.this.cell.setProcessed(!CellDisplay.this.cell.isProcessed());
            CellDisplay.this.repaint();
         }
      });
   }

   @Override
   public void repaint() {
      super.repaint();
      if (this.cell != null) {
         if (this.cell.isProcessed()) {
            this.setBackground(Color.GREEN);
         } else {
            this.setBackground(Color.WHITE);
         }

         int height = this.cell.getHeight();
         this.setText(height > 0 ? "" + height : "");
      } else {
         this.setBackground(Color.WHITE);
         this.setText("");
      }
   }
}
