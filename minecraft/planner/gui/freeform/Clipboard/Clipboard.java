package minecraft.planner.gui.freeform.Clipboard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import minecraft.planner.gui.textures.TextureName;

public class Clipboard {
   private Set<CopyCell> cells = new HashSet<>();

   public Clipboard() {
      this.clear();
   }

   public void clear() {
      this.cells.clear();
   }

   public void addCell(int x, int y, TextureName texture) {
      this.cells.add(new CopyCell(x, y, texture));
   }

   public Collection<CopyCell> getContents() {
      return this.cells;
   }
}
