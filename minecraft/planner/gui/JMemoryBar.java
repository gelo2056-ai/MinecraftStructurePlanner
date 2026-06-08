package minecraft.planner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import minecraft.planner.gui.JMemoryBar.UpdateThread.1;

public class JMemoryBar extends JLabel {
   protected static final int MB = 1048576;
   protected static final Runtime runtime = Runtime.getRuntime();
   private final JMemoryBar.UpdateThread updateThread;
   private long maxMemory;
   private long totalMemory;
   private long usedMemory;
   private static final Color MAX_MEMORY = new Color(230, 230, 230);
   private static final Color TOTAL_MEMORY = new Color(98, 163, 193);
   private static final Color USED_MEMORY = new Color(160, 160, 160);
   private static final Color TEXT = new Color(43, 87, 17);

   public JMemoryBar() {
      this.setOpaque(true);
      Dimension size = new Dimension(75, 20);
      this.setPreferredSize(size);
      this.setMinimumSize(size);
      this.setMaximumSize(size);
      this.calculateMemory();
      this.updateThread = new JMemoryBar.UpdateThread(10000L);
      this.updateThread.start();
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      int width = this.getWidth();
      int height = this.getHeight();
      double totalPercent = (double)this.totalMemory / this.maxMemory;
      double usedPercent = (double)this.usedMemory / this.maxMemory;
      g.setColor(MAX_MEMORY);
      g.fillRect(0, 0, width - 2, height - 2);
      g.setColor(Color.BLACK);
      g.drawRect(0, 0, width - 2, height - 2);
      g.setColor(TOTAL_MEMORY);
      g.fillRect(0, 0, (int)(width * totalPercent) - 2, height - 2);
      g.setColor(Color.BLACK);
      g.drawRect(0, 0, (int)(width * totalPercent) - 2, height - 2);
      g.setColor(USED_MEMORY);
      g.fillRect(0, 0, (int)(width * usedPercent) - 2, height - 2);
      g.setColor(Color.BLACK);
      g.drawRect(0, 0, (int)(width * usedPercent) - 2, height - 2);
      if (this.usedMemory / this.totalMemory > 0.9) {
         g.setColor(Color.RED);
      } else {
         g.setColor(Color.BLACK);
      }

      g.drawString(this.usedMemory + "MB", 5, this.getHeight() / 2 + 4);
      g.setColor(Color.BLACK);
      g.drawRect(0, 0, width - 2, height - 2);
   }

   private void calculateMemory() {
      this.maxMemory = runtime.maxMemory() / 1048576L;
      this.totalMemory = runtime.totalMemory() / 1048576L;
      this.usedMemory = this.totalMemory - runtime.freeMemory() / 1048576L;
   }

   private class UpdateThread extends Thread {
      private long updateRate;

      public UpdateThread(long updateRateMilliseconds) {
         this.setName("JMemoryBar Update " + this.getName());
         this.setDaemon(true);
         this.updateRate = updateRateMilliseconds;
      }

      @Override
      public void run() {
         while (true) {
            JMemoryBar.this.calculateMemory();
            SwingUtilities.invokeLater(new 1(this));

            try {
               Thread.sleep(this.updateRate);
            } catch (InterruptedException var2) {
            }
         }
      }
   }
}
