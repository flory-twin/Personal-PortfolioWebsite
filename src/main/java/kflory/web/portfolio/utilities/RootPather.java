package kflory.web.portfolio.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RootPather
{
   private static final String unescapedRootPath;
   public static final Path rootPath;
 
// Adapted with minor changes from https://www.baeldung.com/java-properties.
   static {
      String tempPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

      // If we're running in the Dev version's build target folder, move up the path until we get to the project root.
      if (tempPath.contains("/target")) {
         tempPath = tempPath.replaceAll("/target.*", "");
      }
      
      // Finally, if the path to the current thread's classloader root contains a space, it will have been re-encoded as %20. Reverse this, then make sure the space is correctly surrounded by quotes so it can be handled as a file system path.
      if (tempPath.contains("%20")) {
         tempPath = tempPath.replaceAll("%20", " ");
         //tempPath = "\"" + tempPath + "\"";
         // TODO: Would need to verify that any " in the path already are matched and do not path-escape the step containing the space.
         // It turns out this has already been done for my single example.
      }
      
      unescapedRootPath = tempPath;
      
      rootPath = Paths.get(unescapedRootPath);
   }
   
}
