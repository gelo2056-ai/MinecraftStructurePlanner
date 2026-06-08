package minecraft.planner.gui.menger;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import minecraft.planner.gui.displaypanels.StructureControlPanel;
import minecraft.planner.model.GenerateListener;
import minecraft.planner.model.menger.MengerParameters;

public class MengerControlPanel extends StructureControlPanel<MengerParameters> {
   private ButtonGroup buttonGroup = new ButtonGroup();
   private JRadioButton level1 = new JRadioButton("Level 1");
   private JRadioButton level2 = new JRadioButton("Level 2");
   private JRadioButton level3 = new JRadioButton("Level 3");
   private int level;

   public MengerControlPanel(GenerateListener<MengerParameters> listener) {
      super(listener);
      int row = 0;
      this.buttonGroup.add(this.level1);
      this.buttonGroup.add(this.level2);
      this.buttonGroup.add(this.level3);
      this.add(this.level1, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(5, 5, 0, 0), 0, 0));
      this.add(this.level2, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(5, 5, 0, 0), 0, 0));
      this.add(this.level3, new GridBagConstraints(0, row++, 2, 1, 0.0, 0.0, 18, 2, new Insets(5, 5, 0, 0), 0, 0));
      this.add(Box.createVerticalBox(), new GridBagConstraints(0, row++, 2, 1, 0.0, 1.0, 15, 1, new Insets(0, 0, 0, 0), 0, 0));
      this.level1.addChangeListener(new MengerControlPanel.RadioChangeListener(this.level1, 1));
      this.level2.addChangeListener(new MengerControlPanel.RadioChangeListener(this.level2, 2));
      this.level3.addChangeListener(new MengerControlPanel.RadioChangeListener(this.level3, 3));
      this.level = 0;
   }

   protected MengerParameters generateParameters() {
      return new MengerParameters(this.level);
   }

   private class RadioChangeListener implements ChangeListener {
      private JRadioButton button;
      private int level;

      public RadioChangeListener(JRadioButton button, int level) {
         this.button = button;
         this.level = level;
      }

      @Override
      public void stateChanged(ChangeEvent e) {
         if (this.button.isSelected() && MengerControlPanel.this.level != this.level) {
            MengerControlPanel.this.level = this.level;
            MengerControlPanel.this.listener.generate(MengerControlPanel.this.generateParameters());
         }
      }
   }
}
