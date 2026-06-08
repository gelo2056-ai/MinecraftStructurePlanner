package minecraft.planner.gui.viaduct;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.viaduct.ViaductParameters;

public class ViaductControlPanel extends StructureControlPanel<ViaductParameters> {
   private JTextField span = new JTextField();
   private JTextField depth = new JTextField();
   private JTextField archRadius = new JTextField();
   private JTextField archHeight = new JTextField();
   private JTextField towerWidth = new JTextField();
   private JTextField towerHeight = new JTextField();
   private JTextField wallHeight = new JTextField();
   private JTextField deckThickness = new JTextField("1");
   private ViaductControlPanel.TextureCombo viaductTexture = new ViaductControlPanel.TextureCombo(TextureName.Cobblestone);
   private ViaductControlPanel.TextureCombo wallTexture = new ViaductControlPanel.TextureCombo(TextureName.Fencepost);

   public ViaductControlPanel(GenerateListener<ViaductParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Viaduct Span:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.span, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Viaduct Depth:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.depth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Arch Radius:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.archRadius, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Arch Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.archHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Tower Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.towerWidth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Tower Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.towerHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Wall Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.wallHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Deck Thickness:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.deckThickness, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Viaduct Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.viaductTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Wall Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.wallTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.addGenerateButton(row);
   }

   protected ViaductParameters generateParameters() {
      int span = Integer.parseInt(this.span.getText());
      int depth = Integer.parseInt(this.depth.getText());
      int archRadius = Integer.parseInt(this.archRadius.getText());
      int archHeight = Integer.parseInt(this.archHeight.getText());
      int towerWidth = Integer.parseInt(this.towerWidth.getText());
      int towerHeight = Integer.parseInt(this.towerHeight.getText());
      int wallHeight = Integer.parseInt(this.wallHeight.getText());
      int deckThickness = Integer.parseInt(this.deckThickness.getText());
      TextureName viaduct = (TextureName)this.viaductTexture.getSelectedItem();
      TextureName wall = (TextureName)this.wallTexture.getSelectedItem();
      return new ViaductParameters(span, depth, archRadius, archHeight, towerWidth, towerHeight, wallHeight, deckThickness, viaduct, wall);
   }

   private class TextureCombo extends JComboBox {
      public TextureCombo(TextureName defaultTexture) {
         this.setModel(new DefaultComboBoxModel<>(TextureName.texturableValues()));
         this.setSelectedItem(defaultTexture);
      }
   }
}
