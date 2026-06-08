package minecraft.planner.gui.textures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.vecmath.Color3f;
import minecraft.planner.gui.java3d.Color;
import minecraft.planner.gui.java3d.NBTKey;
import minecraft.planner.gui.java3d.modelfactory.ModelFactory;
import minecraft.planner.gui.java3d.modelfactory.Orientable;
import minecraft.planner.gui.java3d.modelfactory.impl.BlockModelFactory;
import minecraft.planner.gui.java3d.modelfactory.impl.PostModelFactory;
import minecraft.planner.gui.java3d.modelfactory.impl.SlabModelFactory;
import minecraft.planner.gui.java3d.modelfactory.impl.StairModelFactory;

public enum TextureName {
   None("None", "none.png", BlockModelFactory.class),
   Dirt("Dirt", 2, 0, 3, 0, 0.0F, BlockModelFactory.class),
   Gravel("Gravel", 3, 1, 13, 0, 0.0F, BlockModelFactory.class),
   Clay("Clay", 8, 4, 82, 0, 0.0F, BlockModelFactory.class),
   Grass("Grass", 3, 0, 2, 0, 0.0F, BlockModelFactory.class),
   Cobblestone("Cobblestone", 0, 1, 4, 0, 0.0F, BlockModelFactory.class),
   CobblestoneHalfSlab("Cobblestone Half Slab", 0, 1, 44, 3, 0.0F, SlabModelFactory.class),
   GreenCobblestone("Green Cobblestone", 4, 2, 48, 0, 0.0F, BlockModelFactory.class),
   Sand("Sand", 2, 1, 12, 0, 0.0F, BlockModelFactory.class),
   Sandstone("Sandstone", 0, 12, 24, 0, 0.0F, BlockModelFactory.class),
   SandstoneHalfSlab("Sandstone Half Slab", 0, 12, 44, 1, 0.0F, SlabModelFactory.class),
   Stone("Stone", 1, 0, 1, 0, 0.0F, BlockModelFactory.class),
   StoneDoubleSlab("Stone Double Slab", 5, 0, 43, 0, 0.0F, BlockModelFactory.class),
   StoneHalfSlab("Stone Half Slab", 6, 0, 44, 0, 0.0F, SlabModelFactory.class),
   Plank("Plank", 4, 0, 5, 0, 0.0F, BlockModelFactory.class),
   PlankHalfSlab("Plank Half Slab", 4, 0, 44, 2, 0.0F, SlabModelFactory.class),
   Wood("Wood", 4, 1, 17, 0, 0.0F, BlockModelFactory.class),
   Birch("Birch", 5, 7, 17, 2, 0.0F, BlockModelFactory.class),
   Pine("Pine", 4, 7, 17, 1, 0.0F, BlockModelFactory.class),
   Leaves("Leaves", 4, 3, 18, 0, 0.01F, BlockModelFactory.class),
   LapisLazuli("Lapis Lazuli", 0, 9, 22, 0, 0.0F, BlockModelFactory.class),
   Obsidian("Obsidian", 5, 2, 49, 0, 0.0F, BlockModelFactory.class),
   Brick("Brick", 7, 0, 45, 0, 0.0F, BlockModelFactory.class),
   Bookshelf("Bookshelf", 3, 2, 47, 0, 0.0F, BlockModelFactory.class),
   Netherrack("Netherrack", 7, 6, 87, 0, 0.0F, BlockModelFactory.class),
   Soulsand("Soul Sand", 8, 6, 88, 0, 0.0F, BlockModelFactory.class),
   Glowstone("Glowstone", 9, 6, 89, 0, 0.0F, BlockModelFactory.class),
   Chest("Chest", 11, 1, 54, 0, 0.0F, BlockModelFactory.class),
   Workbench("Workbench", 12, 3, 58, 0, 0.0F, BlockModelFactory.class),
   Furnace("Furnace", 12, 2, 61, 0, 0.0F, BlockModelFactory.class),
   Dispenser("Dispenser", 14, 2, 23, 0, 0.0F, BlockModelFactory.class),
   TNT("TNT", 8, 0, 46, 0, 0.0F, BlockModelFactory.class),
   Fencepost("Fencepost", 4, 0, 85, 0, 0.0F, PostModelFactory.class),
   Cactus("Cactus", 5, 4, 81, 0, 0.0F, BlockModelFactory.class),
   Reeds("Reeds", 9, 4, 83, 0, 0.0F, BlockModelFactory.class),
   Pumpkin("Pumpkin", 6, 7, 91, 0, 0.0F, BlockModelFactory.class),
   Sponge("Sponge", 0, 3, 19, 0, 0.0F, BlockModelFactory.class),
   Steel("Steel", 6, 1, 42, 0, 0.0F, BlockModelFactory.class),
   Gold("Gold", 7, 1, 41, 0, 0.0F, BlockModelFactory.class),
   Diamond("Diamond", 8, 1, 57, 0, 0.0F, BlockModelFactory.class),
   Glass("Glass", 1, 3, 20, 0, 0.001F, BlockModelFactory.class),
   Water("Water", 13, 12, 8, 0, 0.1F, BlockModelFactory.class),
   Ice("Ice", 3, 4, 79, 0, 0.5F, BlockModelFactory.class),
   Snow("Snow", 2, 4, 80, 0, 0.0F, BlockModelFactory.class),
   Lava("Lava", 13, 14, 10, 0, 0.0F, BlockModelFactory.class),
   BlackWool("Black Wool", 1, 7, 35, 15, 0.0F, BlockModelFactory.class),
   DarkGrayWool("Dark Gray Wool", 2, 7, 35, 7, 0.0F, BlockModelFactory.class),
   LightGrayWool("Light Gray Wool", 1, 14, 35, 8, 0.0F, BlockModelFactory.class),
   WhiteWool("White Wool", 0, 4, 35, 0, 0.0F, BlockModelFactory.class),
   RedWool("Red Wool", 1, 8, 35, 14, 0.0F, BlockModelFactory.class),
   PinkWool("Pink Wool", 2, 8, 35, 6, 0.0F, BlockModelFactory.class),
   GreenWool("Green Wool", 1, 9, 35, 13, 0.0F, BlockModelFactory.class),
   LimeWool("Lime Wool", 2, 9, 35, 5, 0.0F, BlockModelFactory.class),
   BrownWool("Brown Wool", 1, 10, 35, 12, 0.0F, BlockModelFactory.class),
   YellowWool("Yellow Wool", 2, 10, 35, 4, 0.0F, BlockModelFactory.class),
   BlueWool("Blue Wool", 1, 11, 35, 11, 0.0F, BlockModelFactory.class),
   LightBlueWool("Light Blue Wool", 2, 11, 35, 3, 0.0F, BlockModelFactory.class),
   PurpleWool("Purple Wool", 1, 12, 35, 10, 0.0F, BlockModelFactory.class),
   MagentaWool("Magenta Wool", 2, 12, 35, 2, 0.0F, BlockModelFactory.class),
   CyanWool("Cyan Wool", 1, 13, 35, 9, 0.0F, BlockModelFactory.class),
   OrangeWool("Orange Wool", 2, 13, 35, 1, 0.0F, BlockModelFactory.class),
   Coal("Coal", 2, 2, 16, 0, 0.0F, BlockModelFactory.class),
   IronOre("Iron Ore", 1, 2, 15, 0, 0.0F, BlockModelFactory.class),
   GoldOre("Gold Ore", 0, 2, 14, 0, 0.0F, BlockModelFactory.class),
   RedstoneOre("Redstone Ore", 3, 3, 73, 0, 0.0F, BlockModelFactory.class),
   DiamondOre("Diamond Ore", 2, 3, 56, 0, 0.0F, BlockModelFactory.class),
   LapisOre("Lapis Ore", 0, 10, 21, 0, 0.0F, BlockModelFactory.class),
   Spawner("Spawner", 1, 4, 52, 0, 0.995F, BlockModelFactory.class),
   Bedrock("Bedrock", 1, 1, 7, 0, 0.0F, BlockModelFactory.class),
   CobbleStair("Cobblestone Stair", 0, 1, 67, 0, 0.0F, StairModelFactory.class),
   WoodStair("Wood Stair", 4, 0, 53, 0, 0.0F, StairModelFactory.class),
   Sky("Sky", "sky.jpg", Color.WHITE),
   Ground("Ground", "ground.png", Color.GREEN),
   ArrowNorth("North", "arrowNorth.png", Color.WHITE),
   ArrowSouth("South", "arrowSouth.png", Color.WHITE),
   ArrowEast("East", "arrowEast.png", Color.WHITE),
   ArrowWest("West", "arrowWest.png", Color.WHITE),
   DiamondPickaxe("Diamond Pickaxe", "diamond-pickaxe.png", Color.WHITE),
   GoldenSword("Golden Sword", "golden-sword.png", Color.WHITE),
   SteelShovel("Steel Shovel", "steel-shovel.png", Color.WHITE);

   private final String displayName;
   private final String resourceName;
   private final int resourceX;
   private final int resourceY;
   private final byte blockValue;
   private final byte dataValue;
   private final Color3f matte;
   private final boolean texturable;
   private final float transparency;
   private final Class<? extends ModelFactory> modelFactoryClass;
   private static Object[] texturableTextures = null;
   private static Collection<TextureName> texturableTexturesCollection = null;
   private static Collection<TextureName> nonTexturableTexturesCollection = null;
   private static final Map<Byte, Set<TextureName>> nbtLookupByBlock = new HashMap<>();
   private static final Map<NBTKey, TextureName> nbtLookupByKey = new HashMap<>();
   private static final Map<String, TextureName> textureMap = new HashMap<>();
   private static boolean textureMapInitialized = false;

   TextureName(
      String displayName, int resourceX, int resourceY, int blockValue, int dataValue, float transparency, Class<? extends ModelFactory> modelFactoryClass
   ) {
      this.displayName = displayName;
      this.blockValue = (byte)blockValue;
      this.dataValue = (byte)dataValue;
      this.resourceX = resourceX;
      this.resourceY = resourceY;
      this.matte = Color.WHITE;
      this.texturable = true;
      this.transparency = transparency;
      this.resourceName = this.name();
      this.modelFactoryClass = modelFactoryClass;
   }

   TextureName(String displayName, String resourceName, Class<? extends ModelFactory> modelFactoryClass) {
      this.displayName = displayName;
      this.blockValue = 0;
      this.dataValue = 0;
      this.resourceX = 0;
      this.resourceY = 0;
      this.matte = Color.WHITE;
      this.texturable = true;
      this.transparency = 0.0F;
      this.resourceName = resourceName;
      this.modelFactoryClass = modelFactoryClass;
   }

   TextureName(String displayName, String resourceName, Color3f matte) {
      this.displayName = displayName;
      this.resourceName = resourceName;
      this.resourceX = 0;
      this.resourceY = 0;
      this.blockValue = 0;
      this.dataValue = 0;
      this.matte = matte;
      this.texturable = false;
      this.transparency = 0.0F;
      this.modelFactoryClass = null;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public byte getBlockValue() {
      return this.blockValue;
   }

   public byte getDataValue() {
      return this.dataValue;
   }

   public int getResourceX() {
      return this.resourceX;
   }

   public int getResourceY() {
      return this.resourceY;
   }

   public String getResourceName() {
      return this.resourceName;
   }

   public Color3f getMatte() {
      return this.matte;
   }

   public boolean isTexturable() {
      return this.texturable;
   }

   public boolean hasOrientation() {
      return this.modelFactoryClass != null && Orientable.class.isAssignableFrom(this.modelFactoryClass);
   }

   public ImageIcon getImageIcon(TexturePack texturePack) {
      return texturePack.getImage(this);
   }

   public ImageIcon getPlanViewImageIcon(TexturePack texturePack) {
      return texturePack.getPlanImage(this);
   }

   public float getTransparency() {
      return this.transparency;
   }

   public Class<? extends ModelFactory> getModelFactoryClass() {
      return this.modelFactoryClass;
   }

   @Override
   public String toString() {
      return this.displayName;
   }

   public static synchronized TextureName getTextureByNBTKey(byte block, byte data) {
      if (!textureMapInitialized) {
         initializeMaps();
      }

      Set<TextureName> textures = nbtLookupByBlock.get(block);
      if (textures != null && textures.size() == 1) {
         return textures.iterator().next();
      }

      NBTKey key = new NBTKey(block, data);
      return nbtLookupByKey.get(key);
   }

   public static synchronized TextureName getTextureByName(String textureName) {
      if (!textureMapInitialized) {
         initializeMaps();
      }

      return textureMap.get(textureName);
   }

   private static synchronized void initializeMaps() {
      TextureName[] var3;
      int var2 = (var3 = values()).length;

      for (int var1 = 0; var1 < var2; var1++) {
         TextureName name = var3[var1];
         textureMap.put(name.getDisplayName(), name);
         if (name.getBlockValue() != 0) {
            if (!nbtLookupByBlock.containsKey(name.getBlockValue())) {
               nbtLookupByBlock.put(name.getBlockValue(), new HashSet<>());
            }

            nbtLookupByBlock.get(name.getBlockValue()).add(name);
            nbtLookupByKey.put(new NBTKey(name.getBlockValue(), name.getDataValue()), name);
         }
      }

      textureMapInitialized = true;
   }

   public static synchronized Object[] texturableValues() {
      if (texturableTextures == null) {
         ArrayList<TextureName> array = new ArrayList<>();
         TextureName[] var4;
         int var3 = (var4 = values()).length;

         for (int var2 = 0; var2 < var3; var2++) {
            TextureName texture = var4[var2];
            if (texture.isTexturable()) {
               array.add(texture);
            }
         }

         texturableTextures = array.toArray();
      }

      return texturableTextures;
   }

   public static synchronized Collection<TextureName> texturableValuesCollection() {
      if (texturableTexturesCollection == null) {
         ArrayList<TextureName> textureList = new ArrayList<>();
         TextureName[] var4;
         int var3 = (var4 = values()).length;

         for (int var2 = 0; var2 < var3; var2++) {
            TextureName texture = var4[var2];
            if (texture.isTexturable()) {
               textureList.add(texture);
            }
         }

         texturableTexturesCollection = textureList;
      }

      return texturableTexturesCollection;
   }

   public static synchronized Collection<TextureName> nonTexturableValuesCollection() {
      if (nonTexturableTexturesCollection == null) {
         ArrayList<TextureName> textureList = new ArrayList<>();
         TextureName[] var4;
         int var3 = (var4 = values()).length;

         for (int var2 = 0; var2 < var3; var2++) {
            TextureName texture = var4[var2];
            if (!texture.isTexturable()) {
               textureList.add(texture);
            }
         }

         nonTexturableTexturesCollection = textureList;
      }

      return nonTexturableTexturesCollection;
   }
}
