package minecraft.planner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.UserConfigurationManager;

public class JContributorsPanel extends JPanel {
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();

   public JContributorsPanel(String contributorsResourceName) {
      this.setLayout(new GridBagLayout());
      Map<Integer, List<String>> donorsByLevel = this.getDonorsByLevel(contributorsResourceName);
      JPanel diamondPickaxe = this.createOrderPanel("Order of the Diamond Pickaxe", "$20 plus", TextureName.DiamondPickaxe, donorsByLevel.get(1));
      JPanel goldenSword = this.createOrderPanel("Order of the Golden Sword", "$10 to $20", TextureName.GoldenSword, donorsByLevel.get(2));
      JPanel steelShovel = this.createOrderPanel("Order of the Steel Shovel", "Up to $10", TextureName.SteelShovel, donorsByLevel.get(3));
      this.add(
         new JLabel("LankyBrit would like to personally thank the following donors for their generosity and support:"),
         new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, 18, 0, new Insets(5, 5, 5, 5), 0, 0)
      );
      this.add(diamondPickaxe, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
      this.add(goldenSword, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
      this.add(steelShovel, new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
   }

   private Map<Integer, List<String>> getDonorsByLevel(String contributorsResourceName) {
      Map<Integer, List<String>> contributors = new HashMap<>();

      try {
         InputStream is = JContributorsPanel.class.getResourceAsStream(contributorsResourceName);
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));
         String entry = null;

         do {
            entry = reader.readLine();
            if (entry != null && entry.length() > 0) {
               String[] splitString = entry.split(",");
               int level = Integer.parseInt(splitString[0]);
               if (!contributors.containsKey(level)) {
                  contributors.put(level, new ArrayList<>());
               }

               contributors.get(level).add(splitString[1]);
            }
         } while (entry != null && entry.length() > 0);

         try {
            is.close();
         } catch (Exception var8) {
         }
      } catch (Throwable var9) {
      }

      return contributors;
   }

   private JPanel createOrderPanel(String name, String amount, TextureName texture, List<String> donors) {
      JTextArea displayArea = new JTextArea();
      displayArea.setEditable(false);
      Collections.sort(donors);

      for (String donor : donors) {
         displayArea.append(donor + "\n");
      }

      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      panel.setBorder(BorderFactory.createEtchedBorder(0));
      JLabel icon = new JLabel(texture.getImageIcon(this.texturePack));
      panel.add(icon, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 11, 0, new Insets(5, 0, 0, 0), 0, 0));
      JLabel title = new JLabel(name);
      panel.add(title, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 11, 0, new Insets(5, 0, 0, 0), 0, 0));
      JLabel donation = new JLabel("(" + amount + ")");
      panel.add(donation, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 11, 0, new Insets(5, 0, 0, 0), 0, 0));
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBorder(BorderFactory.createBevelBorder(1));
      scrollPane.getViewport().add(displayArea);
      panel.add(scrollPane, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, 15, 1, new Insets(5, 5, 5, 5), 0, 0));
      return panel;
   }
}
