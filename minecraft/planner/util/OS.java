package minecraft.planner.util;

public enum OS {
   Unknown("Unknown"),
   MacOSX("Mac OS X"),
   Windows32("Windows 32-bit"),
   Windows64("Windows 64-bit"),
   Linux32("Linux 32-bit"),
   Linux64("Linux 64-bit");

   private final String description;

   OS(String description) {
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }
}
