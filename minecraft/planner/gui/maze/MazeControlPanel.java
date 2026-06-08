package minecraft.planner.gui.maze;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.maze.MazeParameters;

public class MazeControlPanel extends StructureControlPanel<MazeParameters> {
   private ButtonGroup buttonGroup = new ButtonGroup();
   private JTextField mazeWidth = new JTextField();
   private JTextField mazeDepth = new JTextField();
   private JTextField borderHeight = new JTextField();
   private JTextField wallHeight = new JTextField();
   private MazeControlPanel.TextureCombo borderTexture = new MazeControlPanel.TextureCombo(TextureName.Stone);
   private MazeControlPanel.TextureCombo wallTexture = new MazeControlPanel.TextureCombo(TextureName.Cobblestone);
   private MazeControlPanel.TextureCombo floorTexture = new MazeControlPanel.TextureCombo(TextureName.None);
   private JCheckBox centerRoom = new JCheckBox("Create center room");
   private JTextField centerRoomWidth = new JTextField();
   private JTextField centerRoomDepth = new JTextField();
   private JTextField centerRoomHeight = new JTextField();
   private MazeControlPanel.TextureCombo centerRoomTexture = new MazeControlPanel.TextureCombo(TextureName.Gold);

   public MazeControlPanel(GenerateListener<MazeParameters> listener) {
      super(listener);
      int row = 0;
      this.add(new JLabel("Maze Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.mazeWidth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Maze Depth:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.mazeDepth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Border Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.borderHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Wall Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(5, 5, 5, 0), 0, 0));
      this.add(this.wallHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(5, 5, 5, 5), 50, 0));
      this.add(new JLabel("Border Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.borderTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Wall Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.wallTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Floor Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.floorTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(Box.createVerticalStrut(20), new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 3, new Insets(0, 0, 0, 0), 0, 0));
      this.add(this.centerRoom, new GridBagConstraints(0, row++, 2, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Room Width:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.centerRoomWidth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Room Depth:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.centerRoomDepth, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Room Height:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.centerRoomHeight, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(new JLabel("Room Texture:"), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, 18, 0, new Insets(0, 5, 5, 0), 0, 0));
      this.add(this.centerRoomTexture, new GridBagConstraints(1, row++, 1, 1, 1.0, 0.0, 18, 2, new Insets(0, 5, 5, 5), 50, 0));
      this.add(Box.createVerticalStrut(20), new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 3, new Insets(0, 0, 0, 0), 0, 0));
      this.centerRoom.setSelected(false);
      this.centerRoomWidth.setEnabled(false);
      this.centerRoomDepth.setEnabled(false);
      this.centerRoomHeight.setEnabled(false);
      this.centerRoomTexture.setEnabled(false);
      this.centerRoom.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            boolean centerRoomSelected = MazeControlPanel.this.centerRoom.isSelected();
            MazeControlPanel.this.centerRoomWidth.setEnabled(centerRoomSelected);
            MazeControlPanel.this.centerRoomDepth.setEnabled(centerRoomSelected);
            MazeControlPanel.this.centerRoomHeight.setEnabled(centerRoomSelected);
            MazeControlPanel.this.centerRoomTexture.setEnabled(centerRoomSelected);
         }
      });
      this.addGenerateButton(row);
   }

   protected MazeParameters generateParameters() {
      int width = Integer.parseInt(this.mazeWidth.getText());
      int depth = Integer.parseInt(this.mazeDepth.getText());
      int borderHeight = Integer.parseInt(this.borderHeight.getText());
      int wallHeight = Integer.parseInt(this.wallHeight.getText());
      boolean centerRoom = this.centerRoom.isSelected();
      if (centerRoom) {
         int centerRoomWidth = Integer.parseInt(this.centerRoomWidth.getText());
         int centerRoomDepth = Integer.parseInt(this.centerRoomDepth.getText());
         int centerRoomHeight = Integer.parseInt(this.centerRoomHeight.getText());
         TextureName centerRoomTexture = (TextureName)this.centerRoomTexture.getSelectedItem();
         return new MazeParameters(
            width,
            depth,
            borderHeight,
            wallHeight,
            (TextureName)this.borderTexture.getSelectedItem(),
            (TextureName)this.wallTexture.getSelectedItem(),
            (TextureName)this.floorTexture.getSelectedItem(),
            centerRoom,
            centerRoomWidth,
            centerRoomDepth,
            centerRoomHeight,
            centerRoomTexture
         );
      } else {
         return new MazeParameters(
            width,
            depth,
            borderHeight,
            wallHeight,
            (TextureName)this.borderTexture.getSelectedItem(),
            (TextureName)this.wallTexture.getSelectedItem(),
            (TextureName)this.floorTexture.getSelectedItem(),
            false,
            0,
            0,
            0,
            TextureName.None
         );
      }
   }

   private class TextureCombo extends JComboBox {
      public TextureCombo(TextureName defaultTexture) {
         this.setModel(new DefaultComboBoxModel<>(TextureName.texturableValues()));
         this.setSelectedItem(defaultTexture);
      }
   }
}
