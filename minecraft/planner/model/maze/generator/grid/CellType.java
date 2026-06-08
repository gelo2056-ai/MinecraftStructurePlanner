package minecraft.planner.model.maze.generator.grid;

public enum CellType {
   Wall("Wall", '%'),
   Permanent_Wall("Permanent Wall", '*'),
   Reserved_Space("Reserved Space", '.'),
   Entrance("Entrance", '<'),
   Exit("Exit", '>'),
   Passage("Passage", ' ');

   private String name;
   private char display;

   CellType(String name, char display) {
      this.name = name;
      this.display = display;
   }

   public String getName() {
      return this.name;
   }

   public char getDisplay() {
      return this.display;
   }
}
