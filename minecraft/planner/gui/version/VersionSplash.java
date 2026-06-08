package minecraft.planner.gui.version;

import java.awt.BorderLayout;
import java.awt.Dialog$ModalExclusionType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class VersionSplash extends JFrame {
   private VersionInfo info;
   private URLLabel urlLabel;

   public VersionSplash(VersionInfo info) {
      JPanel content = new JPanel();
      this.info = info;
      content.setLayout(new GridBagLayout());
      content.setBorder(BorderFactory.createBevelBorder(0));
      JPanel titlePanel = new JPanel();
      titlePanel.setLayout(new BorderLayout());
      TitlePanel title = new TitlePanel("title1.jpg", "Minecraft Structure Planner Update Available");
      content.add(title, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 11, 0, new Insets(5, 5, 15, 5), 0, 0));
      int row = 1;
      content.add(new JLabel("Your Version:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      content.add(new JLabel("0.99.7"), new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0));
      content.add(new JLabel("Latest Version:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 5), 0, 0));
      content.add(
         new JLabel(info.getLatestVersion() + ", released " + info.getReleaseDate()),
         new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 0, 5, 5), 0, 0)
      );
      JEditorPane changeList = new JEditorPane("text/html", info.getFeatureList());
      changeList.setEditable(false);
      JScrollPane pane = new JScrollPane(20, 31);
      pane.setBorder(BorderFactory.createBevelBorder(1));
      pane.getViewport().add(changeList);
      content.add(new JLabel("Change List:"), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, 18, 0, new Insets(15, 5, 5, 5), 0, 0));
      content.add(pane, new GridBagConstraints(0, row++, 2, 1, 1.0, 1.0, 11, 1, new Insets(0, 5, 5, 5), 0, 0));

      try {
         URL downloadLink = new URL("http://minecraftstructureplanner.com/index.php/download");
         this.urlLabel = new URLLabel(downloadLink, "Click here to download the latest version of the Minecraft Structure Planner");
         content.add(this.urlLabel, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 11, 0, new Insets(5, 5, 5, 5), 0, 0));
      } catch (Exception e) {
         this.urlLabel = null;
         e.printStackTrace();
      }

      JButton closeButton = new JButton("Close");
      content.add(closeButton, new GridBagConstraints(0, row, 2, 1, 1.0, 0.0, 15, 0, new Insets(5, 5, 5, 5), 0, 0));
      closeButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            VersionSplash.this.dispose();
         }
      });
      this.getContentPane().add(content);
      Dimension size = new Dimension(800, 600);
      this.setMinimumSize(size);
      this.setMaximumSize(size);
      this.setPreferredSize(size);
      this.setDefaultCloseOperation(2);
      this.setModalExclusionType(Dialog$ModalExclusionType.APPLICATION_EXCLUDE);
      this.setResizable(false);
      this.setAlwaysOnTop(true);
      this.setUndecorated(true);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation((int)(screenSize.getWidth() - size.getWidth()) / 2, (int)(screenSize.getHeight() - size.getHeight()) / 2);
      InputMap inputMap = this.getRootPane().getInputMap();
      inputMap.put(KeyStroke.getKeyStroke(27, 0), "EscapeAction");
      inputMap.put(KeyStroke.getKeyStroke(10, 0), "EnterAction");
      ActionMap actionMap = this.getRootPane().getActionMap();
      actionMap.put("EscapeAction", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            VersionSplash.this.dispose();
         }
      });
      actionMap.put("EnterAction", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            if (VersionSplash.this.urlLabel != null) {
               VersionSplash.this.urlLabel.browseURL();
            }
         }
      });
      this.setVisible(true);
   }

   public static void main(String[] argv) {
      try {
         URL versionFile = new URL("file:///F:/Users/Neil/Desktop/version.txt");
         VersionInfo versionInfo = new VersionInfo(versionFile);
         new VersionSplash(versionInfo);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
