package minecraft.planner.model.maze;

import minecraft.planner.gui.textures.TextureName;
import minecraft.planner.model.StructureParameters;

public class MazeParameters extends StructureParameters {
   private int width;
   private int depth;
   private int borderHeight;
   private int wallHeight;
   private TextureName borderTexture;
   private TextureName wallTexture;
   private TextureName floorTexture;
   private boolean centerRoom;
   private int centerRoomWidth;
   private int centerRoomDepth;
   private int centerRoomHeight;
   private TextureName centerRoomTexture;

   public MazeParameters(
      int width,
      int depth,
      int borderHeight,
      int wallHeight,
      TextureName borderTexture,
      TextureName wallTexture,
      TextureName floorTexture,
      boolean centerRoom,
      int centerRoomWidth,
      int centerRoomDepth,
      int centerRoomHeight,
      TextureName centerRoomTexture
   ) {
      this.width = width;
      this.depth = depth;
      this.borderHeight = borderHeight;
      this.wallHeight = wallHeight;
      this.borderTexture = borderTexture;
      this.wallTexture = wallTexture;
      this.floorTexture = floorTexture;
      this.centerRoom = centerRoom;
      this.centerRoomWidth = centerRoomWidth;
      this.centerRoomDepth = centerRoomDepth;
      this.centerRoomHeight = centerRoomHeight;
      this.centerRoomTexture = centerRoomTexture;
   }

   public int getWidth() {
      return this.width;
   }

   public int getDepth() {
      return this.depth;
   }

   public int getBorderHeight() {
      return this.borderHeight;
   }

   public int getWallHeight() {
      return this.wallHeight;
   }

   public TextureName getBorderTexture() {
      return this.borderTexture;
   }

   public TextureName getWallTexture() {
      return this.wallTexture;
   }

   public TextureName getFloorTexture() {
      return this.floorTexture;
   }

   public boolean isCenterRoom() {
      return this.centerRoom;
   }

   public int getCenterRoomWidth() {
      return this.centerRoomWidth;
   }

   public int getCenterRoomDepth() {
      return this.centerRoomDepth;
   }

   public int getCenterRoomHeight() {
      return this.centerRoomHeight;
   }

   public TextureName getCenterRoomTexture() {
      return this.centerRoomTexture;
   }
}
