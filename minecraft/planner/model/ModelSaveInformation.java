package minecraft.planner.model;

import java.io.File;

public class ModelSaveInformation {
   private boolean changed;
   private File savedTo = null;

   public ModelSaveInformation() {
      this.changed = false;
   }

   public boolean isChanged() {
      return this.changed;
   }

   public File getFile() {
      return this.savedTo;
   }

   public void setFile(File file) {
      this.savedTo = file;
   }

   public void setChanged() {
      this.changed = true;
   }

   public void clearChanged() {
      this.changed = false;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      if (this.savedTo != null) {
         sb.append(this.savedTo.getName());
      }

      if (this.changed) {
         if (sb.length() > 0) {
            sb.append(' ');
         }

         sb.append('*');
      }

      return sb.toString();
   }
}
