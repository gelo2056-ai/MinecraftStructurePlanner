package minecraft.planner.gui.preferences.texture;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import minecraft.planner.gui.SortedListModel;
import minecraft.planner.gui.preferences.texture.JTextureSetEditorDialog.Button;
import minecraft.planner.gui.preferences.texture.JTextureSetEditorDialog.Operation;
import minecraft.planner.util.TextureSet;
import minecraft.planner.util.UserConfigurationManager;

public class JTextureSetPreferencesPanel extends JPanel {
   private JList textureSetList;
   private SortedListModel<TextureSet> textureSetListModel;
   private JTextureList textureListPanel;
   private JButton newButton;
   private JButton editButton;
   private JButton deleteButton;

   public JTextureSetPreferencesPanel() {
      this.setLayout(new GridBagLayout());
      this.textureSetList = new JList();
      this.textureSetListModel = new SortedListModel<>();
      this.textureSetList.setModel(this.textureSetListModel);
      this.textureSetList.setSelectionMode(0);
      this.textureSetList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
               TextureSet textureSet = (TextureSet)JTextureSetPreferencesPanel.this.textureSetList.getSelectedValue();
               if (textureSet != null && textureSet.getTextures() != null) {
                  JTextureSetPreferencesPanel.this.textureListPanel.setTextures(textureSet.getTextures());
               }

               JTextureSetPreferencesPanel.this.validateButtons();
            }
         }
      });
      List<TextureSet> textureSets = new ArrayList<>(UserConfigurationManager.getTextureSets());
      this.textureListPanel = new JTextureList();
      this.textureListPanel.setEnabled(false);
      this.textureSetListModel.addAll(textureSets);
      this.newButton = new JButton("New...");
      this.editButton = new JButton("Edit...");
      this.deleteButton = new JButton("Delete...");
      this.newButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JTextureSetEditorDialog editor = new JTextureSetEditorDialog(Operation.New, null);
            editor.setVisible(true);
            if (editor.getButtonClicked() == Button.OK) {
               TextureSet newSet = editor.getTextureSet();
               JTextureSetPreferencesPanel.this.textureSetListModel.addElement(newSet);
               JTextureSetPreferencesPanel.this.textureSetList.setSelectedValue(newSet, true);
               UserConfigurationManager.addTexturesToCache(newSet);
               UserConfigurationManager.saveTextureSets();
            }
         }
      });
      this.editButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            TextureSet selectedTextureSet = (TextureSet)JTextureSetPreferencesPanel.this.textureSetList.getSelectedValue();
            if (selectedTextureSet != null) {
               JTextureSetEditorDialog editor = new JTextureSetEditorDialog(Operation.Edit, selectedTextureSet);
               editor.setVisible(true);
               if (editor.getButtonClicked() == Button.OK) {
                  TextureSet editedSet = editor.getTextureSet();
                  JTextureSetPreferencesPanel.this.textureSetListModel.removeElement(selectedTextureSet);
                  JTextureSetPreferencesPanel.this.textureSetListModel.addElement(editedSet);
                  JTextureSetPreferencesPanel.this.textureSetList.setSelectedValue(editedSet, true);
                  UserConfigurationManager.addTexturesToCache(editedSet);
                  UserConfigurationManager.saveTextureSets();
               }
            }
         }
      });
      this.deleteButton
         .addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  TextureSet selectedTextureSet = (TextureSet)JTextureSetPreferencesPanel.this.textureSetList.getSelectedValue();
                  if (selectedTextureSet != null
                     && JOptionPane.showConfirmDialog(
                           JTextureSetPreferencesPanel.this,
                           "Are you sure you want to delete texture set '" + selectedTextureSet.getName() + "?'",
                           "Delete Texture Set",
                           0,
                           3
                        )
                        == 0) {
                     JTextureSetPreferencesPanel.this.textureSetListModel.removeElement(selectedTextureSet);
                     UserConfigurationManager.removeTexturesFromCache(selectedTextureSet);
                     UserConfigurationManager.saveTextureSets();
                     JTextureSetPreferencesPanel.this.textureSetList.clearSelection();
                  }
               }
            }
         );
      JPanel controlPanel = new JPanel();
      controlPanel.setLayout(new GridBagLayout());
      controlPanel.add(this.newButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      controlPanel.add(this.editButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      controlPanel.add(this.deleteButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
      JScrollPane textureSetListPane = new JScrollPane();
      textureSetListPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Texture Sets"));
      textureSetListPane.getViewport().add(this.textureSetList);
      JScrollPane textureListPane = new JScrollPane();
      textureListPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Textures"));
      textureListPane.getViewport().add(this.textureListPanel);
      this.add(textureSetListPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 18, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.add(textureListPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 18, 1, new Insets(5, 0, 5, 5), 0, 0));
      this.add(controlPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0, 12, 3, new Insets(5, 0, 5, 5), 0, 0));
      this.validateButtons();
   }

   private void validateButtons() {
      TextureSet selectedItem = (TextureSet)this.textureSetList.getSelectedValue();
      this.newButton.setEnabled(true);
      if (selectedItem != null) {
         this.editButton.setEnabled(selectedItem.isEditable());
         this.deleteButton.setEnabled(selectedItem.isEditable());
      } else {
         this.editButton.setEnabled(false);
         this.deleteButton.setEnabled(false);
      }
   }
}
