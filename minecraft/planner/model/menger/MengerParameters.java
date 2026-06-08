package minecraft.planner.model.menger;

import minecraft.planner.model.StructureParameters;

public class MengerParameters extends StructureParameters {
   private int level;

   public MengerParameters(int level) {
      this.level = level;
   }

   public int getLevel() {
      return this.level;
   }
}
