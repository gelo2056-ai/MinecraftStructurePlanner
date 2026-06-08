package minecraft.planner.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import minecraft.planner.gui.JErrorDialog;
import minecraft.planner.gui.StructurePlanner;
import minecraft.planner.gui.textures.DefaultTexturePack;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.gui.textures.TexturePack;

public class UserConfigurationManager {
   private static File configurationDirectory = null;
   private static final Map<String, TextureSet> textureSets = new HashMap<>();
   public static final TexturePack DEFAULT_TEXTURES = new DefaultTexturePack();
   private static Set<TexturePack> availableTexturePacks = new HashSet<>();
   public static TexturePack selectedTexturePack = DEFAULT_TEXTURES;
   private static File currentFilesystemPath = null;

   private static final synchronized File getUserConfigurationDirectory() {
      if (configurationDirectory == null) {
         String homeDirectory = System.getProperty("user.home");
         String configDirectory = homeDirectory + File.separator + ".MinecraftStructurePlanner";
         configurationDirectory = new File(configDirectory);
      }

      return copyConfigFile(configurationDirectory, "texture_sets.cfg") ? configurationDirectory : null;
   }

   private static final boolean copyConfigFile(File directory, String configResource) {
      try {
         if (!directory.exists() && !directory.mkdir()) {
            return false;
         }

         InputStream resourceStream = UserConfigurationManager.class.getResourceAsStream("/minecraft/planner/resources/" + configResource);
         File configFile = new File(directory.getAbsolutePath() + File.separator + configResource);
         if (!configFile.exists()) {
            FileOutputStream outputStream = null;

            try {
               outputStream = new FileOutputStream(configFile);
               byte[] b = new byte[10000];
               int len = 0;

               while ((len = resourceStream.read(b)) >= 0) {
                  outputStream.write(b, 0, len);
               }

               return true;
            } catch (Throwable e) {
               new JErrorDialog(StructurePlanner.getFrame(), "Failed to create configuration files", e).setVisible(true);
            } finally {
               try {
                  outputStream.flush();
               } catch (Exception var19) {
               }

               try {
                  outputStream.close();
               } catch (Exception var18) {
               }
            }

            return false;
         } else {
            return true;
         }
      } catch (Throwable e) {
         new JErrorDialog(StructurePlanner.getFrame(), "Failed to create default configuration", e);
         return false;
      }
   }

   public static void initializeConfiguration() {
      checkForTextureDirectory();
      loadTextureSets();
      loadTexturePacks();
   }

   public static List<TextureSet> getTextureSets() {
      return new ArrayList<>(textureSets.values());
   }

   private static void loadTextureSets() {
      File configDirectory = getUserConfigurationDirectory();
      BufferedReader br = null;

      try {
         File textureSetFile = new File(configDirectory.getAbsolutePath() + File.separator + "texture_sets.cfg");
         br = new BufferedReader(new FileReader(textureSetFile));
         String textures = null;

         do {
            textures = br.readLine();
            if (textures != null && textures.length() > 0) {
               String[] textureFields = textures.split(",");
               boolean isEditable = false;
               String setName = null;
               List<TextureName> textureCollection = new ArrayList<>();
               if (textureFields.length >= 3) {
                  if (textureFields[0].equals("1")) {
                     isEditable = true;
                  }

                  setName = textureFields[1];
                  if (textureFields[2].equals("*")) {
                     TextureName[] var11;
                     int texture = (var11 = TextureName.values()).length;

                     for (int textureName = 0; textureName < texture; textureName++) {
                        TextureName texturex = var11[textureName];
                        if (texturex.isTexturable()) {
                           textureCollection.add(texturex);
                        }
                     }
                  } else {
                     for (int index = 2; index < textureFields.length; index++) {
                        String textureName = textureFields[index];
                        TextureName texture = TextureName.valueOf(TextureName.class, textureName);
                        if (texture != null) {
                           textureCollection.add(texture);
                        }
                     }
                  }

                  textureSets.put(textureFields[1], new TextureSet(textureFields[1], textureCollection, isEditable));
               }
            }
         } while (textures != null && textures.length() > 0);
      } catch (Throwable t) {
         new JErrorDialog(null, "Failed to load texture sets", t).setVisible(true);
      } finally {
         try {
            br.close();
         } catch (Throwable var19) {
         }
      }
   }

   public static void saveTextureSets() {
      File configDirectory = getUserConfigurationDirectory();
      BufferedWriter bw = null;

      try {
         File textureSetFile = new File(configDirectory.getAbsolutePath() + File.separator + "texture_sets.cfg");
         bw = new BufferedWriter(new FileWriter(textureSetFile));

         for (String textureSetName : textureSets.keySet()) {
            TextureSet textureSet = textureSets.get(textureSetName);
            if (textureSet.getName().equalsIgnoreCase("All")) {
               bw.write((textureSet.isEditable() ? "1" : "0") + "," + textureSet.getName() + ",*\n");
            } else {
               bw.write((textureSet.isEditable() ? "1" : "0") + "," + textureSet.getName());

               for (TextureName texture : textureSet.getTextures()) {
                  bw.write("," + texture.name());
               }

               bw.write("\n");
            }
         }
      } catch (Throwable t) {
         new JErrorDialog(StructurePlanner.getFrame(), "Failed to load texture sets", t).setVisible(true);
      } finally {
         try {
            bw.flush();
         } catch (Throwable var19) {
         }

         try {
            bw.close();
         } catch (Throwable var18) {
         }
      }
   }

   public static void addTexturesToCache(TextureSet textures) {
      textureSets.put(textures.getName(), textures);
   }

   public static TextureSet getTexturesFromCache(String textureSetName) {
      return textureSets.get(textureSetName);
   }

   public static void removeTexturesFromCache(TextureSet textures) {
      textureSets.remove(textures.getName());
   }

   public static synchronized void loadTexturePacks() {
      File texturePackDirectory = new File(getUserConfigurationDirectory() + File.separator + "TexturePacks");
      if (texturePackDirectory.exists()) {
         File texturePackConfig = new File(getUserConfigurationDirectory() + File.separator + "texture_packs.cfg");
         String chosenTexturePack = null;
         if (texturePackConfig.exists()) {
            try {
               BufferedReader reader = new BufferedReader(new FileReader(texturePackConfig));
               chosenTexturePack = reader.readLine();
               reader.close();
            } catch (Exception e) {
               chosenTexturePack = null;
            }
         }

         String[] texturePacks = texturePackDirectory.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
               return name.toLowerCase().endsWith("tpk");
            }
         });
         String[] var7 = texturePacks;
         int var6 = texturePacks.length;

         for (int var5 = 0; var5 < var6; var5++) {
            String texturePackFile = var7[var5];
            TexturePack texturePack = loadTexturePack(new File(texturePackDirectory, texturePackFile));
            if (texturePack != null) {
               availableTexturePacks.add(texturePack);
               if (chosenTexturePack != null && texturePack.getName().equals(chosenTexturePack)) {
                  selectedTexturePack = texturePack;
               }
            }
         }
      }
   }

   public static synchronized Collection<TexturePack> getTexturePacksFromCache() {
      return availableTexturePacks;
   }

   public static synchronized void addTexturePackToCache(TexturePack texturePack) {
      availableTexturePacks.add(texturePack);
      saveTexturePack(texturePack);
   }

   private static void saveTexturePack(TexturePack pack) {
      String fileName = pack.getFileName();
      if (fileName != null) {
         File texturePackFile = new File(getUserConfigurationDirectory() + File.separator + fileName);
         ObjectOutputStream os = null;

         try {
            os = new ObjectOutputStream(new FileOutputStream(texturePackFile));
            os.writeObject(pack);
         } catch (Exception e) {
            new JErrorDialog(StructurePlanner.getFrame(), "Failed to save Texture Pack", e).setVisible(true);
         } finally {
            if (os != null) {
               try {
                  os.flush();
               } catch (Exception var16) {
               }
            }

            if (os != null) {
               try {
                  os.close();
               } catch (Exception var15) {
               }
            }
         }
      }
   }

   private static void deleteTexturePack(TexturePack pack) {
      String fileName = pack.getFileName();
      if (fileName != null) {
         File texturePackFile = new File(getUserConfigurationDirectory() + File.separator + fileName);
         if (texturePackFile.exists()) {
            try {
               texturePackFile.delete();
            } catch (Exception var4) {
            }
         }
      }
   }

   private static TexturePack loadTexturePack(File texturePackFile) {
      ObjectInputStream is = null;

      try {
         is = new ObjectInputStream(new FileInputStream(texturePackFile));
         return (TexturePack)is.readObject();
      } catch (Exception e) {
         new JErrorDialog(StructurePlanner.getFrame(), "Failed to load texture pack " + texturePackFile.getName(), e);
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (Exception var11) {
            }
         }
      }

      return null;
   }

   public static synchronized void removeTexturePackFromCache(TexturePack texturePack) {
      availableTexturePacks.remove(texturePack);
      deleteTexturePack(texturePack);
   }

   public static synchronized void setSelectedTexturePack(TexturePack texturePack) {
      selectedTexturePack = texturePack;
      File texturePackConfig = new File(getUserConfigurationDirectory() + File.separator + "texture_packs.cfg");
      FileWriter writer = null;

      try {
         writer = new FileWriter(texturePackConfig);
         writer.write(texturePack.getName());
      } catch (Exception var16) {
      } finally {
         if (writer != null) {
            try {
               writer.flush();
            } catch (Exception var15) {
            }
         }

         if (writer != null) {
            try {
               writer.close();
            } catch (Exception var14) {
            }
         }
      }
   }

   public static synchronized TexturePack getSelectedTexturePack() {
      return selectedTexturePack;
   }

   public static synchronized boolean checkForTextureDirectory() {
      File directory = new File(getUserConfigurationDirectory() + File.separator + "TexturePacks");
      return directory.exists() || directory.mkdir();
   }

   public static synchronized void setCurrentFilesystemPath(File currentDirectory) {
      currentFilesystemPath = currentDirectory;
   }

   public static synchronized File getCurrentFilesystemPath() {
      return currentFilesystemPath;
   }
}
