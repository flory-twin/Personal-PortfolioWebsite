package kflory.web.portfolio.images;

public class Image {
	/**
	 * This path points to a resource image--specifically, the path is relative to /resources/static/art. There are NO guarantees as to whether the path actually exists or holds a usable image.
	 */
	private String path;
	
	public Image() 
	{
		this.path = "";
	}
	
	public Image(String pathStringToSet)
	{
		this.path = pathStringToSet;
	}
	/**
	 * No check is performed on whether there's actually an image at the specified location. Instead, the runtime resource loader will throw an exception.
	 * @param pathStringToSet The name/path of an image in /resources/static.
	 */
	public void setPath(String pathStringToSet) 
	{
		this.path = pathStringToSet;
	}
	
	public String getPath()
	{
		return path;
	}
}
