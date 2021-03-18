package kflory.web.portfolio.images;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Scope;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
/**
 * I want to collect a set of images from the filesystem on startup, index them arbitrarily, and then use a URL or cookie fetch scheme to let a site user page through the set of images.
 * This is the first step--providing a 'wireframe' object.
 * @author kflory
 *
 */
@Component
@Scope("singleton")
public class ImageCollection {
	//Note to self: When running as a test (@SpringBootTest, not unit) the following injectible beans either inject as null or can't find resources on the requested path:
	// ApplicationContext: Autowires as null.
	// FileSystemResourceLoader, FileSystemXmlApplicationContext: Autowires as null.
	// ServletContext, PathMatchingResourcePatternResolver: Resolve resources (.getResource()...) against Maven's runtime classpath (.../target...). Do note that .getRealPath("/resources") seems to work with the ServletContext, which is particularly interesting as it should be a MockServletContext. 
	
	@Value("${art.relativeResourceFolder}")
	private String artDirectoryRelativePath;
	private String artDirectoryTargetRelativePath="art/";
	
	@Value("${art.filetypes}")
	private String filetypesString;
	//Can't use static initializer block to parse filetypes because art.relativeResourceFolder isn't available until after construction.
	private String[] filetypes = null;

	private static ArrayList<Image> allImages;
	static {
		allImages = new ArrayList<Image>();
	}
	
	public ImageCollection() {
	}
	
	@PostConstruct 
	public void loadFromFileSystem() throws FileSystemException, IOException {
		loadFromFileSystem(artDirectoryRelativePath);
	}
	
	/**
     *
	 * @param root A set of folder names separated by the path separator /. / at either end are optional.
	 * @throws FileSystemException
	 * @throws IOException
	 */
	public void loadFromFileSystem(String root) throws FileSystemException, IOException {
		//Because a property must be loaded before filetypes can be parsed--and properties aren't available until after construction--filetypes MUST be parsed before use.
		parseFiletypes();
		
		// Note to self: What with the differing conditions available in test vs. non-test, while I tried using various entities injected by the container, none of them worked reliably (different points of initialization, some weren't initialized/were mocks, etc.)
		// As such, initializing my own instance of a resource loader was far more controllable at this point of expertise.
		FileSystemXmlApplicationContext fsxac = new FileSystemXmlApplicationContext();
		// Despite--or due to--the wide array of path choices Spring allows, and how different ResourceLoaders accept different forms--I had to experiment. 
		// Ultimately, only roots formed as /a/b/c/ will work properly. These then get submitted as shown below.
		String globPattern = "";
		if (!root.startsWith("/"))
		{
			root = "/" + root;
		}
		
		if (!root.endsWith("/")) {
			root += "/";
		}
		
		//Yes--adding a directory glob on front is counterintuitive, but (at least in a test context) necessary.
		globPattern = "**" + root + "**/*";
		
		ArrayList<Resource> artResources = new ArrayList<Resource>(filetypes.length);
		for (String filetype : filetypes) {
			artResources.addAll(
				new ArrayList<Resource>(
					Arrays.asList(
						fsxac.getResources(globPattern + filetype))));
		}
		
		ImageCollection.clear();
		
		//Before continuing, remove the leading / so arbitrary consumers will treat the resulting string like a relative path, not an absolute path.
		root = root.substring(1);
		//Now reformat so the target root can be found in a system-canonical path.
		String targetRootAsCanonicalFragment = this.artDirectoryTargetRelativePath.replace("/", File.separator);
		for (Resource r : artResources)
		{
			String pathString = r.getFile().getPath(); 
			ImageCollection.addImage(
				new Image(
					pathString.substring(
						pathString.indexOf(targetRootAsCanonicalFragment))
					.replace(File.separator, "/")));
		}
	}
	
	/**
	 * Safely get a requested range of images:
	 *   If the first index is less than 0, adjust it up to the first valid index (0).
	 *   If the last index is greater than the list size (minus 1), adjust it down to the last valid index (size() - 1).
	 *   Return empty lists for any case where the last index is less than the first index.
	 * @param firstIndex Any index from 0 to the size of the image list, minus 1. 
	 * @param lastIndex Any index from 0 to the size of the image list, minus 1.  
	 * @return
	 */
	public ArrayList<Image> getImages(int firstIndex, int lastIndex){
		if (firstIndex < 0)
		{
			firstIndex = 0;
		}
		
		if (lastIndex > (allImages.size() - 1))
		{
			lastIndex = allImages.size() - 1;
		}
		
		if (firstIndex <= lastIndex)
		{
			//Oddly, the subList() function wants the first index to be inclusive and the last exclusive.
			return new ArrayList(allImages.subList(firstIndex, lastIndex + 1));
		}
		
		//If we fall through...
		return new ArrayList<Image>();
	}
	
	/**
	 * Return a 'safe' ${rowCount} x ${colCount} array of Images.
	 * The array is 'safe' in the sense that, if the starting index is invalid or not enough Images are available, the result will be padded with Null until it has the expected dimensions. 
	 * @param startingFrom
	 * @return
	 */
	public Image[][] getImageArray(int startingFrom, int rowCount, int colCount){
		Image[][] imageArr = new Image[rowCount][colCount];
		if (rowCount >= 0 && colCount >= 0)
		{
			//Get the next (thumbnailCount) images from the runtime image list, beginning at (startingFrom).
			// At present, these should be divided into three arrays of three each.
			for (int rowNum = 1; rowNum <= rowCount; rowNum++)
			{
				ArrayList<Image> contentsOfThisRow = getImages(startingFrom, startingFrom+(colCount-1));
				//Check whether additional null padding entries are needed; add if necessary.
				while (contentsOfThisRow.size() < colCount) {
					contentsOfThisRow.add(null);
				}
				imageArr[rowNum - 1] = contentsOfThisRow.toArray(new Image[0]);
				
				//Increment so the index matches the first member of the next row.
				startingFrom = startingFrom + colCount;
			}
		}

		return imageArr;
	}
	
	public static void addImage(Image toAdd) {
		allImages.add(toAdd);
	}
	
	public static void clear()
	{
		allImages.clear();
	}
	
	private void parseFiletypes() {
		if (filetypes == null) {
			filetypes = filetypesString.split(",");
		}
	}
}
