package minecraft.planner.gui.version;

public class VersionUtilities {
   private static VersionInfo versionInfo = null;

   public static final boolean newerVersionAvailable(VersionInfo info) {
      return !"0.99.7".trim().equals(info.getLatestVersion());
   }

   public static final synchronized VersionInfo getVersionInfo() {
      if (versionInfo == null) {
         try {
            versionInfo = new VersionInfo("http://minecraftstructureplanner.com/download/version.txt");
         } catch (Throwable e) {
            versionInfo = null;
         }
      }

      return versionInfo;
   }
}
