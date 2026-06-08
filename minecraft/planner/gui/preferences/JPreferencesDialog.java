package minecraft.planner.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.preferences.texture.JTextureSetPreferencesPanel;
import minecraft.planner.gui.preferences.texturepack.JTexturePackPreferencesPanel;

public class JPreferencesDialog extends JDialog {
   private static final JTabbedPane preferencesTabs = new JTabbedPane();
   private static JPreferencesDialog instance = null;

   private JPreferencesDialog() {
      super(StructurePlanner.getFrame(), "Structure Planner Preferences", true);
      JTabbedPane tabs = new JTabbedPane();
      this.setLayout(new BorderLayout());
      this.getContentPane().add(tabs, "Center");
      JPanel finishPanel = new JPanel();
      finishPanel.setLayout(new GridBagLayout());
      JButton finish = new JButton("Finish");
      finishPanel.add(Box.createHorizontalBox(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 17, 2, new Insets(0, 0, 0, 0), 0, 0));
      finishPanel.add(finish, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
      this.add(finishPanel, "South");
      finish.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JPreferencesDialog.this.dispose();
         }
      });
      JTextureSetPreferencesPanel textureSetEditor = new JTextureSetPreferencesPanel();
      tabs.add("Texture Sets", textureSetEditor);
      JTexturePackPreferencesPanel texturePackEditor = new JTexturePackPreferencesPanel();
      tabs.add("Texture Packs", texturePackEditor);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(new Dimension(640, 480));
      this.setLocation(new Point((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2));
   }

   public static synchronized JPreferencesDialog getInstance() {
      if (instance == null) {
         instance = new JPreferencesDialog();
      }

      return instance;
   }
}
