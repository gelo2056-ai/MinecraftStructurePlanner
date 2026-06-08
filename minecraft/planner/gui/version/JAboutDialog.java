package minecraft.planner.gui.version;

import java.awt.BorderLayout;
import java.awt.Dialog$ModalExclusionType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import minecraft.planner.gui.JContributorsPanel;
import minecraft.planner.gui.StructurePlanner;

public class JAboutDialog extends JDialog {
   private static final int MB = 1048576;
   private final JButton gc = new JButton("Request Garbage Collect");
   private final JLabel maxMemory = new JLabel();
   private final JLabel totalMemory = new JLabel();
   private final JLabel usedMemory = new JLabel();
   private final Runtime runtime = Runtime.getRuntime();
   private VersionInfo info;

   public JAboutDialog() {
      super(StructurePlanner.getFrame(), "About Minecraft Structure Planner", true);
      JPanel content = new JPanel();
      this.info = VersionUtilities.getVersionInfo();
      content.setLayout(new GridBagLayout());
      content.setBorder(BorderFactory.createBevelBorder(0));
      JPanel titlePanel = new JPanel();
      titlePanel.setLayout(new BorderLayout());
      TitlePanel title = new TitlePanel("title2.jpg", "Minecraft Structure Planner v0.99.7", "(c) Neil Vickers, 2010 - 2011");
      title.setBorder(BorderFactory.createEtchedBorder(0));
      content.add(title, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, 11, 0, new Insets(5, 5, 15, 5), 0, 0));
      int row = 1;
      this.gc.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            JAboutDialog.this.new GCThread().start();
         }
      });
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BorderLayout());
      buttonPanel.add(this.gc, "North");
      this.calculateMemory();
      JPanel memoryPanel = new JPanel();
      memoryPanel.setLayout(new GridBagLayout());
      int memoryRow = 0;
      memoryPanel.add(new JLabel("Maximum Memory:"), new GridBagConstraints(0, memoryRow, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      memoryPanel.add(this.maxMemory, new GridBagConstraints(1, memoryRow++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      memoryPanel.add(new JLabel("Total Memory:"), new GridBagConstraints(0, memoryRow, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      memoryPanel.add(this.totalMemory, new GridBagConstraints(1, memoryRow++, 3, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      memoryPanel.add(new JLabel("Used Memory:"), new GridBagConstraints(0, memoryRow, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      memoryPanel.add(this.usedMemory, new GridBagConstraints(1, memoryRow++, 3, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      content.add(memoryPanel, new GridBagConstraints(0, row, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 0, 0), 0, 0));
      content.add(buttonPanel, new GridBagConstraints(1, row++, 1, 1, 0.0, 0.0, 12, 0, new Insets(0, 0, 0, 5), 0, 0));
      content.add(new JLabel(""), new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      JTextArea systemProperties = new JTextArea();
      systemProperties.setEditable(false);
      systemProperties.setLineWrap(true);
      systemProperties.setWrapStyleWord(true);
      int changeRow = 0;

      for (Object key : System.getProperties().keySet()) {
         if (changeRow > 0) {
            systemProperties.append("\n");
         }

         String keyString = key.toString();
         systemProperties.append(keyString + " = " + System.getProperty(keyString));
         changeRow++;
      }

      JScrollPane propertyPane = new JScrollPane(20, 31);
      propertyPane.setBorder(BorderFactory.createBevelBorder(1));
      propertyPane.getViewport().add(systemProperties);
      systemProperties.setCaretPosition(0);
      JTextArea licenseArea = new JTextArea();
      licenseArea.setEditable(false);
      licenseArea.setLineWrap(true);
      licenseArea.setFont(new Font("Monospaced", 0, 14));
      licenseArea.setWrapStyleWord(true);

      try {
         InputStream is = JAboutDialog.class.getClass().getResourceAsStream("/minecraft/planner/resources/LICENSE.txt");
         byte[] licenseText = new byte[16384];
         is.read(licenseText);
         licenseArea.setText(new String(licenseText).trim());
      } catch (Throwable t) {
         licenseArea.setText("Error loading license file.  Please check LICENSE.TXT within the Minecraft Structure Planner JAR files for details.");
      }

      JScrollPane licensePane = new JScrollPane(20, 31);
      licensePane.setBorder(BorderFactory.createBevelBorder(1));
      licensePane.getViewport().add(licenseArea);
      licenseArea.setCaretPosition(0);
      JTabbedPane tabs = new JTabbedPane();
      tabs.add("Donors", new JContributorsPanel("/minecraft/planner/resources/donors.txt"));
      tabs.add("LICENSE", licensePane);
      tabs.add("System Properties", propertyPane);
      content.add(tabs, new GridBagConstraints(0, row++, 4, 1, 1.0, 1.0, 11, 1, new Insets(0, 5, 5, 5), 0, 0));
      if (this.info != null && !"0.99.7".equals(this.info.getLatestVersion())) {
         try {
            URL downloadLink = new URL("http://minecraftstructureplanner.com/index.php/download");
            content.add(
               new URLLabel(downloadLink, "Click here to download version " + this.info.getLatestVersion() + " of the Minecraft Structure Planner"),
               new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 11, 0, new Insets(5, 5, 5, 5), 0, 0)
            );
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      JButton closeButton = new JButton("Close");
      content.add(closeButton, new GridBagConstraints(0, row, 4, 1, 1.0, 0.0, 15, 0, new Insets(5, 5, 5, 5), 0, 0));
      closeButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            JAboutDialog.this.dispose();
         }
      });
      this.getContentPane().add(content);
      Dimension size = new Dimension(800, 700);
      this.setMinimumSize(size);
      this.setMaximumSize(size);
      this.setPreferredSize(size);
      this.setDefaultCloseOperation(1);
      this.setModalExclusionType(Dialog$ModalExclusionType.APPLICATION_EXCLUDE);
      this.setResizable(false);
      this.setAlwaysOnTop(true);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation((int)(screenSize.getWidth() - size.getWidth()) / 2, (int)(screenSize.getHeight() - size.getHeight()) / 2);
   }

   private void calculateMemory() {
      this.maxMemory.setText(this.runtime.maxMemory() / 1048576L + " MB");
      this.totalMemory.setText(this.runtime.totalMemory() / 1048576L + " MB");
      this.usedMemory.setText((this.runtime.totalMemory() - this.runtime.freeMemory()) / 1048576L + " MB");
   }

   private class GCThread extends Thread {
      public GCThread() {
         this.setName("Requested GC " + this.getName());
      }

      @Override
      public void run() {
         try {
            JAboutDialog.this.gc.setEnabled(false);
            StructurePlanner.setStatus("Requesting VM Garbage Collection...");
            System.gc();
            Thread.sleep(5000L);
            JAboutDialog.this.calculateMemory();
         } catch (Exception var5) {
         } finally {
            JAboutDialog.this.gc.setEnabled(true);
         }
      }
   }
}
