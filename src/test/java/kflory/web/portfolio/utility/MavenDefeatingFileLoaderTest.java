package kflory.web.portfolio.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

/*
 * Note: I realize it's poor practice to couple a unit test to the file system. Unfortunately, I can only seem to get Spring's relative-path ResourceLoader loading to work in highly specific ways on my system.
 * This was frustrating to solve and my solution is fragile, so even a tightly-coupled test is better than none.
 * 
 * Note that, while unit tests run from the source hierarchy, project code runs from the build WAR. This is, therefore, not a complete test under current conditions. 
 * 
 * TODO More research needed.
 */
@SpringBootTest
public class MavenDefeatingFileLoaderTest
{
    private String artDirectoryResourcePath;
    @Value("${testing.resource.sourceFolder}")
    private String pathToArt;
    @Value("#{\"${testing.resource.sourceFolder}\" + \"${art.resource.subfolder}\"}")
    private String deepPathToArt;

    /*
     * Sanity check: demonstrate that the source file structure, rather than the
     * final Maven WAR file structure, is what's visible to my code while
     * testing. This is, strictly speaking, proof that my file loader is solving
     * the right problem, not that test preconditions are set correctly. To
     * document the reasoning behind this check: - The source resources which
     * will make it into the WAR come from (project root)/src/main/resources
     * (and don't include resources from /src/test.) - These resources will map
     * into (WAR root)/WEB-INF/classes/static; there is no /resources subfolder
     * in the WAR. - The WAR will only include generated .class files from
     * /src/main.
     * 
     * Thus, if we can see any file from a child of the /resources subfolder, or
     * we can see Java source files, we know that test code cannot be loading
     * files from the WAR.
     *
     * FILESYSTEM DEPENDENCIES: At least one .jpg lives in /resources or a
     * descendant; there is at least one source file in
     * /src/main/java/kflory/web. NO DEPENDENCY ON: property
     * testing.art.sourceFolder
     */
//	@BeforeAll
//	@Test
//	public static void validateTestingUsesSourceStructure() throws IOException{
//		//We can load a .jpg from /resources, so that folder must be visible.
//		String[] jpgOnly = {".jpg"};
//		Assumptions.assumeThat(
//				MavenDefeatingFileLoader.enumerateDescendantFiles("resources", jpgOnly)
//				.size())
//			.isGreaterThan(0);
//		
//		//We can load .java source, which wouldn't be in the WAR.
//		String[] javaOnly = {".java"};
//		Assumptions.assumeThat(
//				MavenDefeatingFileLoader.enumerateDescendantFiles("src/main/java/kflory/web", javaOnly)
//				.size())
//			.isGreaterThan(0);
//	}

    /*
     * ======================================================================
     * MavenDefeatingFileLoader.getResource()
     * ======================================================================
     */

    /*
     * Verify that a specific, known file can be loaded. FILESYSTEM
     * DEPENDENCIES: This class is named MavenDefeatingFileLoaderTest. NO
     * DEPENDENCY ON: property testing.art.sourceFolder
     */
    @Test
    public void testPositivePathOf_getResource() throws IOException
    {
        Resource r = MavenDefeatingFileLoader
            .getResource("MavenDefeatingFileLoaderTest.class");
        Assertions.assertThat(r.getFile().isFile()).isTrue();
        Assertions.assertThat(r.contentLength()).isGreaterThan(0L);
    }

    /*
     * These are the exception conditions that are expected when loading files.
     * They provide some coverage for specific bad input values. FILESYSTEM
     * DEPENDENCIES: horripilationMongooseFarm.fuz does not exist at any point;
     * main/resources/ is a folder, not a file NO DEPENDENCY ON: property
     * testing.art.sourceFolder
     */
    @Test
    public void testExpectedExceptionsOf_getResource()
    {

        Assertions.assertThatIOException().isThrownBy(
            () -> MavenDefeatingFileLoader.getResource(null)
        ).withMessage(
            "The path requested from the Maven-defeating file loader was null or empty."
        );

        // Multiple files match path
        Assertions.assertThatIOException().isThrownBy(
            () -> MavenDefeatingFileLoader.getResource("*.class")
        ).withMessageContaining(
            "The Maven-defeating file loader found too many filesystem entities matching"
        );

        // Nothing with such a path exists
        Assertions.assertThatExceptionOfType(
            FileNotFoundException.class
        ).isThrownBy(
            () -> MavenDefeatingFileLoader
                .getResource("horripilationMongooseFarm.fuz")
        ).withMessageContaining(
            "The Maven-defeating file loader could not find a filesystem entity"
        );

        // Is a directory, not a file
        Assertions.assertThatExceptionOfType(FileNotFoundException.class)
            .isThrownBy(
                () -> MavenDefeatingFileLoader.getResource("test/resources")
            ).withMessageContaining("exists, but is not a file");

        // TODO: Test isReadable at some point...?
    }

    /*
     * ======================================================================
     * MavenDefeatingFileLoader.getFolderResource()
     * ======================================================================
     */

    /*
     * Verify that a specific, known folder can be 'loaded'. FILESYSTEM
     * DEPENDENCIES: The project location/package of this class exists as a
     * folder with value src/test/java/kflory/web/portfolio/utility. (Or such a
     * folder exists without this class in it, although that's less likely.) NO
     * DEPENDENCY ON: property testing.art.sourceFolder
     */
    @Test
    public void testPositivePathOf_getFolderResource() throws IOException
    {
        Assertions.assertThat(
            MavenDefeatingFileLoader
                .getFolderResource("src/test/java/kflory/web/portfolio/utility")
                .getFile()
        ).exists()
        .isDirectory()
        .matches(
            dir -> dir.getPath().contains("src"),
            "Has 'src' in its stringified path."
        );

        Assertions.assertThat(
            MavenDefeatingFileLoader.getFolderResource("src/test").getFile()
        ).exists()
        .isDirectory()
        .matches(
            dir -> dir.getPath().contains("src"),
            "Has 'src' in its stringified path."
        );

        Assertions.assertThat(
            MavenDefeatingFileLoader.getFolderResource("test").getFile()
        ).exists()
        .isDirectory()
        .matches(
            dir -> dir.getPath().contains("src"),
            "Has 'src' in its stringified path."
        );
    }

    /*
     * These are the exception conditions for folder loading. They're the same
     * as for file loading, except that the final exception check verifies the
     * resource is a directory.
     * 
     * FILESYSTEM DEPENDENCIES: horripilationMongooseFarm.fuz does not exist at
     * any point; main/resources/ is a folder, not a file. NO DEPENDENCY ON:
     * property testing.art.sourceFolder
     */
    @Test
    public void testExpectedExceptionsOf_getFolderResource()
    {
        Assertions.assertThatIOException().isThrownBy(
            () -> MavenDefeatingFileLoader.getFolderResource(null)
        ).withMessage(
            "The path requested from the Maven-defeating file loader was null or empty."
        );

        // Multiple folders match path (at least src/main/.../utility and
        // src/test/.../utility match)
        Assertions.assertThatIOException().isThrownBy(
            () -> MavenDefeatingFileLoader.getFolderResource("utility")
        ).withMessageContaining(
            "The Maven-defeating file loader found too many filesystem entities matching"
        );

        // Nothing with such a path exists
        Assertions.assertThatExceptionOfType(
            FileNotFoundException.class
        ).isThrownBy(
            () -> MavenDefeatingFileLoader
                .getFolderResource("horripilationCandidates")
        ).withMessageContaining(
            "The Maven-defeating file loader could not find a filesystem entity"
        );

        // Is a file, not a directory
        Assertions.assertThatIOException()
            .isThrownBy(
                () -> MavenDefeatingFileLoader
                    .getFolderResource("resources/application.properties")
            ).withMessageContaining("exists, but is not a directory");

        // TODO: Test isReadable at some point...?
    }

    /*
     * Verify that globbing works when requesting a folder. FILESYSTEM
     * DEPENDENCIES: The project location/package of this class exists as a
     * folder with value src/test/java/kflory/web/portfolio/utility. (Or such a
     * folder exists without this class in it, although that's less likely.) NO
     * DEPENDENCY ON: property testing.art.sourceFolder
     */
    @Test
    public void testGetFolderResource_worksWithGlobbing()
            throws IOException
    {
        Assertions.assertThat(
            MavenDefeatingFileLoader.getFolderResource("src/test/**/utility")
                .getFile()
        ).isDirectory()
        .exists()
        .matches(dir ->
        {
            // Did the globbed search string fetch the path I'm expecting?
            String pathOfFetchedDir = dir.getPath().replace('\\', '/');
            String expectedPathShouldContain = "src/test/java/kflory/web/portfolio/utility";
            return pathOfFetchedDir.contains(expectedPathShouldContain);
        });

        // Use globbing with care: In this case, multiple folders match path (at
        // least
        // src/main/.../utility and src/test/.../utility match)
        Assertions.assertThatIOException().isThrownBy(
            () -> MavenDefeatingFileLoader.getFolderResource("src/**/utility")
        ).withMessageContaining(
            "The Maven-defeating file loader found too many filesystem entities matching"
        );
    }
    // TODO Add test for how "" and "/" will not fetch the working directory as
    // a
    // resource.

    /*
     * ======================================================================
     * MavenDefeatingFileLoader.enumerateDescendantFiles()
     * ======================================================================
     */

    /*
     * Show that a simple case of enumerateDescendantFiles() works. FILESYSTEM
     * DEPENDENCIES: xxx PROPERTY DEPENDENCIES: yyyy
     */
    @Test
	public void testPositivePathOf_enumerateDescendantFiles() throws Exception
	{ 
        String[] singleValidPictureType = { ".jpg" };

        //-"WEB-INF/classes/static/images/art/AdmiralPerry"
        //-"static/images/art/AdmiralPerry"
        
        Assertions.assertThat(
            MavenDefeatingFileLoader.enumerateDescendantFiles(
                deepPathToArt,
                singleValidPictureType
            )
        ).isNotEmpty()
        .allMatch(resource ->
        {
            try
            {
                File returnedFile = resource.getFile();
                return returnedFile.exists() && returnedFile.isFile()
                    && returnedFile.getCanonicalPath()
                        .endsWith(singleValidPictureType[0]);
            }
            catch (IOException e)
            {
                return false;
            }
        });

    }

    /*
     * As we ascend up the folder tree, the file loader should encounter more
     * and more files as the path grows less and less specific. FILESYSTEM
     * DEPENDENCIES: See property dependencies. PROPERTY DEPENDENCIES:
     * testing.art.sourceFolder must be a subfolder of images; there must be at
     * least one .jpg in /images, or in some child folder other than
     * testing.art.sourceFolder.
     */
    @Test
    public void testPositivePathOf_enumerateDescendantFiles_showingThatEnumeratedCountsIncreaseFromBottomOfFolderStructure()
            throws Exception
    {
        String[] singleValidPictureType = {".jpg"};
        
        ArrayList<Resource> resources = MavenDefeatingFileLoader.enumerateDescendantFiles(
            deepPathToArt, singleValidPictureType);
        Assertions.assertThat(resources.size())
            .isGreaterThan(0);
        int initialCountOfFiles = resources.size();
        int priorCountOfFiles = initialCountOfFiles;
        String[] pathSteps = deepPathToArt.split("/");
        
        if (pathSteps.length > 0)
        {
            for (int pathSepCount = pathSteps.length - 1; pathSepCount >= 0; pathSepCount-- )
            {
                String ancestorFolderOfPathToArt = "";
                //Add the steps from pathSteps[0...pathSepCount]. This path 
                for (int stepNo = 0; stepNo < pathSepCount; stepNo++)
                {
                    ancestorFolderOfPathToArt += pathSteps[stepNo] + "/";
                }
                
                resources = MavenDefeatingFileLoader.enumerateDescendantFiles(
                    ancestorFolderOfPathToArt, singleValidPictureType);
                Assertions.assertThat(resources.size())
                    .isGreaterThanOrEqualTo(priorCountOfFiles);
                priorCountOfFiles = resources.size();
            }
        }
    }


    /*
     * Show how file type impacts file enumeration.
     */
    @Test
    public void testPositivePathOf_enumerateDescendantFiles_showingMoreResultsWithMoreFileTypesToSearchOn()
            throws Exception
    {
        String[] nonexistentFileType = { ".fuz" };
        String[] multipleValidFileTypes = { ".jpeg", ".jpg", ".png" };
        String[] singleValidPictureType = { ".jpg" };

        // Valid path, no file types match: No exceptions, 0 results
        Assertions.assertThat(
            MavenDefeatingFileLoader
                .enumerateDescendantFiles("", nonexistentFileType).size()
        ).isEqualTo(0);

        ArrayList<Resource> resources = MavenDefeatingFileLoader
            .enumerateDescendantFiles("", singleValidPictureType);
        Assertions.assertThat(resources.size()).isGreaterThan(0);

        // Valid path, multiple file types match: No exceptions, more results
        // than
        // earlier
        Assertions.assertThat(
            MavenDefeatingFileLoader
                .enumerateDescendantFiles("", multipleValidFileTypes).size()
        ).isGreaterThan(resources.size());
    }

    /*
     * Demonstrate how the file loader reacts to bad input when enumerating
     * files. FILESYSTEM DEPENDENCIES: The folder resources/ exists; no folder
     * exists at resources/mongooses/mongeese/whatever/. NO DEPENDENCY ON:
     * property testing.art.sourceFolder
     */
    @Test
    public void testExceptionsAndCornerConditionsOf_enumerateDescendantFiles()
            throws Exception
    {
        // Null path string: Unhandled exception
        Assertions.assertThatExceptionOfType(Exception.class).isThrownBy(
            () -> MavenDefeatingFileLoader
                .enumerateDescendantFiles(null, new String[0])
        );

        // Path exists, no filetypes provided: No exceptions, 0 results
        Assertions.assertThat(
            MavenDefeatingFileLoader
                .enumerateDescendantFiles("resources", new String[0]).size()
        ).isEqualTo(0);

        String[] validFiletype = { ".jsp" };
        // Overspecified path does not exist: No exceptions, 0 results
        Assertions.assertThat(
            MavenDefeatingFileLoader.enumerateDescendantFiles(
                "resources/mongooses/mongeese/whatever", validFiletype
            ).size()
        ).isEqualTo(0);
    }

    /*
     * ======================================================================
     * MavenDefeatingFileLoader.getRelativeResourcePaths()
     * ======================================================================
     */

    /*
     * Prove that, in general, a relative path can be generated for some file in
     * the project folders. Prove that, in particular, we can convert art
     * Resources to a standard relative path representation usable in HTML.
     * FILESYSTEM DEPENDENCIES: This class is named MavenDefeatingFileLoaderTest
     * (and it exists). NO DEPENDENCY ON: property testing.art.sourceFolder
     */
    @Test
    public void testGetRelativeResourcePaths_PositivePath() throws Exception
    {
        Resource thisClassesSourceFile = MavenDefeatingFileLoader
            .getResource("MavenDefeatingFileLoaderTest.java");
        ArrayList<Resource> files = new ArrayList<Resource>(1);
        files.add(thisClassesSourceFile);

        // When only the file name is specified, and the root folder is the
        // folder to
        // relate against, return only the file name.
        Assertions.assertThat(
            MavenDefeatingFileLoader.getRelativeResourcePaths(
                files, "src/test/java/kflory/web/portfolio/utility"
            ).get(0)
        ).isEqualTo(
            "src/test/java/kflory/web/portfolio/utility/MavenDefeatingFileLoaderTest.java"
        );
    }

    /*
     * Show how various combinations of path information affect which relative
     * path we receive.
     * 
     * Strictly speaking, this is a test integrating file --loading-- using
     * relative paths as input, and creation of relative paths using those
     * loaded files.
     * 
     * Note: At present, there is no way to get a path relative to the working
     * directory.
     * 
     * FILESYSTEM DEPENDENCIES: This class is named MavenDefeatingFileLoaderTest
     * (and it exists); it's located in the source folder/package
     * 'src/test/java/kflory/web/portfolio/utility'. NO DEPENDENCY ON: property
     * testing.art.sourceFolder
     */
    @Test
    public void demonstrateUsageOf_getRelativeResourcePaths_overMutiplePaths()
            throws Exception
    {
        Resource thisClassesSourceFile = MavenDefeatingFileLoader
            .getResource("MavenDefeatingFileLoaderTest.java");
        ArrayList<Resource> files = new ArrayList<Resource>(1);
        files.add(thisClassesSourceFile);

        // When only the file name is specified, and the folder to relate to is
        // this
        // folder's parent, return the relative path against that parent folder.
        Assertions.assertThat(
            MavenDefeatingFileLoader.getRelativeResourcePaths(
                files, "src/test/java/kflory/web/portfolio/utility"
            ).get(0)
        ).isEqualTo(
            "src/test/java/kflory/web/portfolio/utility/MavenDefeatingFileLoaderTest.java"
        );

        // When file name is specified, and the folder to relate to is some
        // ancestor,
        // return the relative path against that ancestor folder.
        Resource thisClassesSourceWithAncestorFolders = MavenDefeatingFileLoader
            .getResource("MavenDefeatingFileLoaderTest.java");
        files.clear();
        files.add(thisClassesSourceWithAncestorFolders);
        Assertions.assertThat(
            MavenDefeatingFileLoader.getRelativeResourcePaths(files, "test")
                .get(0)
        ).isEqualTo(
            "test/java/kflory/web/portfolio/utility/MavenDefeatingFileLoaderTest.java"
        );
    }

    @Test
    public void demonstrateUsageOf_getRelativeResourcePaths_noSharedPath()
            throws Exception
    {
        ArrayList<Resource> files = new ArrayList<Resource>(1);
        Resource thisClassesClassFileWithAncestorFolders = MavenDefeatingFileLoader
            .getResource(
                "/web/portfolio/utility/MavenDefeatingFileLoaderTest.class"
            );

        // When there is no shared path between the file and folder, only the
        // filename
        // will be returned.
        files.add(thisClassesClassFileWithAncestorFolders);
        ArrayList<String> fileRelativePaths = MavenDefeatingFileLoader
            .getRelativeResourcePaths(
                files, "src/test/java/kflory/web/portfolio/utility"
            );
        // All class files are in the target folder, not in src.
        Assertions.assertThat(fileRelativePaths.get(0))
            .isEqualTo("MavenDefeatingFileLoaderTest.class");
    }

    @Test
    public void testEnumerateDescendantFilesAnd_GetRelativeResourcePaths_AgainstEachOther() throws IOException
    {
        String[] validPictureTypes = {".jpg", ".png", ".jpeg"};
        
        // Note that deepPathToArt restricts search to .../images/art.
        ArrayList<Resource> resources = MavenDefeatingFileLoader.enumerateDescendantFiles(
            deepPathToArt, validPictureTypes);
        ArrayList<String> relativePaths = MavenDefeatingFileLoader.getRelativeResourcePaths(
                resources, "images");
        
        Assertions.assertThat(resources.size()).isEqualTo(relativePaths.size());
        
        String relativePath = "";
        Resource file = null;
        for (int sharedCounter = 0; sharedCounter < resources.size(); sharedCounter++)
        {
            relativePath = relativePaths.get(sharedCounter);
            file = resources.get(sharedCounter);
            
            // Filenames are same
            Assertions.assertThat(
                file.getFile().getName())
                .isEqualTo(relativePath.substring(relativePath.lastIndexOf('/') + 1));
            
            // Image relative to images/art; while "images" was requested as the format path, the files were all pulled from .../images/art
            Assertions.assertThat(
                relativePath.startsWith("images/art"));     
        }
    }
}
