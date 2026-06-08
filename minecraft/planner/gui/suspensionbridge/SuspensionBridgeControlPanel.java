package minecraft.planner.gui.suspensionbridge;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.suspensionbridge.SuspensionBridgeParameters;

public class SuspensionBridgeControlPanel extends StructureControlPanel<SuspensionBridgeParameters> {
   private JTextField span = new JTextField();
   private JTextField deckWidth = new JTextField();
   private JTextField deckHeight = new JTextField();
   private JTextField towerInset = new JTextField();
   private JTextField towerHeight = new JTextField();
   private JTextField minimumCableHeight = new JTextField();
   private JTextField numberOfCrossBraces = new JTextField();
   private JTextField crossBraceSpacing = new JTextField();
   private SuspensionBridgeControlPanel.TextureCombo deckTexture = new SuspensionBridgeControlPanel.TextureCombo(TextureName.Cobblestone);
   private SuspensionBridgeControlPanel.TextureCombo towerTexture = new SuspensionBridgeControlPanel.TextureCombo(TextureName.Stone);
   private SuspensionBridgeControlPanel.TextureCombo cableTexture = new SuspensionBridgeControlPanel.TextureCombo(TextureName.Wood);

   public SuspensionBridgeControlPanel(GenerateListener<SuspensionBridgeParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Bridge Span:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.span, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Deck Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.deckWidth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Deck Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.deckHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Side Span:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.towerInset, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Tower Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.towerHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Min Cable Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.minimumCableHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Cross Braces:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.numberOfCrossBraces, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Brace Spacing:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.crossBraceSpacing, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Deck Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.deckTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Tower Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.towerTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Cable Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.cableTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.addGenerateButton(row);
   }

   protected SuspensionBridgeParameters generateParameters() {
      int span = Integer.parseInt(this.span.getText());
      int deckWidth = Integer.parseInt(this.deckWidth.getText());
      int deckHeight = Integer.parseInt(this.deckHeight.getText());
      int towerInset = Integer.parseInt(this.towerInset.getText());
      int towerHeight = Integer.parseInt(this.towerHeight.getText());
      int minimumCableHeight = Integer.parseInt(this.minimumCableHeight.getText());
      int numberOfCrossBraces = Integer.parseInt(this.numberOfCrossBraces.getText());
      int crossBraceSpacing = Integer.parseInt(this.crossBraceSpacing.getText());
      TextureName deck = (TextureName)this.deckTexture.getSelectedItem();
      TextureName tower = (TextureName)this.towerTexture.getSelectedItem();
      TextureName cable = (TextureName)this.cableTexture.getSelectedItem();
      return new SuspensionBridgeParameters(
         towerHeight, deckHeight, deckWidth, span, towerInset, minimumCableHeight, numberOfCrossBraces, crossBraceSpacing, deck, tower, cable
      );
   }

   private class TextureCombo extends JComboBox {
      public TextureCombo(TextureName defaultTexture) {
         this.setModel(new DefaultComboBoxModel<>(TextureName.texturableValues()));
         this.setSelectedItem(defaultTexture);
      }
   }
}
