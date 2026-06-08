package minecraft.planner.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import minecraft.planner.gui.displaypanels.StructureProviderPanel;
import minecraft.planner.gui.provider.StructureProvider;

public class JProviderMenuItem extends JMenuItem {
   private final Class<?> providerClass;

   public JProviderMenuItem(JTabbedPane tabbedPane, Class<?> providerClass, String name) {
      this.providerClass = providerClass;
      this.setText(name);
      this.addActionListener(new JProviderMenuItem.MenuItemActionListener(tabbedPane, providerClass));
   }

   private class MenuItemActionListener implements ActionListener {
      private JTabbedPane pane;
      private Class<?> providerClass;

      public MenuItemActionListener(JTabbedPane tabbedPane, Class<?> providerClass) {
         this.pane = tabbedPane;
         this.providerClass = providerClass;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         try {
            StructureProvider<?> provider = (StructureProvider<?>)this.providerClass.newInstance();
            StructureProviderPanel providerPanel = new StructureProviderPanel(provider);
            this.pane.addTab(provider.getModel().getStructureName(), providerPanel);
            int tabIndex = this.pane.indexOfComponent(providerPanel);
            this.pane.setTabComponentAt(tabIndex, new JCloseableTab(this.pane, providerPanel));
            this.pane.setSelectedComponent(providerPanel);
            this.pane.validate();
            this.pane.repaint();
         } catch (Exception ex) {
            System.err.println("Failed to construct instance of " + this.providerClass.getSimpleName());
            ex.printStackTrace();
         }
      }
   }
}
