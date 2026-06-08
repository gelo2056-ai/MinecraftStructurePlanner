package minecraft.planner.gui.preferences.texturepack;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import minecraft.planner.gui.SortedListModel;
import minecraft.planner.gui.preferences.texturepack.JTexturePackLoaderDialog.Button;
import minecraft.planner.gui.textures.FileTexturePack;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.UserConfigurationManager;

public class JTexturePackPreferencesPanel extends JPanel {
   private JList list = new JList();
   private SortedListModel<TexturePack> listModel = new SortedListModel<>();
   private final JPanel userDefinedPanel;

   public JTexturePackPreferencesPanel() {
      this.setLayout(new GridBagLayout());
      JRadioButton defaultPack = new JRadioButton("Default texture pack");
      JRadioButton userDefinedPack = new JRadioButton("Third party texture pack");
      ButtonGroup buttonGroup = new ButtonGroup();
      buttonGroup.add(defaultPack);
      buttonGroup.add(userDefinedPack);
      this.add(defaultPack, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 11, 2, new Insets(5, 5, 5, 5), 0, 0));
      this.add(userDefinedPack, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 11, 2, new Insets(0, 5, 5, 5), 0, 0));
      this.userDefinedPanel = new JPanel();
      this.userDefinedPanel.setLayout(new GridBagLayout());
      this.userDefinedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Texture Packs"));
      this.list.setModel(this.listModel);
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBorder(BorderFactory.createBevelBorder(1));
      scrollPane.getViewport().add(this.list);
      JPanel texturePackControls = new JPanel();
      texturePackControls.setLayout(new GridBagLayout());
      JButton addPack = new JButton("Add");
      JButton removePack = new JButton("Delete");
      JButton previewPack = new JButton("Preview");
      texturePackControls.add(addPack, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      texturePackControls.add(removePack, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 5, 5, 5), 0, 0));
      texturePackControls.add(previewPack, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 5, 5, 5), 0, 0));
      this.userDefinedPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 17, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.userDefinedPanel.add(Box.createHorizontalBox(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.userDefinedPanel.add(texturePackControls, new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0, 10, 3, new Insets(5, 5, 5, 5), 0, 0));
      this.add(this.userDefinedPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, 15, 1, new Insets(0, 5, 5, 5), 0, 0));
      this.listModel.addAll(UserConfigurationManager.getTexturePacksFromCache());
      TexturePack currentPack = UserConfigurationManager.getSelectedTexturePack();
      if (currentPack == UserConfigurationManager.DEFAULT_TEXTURES) {
         defaultPack.setSelected(true);
         this.list.setSelectedValue(null, false);
      } else {
         userDefinedPack.setSelected(true);
         this.list.setSelectedValue(currentPack, true);
      }

      defaultPack.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            UserConfigurationManager.setSelectedTexturePack(UserConfigurationManager.DEFAULT_TEXTURES);
            JTexturePackPreferencesPanel.this.setUserDefinedPanelEnabled(false);
         }
      });
      this.list.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && JTexturePackPreferencesPanel.this.list.getSelectedIndex() >= 0) {
               TexturePack pack = (TexturePack)JTexturePackPreferencesPanel.this.list.getSelectedValue();
               UserConfigurationManager.setSelectedTexturePack(pack);
            }
         }
      });
      userDefinedPack.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JTexturePackPreferencesPanel.this.setUserDefinedPanelEnabled(true);
            TexturePack texturePack = (TexturePack)JTexturePackPreferencesPanel.this.list.getSelectedValue();
            if (texturePack != null) {
               UserConfigurationManager.setSelectedTexturePack(texturePack);
            } else {
               UserConfigurationManager.setSelectedTexturePack(UserConfigurationManager.DEFAULT_TEXTURES);
            }
         }
      });
      addPack.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JTexturePackLoaderDialog dialog = new JTexturePackLoaderDialog();
            dialog.setVisible(true);
            if (dialog.getButtonClicked() == Button.OK) {
               FileTexturePack texturePack = new FileTexturePack(dialog.getTexturePackName(), dialog.getTexturePackFile());
               JTexturePackPreferencesPanel.this.listModel.addElement(texturePack);
               JTexturePackPreferencesPanel.this.list.setSelectedValue(texturePack, true);
               UserConfigurationManager.addTexturePackToCache(texturePack);
            }
         }
      });
      removePack.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               TexturePack texturePack = (TexturePack)JTexturePackPreferencesPanel.this.list.getSelectedValue();
               if (texturePack != null
                  && JOptionPane.showConfirmDialog(
                        JTexturePackPreferencesPanel.this,
                        "Are you sure you want to remove texture pack '" + texturePack.getName() + "?'",
                        "Delete Texture Pack",
                        0,
                        3
                     )
                     == 0) {
                  JTexturePackPreferencesPanel.this.listModel.removeElement(texturePack);
                  UserConfigurationManager.removeTexturePackFromCache(texturePack);
               }
            }
         }
      );
      previewPack.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            TexturePack texturePack = (TexturePack)JTexturePackPreferencesPanel.this.list.getSelectedValue();
            if (texturePack != null) {
               new JTexturePackPreviewDialog(texturePack);
            }
         }
      });
      defaultPack.setSelected(UserConfigurationManager.getSelectedTexturePack() == UserConfigurationManager.DEFAULT_TEXTURES);
      userDefinedPack.setSelected(UserConfigurationManager.getSelectedTexturePack() != UserConfigurationManager.DEFAULT_TEXTURES);
      this.setUserDefinedPanelEnabled(UserConfigurationManager.getSelectedTexturePack() != UserConfigurationManager.DEFAULT_TEXTURES);
   }

   private void setUserDefinedPanelEnabled(boolean enabled) {
      this.setComponentEnabled(this.userDefinedPanel, enabled);
   }

   private void setComponentEnabled(Component c, boolean enabled) {
      c.setEnabled(enabled);
      if (Container.class.isAssignableFrom(c.getClass())) {
         Container container = (Container)c;
         Component[] var7;
         int var6 = (var7 = container.getComponents()).length;

         for (int var5 = 0; var5 < var6; var5++) {
            Component child = var7[var5];
            this.setComponentEnabled(child, enabled);
         }
      }
   }
}
