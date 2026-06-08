package minecraft.planner.gui.displaypanels;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import minecraft.planner.gui.JCollapsablePanel;
import minecraft.planner.gui.SortedListModel;
import minecraft.planner.gui.displaypanels.TexturePanel.TextureArea.1;
import minecraft.planner.gui.displaypanels.TexturePanel.TextureArea.2;
import minecraft.planner.gui.displaypanels.TexturePanel.TextureArea.JTextureButton;
import minecraft.planner.gui.displaypanels.TexturePanel.TextureArea.JTextureIcon;
import minecraft.planner.gui.java3d.TextureCategory;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.util.TextureSet;
import minecraft.planner.util.UserConfigurationManager;

public class TexturePanel extends JPanel {
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private JPanel iconPanel = new JPanel();
   private JScrollPane scrollPane = new JScrollPane();
   private Set<TextureName> textures = new TreeSet<>();
   private static final int TEXTURE_SIZE = 32;
   private Set<TextureChangeListener> listeners = new HashSet<>();
   private Set<TextureFilterChangeListener> filterListeners = new HashSet<>();
   private TexturePanel.TextureFilter textureFilter = new TexturePanel.TextureFilter();
   private TexturePanel.TextureArea textureArea;
   private boolean allowSelect;

   public Collection<TextureName> getFilteredTextures() {
      return this.textures;
   }

   public TexturePanel(boolean allowSelect) {
      this(allowSelect, UserConfigurationManager.getTexturesFromCache("All"));
   }

   public TexturePanel(boolean allowSelect, TextureSet defaultTextureSet) {
      this.allowSelect = allowSelect;
      this.textureArea = new TexturePanel.TextureArea(defaultTextureSet);
      this.setLayout(new BorderLayout());
      this.add(this.textureFilter, "North");
      this.add(this.textureArea, "Center");
   }

   public void addTextureChangeListener(TextureChangeListener listener) {
      this.listeners.add(listener);
   }

   public void removeTextureChangeListener(TextureChangeListener listener) {
      this.listeners.remove(listener);
   }

   public void addTextureFilterChangeListener(TextureFilterChangeListener listener) {
      this.filterListeners.add(listener);
   }

   public void removeTextureFilterChangeListener(TextureFilterChangeListener listener) {
      this.filterListeners.remove(listener);
   }

   private void fireTextureFilterChanged(Collection<TextureName> textures) {
      for (TextureFilterChangeListener listener : this.filterListeners) {
         listener.textureFilterChanged(textures);
      }
   }

   private void fireTextureChanged(TextureName texture) {
      for (TextureChangeListener listener : this.listeners) {
         listener.textureChanged(texture);
      }
   }

   private class JCategoryCheckBox extends JCheckBox {
      private TextureCategory category;

      public JCategoryCheckBox(TextureCategory category, boolean isSelected) {
         super(category.name(), isSelected);
         this.category = category;
      }

      public TextureCategory getCategory() {
         return this.category;
      }
   }

   protected class TextureArea extends JPanel {
      private final TextureSet defaultTextures;

      public TextureArea(TextureSet defaultTextures) {
         this.defaultTextures = defaultTextures;
         this.setFocusable(false);
         this.setLayout(new BorderLayout());
         this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Textures"));
         TexturePanel.this.scrollPane.setHorizontalScrollBarPolicy(31);
         TexturePanel.this.iconPanel.setLayout(new GridBagLayout());
         TexturePanel.this.scrollPane.getViewport().add(TexturePanel.this.iconPanel);
         this.add(TexturePanel.this.scrollPane, "Center");
         InputMap map = TexturePanel.this.scrollPane.getInputMap(1);
         map.put(KeyStroke.getKeyStroke("UP"), "none");
         map.put(KeyStroke.getKeyStroke("DOWN"), "none");
         map.put(KeyStroke.getKeyStroke("LEFT"), "none");
         map.put(KeyStroke.getKeyStroke("RIGHT"), "none");
         this.addComponentListener(new 1(this));
         SwingUtilities.invokeLater(new 2(this));
      }

      protected void setTextures() {
         ButtonGroup buttonGroup = new ButtonGroup();
         JTextureButton textureButton = null;
         JTextureIcon textureIcon = null;
         TexturePanel.this.iconPanel.removeAll();
         TexturePanel.this.iconPanel.add(Box.createVerticalBox(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 11, 0, new Insets(0, 0, 0, 0), 0, 10));
         int row = 0;
         int count = 0;
         int numberOfColumns = (int)Math.floor(this.getWidth() / 80.0);
         if (numberOfColumns < 1) {
            numberOfColumns = 1;
         }

         for (TextureName name : TexturePanel.this.textures) {
            JPanel individualTexturePanel = new JPanel();
            individualTexturePanel.setLayout(new FlowLayout());
            row = (int)Math.floor(count / numberOfColumns);
            int col = count % numberOfColumns * numberOfColumns;
            if (TexturePanel.this.allowSelect) {
               textureButton = new JTextureButton(this, name);
               individualTexturePanel.add(textureButton);
               buttonGroup.add(textureButton);
            }

            textureIcon = new JTextureIcon(this, name, textureButton);
            individualTexturePanel.add(textureIcon);
            TexturePanel.this.iconPanel.add(individualTexturePanel, new GridBagConstraints(col, row, 1, 1, 1.0, 0.0, 17, 2, new Insets(2, 0, 2, 2), 0, 0));
            count++;
         }

         TexturePanel.this.iconPanel.add(Box.createVerticalBox(), new GridBagConstraints(0, row + 1, 1, 1, 1.0, 1.0, 15, 3, new Insets(0, 0, 0, 0), 0, 0));
         TexturePanel.this.iconPanel.revalidate();
         TexturePanel.this.iconPanel.repaint();
      }
   }

   private class TextureFilter extends JCollapsablePanel {
      private final JList textureSetList = new JList();

      public TextureFilter() {
         super("Texture Filter", false, false);
         Container pane = this.getContentPane();
         pane.setLayout(new GridBagLayout());
         this.textureSetList.setSelectionMode(2);
         SortedListModel<TextureSet> textureSetModel = new SortedListModel<>();
         this.textureSetList.setModel(textureSetModel);
         textureSetModel.addAll(UserConfigurationManager.getTextureSets());
         JScrollPane scrollPane = new JScrollPane();
         scrollPane.setBorder(BorderFactory.createBevelBorder(1));
         scrollPane.getViewport().add(this.textureSetList);
         pane.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 2, new Insets(5, 5, 5, 5), 0, 0));
         this.textureSetList.addListSelectionListener(new minecraft.planner.gui.displaypanels.TexturePanel.TextureFilter.1(this));
      }

      public void filterChanged() {
         TexturePanel.this.textures.clear();
         TexturePanel.this.textures.add(TextureName.None);
         Object[] var4;
         int var3 = (var4 = this.textureSetList.getSelectedValues()).length;

         for (int var2 = 0; var2 < var3; var2++) {
            Object o = var4[var2];
            TextureSet textureSet = (TextureSet)o;
            TexturePanel.this.textures.addAll(textureSet.getTextures());
         }

         TexturePanel.this.textureArea.setTextures();
      }
   }
}
