package kflory.web.portfolio.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/*
 * I'm not able to use a generic, injected ResourceLoader to access most files using relative paths of form "/x/y/z.*" or "x/y/z.*", even for some one-step paths. This class encapsulates the most consistent way I've found.
 * 
 * Notes on the underlying issue:
 *  - I've verified that (at least during unit testing) files are being pulled from the project folder. (I haven't verified that a file can be pulled directly from the project root, nor that files at a level above the project root are unavailable.) 
 *  - Most ResourceLoaders/ApplicationContexts I've tried--whether autowired from the container or initialized with new()--don't seem able to load unqualified relative paths (like 'resources/static/KFlory.resume.pdf') or unqualified absolute paths such as '/resources/static/KFlory.resume.pdf'. (And I've noticed that specifying a prefix ("file:", "class:", etc.) gives unpredictable results, possibly because of trouble telling which ResourceLoader was actually being injected and/or used.) 
 *  - Part of the reason the context loads are failing --may-- be that different contexts may have different roots See note in API for ContextResource.getPathWithinContext(): "This is typically path relative to a context-specific root directory, e.g. a ServletContext root or a PortletContext root."
 *  - This is only true in Java code. I'm not sure what mechanism is used in, say, JSP, but using paths of form /images/\<imagename\> works just fine. Interestingly, this also holds true for file paths in the PropertySource annotation. I'm not sure exactly how image (and other resource) loading works on local vs. how it works when running from WAR (that is, I'm not sure just how much I could vary from the paths that are known to work.)
 * @author kflory
 *
 */
public class MavenDefeatingFileLoader {
	
	// Note to self: While I've tried autowiring a number of *Contexts, I've only had luck with an explicitly allocated FileSystemXmlApplicationContext.
	//  One of the problems with autowiring is that I might need access from different types of test (from SpringBootTest to unit.)
	private static FileSystemXmlApplicationContext fsxac;
	static {
		fsxac = new FileSystemXmlApplicationContext();
	}

	/**
	 * Since getResource() and enumerateDescendantFiles() must work even if no path information is provided, glob the search path so that it includes all folders between the filename and the working directory/target directory root.   
	 * @param toBeGlobbed
	 * @return
	 */
	private static String globToAllowResultsFromAnyFolder(String toBeGlobbed)
	{	
		return "**" + 
			(toBeGlobbed.startsWith("/") ? "" : "/") +
			toBeGlobbed;
	}
	
	/**
	 * For every filetype string in the passed array, get a Resource object for every file of that type which can be reached from the given root.
	 * 
	 * @param root The root to search from. This should be a relative path; it will be tokenized into path steps based on '/'. Note that '/' can but need not be included at the start of the string, and the end of the string. If the root is the empty string or '/', the runtime root will be used, which has the effect of searching the entire project for the given filetypes.
	 * @param filetypes An array of filetypes to look for. (This assumes that your files are named with a . extension, although not restricted to 3-character extensions.) --NO-- checking is performed on this parameter or its members.
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Resource> enumerateDescendantFiles(String root, String[] filetypes) throws IOException
	{
		ArrayList<Resource> artResources = new ArrayList<Resource>(filetypes.length);
		
		String globPattern = globToAllowResultsFromAnyFolder(root) + 
				(root.endsWith("/") ? "" : "/") + 
				"**";
		for (String filetype : filetypes) {
		    String toSearchFor = globPattern + "/*" + filetype;
			artResources.addAll(
				new ArrayList<Resource>(
					Arrays.asList(
						fsxac.getResources(toSearchFor))));
		}
		
		return artResources;
	}
	
	//FileSystemXmlApplicationContext.getResource() isn't returning a resource for project-relative paths on known-good resources such as static/KFlory.Resume.pdf (in the WAR) or resources/static/KFlory.Resume.pdf (in the file system).  
	//Thus, the use of getResources() with a glob pattern on a single file.
	//Note that the resource root (the working directory) CANNOT be loaded as a Spring resource.
	/**
	 * Base handling shared by getResource() (file) and getFolderResource() (folder).
	 * @param relativePath The path to fetch. This must be ended with '/' if the resource to fetch is a folder; otherwise, this method wouldn't be able to differentiate the two.
	 * @return
	 * @throws IOException
	 */
	private static Resource getFileOrFolderResource(String relativePath) throws IOException
	{
		if (relativePath == null || relativePath.isEmpty())
		{
			throw new IOException("The path requested from the Maven-defeating file loader was null or empty.");
		}
		
		//Apply a path glob ONLY if we're searching for a file. If searching for a folder, adding a directory glob at the front of the search string would produce far too many results, but an unglobbed search will produce one unique result per folder matching the given path fragment.
		if (!relativePath.endsWith("/"))
		{
			relativePath = globToAllowResultsFromAnyFolder(relativePath);
		}
		// Otherwise, if this is a folder resource, prepend '/'.
		else 
		{
			relativePath = (relativePath.startsWith("/") ? "" : "/") + relativePath;
		}
		
		Resource[] returnedResources = fsxac.getResources(relativePath);
		if (returnedResources.length == 0)
		{
			throw new FileNotFoundException("The Maven-defeating file loader could not find a filesystem entity with requested relative path >>" + relativePath + "<<.");
		}
		if (returnedResources.length > 1)
		{
				throw new IOException("The Maven-defeating file loader found too many filesystem entities matching " +
						"requested relative path >>" + relativePath + "<<; " +
						"the first two are >>" + returnedResources[0].getFile().getPath() + "<< and " +
						">>" + returnedResources[1].getFile().getPath() + "<<.");
		}
		
		Resource atPath = returnedResources[0];
		return atPath;
	}
	
	//FileSystemXmlApplicationContext.getResource() isn't returning a resource for project-relative paths on known-good resources such as static/KFlory.Resume.pdf (in the WAR) or resources/static/KFlory.Resume.pdf (in the file system).  
	//Thus, this method internally uses getResources() instead.
	public static Resource getResource(String relativePath) throws IOException
	{
		Resource atPath = getFileOrFolderResource(relativePath);
		if (!atPath.getFile().isFile())
		{
			throw new FileNotFoundException("The filesystem entity at relative path >>" + relativePath + 
					"<< (canonical path >>" + atPath.getFile().getCanonicalPath() +
					"<<) exists, but is not a file.");
		}			
		if (!atPath.getFile().canRead())
		{
			throw new IOException("The file at relative path >>" + relativePath + 
					"<< (canonical path >>" + atPath.getFile().getCanonicalPath() +
					"<<) exists, but is not readable.");
		}
		
		return atPath;
	}
	
	public static Resource getFolderResource(String relativePath) throws IOException
	{
		if (relativePath != null && !relativePath.endsWith("/")) 
		{
			relativePath += "/";
		}
		
		Resource atPath = getFileOrFolderResource(relativePath);
		// It seems that, when a single-step folder is requested and it's not a direct child of the working directory/WAR root, getResources() will return a non-existent file whose path is (root) + (step).
		// Thus, test first for existence!
		if (!atPath.getFile().exists())
		{
			// Secondary strategy: Try it with a prepended glob...
			atPath = getFileOrFolderResource("**/" + relativePath);
		}
		
		if (!atPath.getFile().isDirectory())
		{
			throw new FileNotFoundException("The filesystem entity at relative path >>" + relativePath + 
					"<< (canonical path >>" + atPath.getFile().getCanonicalPath() +
					"<<) exists, but is not a directory.");
		}		
		
		return atPath;
	}

	/**
	 * HTML URIs need to use paths relative to their web content root, not system paths. Most of my Resource use cases actually involve such resources, loaded as a resource, but invoked in display using only the relative path of that resource. 
	 * NOTE: Just judging from my WAR's layout, it looks like (project root)/src/main/resources/static/**\/*.* maps to (WAR root)/WEB-INF/classes/static, with (project root)/src/main/resources/*.properties mapping to (WAR root)/WEB-INF/classes/*.properties.  
	 * 
	 * @return
	 */
	public static ArrayList<String> getRelativeResourcePaths(ArrayList<Resource> resources, String relativePathOfFolderToRootFrom) throws IOException
	{
		ArrayList<String> relativePaths = new ArrayList<String>(resources.size());
		
		for (Resource file : resources)
		{
			String path = file.getFile().getPath();
			// If these paths used the Windows separator, change to the Unix path separator. 
			path = path.replace('\\', '/');
			
			int relativeRootLocation = path.indexOf(relativePathOfFolderToRootFrom);
			// Only add a relative path if the requested root is in the resource's system path.
			if (relativeRootLocation > 0)
			{
				// Use the relative root path as a mask onto the full file path.
				path = path.substring(relativeRootLocation);
			}
			// Otherwise, add ONLY the resource's filename without any path steps.
			else
			{
				path = file.getFilename();
			}
			relativePaths.add(path);
		}
		
		return relativePaths;
	}
}
