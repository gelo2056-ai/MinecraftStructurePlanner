package minecraft.planner.gui.version;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class VersionInfo {
   private String latestVersion;
   private String releaseDate;
   private String features;

   public VersionInfo(String versionURLString) throws MalformedURLException {
      this(new URL(versionURLString));
   }

   public VersionInfo(URL versionURL) {
      InputStream is = null;
      BufferedReader reader = null;

      try {
         is = versionURL.openStream();
         reader = new BufferedReader(new InputStreamReader(is));
         this.features = "";
         StringBuffer featuresHTML = new StringBuffer();

         String line;
         do {
            line = reader.readLine();
            if (line != null) {
               line = line.trim();
               if (line.startsWith("v")) {
                  this.latestVersion = line.substring(1);
               } else if (line.startsWith("r")) {
                  this.releaseDate = line.substring(1);
               } else {
                  featuresHTML.append(line);
               }
            }
         } while (line != null);

         this.features = featuresHTML.toString();
      } catch (Throwable var18) {
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (Exception var17) {
            }
         }

         if (is != null) {
            try {
               is.close();
            } catch (Exception var16) {
            }
         }
      }
   }

   public String getLatestVersion() {
      return this.latestVersion;
   }

   public String getReleaseDate() {
      return this.releaseDate;
   }

   public String getFeatureList() {
      return this.features;
   }
}
