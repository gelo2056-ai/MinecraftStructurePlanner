package minecraft.planner.model.freeform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import minecraft.planner.gui.textures.TextureName;

public class Ruins {
   private int width;
   private int depth;
   private int height;
   protected final Random rnd = new Random();
   private TextureName[][][] layers;
   private Map<Integer, Ruins.Rule> blockMap = new HashMap<>();

   public Ruins(File binvoxFile) {
      try {
         this.initialize(binvoxFile);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public int getWidth() {
      return this.width;
   }

   public int getDepth() {
      return this.depth;
   }

   public int getHeight() {
      return this.height;
   }

   public TextureName getValue(int x, int y, int z) {
      if (x < 0 || x >= this.width) {
         throw new IndexOutOfBoundsException("X index out of bounds");
      } else if (y < 0 || y >= this.depth) {
         throw new IndexOutOfBoundsException("Y index out of bounds");
      } else if (z >= 0 && z < this.height) {
         return this.layers[x][y][z];
      } else {
         throw new IndexOutOfBoundsException("Z index out of bounds");
      }
   }

   private void initialize(File file) throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      int layer = 0;

      String line;
      do {
         line = reader.readLine();
         if (line != null) {
            line = line.trim().toLowerCase();
            if (line.equals("layer")) {
               this.parseLayer(reader, layer++);
            } else if (line.startsWith("rule")) {
               this.parseRule(line);
            } else {
               this.parseHeader(line);
            }
         }
      } while (line != null);

      reader.close();
   }

   private void parseHeader(String line) {
      if (line.startsWith("dimensions")) {
         String[] components = line.split("=");
         String[] dimensions = components[1].split(",");
         this.height = Integer.parseInt(dimensions[0]);
         this.depth = Integer.parseInt(dimensions[1]);
         this.width = Integer.parseInt(dimensions[2]);
         this.layers = new TextureName[this.width][this.depth][this.height];
      }
   }

   private void parseRule(String line) {
      String[] components = line.split("=");
      String rule = components[0];
      String blockParameters = components[1];
      int ruleNumber = Integer.parseInt(rule.substring(4));
      String[] blockNumbers = blockParameters.split(",");
      int percentageChance = Integer.parseInt(blockNumbers[1]);
      List<Byte> blocks = new ArrayList<>();
      List<Byte> data = new ArrayList<>();

      for (int i = 2; i < blockNumbers.length; i++) {
         int minusIndex = blockNumbers[i].indexOf(45);
         int blockValue = 0;
         int dataValue = 0;
         if (minusIndex > -1) {
            blockValue = Integer.parseInt(blockNumbers[i].substring(0, minusIndex));
            dataValue = Integer.parseInt(blockNumbers[i].substring(minusIndex + 1));
            blocks.add((byte)(blockValue & 0xFF));
            data.add((byte)(dataValue & 0xFF));
         } else {
            blockValue = Integer.parseInt(blockNumbers[i]);
            blocks.add((byte)(blockValue & 0xFF));
            data.add((byte)0);
         }
      }

      this.blockMap.put(ruleNumber, new Ruins.Rule(percentageChance, blocks, data));
   }

   private void parseLayer(BufferedReader reader, int layer) throws IOException {
      String layerLine = null;
      int y = 0;

      do {
         layerLine = reader.readLine();
         if (layerLine != null && layerLine.trim().length() > 0) {
            layerLine = layerLine.trim().toLowerCase();
            if (!layerLine.equals("endlayer")) {
               String[] ruleNumbers = layerLine.split(",");

               for (int x = 0; x < ruleNumbers.length; x++) {
                  int ruleNumber = Integer.parseInt(ruleNumbers[x]);
                  Ruins.Rule rule = this.blockMap.get(ruleNumber);
                  if (rule != null) {
                     this.layers[x][y][layer] = this.getBlock(this.blockMap.get(ruleNumber));
                  } else {
                     this.layers[x][y][layer] = null;
                  }
               }
            }
         }

         y++;
      } while (layerLine != null && !layerLine.equals("endlayer"));
   }

   private TextureName getBlock(Ruins.Rule rule) {
      int percent = this.rnd.nextInt(100);
      return percent <= rule.percentageChance ? rule.getRandomBlock() : TextureName.None;
   }

   private class Rule {
      private int percentageChance;
      private List<Byte> blocks;
      private List<Byte> data;

      public Rule(int percentageChance, List<Byte> blocks, List<Byte> data) {
         this.percentageChance = percentageChance;
         this.blocks = new ArrayList<>(blocks);
         this.data = new ArrayList<>(data);
      }

      public int getPercentageChance() {
         return this.percentageChance;
      }

      public TextureName getRandomBlock() {
         if (this.blocks.isEmpty()) {
            return TextureName.None;
         }

         if (this.blocks.size() == 1) {
            return TextureName.getTextureByNBTKey(this.blocks.get(0), this.data.get(0));
         }

         int index = Ruins.this.rnd.nextInt(this.blocks.size());
         return TextureName.getTextureByNBTKey(this.blocks.get(index), this.data.get(index));
      }
   }
}
