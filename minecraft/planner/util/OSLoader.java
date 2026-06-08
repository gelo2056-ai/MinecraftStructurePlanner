package minecraft.planner.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.version.TitlePanel;

public class OSLoader {
   private static final String RESOURCE_PACKAGE = "/minecraft/planner/resources/java3d";
   private static final TitlePanel splash = new TitlePanel("title1.jpg", "Minecraft Structure Planner v0.99.7", "(c) Neil Vickers, 2010 - 2011");
   private static final JLabel status = new JLabel(" ");
   private static final JFrame splashScreen = new JFrame();
   private static final JPanel splashScreenPanel = new JPanel();

   static {
      createSplashScreen();
   }

   public static final OS determineOS() {
      String name = System.getProperty("os.name").toLowerCase();
      Architecture arch = determineArchitecture();
      if (name.contains("win")) {
         if (arch == Architecture.x32) {
            return OS.Windows32;
         }

         if (arch == Architecture.x64) {
            return OS.Windows64;
         }
      } else {
         if (name.contains("mac")) {
            return OS.MacOSX;
         }

         if (name.contains("nux")) {
            if (arch == Architecture.x32) {
               return OS.Linux32;
            }

            if (arch == Architecture.x64) {
               return OS.Linux64;
            }
         }
      }

      return OS.Unknown;
   }

   private static final Architecture determineArchitecture() {
      String architecture = System.getProperty("sun.arch.data.model");
      if (architecture.contains("32")) {
         return Architecture.x32;
      } else {
         return architecture.contains("64") ? Architecture.x64 : Architecture.Unknown;
      }
   }

   private static final void createSplashScreen() {
      splashScreenPanel.setBorder(BorderFactory.createBevelBorder(0));
      splashScreenPanel.setLayout(new BorderLayout());
      splashScreenPanel.add(splash, "Center");
      splashScreenPanel.add(status, "South");
      splashScreen.getContentPane().add(splashScreenPanel);
      Dimension size = new Dimension(splash.getPreferredSize().width, splash.getPreferredSize().height + status.getHeight());
      splashScreen.setPreferredSize(size);
      splashScreen.setMinimumSize(size);
      splashScreen.setMaximumSize(size);
      splashScreen.setUndecorated(true);

      try {
         URL iconResource = StructurePlanner.class.getResource("/minecraft/planner/resources/icon.png");
         Image img = Toolkit.getDefaultToolkit().createImage(iconResource);
         splashScreen.setIconImage(img);
      } catch (Throwable t) {
         System.err.println("Failed to set application icon due to " + t);
      }
   }

   private static final void showSplashScreen() {
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Dimension screenSize = toolkit.getScreenSize();
      splashScreen.setLocation((screenSize.width - splashScreen.getWidth()) / 2, (screenSize.height - splashScreen.getHeight()) / 2);
      splashScreen.setVisible(true);
   }

   private static final void hideSplashScreen() {
      splashScreen.setVisible(false);
   }

   private static final void setStatus(String message) {
      status.setText(message);
   }

   private static final void pause(int seconds) {
      try {
         Thread.sleep(seconds * 1000);
      } catch (Exception var2) {
      }
   }

   public static final boolean loadLibraries(OS os) {
      showSplashScreen();
      setStatus("Loading Java3D 1.5.1 for " + os.getDescription() + "...");
      pause(2);
      switch (os) {
         case MacOSX:
            loadMacOSX();
            break;
         case Windows32:
            loadWindows(Architecture.x32);
            break;
         case Windows64:
            loadWindows(Architecture.x64);
            break;
         case Linux32:
            loadLinux(Architecture.x32);
            break;
         case Linux64:
            loadLinux(Architecture.x64);
            break;
         default:
            return false;
      }

      setStatus("Loading user configuration...");
      UserConfigurationManager.initializeConfiguration();
      pause(1);
      setStatus("Load complete.");
      pause(1);
      hideSplashScreen();
      return true;
   }

   private static final boolean loadWindows(Architecture arch) {
      String resourceBase = "/minecraft/planner/resources/java3d/windows/";
      String resourceArch = resourceBase + arch.name() + "/";

      try {
         switch (arch) {
            case x32:
               setStatus("Loading Windows 32-bit libraries...");
               pause(2);
               loadLibrary(resourceArch, "j3dcore-ogl.dll");
               loadLibrary(resourceArch, "j3dcore-ogl-cg.dll");
               loadLibrary(resourceArch, "j3dcore-ogl-chk.dll");
               break;
            case x64:
               setStatus("Loading Windows 64-bit libraries...");
               pause(2);
               loadLibrary(resourceArch, "j3dcore-ogl.dll");
         }

         return true;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private static final boolean loadMacOSX() {
      System.out.println("Loading Mac OSX libraries...");
      pause(2);
      return true;
   }

   private static final boolean loadLinux(Architecture arch) {
      try {
         String resourceBase = "/minecraft/planner/resources/java3d/linux/";
         String resourceArch = resourceBase + arch.name() + "/";
         switch (arch) {
            case x32:
               setStatus("Loading Linux 32-bit libraries...");
               pause(2);
               loadLibrary(resourceArch, "libj3dcore-ogl-cg.so");
               loadLibrary(resourceArch, "libj3dcore-ogl.so");
               break;
            case x64:
               setStatus("Loading Linux 64-bit libraries...");
               pause(2);
               loadLibrary(resourceArch, "libj3dcore-ogl.so");
         }

         return true;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private static final boolean loadLibrary(String resourceArch, String library) {
      try {
         String tempDir = System.getProperty("java.io.tmpdir");
         InputStream resourceStream = OSLoader.class.getResourceAsStream(resourceArch + library);
         File tempLibrary = new File(tempDir + "/" + library);
         if (!tempLibrary.exists()) {
            FileOutputStream outputStream = null;

            try {
               outputStream = new FileOutputStream(tempLibrary);
               byte[] b = new byte[10000];
               int len = 0;

               while ((len = resourceStream.read(b)) >= 0) {
                  outputStream.write(b, 0, len);
               }
            } catch (Exception e) {
               e.printStackTrace();
            } finally {
               try {
                  outputStream.flush();
               } catch (Exception var20) {
               }

               try {
                  outputStream.close();
               } catch (Exception var19) {
               }
            }
         }

         String javaLibraryPath = System.getProperty("java.library.path");
         if (!javaLibraryPath.contains(tempLibrary.getParent())) {
            setStatus("Extending Java library path to include Java3D native libraries...");
            pause(3);
            System.setProperty("java.library.path", tempLibrary.getParent() + File.pathSeparatorChar + System.getProperty("java.library.path"));
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
         }

         return true;
      } catch (Exception e) {
         setStatus("Failed to load library due to " + e);
         pause(5);
         return false;
      }
   }
}
