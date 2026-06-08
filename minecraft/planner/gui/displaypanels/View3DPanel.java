package minecraft.planner.gui.displaypanels;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Link;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Switch;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import minecraft.planner.gui.JErrorDialog;
import minecraft.planner.gui.JStatusBar$Mode;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.displaypanels.View3DPanel.InitializationThread.1;
import minecraft.planner.gui.java3d.TextureCache;
import minecraft.planner.gui.java3d.modelfactory.ModelFactory;
import minecraft.planner.gui.java3d.modelfactory.ModelParameters;
import minecraft.planner.gui.java3d.modelfactory.impl.BlockModel;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.StructureModelChangeListener;
import minecraft.planner.model.StructureParameters;
import minecraft.planner.model.grid.WorldGrid;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.util.UserConfigurationManager;

public class View3DPanel<P extends StructureParameters> extends JPanel implements StructureModelChangeListener, TextureChangeListener {
   private static final int MAX_DISPOSING_UNIVERSES = 2;
   private static final LinkedBlockingQueue<VirtualUniverse> universeDisposalQueue = new LinkedBlockingQueue<>();
   private static final View3DPanel.UniverseDisposalThread disposalThread = new View3DPanel.UniverseDisposalThread(universeDisposalQueue);
   protected boolean needsUpdate;
   protected boolean isVisible = false;
   protected StructureModel<P> model;
   private Component contents;
   private TexturePack texturePack = UserConfigurationManager.getSelectedTexturePack();
   private VirtualUniverse universe = null;
   private Locale locale = null;
   private BranchGroup sceneGroup = null;
   private BranchGroup viewGroup = null;
   private ViewingPlatform viewingPlatform;
   private TransformGroup viewTransformGroup;
   private BranchGroup displayBranchGroup;
   private TransformGroup displayTransformGroup;
   private Canvas3D canvas = null;
   private View view = null;
   private BranchGroup modelGroup = null;
   private TextureName textureName = TextureName.None;
   private double yRot = Math.PI / 4;
   private double xRot = Math.PI / 8;
   private double xTranslate = 0.0;
   private double yTranslate = 0.0;
   private double scaleFactor = 25.0;
   private boolean active;
   private final ModelFactory modelFactory = new ModelFactory();
   private GraphicsConfiguration graphicsConfiguration;
   private final BlockingQueue<Object> structureModelChangeQueue = new LinkedBlockingQueue<>();
   private View3DPanel<P>.StructureModelChangeProcessor structureModelChangeProcessor = null;
   private int mouseX = 0;
   private int mouseY = 0;
   private JSlider layerSlider = new JSlider(1);
   private Set<BlockModel> blockModelSet = new HashSet<>();
   private ArrayList<Switch> switchArray = new ArrayList<>();
   private Box floor = null;
   private View3DPanel<P>.TexturingThread texturingThread = null;
   private TextureName previousTexture;

   static {
      disposalThread.start();
   }

   private GraphicsConfiguration getCanvasConfiguration() {
      GraphicsConfiguration currentConfiguration = StructurePlanner.getFrame().getGraphicsConfiguration();
      GraphicsDevice currentDevice = currentConfiguration.getDevice();
      GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
      template.setSceneAntialiasing(2);
      this.graphicsConfiguration = currentDevice.getBestConfiguration(template);
      return this.graphicsConfiguration;
   }

   public View3DPanel(StructureModel<P> model) {
      this(model, true, true);
   }

   public View3DPanel(StructureModel<P> model, boolean includeTexturePanel) {
      this(model, includeTexturePanel, true);
   }

   public View3DPanel(StructureModel<P> model, boolean includeTexturePanel, boolean registerForModelChanges) {
      this.active = true;
      this.model = model;
      this.setLayout(new BorderLayout());
      this.contents = null;
      this.needsUpdate = true;
      if (this.active && registerForModelChanges) {
         this.model.registerListener(this);
      }

      new View3DPanel.InitializationThread(model, includeTexturePanel).start();
   }

   private void initialize3DView(StructureModel<P> model, boolean includeTexturePanel, boolean hadToWait) {
      StructurePlanner.setStatus("Initializing " + model.getStructureName() + " 3D view...");

      try {
         try {
            this.canvas = new Canvas3D(this.getCanvasConfiguration());
         } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            System.err.println("java.library.path:  " + System.getProperty("java.library.path"));
         }

         this.viewGroup = new BranchGroup();
         this.viewTransformGroup = new TransformGroup();
         this.viewTransformGroup.setCapability(17);
         this.viewTransformGroup.setCapability(18);
         ViewPlatform viewPlatform = new ViewPlatform();
         viewPlatform.setActivationRadius(10000.0F);
         this.viewGroup.addChild(this.viewTransformGroup);
         this.viewingPlatform = new ViewingPlatform();
         this.viewingPlatform.setViewPlatform(viewPlatform);
         this.viewGroup.addChild(this.viewingPlatform);
         this.view = new View();
         this.view.addCanvas3D(this.canvas);
         this.view.attachViewPlatform(viewPlatform);
         this.view.setBackClipDistance(10000.0);
         this.view.setSceneAntialiasingEnable(true);
         this.displayBranchGroup = new BranchGroup();
         this.displayTransformGroup = new TransformGroup();
         this.displayTransformGroup.setCapability(18);
         this.displayTransformGroup.setCapability(13);
         this.displayTransformGroup.setCapability(14);
         this.displayBranchGroup.addChild(this.displayTransformGroup);
         TransformGroup whiteLight = this.createSphereLight(-100.0F, 100.0F, 100.0F, new Color3f(1.0F, 1.0F, 1.0F));
         TransformGroup redLight = this.createSphereLight(-25.0F, 25.0F, -25.0F, new Color3f(0.5F, 0.5F, 0.5F));
         TransformGroup greenLight = this.createSphereLight(25.0F, 25.0F, -25.0F, new Color3f(0.5F, 0.5F, 0.5F));
         TransformGroup blueLight = this.createSphereLight(0.0F, 25.0F, 25.0F, new Color3f(0.5F, 0.5F, 0.5F));
         this.displayTransformGroup.addChild(whiteLight);
         this.displayTransformGroup.addChild(redLight);
         this.displayTransformGroup.addChild(greenLight);
         this.displayTransformGroup.addChild(blueLight);
         AmbientLight light = new AmbientLight(new Color3f(0.05F, 0.05F, 0.05F));
         light.setEnable(true);
         light.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0));
         this.viewGroup.addChild(light);
         this.viewGroup.addChild(this.generateSky());
         this.universe = new VirtualUniverse();
         this.locale = new Locale(this.universe);
         PhysicalBody body = new PhysicalBody();
         body.setNominalEyeHeightFromGround(1.6);
         body.setNominalEyeOffsetFromNominalScreen(0.25);
         this.view.setPhysicalBody(body);
         PhysicalEnvironment environment = new PhysicalEnvironment();
         this.view.setPhysicalEnvironment(environment);
         this.locale.addBranchGraph(this.viewGroup);
         this.locale.addBranchGraph(this.displayBranchGroup);
         if (this.contents != null) {
            this.remove(this.contents);
         }

         JPanel canvasPanel = new JPanel();
         canvasPanel.setLayout(new BorderLayout());
         canvasPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Projection"));
         canvasPanel.add(this.canvas, "Center");
         JPanel layerSliderPanel = new JPanel();
         layerSliderPanel.setLayout(new GridBagLayout());
         layerSliderPanel.add(this.layerSlider, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, 11, 3, new Insets(0, 5, 0, 5), 0, 0));
         canvasPanel.add(layerSliderPanel, "East");
         this.layerSlider.setMinimum(1);
         this.layerSlider.setMaximum(1);
         this.layerSlider.setValue(1);
         this.layerSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               for (int index = 0; index < View3DPanel.this.switchArray.size(); index++) {
                  Switch node = View3DPanel.this.switchArray.get(index);
                  int layer = index + 1;
                  if (View3DPanel.this.layerSlider.isEnabled() && layer > View3DPanel.this.layerSlider.getValue()) {
                     node.setWhichChild(-1);
                  } else {
                     node.setWhichChild(-2);
                  }
               }
            }
         });
         this.contents = this.canvas;
         if (includeTexturePanel) {
            TexturePanel texturePanel = new TexturePanel(true);
            Dimension size = new Dimension(180, 0);
            texturePanel.setMinimumSize(size);
            texturePanel.setPreferredSize(size);
            this.add(texturePanel, "West");
            texturePanel.addTextureChangeListener(this);
         }

         this.add(canvasPanel, "Center");
         this.canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
               View3DPanel.this.mouseX = e.getX();
               View3DPanel.this.mouseY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
         });
         this.canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
               int scrollAmount = e.getWheelRotation();
               if (scrollAmount < 0) {
                  View3DPanel.this.scaleIn(Math.abs(scrollAmount));
               } else {
                  View3DPanel.this.scaleOut(scrollAmount);
               }
            }
         });
         this.canvas.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
               int newX = e.getX();
               int newY = e.getY();
               int xDiff = (newX - View3DPanel.this.mouseX) / 8;
               int yDiff = (newY - View3DPanel.this.mouseY) / 8;
               if (xDiff != 0) {
                  if (xDiff < 0) {
                     View3DPanel.this.rotateRight(Math.abs(xDiff));
                  } else {
                     View3DPanel.this.rotateLeft(Math.abs(xDiff));
                  }

                  View3DPanel.this.mouseX = newX;
               }

               if (yDiff != 0) {
                  if (yDiff < 0) {
                     View3DPanel.this.rotateDown(Math.abs(yDiff));
                  } else {
                     View3DPanel.this.rotateUp(Math.abs(yDiff));
                  }

                  View3DPanel.this.mouseY = newY;
               }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
         });
         this.canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == 37) {
                  if (e.isAltDown()) {
                     View3DPanel.this.translateLeft();
                  } else if (e.isShiftDown()) {
                     View3DPanel.this.scaleOut(1);
                  } else {
                     View3DPanel.this.rotateLeft(1.0);
                  }
               } else if (e.getKeyCode() == 39) {
                  if (e.isAltDown()) {
                     View3DPanel.this.translateRight();
                  } else if (e.isShiftDown()) {
                     View3DPanel.this.scaleIn(1.0);
                  } else {
                     View3DPanel.this.rotateRight(1.0);
                  }
               } else if (e.getKeyCode() == 38) {
                  if (e.isAltDown()) {
                     View3DPanel.this.translateUp();
                  } else if (e.isShiftDown()) {
                     View3DPanel.this.scaleIn(1.0);
                  } else {
                     View3DPanel.this.rotateUp(1.0);
                  }
               } else if (e.getKeyCode() == 40) {
                  if (e.isAltDown()) {
                     View3DPanel.this.translateDown();
                  } else if (e.isShiftDown()) {
                     View3DPanel.this.scaleOut(1);
                  } else {
                     View3DPanel.this.rotateDown(1.0);
                  }
               }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
         });
      } catch (Exception e) {
         new JErrorDialog(StructurePlanner.getFrame(), "Failed to create 3D view panel for " + model.getStructureName(), e);
      }

      if (hadToWait) {
         this.structureModelChanged(this);
      }

      this.structureModelChangeProcessor = new View3DPanel.StructureModelChangeProcessor();
      this.structureModelChangeProcessor.start();
      this.needsUpdate = true;
      this.isVisible = false;
      this.addComponentListener(new ComponentListener() {
         @Override
         public void componentHidden(ComponentEvent arg0) {
            View3DPanel.this.isVisible = false;
         }

         @Override
         public void componentMoved(ComponentEvent arg0) {
         }

         @Override
         public void componentResized(ComponentEvent arg0) {
         }

         @Override
         public void componentShown(ComponentEvent arg0) {
            View3DPanel.this.isVisible = true;
            View3DPanel.this.request3DModelRevalidation(this);
         }
      });
      this.scaleViewPlatform(this.scaleFactor);
      StructurePlanner.setStatus(model.getStructureName() + " 3D view ready.");
   }

   private void scaleOut(int steps) {
      this.scaleFactor += steps;
      this.scaleViewPlatform(this.scaleFactor);
   }

   private void scaleIn(double steps) {
      this.scaleFactor -= steps;
      if (this.scaleFactor < 0.0) {
         this.scaleFactor = 0.0;
      }

      this.scaleViewPlatform(this.scaleFactor);
   }

   private void scaleViewPlatform(double scaleFactor) {
      Transform3D vpTransform = new Transform3D();
      TransformGroup viewPlatformTransformGroup = this.viewingPlatform.getViewPlatformTransform();
      viewPlatformTransformGroup.getTransform(vpTransform);
      Matrix4d transformMatrix = new Matrix4d();
      vpTransform.get(transformMatrix);
      transformMatrix.setElement(2, 3, this.scaleFactor);
      vpTransform.set(transformMatrix);
      viewPlatformTransformGroup.setTransform(vpTransform);
   }

   private void translateLeft() {
      double translation = this.xTranslate;
      translation -= 0.1;
      this.positionDisplay(this.xRot, this.yRot, translation, this.yTranslate, this.scaleFactor);
      this.xTranslate = translation;
   }

   private void translateRight() {
      double translation = this.xTranslate;
      translation += 0.1;
      this.positionDisplay(this.xRot, this.yRot, translation, this.yTranslate, this.scaleFactor);
      this.xTranslate = translation;
   }

   private void translateUp() {
      double translation = this.yTranslate;
      translation -= 0.1;
      this.positionDisplay(this.xRot, this.yRot, this.xTranslate, translation, this.scaleFactor);
      this.yTranslate = translation;
   }

   private void translateDown() {
      double translation = this.yTranslate;
      translation += 0.1;
      this.positionDisplay(this.xRot, this.yRot, this.xTranslate, translation, this.scaleFactor);
      this.yTranslate = translation;
   }

   private void rotateLeft(double steps) {
      new Transform3D();
      double rotation = this.yRot;
      rotation += 0.08726646259971647;
      if (rotation > Math.PI * 2) {
         rotation = 0.0;
      }

      this.positionDisplay(this.xRot, rotation, 0.0, this.yTranslate, this.scaleFactor);
      this.yRot = rotation;
   }

   private void rotateRight(double steps) {
      double rotation = this.yRot;
      rotation -= 0.08726646259971647;
      if (rotation > Math.PI * 2) {
         rotation = 0.0;
      }

      this.positionDisplay(this.xRot, rotation, 0.0, this.yTranslate, this.scaleFactor);
      this.yRot = rotation;
   }

   private void rotateUp(double steps) {
      double rotation = this.xRot;
      rotation += 0.08726646259971647;
      if (rotation > Math.PI / 2) {
         rotation = Math.PI / 2;
      }

      this.positionDisplay(rotation, this.yRot, this.xTranslate, 0.0, this.scaleFactor);
      this.xRot = rotation;
   }

   private void rotateDown(double steps) {
      double rotation = this.xRot;
      rotation -= 0.08726646259971647;
      if (rotation < 0.0) {
         rotation = 0.0;
      }

      this.positionDisplay(rotation, this.yRot, this.xTranslate, 0.0, this.scaleFactor);
      this.xRot = rotation;
   }

   private Background generateSky() {
      URL skyURL = View3DPanel.class.getClass().getResource("/minecraft/planner/resources/sky.jpg");
      TextureLoader skyTextureLoader = new TextureLoader(skyURL, "RGBA", new Container());
      ImageComponent2D skyTexture = skyTextureLoader.getImage();
      Background background = new Background(skyTexture);
      background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 500.0));
      background.setImageScaleMode(3);
      BranchGroup bgGeometry = new BranchGroup();
      Appearance app = new Appearance();
      Texture tex = skyTextureLoader.getTexture();
      app.setTexture(tex);
      Box box = new Box(2.0F, 2.0F, 2.0F, 6, app);
      Transform3D rotate = new Transform3D();
      rotate.rotX(Math.PI / 4);
      TransformGroup ObjRotate = new TransformGroup(rotate);
      bgGeometry.addChild(ObjRotate);
      ObjRotate.addChild(box);
      background.setGeometry(bgGeometry);
      return background;
   }

   private TransformGroup createSphereLight(float x, float y, float z, Color3f color) {
      TransformGroup lightGroup = new TransformGroup();
      Transform3D translation = new Transform3D();
      translation.setTranslation(new Vector3f(x, y, z));
      Transform3D direction = new Transform3D(translation);
      direction.invert();
      direction.normalize();
      Vector3f directionVector = new Vector3f();
      direction.get(directionVector);
      lightGroup.setTransform(translation);
      DirectionalLight light = new DirectionalLight(color, directionVector);
      BoundingSphere lightBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
      light.setInfluencingBounds(lightBounds);
      light.setEnable(true);
      lightGroup.addChild(light);
      return lightGroup;
   }

   private void positionDisplay(double rotX, double rotY, double tranX, double tranY, double scaleFactor) {
      if (this.displayTransformGroup != null) {
         Transform3D rotateX = new Transform3D();
         Transform3D rotateY = new Transform3D();
         Transform3D translate = new Transform3D();
         Transform3D transform = new Transform3D();
         rotateX.rotX(rotX);
         rotateY.rotY(rotY);
         translate.setTranslation(new Vector3d(tranX, 0.0, tranY));
         transform.mul(rotateX);
         transform.mul(rotateY);
         transform.mul(translate);
         this.displayTransformGroup.setTransform(transform);
      }
   }

   @Override
   public void structureModelChanged(Object originator) {
      if (this.active && this.structureModelChangeQueue.isEmpty()) {
         this.needsUpdate = true;
         this.layerSlider.setMinimum(1);
         int maximum = this.model.getWorldGrid().getMaxHeight();
         if (maximum < 1) {
            maximum = 1;
         }

         this.layerSlider.setMaximum(maximum);
         this.layerSlider.setValue(maximum);
         this.layerSlider.setEnabled(maximum > 1);

         try {
            this.structureModelChangeQueue.put(originator);
         } catch (Exception var4) {
         }
      }
   }

   private void request3DModelRevalidation(Object originator) {
      try {
         this.structureModelChangeQueue.put(originator);
      } catch (Exception var3) {
      }
   }

   private void revalidate3DModel() {
      if (this.model != null && this.model.getWorldGrid() != null) {
         if (this.isVisible && this.needsUpdate) {
            try {
               WorldGrid grid = this.model.getWorldGrid();
               this.blockModelSet.clear();
               float zOffset = grid.getMaxHeight() / 2.0F;
               BranchGroup existingSceneGroup = this.sceneGroup;
               this.sceneGroup = new BranchGroup();
               this.sceneGroup.setCapability(17);
               this.modelGroup = new BranchGroup();
               this.modelGroup.setCapability(17);
               int primitiveFlags = 66;
               StructurePlanner.setStatusMode(JStatusBar$Mode.Percent);
               double percentage = 0.0;
               double maxHeight = grid.getMaxHeight();
               StructurePlanner.setPercent(0.0);
               Vector3f translateVector = new Vector3f();
               Transform3D transform = new Transform3D();
               ModelParameters p = new ModelParameters();
               this.switchArray.clear();

               for (int layer = 1; layer <= grid.getMaxHeight(); layer++) {
                  if (!grid.layerIsEmpty(layer)) {
                     TransformGroup layerGroup = new TransformGroup();

                     for (int x = 0; x < grid.getWidth(); x++) {
                        for (int y = 0; y < grid.getHeight(); y++) {
                           WorldGrid$Cell cell = grid.getCell(x, y);
                           if (cell != null) {
                              WorldGrid$Cell.ZValue zValue = cell.getStack().getZValue(layer);
                              if (zValue != null) {
                                 TextureName texture = zValue.getTexture();
                                 if (texture != null) {
                                    TransformGroup objectGroup = new TransformGroup();
                                    p.setTexture(texture);
                                    p.setOrientation(zValue.getOrientation());
                                    BlockModel blockModel = this.modelFactory.getModel(p);
                                    this.blockModelSet.add(blockModel);
                                    translateVector.set(x, p.getY() < 0.5 ? -p.getY() : 0.0F, -(grid.getHeight() - y) + 1);
                                    transform.setTranslation(translateVector);
                                    objectGroup.addChild(new Link(blockModel));
                                    objectGroup.setTransform(transform);
                                    layerGroup.addChild(objectGroup);
                                 }
                              }
                           }
                        }
                     }

                     translateVector.set(-(grid.getWidth() / 2.0F) + 0.5F, layer - grid.getMaxHeight() / 2.0F, grid.getHeight() / 2.0F - 0.5F);
                     transform.setTranslation(translateVector);
                     layerGroup.setTransform(transform);
                     Switch switchNode = new Switch();
                     switchNode.setCapability(18);
                     switchNode.addChild(layerGroup);
                     switchNode.setWhichChild(-2);
                     this.switchArray.add(switchNode);
                     this.sceneGroup.addChild(switchNode);
                  }

                  percentage = layer / maxHeight;
                  StructurePlanner.setPercent(percentage);
               }

               StructurePlanner.setStatusMode(JStatusBar$Mode.Text);
               Appearance grassAppearance = TextureCache.getTexture(this.texturePack, TextureName.Ground);
               float maxSize = grid.getWidth() > grid.getHeight() ? grid.getWidth() : grid.getHeight();
               float floorSize = maxSize * 1.5F;
               float floorOffset = floorSize / 2.0F;
               this.floor = new Box(floorOffset, 1.0E-4F, floorOffset, primitiveFlags, grassAppearance);
               TransformGroup floorStackGroup = new TransformGroup();
               Transform3D floorTransform = new Transform3D();
               Vector3f floorTranslateVector = new Vector3f(0.0F, -(zOffset - 0.5F), 0.0F);
               floorTransform.setTranslation(floorTranslateVector);
               floorStackGroup.setTransform(floorTransform);
               floorStackGroup.addChild(this.floor);
               this.modelGroup.addChild(floorStackGroup);
               this.sceneGroup.addChild(this.modelGroup);
               if (existingSceneGroup != null) {
                  this.displayTransformGroup.removeChild(existingSceneGroup);
                  new View3DPanel.DisposalThread(existingSceneGroup).start();
               }

               if (this.displayTransformGroup != null && this.sceneGroup != null) {
                  this.displayTransformGroup.addChild(this.sceneGroup);
               }

               this.positionDisplay(this.xRot, this.yRot, this.xTranslate, this.yTranslate, this.scaleFactor);
            } catch (Exception e) {
               new JErrorDialog(StructurePlanner.getFrame(), "Failed to process 3D display structure model change for " + this.model.getStructureName(), e);
            }

            this.needsUpdate = false;
         }
      }
   }

   @Override
   public void textureChanged(TextureName name) {
      this.previousTexture = this.textureName;
      this.textureName = name;
      this.model.setDefaultTexture(name);
      if (this.texturingThread != null && this.texturingThread.isAlive()) {
         this.texturingThread.cancel();

         try {
            this.texturingThread.join();
         } catch (Exception var3) {
         }
      }

      this.texturingThread = new View3DPanel.TexturingThread(this.previousTexture, this.textureName);
      this.texturingThread.start();
   }

   public void dispose() {
      this.active = false;
      new View3DPanel.DisposalThread().start();
   }

   public void forceUpdate() {
      this.needsUpdate = true;
   }

   private class DisposalThread extends Thread {
      private BranchGroup branchGroup;

      public DisposalThread() {
         this(null);
      }

      public DisposalThread(BranchGroup branchGroup) {
         this.branchGroup = branchGroup;
         this.setName("View3D Disposal" + this.getName());
      }

      @Override
      public void run() {
         StructurePlanner.setStatus("Starting 3D model disposal...");
         long startTime = System.currentTimeMillis();
         if (this.branchGroup != null) {
            StructurePlanner.setStatus("Disposing of model branch group...");
            this.branchGroup.removeAllChildren();
            this.branchGroup = null;
            long endTime = System.currentTimeMillis();
            StructurePlanner.setStatus(
               "Disposing of "
                  + View3DPanel.this.model.getStructureName()
                  + " branch group completed in "
                  + StructurePlanner.formatSeconds(endTime - startTime)
                  + " seconds"
            );
         } else {
            StructurePlanner.setStatus("Stopping 3D renderer...");
            if (View3DPanel.this.canvas != null) {
               View3DPanel.this.canvas.stopRenderer();
               if (View3DPanel.this.canvas.isOffScreen()) {
                  System.out.println(this.getName() + ":  Setting Canvas3D off-screen buffer to NULL");
                  View3DPanel.this.canvas.setOffScreenBuffer(null);
               }
            }

            if (View3DPanel.this.view != null) {
               StructurePlanner.setStatus("Removing 3D objects from view...");
               View3DPanel.this.view.removeAllCanvas3Ds();
               StructurePlanner.setStatus("Detaching view platform...");
               View3DPanel.this.view.attachViewPlatform(null);
            }

            if (View3DPanel.this.displayBranchGroup != null) {
               StructurePlanner.setStatus("Disposing of display branch group...");
               View3DPanel.this.displayBranchGroup = null;
            }

            if (View3DPanel.this.viewingPlatform != null) {
               StructurePlanner.setStatus("Disposing of viewing platform...");
               View3DPanel.this.viewingPlatform = null;
            }

            if (View3DPanel.this.viewTransformGroup != null) {
               StructurePlanner.setStatus("Disposing of view transform group...");
               View3DPanel.this.viewTransformGroup.removeAllChildren();
               View3DPanel.this.viewTransformGroup = null;
            }

            if (View3DPanel.this.displayTransformGroup != null) {
               StructurePlanner.setStatus("Disposing of display transform group...");
               View3DPanel.this.displayTransformGroup = null;
            }

            if (View3DPanel.this.sceneGroup != null) {
               StructurePlanner.setStatus("Disposing of scene group...");
               View3DPanel.this.sceneGroup = null;
            }

            if (View3DPanel.this.viewGroup != null) {
               StructurePlanner.setStatus("Disposing of view group...");
               View3DPanel.this.viewGroup = null;
            }

            View3DPanel.universeDisposalQueue.add(View3DPanel.this.universe);
            View3DPanel.this.universe = null;
            View3DPanel.this.locale = null;
            View3DPanel.this.canvas = null;
            View3DPanel.this.view = null;
            long endTime = System.currentTimeMillis();
            StructurePlanner.setStatus(
               "Disposal of " + View3DPanel.this.model.getStructureName() + " complete in " + StructurePlanner.formatSeconds(endTime - startTime) + " seconds"
            );
         }
      }
   }

   private class InitializationThread extends Thread {
      private JPanel waitPanel;
      private StructureModel<P> model;
      private boolean includeTexturePanel;

      public InitializationThread(StructureModel<P> model, boolean includeTexturePanel) {
         this.model = model;
         this.includeTexturePanel = includeTexturePanel;
         this.setName("View3D Initialization " + this.getName());
         this.setDaemon(true);
         this.setPriority(1);
      }

      @Override
      public void run() {
         boolean hadToWait = false;
         if (View3DPanel.universeDisposalQueue.size() >= 2) {
            hadToWait = true;
            this.waitPanel = new JPanel();
            this.waitPanel.setLayout(new GridBagLayout());
            JLabel waitLabel1 = new JLabel("Waiting for previous 3D displays to finish disposing.");
            JLabel waitLabel2 = new JLabel("3D view will appear shortly.");
            JLabel waitLabel3 = new JLabel();
            this.waitPanel.add(waitLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
            this.waitPanel.add(waitLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
            this.waitPanel.add(waitLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
            SwingUtilities.invokeLater(new 1(this));
            View3DPanel.this.contents = this.waitPanel;

            int queueSize;
            while (View3DPanel.this.active && (queueSize = View3DPanel.universeDisposalQueue.size()) >= 2) {
               try {
                  waitLabel3.setText("(Waiting on " + queueSize + " universe disposal" + (queueSize != 1 ? "s" : "") + ")");
                  Thread.sleep(5000L);
                  Thread.yield();
               } catch (Exception var7) {
               }
            }
         }

         this.waitPanel = null;
         if (View3DPanel.this.active) {
            View3DPanel.this.initialize3DView(this.model, this.includeTexturePanel, hadToWait);
         }
      }
   }

   private class StructureModelChangeProcessor extends Thread {
      public StructureModelChangeProcessor() {
         this.setName("Structure Model Change Processor " + this.getName());
         this.setDaemon(true);
      }

      @Override
      public void run() {
         while (true) {
            try {
               Object originator = View3DPanel.this.structureModelChangeQueue.take();
               View3DPanel.this.revalidate3DModel();
            } catch (Exception var2) {
            }
         }
      }
   }

   private class TexturingThread extends Thread {
      private TextureName previousTexture;
      private TextureName texture;
      private boolean canceled = false;

      public TexturingThread(TextureName previousTexture, TextureName newTexture) {
         this.setName("Texturing " + this.getName());
         this.previousTexture = previousTexture;
         this.texture = newTexture;
      }

      @Override
      public void run() {
         StructurePlanner.setStatus("Re-texturing " + View3DPanel.this.model.getStructureName() + " to " + this.texture + "...");
         long startTime = System.currentTimeMillis();

         try {
            if (View3DPanel.this.modelGroup == null) {
               View3DPanel.this.structureModelChanged(this);
            } else if (this.previousTexture.getModelFactoryClass() != View3DPanel.this.textureName.getModelFactoryClass()) {
               WorldGrid grid = View3DPanel.this.model.getWorldGrid();

               for (int x = 0; x < grid.getWidth(); x++) {
                  for (int y = 0; y < grid.getHeight(); y++) {
                     WorldGrid$Cell cell = grid.getCell(x, y);
                     WorldGrid$Cell.ZStack stack = cell.getStack();

                     for (WorldGrid$Cell.ZValue value : stack.getZValues()) {
                        value.setTexture(this.texture);
                     }
                  }
               }

               View3DPanel.this.needsUpdate = true;
               if (View3DPanel.this.isVisible) {
                  View3DPanel.this.request3DModelRevalidation(this);
               }
            } else {
               for (BlockModel model : View3DPanel.this.blockModelSet) {
                  model.retexture(this.texture);
               }
            }
         } catch (Exception e) {
            new JErrorDialog(StructurePlanner.getFrame(), "Failed to process 3D display texture change for " + View3DPanel.this.model.getStructureName(), e);
         }

         if (!this.canceled) {
            long endTime = System.currentTimeMillis();
            StructurePlanner.setStatus(
               "Re-texturing of "
                  + View3DPanel.this.model.getStructureName()
                  + " completed in "
                  + StructurePlanner.formatSeconds(endTime - startTime)
                  + " seconds"
            );
         }
      }

      public void cancel() {
         this.canceled = true;
      }
   }

   private static class UniverseDisposalThread extends Thread {
      private BlockingQueue<VirtualUniverse> queue;

      public UniverseDisposalThread(BlockingQueue<VirtualUniverse> disposalQueue) {
         this.setName("Universe Disposal Thread");
         this.setDaemon(true);
         this.queue = disposalQueue;
      }

      @Override
      public void run() {
         while (true) {
            try {
               VirtualUniverse universe = this.queue.take();
               long startTime = System.currentTimeMillis();
               StructurePlanner.setStatus("Disposing of VirtualUniverse...");
               universe.removeAllLocales();
               long endTime = System.currentTimeMillis();
               StructurePlanner.setStatus("VirtualUniverse disposed in " + (endTime - startTime) + "ms");
            } catch (Throwable t) {
               System.err.println("Failed to dispose of VirtualUniverse due to " + t.getMessage());
               t.printStackTrace();
            }
         }
      }
   }
}
