package minecraft.planner.gui.textures;

import javax.swing.ImageIcon;

public class DefaultTexturePack extends AbstractTexturePack {
   private static final long serialVersionUID = 7217539574773080926L;
   private final ImageIcon[][] textures = new ImageIcon[16][16];

   public DefaultTexturePack() {
      super("Default", null);
      this.textures[0][0] = getResourceIcon("none.png");
      this.textures[1][0] = getResourceIcon("stone.png");
      this.textures[2][0] = getResourceIcon("dirt.png");
      this.textures[3][0] = getResourceIcon("grass.png");
      this.textures[4][0] = getResourceIcon("plank.png");
      this.textures[5][0] = getResourceIcon("doubleslab.png");
      this.textures[6][0] = getResourceIcon("halfslab-stone-texture.png");
      this.textures[7][0] = getResourceIcon("brick.png");
      this.textures[8][0] = getResourceIcon("tnt.png");
      this.textures[0][1] = getResourceIcon("cobblestone.png");
      this.textures[1][1] = getResourceIcon("bedrock.png");
      this.textures[2][1] = getResourceIcon("sand.png");
      this.textures[3][1] = getResourceIcon("gravel.png");
      this.textures[4][1] = getResourceIcon("wood.png");
      this.textures[6][1] = getResourceIcon("steel.png");
      this.textures[7][1] = getResourceIcon("gold.png");
      this.textures[8][1] = getResourceIcon("diamond.png");
      this.textures[11][1] = getResourceIcon("chest.png");
      this.textures[0][2] = getResourceIcon("gold-ore.png");
      this.textures[1][2] = getResourceIcon("iron-ore.png");
      this.textures[2][2] = getResourceIcon("coal.png");
      this.textures[3][2] = getResourceIcon("Bookshelf.png");
      this.textures[4][2] = getResourceIcon("green_cobblestone.png");
      this.textures[5][2] = getResourceIcon("obsidian.png");
      this.textures[12][2] = getResourceIcon("furnace.png");
      this.textures[14][2] = getResourceIcon("Dispenser.png");
      this.textures[0][3] = getResourceIcon("sponge.png");
      this.textures[1][3] = getResourceIcon("glass.png");
      this.textures[2][3] = getResourceIcon("diamond-ore.png");
      this.textures[3][3] = getResourceIcon("redstone-ore.png");
      this.textures[4][3] = getResourceIcon("leaves.png");
      this.textures[12][3] = getResourceIcon("workbench.png");
      this.textures[0][4] = getResourceIcon("wool.png");
      this.textures[1][4] = getResourceIcon("spawner.png");
      this.textures[2][4] = getResourceIcon("snow.png");
      this.textures[3][4] = getResourceIcon("ice.png");
      this.textures[5][4] = getResourceIcon("cactus.png");
      this.textures[8][4] = getResourceIcon("clay.png");
      this.textures[9][4] = getResourceIcon("reeds.png");
      this.textures[7][6] = getResourceIcon("robblestone.png");
      this.textures[8][6] = getResourceIcon("slowstone.png");
      this.textures[9][6] = getResourceIcon("glowstone.png");
      this.textures[1][7] = getResourceIcon("BlackWool.png");
      this.textures[2][7] = getResourceIcon("DarkGrayWool.png");
      this.textures[4][7] = getResourceIcon("Pine.png");
      this.textures[5][7] = getResourceIcon("Birch.png");
      this.textures[6][7] = getResourceIcon("pumpkin.png");
      this.textures[1][8] = getResourceIcon("RedWool.png");
      this.textures[2][8] = getResourceIcon("PinkWool.png");
      this.textures[0][9] = getResourceIcon("LapisLazuli.png");
      this.textures[1][9] = getResourceIcon("GreenWool.png");
      this.textures[2][9] = getResourceIcon("LimeWool.png");
      this.textures[0][10] = getResourceIcon("lapis-ore.png");
      this.textures[1][10] = getResourceIcon("BrownWool.png");
      this.textures[2][10] = getResourceIcon("YellowWool.png");
      this.textures[1][11] = getResourceIcon("BlueWool.png");
      this.textures[2][11] = getResourceIcon("LightBlueWool.png");
      this.textures[0][12] = getResourceIcon("Sandstone.png");
      this.textures[1][12] = getResourceIcon("PurpleWool.png");
      this.textures[2][12] = getResourceIcon("MagentaWool.png");
      this.textures[13][12] = getResourceIcon("water.png");
      this.textures[1][13] = getResourceIcon("CyanWool.png");
      this.textures[2][13] = getResourceIcon("OrangeWool.png");
      this.textures[1][14] = getResourceIcon("LightGrayWool.png");
      this.textures[13][14] = getResourceIcon("lava.png");
   }

   @Override
   public ImageIcon getImage(int x, int y) {
      return this.textures[x][y];
   }

   @Override
   public ImageIcon getImage(TextureName texture) {
      if (texture == TextureName.None) {
         return AbstractTexturePack.getResourceIcon(TextureName.None.getResourceName());
      } else {
         return texture.isTexturable() ? this.textures[texture.getResourceX()][texture.getResourceY()] : this.getNonTexturableImage(texture);
      }
   }
}
