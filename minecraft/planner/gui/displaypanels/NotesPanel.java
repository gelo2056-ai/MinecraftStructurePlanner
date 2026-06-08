package minecraft.planner.gui.displaypanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;

public class NotesPanel<P extends StructureParameters> extends JPanel implements StructureModelChangeListener {
   private StructureModel<P> model;
   private JTextArea notesArea = new JTextArea();

   public NotesPanel(StructureModel<P> model) {
      this.model = model;
      this.setLayout(new GridBagLayout());
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.getViewport().add(this.notesArea);
      scrollPane.setBorder(BorderFactory.createBevelBorder(1));
      this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
      this.notesArea.addKeyListener(new KeyListener() {
         @Override
         public void keyTyped(KeyEvent e) {
            NotesPanel.this.model.setNotes(NotesPanel.this.notesArea.getText().trim());
            if (!NotesPanel.this.model.getModelSaveInfo().isChanged()) {
               NotesPanel.this.model.getModelSaveInfo().setChanged();
               NotesPanel.this.model.notifyListenersOfChange(NotesPanel.this);
            }
         }

         @Override
         public void keyReleased(KeyEvent e) {
         }

         @Override
         public void keyPressed(KeyEvent e) {
         }
      });
      this.model.registerListener(this);
   }

   @Override
   public void structureModelChanged(Object originator) {
      if (this != originator) {
         this.notesArea.setText(this.model.getNotes());
         this.notesArea.setCaretPosition(0);
      }
   }
}
