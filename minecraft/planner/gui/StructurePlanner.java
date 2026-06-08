package minecraft.planner.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import minecraft.planner.gui.StructurePlanner.FileChooserAccessoryManager.1;
import minecraft.planner.gui.StructurePlanner.FileChooserAccessoryManager.2;
import minecraft.planner.gui.displaypanels.StructureDisplayPanel;
import minecraft.planner.gui.displaypanels.StructureProviderPanel;
import minecraft.planner.gui.freeform.FreeformControlPanel;
import minecraft.planner.gui.freeform.FreeformStructureDisplayPanel;
import minecraft.planner.gui.preferences.JPreferencesDialog;
import minecraft.planner.gui.provider.BitmapProvider;
import minecraft.planner.gui.provider.BridgeProvider;
import minecraft.planner.gui.provider.CircleProvider;
import minecraft.planner.gui.provider.DomeProvider;
import minecraft.planner.gui.provider.EasterIslandProvider;
import minecraft.planner.gui.provider.FreeformProvider;
import minecraft.planner.gui.provider.HemisphereProvider;
import minecraft.planner.gui.provider.MengerProvider;
import minecraft.planner.gui.provider.SphereProvider;
import minecraft.planner.gui.provider.StructureProvider;
import minecraft.planner.gui.provider.SuspensionBridgeProvider;
import minecraft.planner.gui.provider.TorusProvider;
import minecraft.planner.gui.provider.ViaductProvider;
import minecraft.planner.gui.provider.maze.DepthFirstMazeProvider;
import minecraft.planner.gui.provider.maze.NewestGrowingTreeMazeProvider;
import minecraft.planner.gui.provider.maze.OldestGrowingTreeMazeProvider;
import minecraft.planner.gui.provider.maze.PrimMazeProvider;
import minecraft.planner.gui.provider.maze.RandomGrowingTreeMazeProvider;
import minecraft.planner.gui.provider.maze.RecursiveBacktrackerMazeProvider;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.gui.version.JAboutDialog;
import minecraft.planner.gui.version.VersionInfo;
import minecraft.planner.gui.version.VersionSplash;
import minecraft.planner.gui.version.VersionUtilities;
import minecraft.planner.model.ModelSaveInformation;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.freeform.Binvox;
import minecraft.planner.model.freeform.FreeformParameters;
import minecraft.planner.model.freeform.Ruins;
import minecraft.planner.model.grid.UndoBuffer;
import minecraft.planner.model.util.Tag;
import minecraft.planner.util.OS;
import minecraft.planner.util.OSLoader;
import minecraft.planner.util.UserConfigurationManager;

public class StructurePlanner {
   private static final int MAX_CELL_SIZE = 64;
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private static boolean is3DEnabled;
   private static boolean isLoaderEnabled;
   private JTabbedPane contentPanel;
   private JContributorsPanel contributorsPanel;
   private static JStatusBar statusBar = new JStatusBar();
   private static JFrame frame;
   private JMenuItem saveMenu;
   private JMenuItem saveAsMenu;
   private JMenuItem exportMenu;
   private JMenuItem importMenu;
   private JMenuItem undoItem;
   private JMenuItem copyItem;
   private JMenuItem pasteItem;
   private JMenuItem printItem;
   private StructurePlanner.StructurePlannerFileFilter structurePlannerFilter;
   private StructurePlanner.StructurePlannerFileFilter schematicFilter;
   private StructurePlanner.StructurePlannerFileFilter binvoxFilter;
   private StructurePlanner.StructurePlannerFileFilter ruinsFilter;
   private StructurePlanner.StructurePlannerFileFilter imageFilter;
   private static final StructurePlanner instance = new StructurePlanner();
   private static final JAboutDialog about = new JAboutDialog();

   public static StructurePlanner getInstance() {
      return instance;
   }

   private StructurePlanner() {
      String disable3D = System.getProperty("disable3D");
      String disableOSLoader = System.getProperty("disableLoader");
      isLoaderEnabled = true;
      if (disableOSLoader != null && disableOSLoader.trim().length() > 0) {
         try {
            isLoaderEnabled = !Boolean.parseBoolean(disableOSLoader);
         } catch (Throwable t) {
            isLoaderEnabled = true;
         }
      }

      OS os = OSLoader.determineOS();
      OSLoader.loadLibraries(os);
      this.contentPanel = new JTabbedPane();
      is3DEnabled = true;
      if (disable3D != null && disable3D.trim().length() > 0) {
         try {
            is3DEnabled = !Boolean.parseBoolean(disable3D);
         } catch (Throwable t) {
            is3DEnabled = true;
         }
      }

      if (is3DEnabled) {
         try {
            setStatus("Checking Java3D install...");
            Class<?> canvas3D = Class.forName("javax.media.j3d.Canvas3D");
            is3DEnabled = true;
         } catch (Throwable t) {
            t.printStackTrace();
            is3DEnabled = false;
            Object[] message = new Object[]{"Java3D does not appear to be installed", "3D View functionality may be unavailable"};
            JOptionPane.showOptionDialog(this.contentPanel, message, "Warning - Java3D does not appear to be installed", -1, 0, null, null, 0);
         }
      }

      frame = new JFrame("Minecraft Structure Planner");
      frame.setJMenuBar(this.createMenuBar());
      frame.setDefaultCloseOperation(0);
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(this.contentPanel, "Center");
      frame.getContentPane().add(statusBar, "South");
      frame.setSize(1024, 768);

      try {
         URL iconResource = StructurePlanner.class.getResource("/minecraft/planner/resources/icon.png");
         Image img = Toolkit.getDefaultToolkit().createImage(iconResource);
         frame.setIconImage(img);
      } catch (Throwable t) {
         System.err.println("Failed to set application icon due to " + t);
      }

      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Dimension screensize = toolkit.getScreenSize();
      frame.setLocation((screensize.width - frame.getWidth()) / 2, (screensize.height - frame.getHeight()) / 2);
      frame.setVisible(true);
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            List<JCloseableTab> tabs = new ArrayList<>();

            for (int i = 0; i < StructurePlanner.this.contentPanel.getTabCount(); i++) {
               tabs.add((JCloseableTab)StructurePlanner.this.contentPanel.getTabComponentAt(i));
            }

            for (JCloseableTab tab : tabs) {
               if (tab.close() == 2) {
                  break;
               }
            }

            if (StructurePlanner.this.contentPanel.getTabCount() == 0) {
               StructurePlanner.getFrame().dispose();
               System.exit(0);
            }
         }
      });
      setStatus("Welcome to Minecraft Structure Planner Version 0.99.7");
      VersionInfo info = VersionUtilities.getVersionInfo();
      if (info != null && info.getLatestVersion() != null && !info.getLatestVersion().equalsIgnoreCase("0.99.7")) {
         new VersionSplash(info);
      }
   }

   public void validateMenuItems() {
      if (this.contentPanel.getSelectedIndex() > -1) {
         StructureProviderPanel component = (StructureProviderPanel)this.contentPanel.getSelectedComponent();
         StructureProvider<?> provider = component.getProvider();
         StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
         this.printItem.setEnabled(Printable.class.isAssignableFrom(displayPanel.getClass()));
      } else {
         this.printItem.setEnabled(false);
      }

      this.saveAsMenu.setEnabled(this.contentPanel.getSelectedIndex() > -1);
      this.exportMenu.setEnabled(this.contentPanel.getSelectedIndex() > -1);
      if (this.contentPanel.getSelectedIndex() > -1) {
         StructureProviderPanel panel = (StructureProviderPanel)this.contentPanel.getSelectedComponent();
         if (panel != null) {
            StructureModel<?> model = panel.getProvider().getModel();
            ModelSaveInformation saveInfo = model.getModelSaveInfo();
            this.saveMenu.setEnabled(saveInfo.isChanged());
         }
      } else {
         this.saveMenu.setEnabled(false);
      }

      this.copyItem.setEnabled(false);
      this.pasteItem.setEnabled(false);
      this.undoItem.setEnabled(false);
      StructureProviderPanel panel = (StructureProviderPanel)this.contentPanel.getSelectedComponent();
      if (panel != null) {
         StructureProvider<?> provider = panel.getProvider();
         if (provider != null) {
            boolean copyPasteEnabled = provider.getModel().getStructureName().equals("Freeform");
            this.copyItem.setEnabled(copyPasteEnabled);
            this.pasteItem.setEnabled(copyPasteEnabled);
            StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
            if (displayPanel != null) {
               UndoBuffer buffer = displayPanel.getUndoBuffer();
               this.undoItem.setEnabled(buffer != null && !buffer.isEmpty());
            }
         }
      }
   }

   public static void main(String[] argv) {
      getInstance();
   }

   private JMenuBar createMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(this.createFileMenu());
      menuBar.add(this.createEditMenu());
      menuBar.add(this.createStructureMenu());
      menuBar.add(this.createHelpMenu());
      return menuBar;
   }

   private StructureModel<? extends StructureParameters> getSelectedModel() {
      StructureProviderPanel panel = (StructureProviderPanel)this.contentPanel.getSelectedComponent();
      return (StructureModel<? extends StructureParameters>)(panel != null ? panel.getProvider().getModel() : null);
   }

   private JMenu createFileMenu() {
      JMenu fileMenu = new JMenu("File");
      this.saveMenu = new JMenuItem("Save...");
      this.saveMenu.setEnabled(false);
      this.saveMenu.setAccelerator(KeyStroke.getKeyStroke("control S"));
      this.saveAsMenu = new JMenuItem("Save As...");
      this.saveAsMenu.setEnabled(false);
      fileMenu.add(this.saveMenu);
      fileMenu.add(this.saveAsMenu);
      this.structurePlannerFilter = new StructurePlanner.StructurePlannerFileFilter("xsp", "Minecraft Structure Planner File");
      this.schematicFilter = new StructurePlanner.StructurePlannerFileFilter("schematic", "MCEdit Schematic File");
      this.binvoxFilter = new StructurePlanner.StructurePlannerFileFilter("binvox", "Binvox Schematic File");
      this.ruinsFilter = new StructurePlanner.StructurePlannerFileFilter("tml", "Ruins Schematic File");
      this.imageFilter = new StructurePlanner.StructurePlannerFileFilter("png", "Plan Image File");
      this.saveMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            StructurePlanner.this.saveModel(panel);
         }
      });
      this.saveAsMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            try {
               StructureModel<? extends StructureParameters> model = StructurePlanner.this.getSelectedModel();
               ModelSaveInformation saveInfo = model.getModelSaveInfo();
               JFileChooser fileChooser = new JFileChooser(UserConfigurationManager.getCurrentFilesystemPath());
               fileChooser.setFileSelectionMode(0);
               fileChooser.setDialogTitle("Save " + model.getStructureName());
               fileChooser.setDialogType(1);
               fileChooser.setMultiSelectionEnabled(false);
               fileChooser.setFileFilter(StructurePlanner.this.structurePlannerFilter);
               int buttonPressed = fileChooser.showSaveDialog(StructurePlanner.this.contentPanel);
               if (buttonPressed == 0) {
                  File selectedFile = fileChooser.getSelectedFile();
                  if (selectedFile != null) {
                     UserConfigurationManager.setCurrentFilesystemPath(selectedFile.getParentFile());
                     if (!selectedFile.getName().toLowerCase().endsWith(".xsp")) {
                        selectedFile = new File(selectedFile.getPath() + "." + "xsp");
                     }

                     saveInfo.setFile(selectedFile);
                     StructurePlanner.setStatus("Saving structure to " + saveInfo.getFile().getAbsolutePath() + "...");
                     model.saveAsXML(saveInfo.getFile());
                     StructurePlanner.setStatus("Structure saved");
                     saveInfo.clearChanged();
                     int index = StructurePlanner.this.contentPanel.getSelectedIndex();
                     if (index > -1) {
                        JCloseableTab tab = (JCloseableTab)StructurePlanner.this.contentPanel.getTabComponentAt(index);
                        tab.updateTitle(model.getStructureName() + " - " + saveInfo.toString());
                     }
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
      JMenuItem loadMenu = new JMenuItem("Load...");
      loadMenu.setEnabled(true);
      fileMenu.add(loadMenu);
      loadMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            try {
               JFileChooser fileChooser = new JFileChooser(UserConfigurationManager.getCurrentFilesystemPath());
               fileChooser.setFileSelectionMode(0);
               fileChooser.setDialogTitle("Load Freeform Plan");
               fileChooser.setDialogType(0);
               fileChooser.setMultiSelectionEnabled(false);
               fileChooser.setFileFilter(StructurePlanner.this.structurePlannerFilter);
               int buttonPressed = fileChooser.showOpenDialog(StructurePlanner.this.contentPanel);
               if (buttonPressed == 0) {
                  File selectedFile = fileChooser.getSelectedFile();
                  if (selectedFile != null) {
                     UserConfigurationManager.setCurrentFilesystemPath(selectedFile.getParentFile());
                     if (!selectedFile.getName().toLowerCase().endsWith(".xsp")) {
                        selectedFile = new File(selectedFile.getPath() + "." + "xsp");
                     }

                     StructurePlanner.setStatus("Loading structure from " + selectedFile.getAbsolutePath() + "...");
                     StructureProvider<FreeformParameters> provider = new FreeformProvider(selectedFile);
                     StructureModel<FreeformParameters> model = provider.getModel();
                     StructureProviderPanel providerPanel = new StructureProviderPanel(provider);
                     StructurePlanner.this.contentPanel.addTab(model.getStructureName(), providerPanel);
                     int tabIndex = StructurePlanner.this.contentPanel.indexOfComponent(providerPanel);
                     StructurePlanner.this.contentPanel.setTabComponentAt(tabIndex, new JCloseableTab(StructurePlanner.this.contentPanel, providerPanel));
                     StructurePlanner.this.contentPanel.setSelectedIndex(tabIndex);
                     FreeformControlPanel controlPanel = (FreeformControlPanel)provider.getControlPanel();
                     controlPanel.setParameters(model.getParameters());
                     StructurePlanner.this.contentPanel.revalidate();
                     StructurePlanner.this.contentPanel.repaint();
                     StructurePlanner.this.validateMenuItems();
                     ModelSaveInformation saveInfo = model.getModelSaveInfo();
                     saveInfo.clearChanged();
                     saveInfo.setFile(selectedFile);
                     model.notifyListenersOfChange(this);
                     StructurePlanner.setStatus("Structure loaded");
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
      fileMenu.addSeparator();
      this.exportMenu = new JMenuItem("Export Schematic...");
      this.exportMenu.setEnabled(false);
      fileMenu.add(this.exportMenu);
      this.exportMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            if (panel != null) {
               try {
                  StructureModel<?> model = panel.getProvider().getModel();
                  StructurePlanner.StructurePlannerFileChooser fileChooser = StructurePlanner.this.new StructurePlannerFileChooser();
                  fileChooser.setFileSelectionMode(0);
                  fileChooser.setDialogTitle("Export " + model.getStructureName() + " Schematic");
                  fileChooser.setDialogType(1);
                  fileChooser.setMultiSelectionEnabled(false);
                  fileChooser.addChoosableFileFilter(StructurePlanner.this.imageFilter);
                  fileChooser.addChoosableFileFilter(StructurePlanner.this.schematicFilter);
                  int buttonPressed = fileChooser.showSaveDialog(StructurePlanner.this.contentPanel);
                  if (buttonPressed == 0) {
                     File selectedFile = fileChooser.getSelectedFile();
                     if (selectedFile != null) {
                        UserConfigurationManager.setCurrentFilesystemPath(selectedFile.getParentFile());
                        FileFilter selectedFilter = fileChooser.getFileFilter();
                        if (selectedFilter == StructurePlanner.this.imageFilter) {
                           if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                              selectedFile = new File(selectedFile.getPath() + "." + "png");
                           }

                           StructurePlanner.setStatus("Exporting schematic image to " + selectedFile.getAbsolutePath() + "...");
                           model.saveAsImage(selectedFile, fileChooser.getCellSize());
                        } else {
                           if (!selectedFile.getName().toLowerCase().endsWith(".schematic")) {
                              selectedFile = new File(selectedFile.getPath() + "." + "schematic");
                           }

                           StructurePlanner.setStatus("Exporting schematic to " + selectedFile.getAbsolutePath() + "...");
                           model.saveAsSchematic(selectedFile);
                        }

                        StructurePlanner.setStatus("Schematic exported");
                     }
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      });
      this.importMenu = new JMenuItem("Import Schematic...");
      fileMenu.add(this.importMenu);
      this.importMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            try {
               StructurePlanner.StructurePlannerFileChooser fileChooser = StructurePlanner.this.new StructurePlannerFileChooser();
               fileChooser.setFileSelectionMode(0);
               fileChooser.setDialogTitle("Import Schematic");
               fileChooser.setDialogType(0);
               fileChooser.setMultiSelectionEnabled(false);
               fileChooser.addChoosableFileFilter(StructurePlanner.this.binvoxFilter);
               fileChooser.addChoosableFileFilter(StructurePlanner.this.ruinsFilter);
               fileChooser.addChoosableFileFilter(StructurePlanner.this.schematicFilter);
               int buttonPressed = fileChooser.showOpenDialog(StructurePlanner.this.contentPanel);
               if (buttonPressed == 0) {
                  StructurePlanner.StructurePlannerFileFilter filter = (StructurePlanner.StructurePlannerFileFilter)fileChooser.getFileFilter();
                  File selectedFile = fileChooser.getSelectedFile();
                  if (selectedFile != null) {
                     if (!selectedFile.getName().toLowerCase().endsWith("." + filter.getSuffix())) {
                        selectedFile = new File(selectedFile.getPath() + "." + filter.getSuffix());
                     }

                     StructurePlanner.setStatus("Importing structure from " + selectedFile.getAbsolutePath() + "...");
                     if (filter == StructurePlanner.this.schematicFilter) {
                        Tag importedTag = Tag.readFrom(new FileInputStream(selectedFile));
                        StructureProvider<FreeformParameters> provider = new FreeformProvider(importedTag);
                        StructureModel<FreeformParameters> model = provider.getModel();
                        StructureProviderPanel providerPanel = new StructureProviderPanel(provider);
                        StructurePlanner.this.contentPanel.addTab(model.getStructureName(), providerPanel);
                        int tabIndex = StructurePlanner.this.contentPanel.indexOfComponent(providerPanel);
                        StructurePlanner.this.contentPanel.setTabComponentAt(tabIndex, new JCloseableTab(StructurePlanner.this.contentPanel, providerPanel));
                        StructurePlanner.this.contentPanel.setSelectedIndex(tabIndex);
                        FreeformControlPanel controlPanel = (FreeformControlPanel)provider.getControlPanel();
                        controlPanel.setParameters(model.getParameters());
                        StructurePlanner.this.contentPanel.revalidate();
                        StructurePlanner.this.contentPanel.repaint();
                        StructurePlanner.this.validateMenuItems();
                        model.notifyListenersOfChange(this);
                        StructurePlanner.setStatus("Schematic imported");
                     } else if (filter == StructurePlanner.this.binvoxFilter) {
                        Binvox binvox = new Binvox(selectedFile);
                        StructureProvider<FreeformParameters> provider = new FreeformProvider(binvox, fileChooser.getDefaultTexture());
                        StructureModel<FreeformParameters> model = provider.getModel();
                        StructureProviderPanel providerPanel = new StructureProviderPanel(provider);
                        StructurePlanner.this.contentPanel.addTab(model.getStructureName(), providerPanel);
                        int tabIndex = StructurePlanner.this.contentPanel.indexOfComponent(providerPanel);
                        StructurePlanner.this.contentPanel.setTabComponentAt(tabIndex, new JCloseableTab(StructurePlanner.this.contentPanel, providerPanel));
                        StructurePlanner.this.contentPanel.setSelectedIndex(tabIndex);
                        FreeformControlPanel controlPanel = (FreeformControlPanel)provider.getControlPanel();
                        controlPanel.setParameters(model.getParameters());
                        StructurePlanner.this.contentPanel.revalidate();
                        StructurePlanner.this.contentPanel.repaint();
                        StructurePlanner.this.validateMenuItems();
                        model.notifyListenersOfChange(this);
                        StructurePlanner.setStatus("Binvox imported");
                     } else if (filter == StructurePlanner.this.ruinsFilter) {
                        Ruins ruins = new Ruins(selectedFile);
                        StructureProvider<FreeformParameters> provider = new FreeformProvider(ruins);
                        StructureModel<FreeformParameters> model = provider.getModel();
                        StructureProviderPanel providerPanel = new StructureProviderPanel(provider);
                        StructurePlanner.this.contentPanel.addTab(model.getStructureName(), providerPanel);
                        int tabIndex = StructurePlanner.this.contentPanel.indexOfComponent(providerPanel);
                        StructurePlanner.this.contentPanel.setTabComponentAt(tabIndex, new JCloseableTab(StructurePlanner.this.contentPanel, providerPanel));
                        StructurePlanner.this.contentPanel.setSelectedIndex(tabIndex);
                        FreeformControlPanel controlPanel = (FreeformControlPanel)provider.getControlPanel();
                        controlPanel.setParameters(model.getParameters());
                        StructurePlanner.this.contentPanel.revalidate();
                        StructurePlanner.this.contentPanel.repaint();
                        StructurePlanner.this.validateMenuItems();
                        model.notifyListenersOfChange(this);
                        StructurePlanner.setStatus("Ruins imported");
                     }
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
      fileMenu.addSeparator();
      this.printItem = new JMenuItem("Print...");
      fileMenu.add(this.printItem);
      this.printItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructureProviderPanel component = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            StructureProvider<?> provider = component.getProvider();
            StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
            StructureModel<?> model = provider.getModel();
            if (Printable.class.isAssignableFrom(displayPanel.getClass())) {
               StructurePlanner.setStatus("Printing " + model.getStructureName() + "...");
               StructurePlanner.this.new PrintThread(model, displayPanel).start();
            }
         }
      });
      fileMenu.addSeparator();
      JMenuItem preferences = new JMenuItem("Preferences...");
      preferences.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JPreferencesDialog dialog = JPreferencesDialog.getInstance();
            dialog.setVisible(true);
         }
      });
      fileMenu.add(preferences);
      fileMenu.addSeparator();
      JMenuItem exitMenu = new JMenuItem("Exit");
      exitMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
         }
      });
      fileMenu.add(exitMenu);
      fileMenu.addMenuListener(new MenuListener() {
         @Override
         public void menuCanceled(MenuEvent arg0) {
         }

         @Override
         public void menuDeselected(MenuEvent arg0) {
         }

         @Override
         public void menuSelected(MenuEvent arg0) {
            StructurePlanner.this.validateMenuItems();
         }
      });
      return fileMenu;
   }

   public boolean saveModel(StructureProviderPanel panel) {
      if (panel != null) {
         StructureModel<?> model = panel.getProvider().getModel();
         ModelSaveInformation saveInfo = model.getModelSaveInfo();

         try {
            if (saveInfo.isChanged()) {
               if (saveInfo.getFile() == null) {
                  JFileChooser fileChooser = new JFileChooser(UserConfigurationManager.getCurrentFilesystemPath());
                  fileChooser.setFileSelectionMode(0);
                  fileChooser.setDialogTitle("Save " + model.getStructureName());
                  fileChooser.setDialogType(1);
                  fileChooser.setMultiSelectionEnabled(false);
                  fileChooser.setFileFilter(this.structurePlannerFilter);
                  int buttonPressed = fileChooser.showSaveDialog(this.contentPanel);
                  if (buttonPressed == 0) {
                     File selectedFile = fileChooser.getSelectedFile();
                     if (selectedFile != null) {
                        UserConfigurationManager.setCurrentFilesystemPath(selectedFile.getParentFile());
                        if (!selectedFile.getName().toLowerCase().endsWith(".xsp")) {
                           selectedFile = new File(selectedFile.getPath() + "." + "xsp");
                        }

                        saveInfo.setFile(selectedFile);
                     }
                  }
               }

               if (saveInfo.getFile() != null) {
                  setStatus("Saving structure to " + saveInfo.getFile().getAbsolutePath() + "...");
                  model.saveAsXML(saveInfo.getFile());
                  setStatus("Structure saved");
                  saveInfo.clearChanged();
                  int index = this.contentPanel.getSelectedIndex();
                  if (index > -1) {
                     JCloseableTab tab = (JCloseableTab)this.contentPanel.getTabComponentAt(index);
                     tab.updateTitle(model.getStructureName() + " - " + saveInfo.toString());
                  }

                  return true;
               }
            }
         } catch (Exception e) {
            new JErrorDialog(getFrame(), "Failed to save file " + saveInfo.getFile().getName(), e).setVisible(true);
         }
      }

      return false;
   }

   private JMenu createEditMenu() {
      JMenu editMenu = new JMenu("Edit");
      this.copyItem = new JMenuItem("Copy");
      this.copyItem.setAccelerator(KeyStroke.getKeyStroke("control C"));
      this.copyItem.setEnabled(false);
      this.pasteItem = new JMenuItem("Paste");
      this.pasteItem.setAccelerator(KeyStroke.getKeyStroke("control V"));
      this.pasteItem.setEnabled(false);
      this.undoItem = new JMenuItem("Undo");
      this.undoItem.setAccelerator(KeyStroke.getKeyStroke("control Z"));
      this.undoItem.setEnabled(false);
      editMenu.addMenuListener(new MenuListener() {
         @Override
         public void menuCanceled(MenuEvent arg0) {
         }

         @Override
         public void menuDeselected(MenuEvent arg0) {
         }

         @Override
         public void menuSelected(MenuEvent arg0) {
            StructurePlanner.this.copyItem.setEnabled(false);
            StructurePlanner.this.pasteItem.setEnabled(false);
            StructurePlanner.this.undoItem.setEnabled(false);
            StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            if (panel != null) {
               StructureProvider<?> provider = panel.getProvider();
               if (provider != null) {
                  boolean copyPasteEnabled = provider.getModel().getStructureName().equals("Freeform");
                  StructurePlanner.this.copyItem.setEnabled(copyPasteEnabled);
                  StructurePlanner.this.pasteItem.setEnabled(copyPasteEnabled);
                  StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
                  if (displayPanel != null) {
                     UndoBuffer buffer = displayPanel.getUndoBuffer();
                     StructurePlanner.this.undoItem.setEnabled(buffer != null && !buffer.isEmpty());
                  }
               }
            }
         }
      });
      this.copyItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            if (panel != null) {
               StructureProvider<?> provider = panel.getProvider();
               if (provider != null) {
                  StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
                  if (displayPanel != null) {
                     FreeformStructureDisplayPanel freeformDisplay = (FreeformStructureDisplayPanel)displayPanel;
                     freeformDisplay.getLayerPanel().copyLayer();
                  }
               }
            }
         }
      });
      this.pasteItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            if (panel != null) {
               StructureProvider<?> provider = panel.getProvider();
               if (provider != null) {
                  StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
                  if (displayPanel != null) {
                     FreeformStructureDisplayPanel freeformDisplay = (FreeformStructureDisplayPanel)displayPanel;
                     freeformDisplay.getLayerPanel().pasteLayer();
                  }
               }
            }
         }
      });
      this.undoItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
            if (panel != null) {
               StructureProvider<?> provider = panel.getProvider();
               if (provider != null) {
                  StructureDisplayPanel<?> displayPanel = provider.getStructurePanel();
                  if (displayPanel != null) {
                     displayPanel.undo();
                  }
               }
            }
         }
      });
      editMenu.add(this.copyItem);
      editMenu.add(this.pasteItem);
      editMenu.add(this.undoItem);
      this.undoItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            int selectedTab = StructurePlanner.this.contentPanel.getSelectedIndex();
            if (selectedTab >= 0) {
               StructureProviderPanel panel = (StructureProviderPanel)StructurePlanner.this.contentPanel.getSelectedComponent();
               StructureDisplayPanel<?> displayPanel = panel.getProvider().getStructurePanel();
               displayPanel.undo();
            }
         }
      });
      return editMenu;
   }

   private JMenu createStructureMenu() {
      JMenu structureMenu = new JMenu("Structures");
      structureMenu.add(new JProviderMenuItem(this.contentPanel, CircleProvider.class, "Circle"));
      structureMenu.add(new JProviderMenuItem(this.contentPanel, HemisphereProvider.class, "Hemispherical Dome"));
      structureMenu.add(new JProviderMenuItem(this.contentPanel, SphereProvider.class, "Sphere"));
      structureMenu.add(new JProviderMenuItem(this.contentPanel, DomeProvider.class, "Domed Roof"));
      structureMenu.add(new JProviderMenuItem(this.contentPanel, TorusProvider.class, "Torus"));
      structureMenu.addSeparator();
      JMenu bridgeMenu = new JMenu("Bridges");
      bridgeMenu.add(new JProviderMenuItem(this.contentPanel, BridgeProvider.class, "Arch Bridge"));
      bridgeMenu.add(new JProviderMenuItem(this.contentPanel, SuspensionBridgeProvider.class, "Suspension Bridge"));
      bridgeMenu.add(new JProviderMenuItem(this.contentPanel, ViaductProvider.class, "Viaduct"));
      structureMenu.add(bridgeMenu);
      JMenu mazeMenu = new JMenu("Mazes");
      mazeMenu.add(new JProviderMenuItem(this.contentPanel, DepthFirstMazeProvider.class, "Depth First Maze"));
      mazeMenu.add(new JProviderMenuItem(this.contentPanel, RecursiveBacktrackerMazeProvider.class, "Recursive Backtracker Maze"));
      mazeMenu.add(new JProviderMenuItem(this.contentPanel, PrimMazeProvider.class, "Prim Maze"));
      JMenu growingTreeMenu = new JMenu("Growing Tree Mazes");
      growingTreeMenu.add(new JProviderMenuItem(this.contentPanel, RandomGrowingTreeMazeProvider.class, "Random Growing Tree Maze"));
      growingTreeMenu.add(new JProviderMenuItem(this.contentPanel, NewestGrowingTreeMazeProvider.class, "Newest Growing Tree Maze"));
      growingTreeMenu.add(new JProviderMenuItem(this.contentPanel, OldestGrowingTreeMazeProvider.class, "Oldest Growing Tree Maze"));
      mazeMenu.add(growingTreeMenu);
      structureMenu.add(mazeMenu);
      JMenu fractalsMenu = new JMenu("Fractals");
      fractalsMenu.add(new JProviderMenuItem(this.contentPanel, MengerProvider.class, "Menger Sponge"));
      structureMenu.add(fractalsMenu);
      JMenu artifactMenu = new JMenu("Miscellaneous");
      artifactMenu.add(new JProviderMenuItem(this.contentPanel, EasterIslandProvider.class, "Easter Island"));
      structureMenu.add(artifactMenu);
      structureMenu.addSeparator();
      structureMenu.add(new JProviderMenuItem(this.contentPanel, FreeformProvider.class, "Freeform"));
      structureMenu.add(new JProviderMenuItem(this.contentPanel, BitmapProvider.class, "Pixel Art"));
      return structureMenu;
   }

   private JMenu createHelpMenu() {
      JMenu helpMenu = new JMenu("Help");
      JMenuItem aboutMenu = new JMenuItem("About...");
      helpMenu.add(aboutMenu);
      aboutMenu.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            StructurePlanner.about.setVisible(true);
         }
      });
      return helpMenu;
   }

   private StructureProviderPanel getStructureProviderTab(StructureProvider<?> provider) {
      return new StructureProviderPanel(provider);
   }

   public static boolean is3DEnabled() {
      return is3DEnabled;
   }

   public static JFrame getFrame() {
      return frame;
   }

   public static String formatSeconds(long milliseconds) {
      DecimalFormat formatter = new DecimalFormat("0.00");
      return formatter.format(milliseconds / 1000.0);
   }

   public static void setStatus(String status) {
      statusBar.updateStatus(status);
   }

   public static void setPercent(double percent) {
      statusBar.setPercent(percent);
   }

   public static void setStatusMode(JStatusBar$Mode mode) {
      statusBar.setMode(mode);
   }

   private class FileChooserAccessoryManager extends JPanel {
      private Map<String, JPanel> extensionPanels = new HashMap<>();
      private StructurePlanner.TextureCombo textureCombo;
      private JSpinner cellSizeSpinner;
      private final CardLayout layout;
      private JLabel iconLabel = new JLabel();

      public FileChooserAccessoryManager() {
         this.layout = new CardLayout();
         this.setLayout(this.layout);
         this.textureCombo = StructurePlanner.this.new TextureCombo(TextureName.Stone);
         JPanel textureChooser = new JPanel();
         textureChooser.setLayout(new BorderLayout());
         textureChooser.add(this.textureCombo, "North");
         textureChooser.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Default Texture"));
         textureChooser.setVisible(false);
         String binvoxFilterDescription = StructurePlanner.this.binvoxFilter.getDescription();
         this.extensionPanels.put(binvoxFilterDescription, textureChooser);
         this.add(textureChooser, binvoxFilterDescription);
         SpinnerNumberModel numberModel = new SpinnerNumberModel(20, 8, 64, 1);
         this.cellSizeSpinner = new JSpinner(numberModel);
         JPanel imageChooser = new JPanel();
         JButton previewButton = new JButton("Preview");
         previewButton.addActionListener(new 1(this));
         imageChooser.setLayout(new GridBagLayout());
         imageChooser.add(new JLabel("Cell Size:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 10), 0, 0));
         imageChooser.add(this.cellSizeSpinner, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
         imageChooser.add(this.iconLabel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 10, 0, new Insets(10, 0, 0, 0), 0, 0));
         imageChooser.add(previewButton, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, 10, 0, new Insets(10, 0, 0, 0), 0, 0));
         imageChooser.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Image Cell Size"));
         imageChooser.setVisible(false);
         String imageChooserDescription = StructurePlanner.this.imageFilter.getDescription();
         this.extensionPanels.put(imageChooserDescription, imageChooser);
         this.add(imageChooser, imageChooserDescription);
         this.cellSizeSpinner.addChangeListener(new 2(this));
         this.setScaledIcon();
      }

      private void setScaledIcon() {
         ImageIcon image = TextureName.Dirt.getPlanViewImageIcon(StructurePlanner.this.texturePack);
         int cellSize = this.getCellSize();
         BufferedImage destination = new BufferedImage(64, 64, 1);
         Graphics2D g2 = destination.createGraphics();
         int origin = (64 - cellSize) / 2;
         g2.drawImage(image.getImage(), origin, origin, cellSize, cellSize, null);
         g2.dispose();
         this.iconLabel.setIcon(new ImageIcon(destination));
      }

      public void selectAccessory(StructurePlanner.StructurePlannerFileFilter filter) {
         if (filter != null) {
            this.layout.show(this, filter.getDescription());
         }
      }

      public boolean hasAccessory(StructurePlanner.StructurePlannerFileFilter filter) {
         return this.extensionPanels.containsKey(filter.getDescription());
      }

      public TextureName getTexture() {
         return (TextureName)this.textureCombo.getSelectedItem();
      }

      public int getCellSize() {
         return (Integer)this.cellSizeSpinner.getValue();
      }
   }

   private class PrintThread extends Thread {
      private StructureModel<?> model;
      private StructureDisplayPanel<?> displayPanel;

      public PrintThread(StructureModel<?> model, StructureDisplayPanel<?> displayPanel) {
         this.setName("Structure Print " + this.getName());
         this.setDaemon(true);
         this.model = model;
         this.displayPanel = displayPanel;
      }

      @Override
      public void run() {
         PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
         if (this.model.getWorldGrid().getHeight() > this.model.getWorldGrid().getWidth()) {
            attributes.add(OrientationRequested.PORTRAIT);
         } else {
            attributes.add(OrientationRequested.LANDSCAPE);
         }

         PrinterJob pj = PrinterJob.getPrinterJob();
         PrintService[] services = PrinterJob.lookupPrintServices();
         if (services.length > 0) {
            try {
               pj.setPrintService(services[0]);
               pj.setJobName("Printing " + this.model.getStructureName() + " layer plan");
               pj.pageDialog(attributes);
               pj.setPrintable((Printable)this.displayPanel);
               if (pj.printDialog(attributes)) {
                  pj.print(attributes);
                  StructurePlanner.setStatus(this.model.getStructureName() + " printing completed");
               }
            } catch (Exception e) {
               new JErrorDialog(StructurePlanner.getFrame(), "Failed to print plans for " + this.model.getStructureName(), e).setVisible(true);
               StructurePlanner.setStatus("");
            }
         }
      }
   }

   private class StructurePlannerFileChooser extends JFileChooser implements PropertyChangeListener {
      private StructurePlanner.FileChooserAccessoryManager fileChooserAccessoryManager = StructurePlanner.this.new FileChooserAccessoryManager();

      public StructurePlannerFileChooser() {
         super(UserConfigurationManager.getCurrentFilesystemPath());
         this.setAccessory(this.fileChooserAccessoryManager);
         this.addPropertyChangeListener(this);
      }

      @Override
      public void propertyChange(PropertyChangeEvent e) {
         String propertyName = e.getPropertyName();
         if (propertyName.equalsIgnoreCase("fileFilterChanged")) {
            FileFilter filter = this.getFileFilter();
            if (filter instanceof StructurePlanner.StructurePlannerFileFilter
               && this.fileChooserAccessoryManager.hasAccessory((StructurePlanner.StructurePlannerFileFilter)filter)) {
               this.fileChooserAccessoryManager.setVisible(true);
               this.setAccessory(this.fileChooserAccessoryManager);
               this.fileChooserAccessoryManager.selectAccessory((StructurePlanner.StructurePlannerFileFilter)filter);
               this.validate();
            } else {
               this.fileChooserAccessoryManager.setVisible(false);
               this.validate();
            }
         }
      }

      public TextureName getDefaultTexture() {
         return this.fileChooserAccessoryManager.getTexture();
      }

      public int getCellSize() {
         return this.fileChooserAccessoryManager.getCellSize();
      }
   }

   private class StructurePlannerFileFilter extends FileFilter {
      private String suffix;
      private String description;

      public StructurePlannerFileFilter(String suffix, String description) {
         this.suffix = suffix.trim();
         this.description = description.trim();
      }

      public String getSuffix() {
         return this.suffix;
      }

      @Override
      public boolean accept(File f) {
         return f.isDirectory() || f.isFile() && f.getName().toLowerCase().endsWith(this.suffix);
      }

      @Override
      public String getDescription() {
         return this.description;
      }
   }

   private class TextureCombo extends JComboBox {
      public TextureCombo(TextureName defaultTexture) {
         this.setModel(new DefaultComboBoxModel<>(TextureName.texturableValues()));
         this.setSelectedItem(defaultTexture);
      }
   }
}
