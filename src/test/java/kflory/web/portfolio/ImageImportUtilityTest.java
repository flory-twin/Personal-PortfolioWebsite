package kflory.web.portfolio;

import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import kflory.web.portfolio.utilities.ImageImportUtility;
import kflory.web.portfolio.utilities.RootPather;

@SpringBootTest
public class ImageImportUtilityTest
{
      public static Path absoluteLocalFilePath = Paths.get(RootPather.rootPath.toString() +
            "/media/kflory/Kevin's Stuff/WorkingFiles/Reference/MyWork/HumanAndNude/Release/Nudes/Victoria/20151206-44.jpg");
      
      @Test
      void testReadSmallPngImageFromFilesystem() {
         Path smallImageFilepath = Paths.get(
               RootPather.rootPath.toString() +
               "/src/test/resources/images/" +
               "VerySmallPNG-11k.png");
         byte[] smallImageBytes = new byte[0];
         try {
            smallImageBytes = ImageImportUtility.readBytesFromFilesystem(smallImageFilepath);
         } catch(Exception e) {
            fail(e.getMessage());
         }
         //If no exception has occurred, the test passes.
      }
}
