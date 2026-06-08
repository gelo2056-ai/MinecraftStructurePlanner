package minecraft.planner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import minecraft.planner.gui.JCloseableTab.TabCloseButton.1;
import minecraft.planner.gui.displaypanels.StructureProviderPanel;
import minecraft.planner.model.ModelSaveInformation;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;

public class JCloseableTab extends JPanel implements StructureModelChangeListener {
   private JTabbedPane pane;
   private StructureProviderPanel providerPanel;
   private JLabel text = new JLabel();
   private JCloseableTab.TabCloseButton closeButton;

   public JCloseableTab(JTabbedPane pane, StructureProviderPanel providerPanel) {
      this.pane = pane;
      this.providerPanel = providerPanel;
      this.setOpaque(false);
      this.setLayout(new GridBagLayout());
      this.text.setText(providerPanel.getProvider().getModel().getStructureName());
      this.add(this.text, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(4, 4, 4, 4), 0, 0));
      this.add(Box.createHorizontalBox(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
      this.closeButton = new JCloseableTab.TabCloseButton();
      this.add(this.closeButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 13, 0, new Insets(4, 0, 4, 4), 0, 0));
      providerPanel.getProvider().getModel().registerListener(this);
      this.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent arg0) {
            StructurePlanner.getInstance().validateMenuItems();
         }

         @Override
         public void focusLost(FocusEvent arg0) {
            StructurePlanner.getInstance().validateMenuItems();
         }
      });
   }

   public void updateTitle(String title) {
      this.text.setText(title);
      this.repaint();
   }

   public int close() {
      StructureModel<?> model = this.providerPanel.getProvider().getModel();
      ModelSaveInformation info = model.getModelSaveInfo();
      int answer = 1;
      if (info.isChanged()) {
         answer = JOptionPane.showConfirmDialog(
            StructurePlanner.getFrame(), "There are unsaved changes in tab '" + this.text.getText() + "'.  Do you want to save?", "Confirm Tab Close", 1
         );
      }

      if (answer == 1) {
         model.unregisterListener(this);
         this.providerPanel.getProvider().getStructurePanel().dispose();
         this.pane.remove(this.providerPanel);
         this.pane.repaint();
      } else if (answer == 0) {
         File fileInfo = info.getFile();

         try {
            if (StructurePlanner.getInstance().saveModel(this.providerPanel)) {
               model.unregisterListener(this);
               this.providerPanel.getProvider().getStructurePanel().dispose();
               this.pane.remove(this.providerPanel);
               this.pane.repaint();
            }
         } catch (Exception e) {
            new JErrorDialog(StructurePlanner.getFrame(), "Failed to save to " + fileInfo.getName(), e).setVisible(true);
         }
      }

      return answer;
   }

   @Override
   public void structureModelChanged(Object originator) {
      StringBuffer sb = new StringBuffer();
      StructureModel<?> model = this.providerPanel.getProvider().getModel();
      sb.append(model.getStructureName());
      ModelSaveInformation modelSaveInfo = model.getModelSaveInfo();
      String saveInfoString = modelSaveInfo.toString().trim();
      if (saveInfoString.length() > 0) {
         if (saveInfoString.length() > 1) {
            sb.append(" - ").append(saveInfoString);
         } else {
            sb.append(" ").append(saveInfoString);
         }
      }

      this.updateTitle(sb.toString());
      this.repaint();
   }

   private class TabCloseButton extends JButton {
      private Color buttonColor = Color.BLACK;

      public TabCloseButton() {
         int size = 17;
         this.setPreferredSize(new Dimension(size, size));
         this.setToolTipText("Close " + JCloseableTab.this.providerPanel.getProvider().getModel().getStructureName());
         this.setContentAreaFilled(false);
         this.setFocusable(false);
         this.setEnabled(true);
         this.setBorder(BorderFactory.createEtchedBorder());
         this.setBorderPainted(false);
         this.setRolloverEnabled(true);
         this.addMouseListener(new 1(this));
      }

      @Override
      public void updateUI() {
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g.create();
         g2.setColor(this.buttonColor);
         g2.drawLine(5, 5, this.getWidth() - 6, this.getHeight() - 6);
         g2.drawLine(this.getWidth() - 6, 5, 5, this.getHeight() - 6);
         g2.dispose();
      }
   }
}
