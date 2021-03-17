package kflory.web.portfolio.images;

public class Image {
	/**
	 * This path points to a resource image--specifically, the path should be relative to the art.relativeResourceFolder property. There are NO guarantees as to whether the path actually exists or holds a usable image.
	 */
	private String relativeResourcePath;
	
	public Image() 
	{
		this.relativeResourcePath = "";
	}
	
	public Image(String pathStringToSet)
	{
		this.relativeResourcePath = pathStringToSet;
	}
	
	/**
	 * No check is performed on whether the supplied path is relative to the art.relativeResourceFolder property.
	 * No check is performed on whether there's actually an image at the specified location. Instead, the runtime resource loader will throw an exception.
	 * @param pathStringToSet The name/path of an image in /resources/static.
	 */
	public void setRelativeResourcePath(String pathStringToSet) 
	{
		this.relativeResourcePath = pathStringToSet;
	}
	
	public String getRelativeResourcePath()
	{
		return relativeResourcePath;
	}
}
