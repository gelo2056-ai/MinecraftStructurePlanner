package minecraft.planner.gui.preferences.texture;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.UserConfigurationManager;

public class JTextureList extends JList {
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private Set<JTextureListSelectionChangeListener> listeners = new HashSet<>();
   private DefaultListModel listModel;

   public void registerListSelectionListener(JTextureListSelectionChangeListener listener) {
      this.listeners.add(listener);
   }

   public JTextureList() {
      this((List<TextureName>)null);
   }

   public JTextureList(TextureName[] textures) {
      DefaultListModel listModel = new DefaultListModel();
      this.setModel(listModel);
      List<TextureName> textureList = new ArrayList<>();
      TextureName[] var7 = textures;
      int var6 = textures.length;

      for (int var5 = 0; var5 < var6; var5++) {
         TextureName texture = var7[var5];
         if (texture != TextureName.None && texture.isTexturable()) {
            textureList.add(texture);
         }
      }

      Collections.sort(textureList);

      for (TextureName texture : textureList) {
         listModel.addElement(texture);
      }

      this.setCellRenderer(new JTextureList.JTextureListCellRenderer(null));
      this.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            for (JTextureListSelectionChangeListener listener : JTextureList.this.listeners) {
               listener.listSelectionChanged((JTextureList)e.getSource());
            }
         }
      });
   }

   public JTextureList(List<TextureName> textures) {
      this.listModel = new DefaultListModel();
      this.setModel(this.listModel);
      this.setTextures(textures);
      this.setCellRenderer(new JTextureList.JTextureListCellRenderer(null));
      this.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            for (JTextureListSelectionChangeListener listener : JTextureList.this.listeners) {
               listener.listSelectionChanged((JTextureList)e.getSource());
            }
         }
      });
   }

   public void setTextures(Collection<TextureName> textures) {
      this.listModel.removeAllElements();
      List<TextureName> textureList = new ArrayList<>();
      if (textures != null) {
         for (TextureName texture : textures) {
            if (texture != TextureName.None && texture.isTexturable()) {
               textureList.add(texture);
            }
         }
      }

      Collections.sort(textureList);

      for (TextureName texture : textureList) {
         this.listModel.addElement(texture);
      }
   }

   private class JTextureListCellRenderer extends DefaultListCellRenderer {
      private JTextureListCellRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         JPanel panel = new JPanel();
         panel.setLayout(new GridBagLayout());
         TextureName texture = (TextureName)value;
         JLabel imageIcon = new JLabel(texture.getPlanViewImageIcon(JTextureList.this.texturePack));
         JLabel textureLabel = new JLabel(texture.getDisplayName());
         imageIcon.setOpaque(false);
         textureLabel.setOpaque(false);
         if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
            textureLabel.setForeground(list.getSelectionForeground());
         } else {
            panel.setBackground(Color.WHITE);
            textureLabel.setForeground(Color.BLACK);
         }

         panel.add(imageIcon, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 0), 0, 0));
         panel.add(textureLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 13, 2, new Insets(5, 5, 5, 5), 0, 0));
         return panel;
      }
   }
}
