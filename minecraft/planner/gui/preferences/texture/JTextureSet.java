package minecraft.planner.gui.preferences.texture;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import minecraft.planner.gui.SortedListModel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.util.TextureSet;

public class JTextureSet extends JPanel {
   private final minecraft.planner.gui.JTextureList toList;
   private TextureSet textureSet;
   private final List<TextureName> selectedTextures;

   public JTextureSet(TextureSet textureSet) {
      this.textureSet = textureSet;
      this.setLayout(new GridBagLayout());
      this.selectedTextures = new ArrayList<>();
      if (textureSet != null) {
         this.selectedTextures.addAll(textureSet.getTextures());
      }

      List<TextureName> fromTextures = new ArrayList<>(TextureName.texturableValuesCollection());
      if (textureSet != null) {
         fromTextures.removeAll(this.selectedTextures);
      }

      minecraft.planner.gui.JTextureList fromList = new minecraft.planner.gui.JTextureList(fromTextures);
      this.toList = new minecraft.planner.gui.JTextureList(this.selectedTextures);
      JScrollPane fromScrollPane = new JScrollPane();
      fromScrollPane.getViewport().add(fromList);
      fromScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Available Textures"));
      JScrollPane toScrollPane = new JScrollPane();
      toScrollPane.getViewport().add(this.toList);
      toScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Selected Textures"));
      JTextureSet.JListControls listControl = new JTextureSet.JListControls(fromList, this.toList);
      this.add(fromScrollPane, new GridBagConstraints(0, 0, 1, 1, 0.45, 1.0, 18, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.add(toScrollPane, new GridBagConstraints(2, 0, 1, 1, 0.45, 1.0, 18, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.add(listControl, new GridBagConstraints(1, 0, 1, 2, 0.1, 1.0, 10, 3, new Insets(5, 5, 5, 5), 0, 0));
      listControl.validateButtons();
   }

   public List<TextureName> getSelectedTextures() {
      return this.toList.getTextures();
   }

   public static void main(String[] argv) {
      JTextureSet textureSet = new JTextureSet(null);
      JFrame frame = new JFrame("JTextureSet Test");
      frame.setDefaultCloseOperation(3);
      frame.setSize(new Dimension(800, 600));
      frame.getContentPane().add(textureSet);
      frame.setVisible(true);
   }

   private class JListControls extends JPanel implements minecraft.planner.gui.JTextureListSelectionChangeListener {
      private minecraft.planner.gui.JTextureList fromList;
      private minecraft.planner.gui.JTextureList toList;
      private JButton oneToList = new JButton(">");
      private JButton allToList = new JButton(">>");
      private JButton oneFromList = new JButton("<");
      private JButton allFromList = new JButton("<<");

      public JListControls(minecraft.planner.gui.JTextureList fromList, minecraft.planner.gui.JTextureList toList) {
         this.oneToList.setToolTipText("Select highlighted textures");
         this.allToList.setToolTipText("Select all textures");
         this.oneFromList.setToolTipText("Remove highlighted textures");
         this.allFromList.setToolTipText("Remove all textures");
         this.fromList = fromList;
         this.toList = toList;
         this.setLayout(new GridBagLayout());
         this.add(this.oneToList, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
         this.add(this.allToList, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
         this.add(this.oneFromList, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
         this.add(this.allFromList, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
         this.fromList.registerListSelectionListener(this);
         this.toList.registerListSelectionListener(this);
         this.oneToList.addActionListener(JTextureSet.this.new MoveSelectedActionListener(this.fromList, this.toList));
         this.oneFromList.addActionListener(JTextureSet.this.new MoveSelectedActionListener(this.toList, this.fromList));
         this.allToList.addActionListener(JTextureSet.this.new MoveAllActionListener(this.fromList, this.toList));
         this.allFromList.addActionListener(JTextureSet.this.new MoveAllActionListener(this.toList, this.fromList));
      }

      public void validateButtons() {
         this.oneToList.setEnabled(this.fromList.getSelectedValues() != null && this.fromList.getSelectedValues().length > 0);
         this.allToList.setEnabled(this.fromList.getModel().getSize() > 0);
         this.oneFromList.setEnabled(this.toList.getSelectedValues() != null && this.toList.getSelectedValues().length > 0);
         this.allFromList.setEnabled(this.toList.getModel().getSize() > 0);
      }

      @Override
      public void listSelectionChanged(minecraft.planner.gui.JTextureList textureList) {
         this.validateButtons();
      }
   }

   public class MoveAllActionListener implements ActionListener {
      private SortedListModel<TextureName> sourceModel;
      private SortedListModel<TextureName> destinationModel;

      public MoveAllActionListener(minecraft.planner.gui.JTextureList source, minecraft.planner.gui.JTextureList destination) {
         this.sourceModel = source.getModel();
         this.destinationModel = destination.getModel();
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         List<TextureName> texturesToMove = new ArrayList<>();
         texturesToMove.addAll(this.sourceModel.elements());
         this.sourceModel.clear();
         this.destinationModel.addAll(texturesToMove);
      }
   }

   public class MoveSelectedActionListener implements ActionListener {
      private minecraft.planner.gui.JTextureList source;
      private minecraft.planner.gui.JTextureList destination;
      private SortedListModel<TextureName> sourceModel;
      private SortedListModel<TextureName> destinationModel;

      public MoveSelectedActionListener(minecraft.planner.gui.JTextureList source, minecraft.planner.gui.JTextureList destination) {
         this.source = source;
         this.destination = destination;
         this.sourceModel = source.getModel();
         this.destinationModel = destination.getModel();
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         List<TextureName> texturesToMove = new ArrayList<>();

         for (int index = 0; index < this.sourceModel.getSize(); index++) {
            if (this.source.isSelectedIndex(index)) {
               TextureName texture = (TextureName)this.sourceModel.getElementAt(index);
               texturesToMove.add(texture);
            }
         }

         this.sourceModel.removeAll(texturesToMove);
         List<TextureName> combinedList = new ArrayList<>();
         combinedList.addAll(texturesToMove);
         combinedList.addAll(this.destinationModel.elements());
         this.destinationModel.clear();
         this.destinationModel.addAll(combinedList);
      }
   }
}
