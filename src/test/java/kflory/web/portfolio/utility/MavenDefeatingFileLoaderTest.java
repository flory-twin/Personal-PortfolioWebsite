package kflory.web.portfolio.utility;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.assertj.core.api.Assertions;

/*
 * Note: I realize it's poor practice to couple a unit test to the file system. Unfortunately, I can only seem to get Spring's relative-path ResourceLoader loading to work in highly specific ways on my system.
 * This was frustrating to solve and is a fragile solution, so a tightly coupled test that tells me when my loader is failing is still very valuable.
 */
@SpringBootTest
public class MavenDefeatingFileLoaderTest {
	//During testing, the source folder resource location should be used--not the WAR location.
	@Value("${testing.art.sourceFolder}")
	private String pathToArt;
	
	@Test
	public void testExceptionsTo_getResource() {
		Assertions.assertThatIOException()
		.isThrownBy(
				() -> MavenDefeatingFileLoader.getResource(null))
		.withMessage("The filepath requested from the Maven-defeating file loader was blank or null.");
		
		//Multiple files match path
		Assertions.assertThatIOException()
		.isThrownBy(
				() -> MavenDefeatingFileLoader.getResource("*.class"))
		.withMessageContaining("The Maven-defeating file loader found too many files matching");
		
		//Nothing with such a path exists
		Assertions.assertThatExceptionOfType(FileNotFoundException.class)
		.isThrownBy(
				() -> MavenDefeatingFileLoader.getResource("horripilationMongooseFarm.fuz"))
		.withMessageContaining("The Maven-defeating file loader could not find a filesystem entity");
		
		//Is a directory, not a file
		Assertions.assertThatExceptionOfType(FileNotFoundException.class)
		.isThrownBy(
				() -> MavenDefeatingFileLoader.getResource("main/resources"))
		.withMessageContaining("exists, but is not a file");	
		
		//TODO: Test isReadable at some point...?
	}
	
	@Test
	public void testPositivePathOf_getResource() throws IOException{
		Resource r = MavenDefeatingFileLoader.getResource("MavenDefeatingFileLoaderTest.class");
		Assertions.assertThat(r).isInstanceOf(Resource.class);
		Assertions.assertThat(r.contentLength()).isGreaterThan(0L);
	}
	
	@Test
	public void testExceptionsAndCornerConditionsOf_EnumerateDescendantFiles() throws Exception{
		//Null path string: Unhandled
		Assertions.assertThatExceptionOfType(Exception.class)
		.isThrownBy(
				() -> MavenDefeatingFileLoader.enumerateDescendantFiles(null, new String[0]));
		
		//Path exists, no filetypes provided: No exceptions, 0 results
		Assertions.assertThat(
			MavenDefeatingFileLoader.enumerateDescendantFiles("resources", new String[0])
			.size())
		.isEqualTo(0);
		
		String[] validFiletype = {".jsp"}; 
		//Overspecified path does not exist: No exceptions, 0 results
		Assertions.assertThat(
				MavenDefeatingFileLoader.enumerateDescendantFiles("resources/mongooses/mongeese/whatever", validFiletype)
				.size())
			.isEqualTo(0);
		
		//Valid path, no file extensions: No exceptions, 0 results
		Assertions.assertThat(
				MavenDefeatingFileLoader.enumerateDescendantFiles("resources", new String[0])
				.size())
			.isEqualTo(0);
	}
	
	@Test
	public void testPositivePath_EnumerateDescendantFiles_canGetFilesFromMultipleLevels() throws Exception
	{ 
		String[] singleValidPictureType = {".jpg"};
		
		ArrayList<Resource> resources = MavenDefeatingFileLoader.enumerateDescendantFiles(pathToArt, singleValidPictureType);
		Assertions.assertThat(resources.size())
			.isGreaterThan(0);
		int priorCountOfFiles = resources.size();
		String[] pathSteps = pathToArt.split("/");
		
		if (pathSteps.length > 0)
		{
			for (int pathSepCount = pathSteps.length - 1; pathSepCount > 0; pathSepCount-- )
			{
				String lessSpecificPath = "";
				for (int stepNo = 0; stepNo < pathSepCount; stepNo++)
				{
					lessSpecificPath += pathSteps[stepNo] + "/";
				}
				
				resources = MavenDefeatingFileLoader.enumerateDescendantFiles(lessSpecificPath, singleValidPictureType);
				Assertions.assertThat(resources.size())
					.isGreaterThanOrEqualTo(priorCountOfFiles);
				priorCountOfFiles = resources.size();
			}
		}
	}
	
	@Test
	public void testPositivePath_EnumerateDescendantFiles_showingPathSpecificity() throws Exception{
		String[] singleValidPictureType = {".jpg"};
		
		ArrayList<Resource> resources = MavenDefeatingFileLoader.enumerateDescendantFiles(pathToArt, singleValidPictureType);
		Assertions.assertThat(resources.size())
			.isGreaterThan(0);
		int priorCountOfFiles = resources.size();
		
		resources = MavenDefeatingFileLoader.enumerateDescendantFiles(pathToArt, singleValidPictureType);
		Assertions.assertThat(resources.size())
			.isGreaterThanOrEqualTo(priorCountOfFiles);
		priorCountOfFiles = resources.size();
	
		resources = MavenDefeatingFileLoader.enumerateDescendantFiles(pathToArt, singleValidPictureType);
		Assertions.assertThat(resources.size())
			.isGreaterThanOrEqualTo(priorCountOfFiles);
		priorCountOfFiles = resources.size();

		resources = MavenDefeatingFileLoader.enumerateDescendantFiles("", singleValidPictureType);
		Assertions.assertThat(resources.size())
			.isGreaterThanOrEqualTo(priorCountOfFiles);
	}
	
	@Test
	public void testPositivePath_EnumerateDescendantFiles_numberOfFileTypes() throws Exception{
		String[] nonexistentFileType = {".fuz"}; 
		String[] multipleValidFileTypes = {".jpeg", ".jpg", ".png"}; 
		String[] singleValidPictureType = {".jpg"};
		
		//Valid path, no file types match: No exceptions, 0 results
		Assertions.assertThat(
				MavenDefeatingFileLoader.enumerateDescendantFiles("", nonexistentFileType)
				.size())
			.isEqualTo(0);

		ArrayList<Resource> resources = MavenDefeatingFileLoader.enumerateDescendantFiles("", singleValidPictureType);
		Assertions.assertThat(resources.size())
			.isGreaterThan(0);
		
		//Valid path, multiple file types match: No exceptions, more results than earlier
		Assertions.assertThat(
				MavenDefeatingFileLoader.enumerateDescendantFiles("", multipleValidFileTypes)
				.size())
			.isGreaterThan(resources.size());
	}
}
