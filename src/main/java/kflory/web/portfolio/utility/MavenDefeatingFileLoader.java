package kflory.web.portfolio.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/*
 * I'm not able to use a ResourceLoader to access most files using relative paths of form "/x/y/z.*" or "x/y/z.*". This class encapsulates the most consistent way I've found as a bean.
 * 
 * Notes on the underlying issue:
 *  - Maven is known to make loading from the classpath at runtime difficult. In my case, it looks like it's attempting to search using /target as the classpath root, but even there, results are unexplainably odd; attempts to get at, for example, "classpath:**\/*.*" has at times returned only four or five files.
 *  - Most ResourceLoaders/ApplicationContexts I've tried--whether autowired from the container or initialized with new()--don't seem able to load unqualified relative paths (like 'resources/static/KFlory.resume.pdf') or unqualified absolute paths such as '/resources/static/KFlory.resume.pdf'. (
 *  - Part of the reason the context loads are failing may be that different contexts have different roots; in particular, I'm already having trouble telling which contexts I should expect in tests vs. production. See note in API for ContextResource.getPathWithinContext(): "This is typically path relative to a context-specific root directory, e.g. a ServletContext root or a PortletContext root."
 *  - This is only true in Java code. I'm not sure what mechanism is used in, say, JSP, but using paths of form /resource/static/\<imagename\> works just fine. Interestingly, this also holds true for the PropertySource annotation. 
 * @author kflory
 *
 */
public class MavenDefeatingFileLoader {
	
	// Note to self: While I've tried autowiring a number of *Contexts, I've only had luck with an explicitly allocated FileSystemXmlApplicationContext.
	//  One of the problems with autowiring is that I might need access from different types of test (full context available on down to unit); at the very least, since I was originally trying to develope using a test/code cycle, it was playing merry hob with telling what was wrong.
	private static FileSystemXmlApplicationContext fsxac;
	static {
		fsxac = new FileSystemXmlApplicationContext();
	}
	
	//I abhor having to do things like this, but even FileSystemXmlApplicationContext.getResource() isn't returning a resource for project-relative paths on known-good resources such as "resources/static/KFlory.Resume.pdf".  
	//Thus, the use of getResources() on a single file.
	public static Resource getResource(String relativePath) throws IOException
	{
		if (relativePath == null || relativePath.isEmpty())
		{
			throw new IOException("The filepath requested from the Maven-defeating file loader was blank or null.");
		}
		
		String globbedPath = globulizePath(relativePath);
		Resource[] returnedResources = fsxac.getResources(globbedPath);
		if (returnedResources.length == 0)
		{
			throw new FileNotFoundException("The Maven-defeating file loader could not find a filesystem entity at " + relativePath);
		}
		if (returnedResources.length > 1)
		{
				throw new IOException("The Maven-defeating file loader found too many files matching " + relativePath);
		}
		
		Resource atPath = returnedResources[0];
		if (!atPath.getFile().isFile())
		{
			throw new FileNotFoundException("The entity at relative path " + relativePath + 
					" (canonical path " + atPath.getFile().getCanonicalPath() +
					") exists, but is not a file.");
		}			
		if (!atPath.getFile().canRead())
		{
			throw new IOException("The file at relative path " + relativePath + 
					" (canonical path " + atPath.getFile().getCanonicalPath() +
					") exists, but is not readable.");
		}
		
		return atPath;
	}
	
	private static String globulizePath(String toBeGlobbed)
	{
		String globPattern = "";
		if (!toBeGlobbed.startsWith("/"))
		{
			toBeGlobbed = "/" + toBeGlobbed;
		}
		
		return "**" + toBeGlobbed;

	}
	public static ArrayList<Resource> enumerateDescendantFiles(String root, String[] filetypes) throws IOException
	{
		ArrayList<Resource> artResources = new ArrayList<Resource>(filetypes.length);
		
		if (!root.endsWith("/")) {
			root += "/";
		}
		
		String globPattern = globulizePath(root + "**/*");
		for (String filetype : filetypes) {
			artResources.addAll(
				new ArrayList<Resource>(
					Arrays.asList(
						fsxac.getResources(globPattern + filetype))));
		}
		
		return artResources;
	}
}
