package minecraft.planner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import minecraft.planner.gui.JStatusBar.StatusUpdater.1;

public class JStatusBar extends JPanel {
   public static final long STATUS_CLEAR_DELAY = 10000L;
   public static final long STATUS_CLEAR_WITH_MESSAGES = 1000L;
   private static final BlockingQueue<String> statusMessages = new LinkedBlockingQueue<>();
   private JLabel statusMessage = new JLabel();
   private JProgressBar progressBar = new JProgressBar(0, 100);
   private JMemoryBar memoryBar = new JMemoryBar();
   private Timer clearTimer = new Timer(true);
   private TimerTask clearTask = null;
   private JStatusBar.Mode statusBarMode;
   private int previousValue = 0;

   public JStatusBar() {
      this.setLayout(new GridBagLayout());
      this.setBorder(BorderFactory.createBevelBorder(0));
      this.add(this.statusMessage, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
      this.add(this.progressBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 17, 2, new Insets(1, 1, 1, 20), 0, 0));
      this.add(this.memoryBar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 13, 0, new Insets(0, 0, 0, 0), 75, 0));
      this.statusMessage.setHorizontalAlignment(2);
      this.setMode(JStatusBar.Mode.Text);
      new JStatusBar.StatusUpdateThread().start();
   }

   public void updateStatus(String status) {
      try {
         statusMessages.put(status);
      } catch (Exception var3) {
      }
   }

   public JStatusBar.Mode getMode() {
      return this.statusBarMode;
   }

   public void setPercent(double percent) {
      int value = (int)Math.round(percent * 100.0);
      if (value != this.previousValue) {
         this.progressBar.setValue(value);
         this.previousValue = value;
      }
   }

   public void setMode(JStatusBar.Mode mode) {
      this.statusBarMode = mode;
      this.updateStatusMode();
   }

   private void updateStatusMode() {
      this.progressBar.setVisible(this.statusBarMode != JStatusBar.Mode.Text);
      this.statusMessage.setVisible(this.statusBarMode == JStatusBar.Mode.Text);
      this.repaint();
   }

   public enum Mode {
      Text,
      Percent;
   }

   private class ProgressBarUpdate implements Runnable {
      private int percent;

      public ProgressBarUpdate(int percent) {
         this.percent = percent;
      }

      @Override
      public void run() {
         JStatusBar.this.progressBar.setValue(this.percent);
      }
   }

   private class StatusUpdateThread extends Thread {
      private String message;

      public StatusUpdateThread() {
         this.setName("Status Update Thread");
         this.setDaemon(true);
      }

      @Override
      public void run() {
         while (true) {
            try {
               String message = JStatusBar.statusMessages.take();
               SwingUtilities.invokeLater(JStatusBar.this.new StatusUpdater(message));
               if (!JStatusBar.statusMessages.isEmpty()) {
                  Thread.sleep(1000L);
               }
            } catch (Exception var2) {
            }
         }
      }
   }

   private class StatusUpdater implements Runnable {
      private String message;

      public StatusUpdater(String message) {
         this.message = message;
      }

      @Override
      public void run() {
         JStatusBar.this.statusMessage.setText(this.message);
         if (JStatusBar.this.clearTask != null) {
            JStatusBar.this.clearTask.cancel();
         }

         JStatusBar.this.clearTask = new 1(this);
         JStatusBar.this.clearTimer.schedule(JStatusBar.this.clearTask, 10000L);
      }
   }
}
