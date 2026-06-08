package minecraft.planner.model.freeform;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import minecraft.planner.gui.java3d.modelfactory.impl.StairModelFactory;
import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureModel;
import minecraft.planner.model.grid.WorldGrid$Cell;
import minecraft.planner.model.grid.attributes.Orientation;
import minecraft.planner.model.util.Tag;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class FreeformModel extends StructureModel<FreeformParameters> {
   public static final String NAME = "Freeform";

   public FreeformModel() {
   }

   public FreeformModel(File file) {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser parser = factory.newSAXParser();
         parser.parse(file, new FreeformModel.ModelParser(null));
         this.grid.updateMetrics(this);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public FreeformModel(Binvox binvox, TextureName texture) {
      try {
         int width = binvox.getWidth();
         int length = binvox.getDepth();
         int height = binvox.getHeight();
         this.parameters = new FreeformParameters(width, length, height);
         this.grid.initialize(width, length);
         int x = 0;
         int y = 0;
         int z = 0;

         for (int i = 0; i < binvox.getSize(); i++) {
            int value = binvox.getValue(i);
            if (value != 0) {
               this.grid.getCell(x, y).getStack().addZ(z + 1, texture);
            }

            if (++z % height == 0) {
               z = 0;
               if (++x % width == 0) {
                  x = 0;
                  y++;
               }
            }
         }

         this.grid.updateMetrics(this);
         this.grid.trim();
         this.removeHiddenVoxels();
         this.fireModelChangedEvent();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public FreeformModel(Ruins ruins) {
      try {
         int width = ruins.getWidth();
         int length = ruins.getDepth();
         int height = ruins.getHeight();
         this.parameters = new FreeformParameters(width, length, height);
         this.grid.initialize(width, length);

         for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
               WorldGrid$Cell.ZStack stack = this.grid.getCell(x, y).getStack();

               for (int z = 0; z < height; z++) {
                  TextureName texture = ruins.getValue(x, y, z);
                  if (texture != null && texture != TextureName.None) {
                     stack.addZ(z + 1, texture);
                  }
               }
            }
         }

         this.grid.updateMetrics(this);
         this.grid.trim();
         this.removeHiddenVoxels();
         this.fireModelChangedEvent();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public FreeformModel(Tag schematic) {
      try {
         Short height = (Short)schematic.findTagByName("Height").getValue();
         Short width = (Short)schematic.findTagByName("Width").getValue();
         Short length = (Short)schematic.findTagByName("Length").getValue();
         byte[] blocks = (byte[])schematic.findTagByName("Blocks").getValue();
         byte[] data = (byte[])schematic.findTagByName("Data").getValue();
         this.parameters = new FreeformParameters(width, length, height);
         this.grid.initialize(width, length);
         int layerSize = width * length;

         for (int i = 0; i < blocks.length; i++) {
            int x = width - 1 - i % width;
            int y = length - 1 - (int)Math.floor(i % layerSize / width);
            int z = (int)Math.floor(i / layerSize);
            byte block = blocks[i];
            byte datum = data[i];
            TextureName texture = TextureName.getTextureByNBTKey(block, datum);
            if (texture != null) {
               Orientation o = Orientation.None;
               if (texture.hasOrientation() && texture.getModelFactoryClass() == StairModelFactory.class) {
                  switch (datum) {
                     case 0:
                        o = Orientation.South;
                        break;
                     case 1:
                        o = Orientation.North;
                        break;
                     case 2:
                        o = Orientation.West;
                        break;
                     case 3:
                        o = Orientation.East;
                        break;
                     default:
                        o = Orientation.None;
                  }
               }

               this.grid.getCell(x, y).getStack().addZ(z + 1, texture, o);
            }
         }

         this.grid.updateMetrics(this);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void generate(FreeformParameters parameters) {
      super.generate(parameters);
      this.grid.initialize(parameters.getX(), parameters.getY());
      super.generated();
   }

   @Override
   public void initialize() {
      this.initialized = true;
   }

   public void fireModelChangedEvent() {
      this.notifyListenersOfChange(this);
   }

   @Override
   public String getStructureName() {
      return "Freeform";
   }

   @Override
   public TextureName getDefaultTexture() {
      return null;
   }

   private class ModelParser extends DefaultHandler {
      private WorldGrid$Cell cell = null;
      private StringBuffer notes = new StringBuffer();

      private ModelParser() {
      }

      @Override
      public void endElement(String uri, String localName, String qName) {
         if (qName.equals("notes")) {
            FreeformModel.this.setNotes(this.notes.toString());
         }
      }

      @Override
      public void characters(char[] characters, int start, int length) {
         this.notes.append(String.valueOf(characters, start, length));
      }

      @Override
      public void startElement(String uri, String localName, String qName, Attributes attributes) {
         if (!qName.equals("structure") && !qName.equals("notes")) {
            if (qName.equals("grid")) {
               int width = this.parseInt(attributes.getValue("width"), 64);
               int height = this.parseInt(attributes.getValue("height"), 64);
               int maxHeight = this.parseInt(attributes.getValue("maxheight"), 64);
               FreeformModel.this.parameters = new FreeformParameters(width, height, maxHeight);
               FreeformModel.this.grid.initialize(width, height);
            } else if (qName.equals("cell")) {
               int x = this.parseInt(attributes.getValue("x"), 0);
               int y = this.parseInt(attributes.getValue("y"), 0);
               this.cell = FreeformModel.this.grid.getCell(x, y);
            } else if (qName.equals("block")) {
               try {
                  int height = this.parseInt(attributes.getValue("height"), 0);
                  TextureName texture = TextureName.getTextureByName(attributes.getValue("texture"));
                  Orientation orientation = Orientation.None;
                  String orientationName = attributes.getValue("orientation");
                  if (orientationName != null && orientationName.trim().length() != 0) {
                     orientation = Orientation.valueOf(orientationName);
                  }

                  if (texture == TextureName.None) {
                     texture = TextureName.Dirt;
                  }

                  this.cell.getStack().addZ(height, texture, orientation);
               } catch (Exception var9) {
               }
            }
         }
      }

      private int parseInt(String intString, int defaultValue) {
         if (intString != null) {
            try {
               return Integer.parseInt(intString);
            } catch (Exception var4) {
            }
         }

         return defaultValue;
      }
   }
}
