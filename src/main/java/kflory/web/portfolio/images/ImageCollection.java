package kflory.web.portfolio.images;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
/**
 * I want to collect a set of images from the filesystem on startup, index them arbitrarily, and then use a URL or cookie fetch scheme to let a site user page through the set of images.
 * This is the first step--providing a 'wireframe' object.
 * @author kflory
 *
 */
@Component
@Scope("singleton")
public class ImageCollection {
	
	@Value("${art.rowCount}")
	private int rowCount;
	
	@Value("${art.colCount}")
	private int colCount;
	
	private static ArrayList<Image> allImages;
	static {
		allImages = new ArrayList<Image>();
	}
	
	public ImageCollection() {
		
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
				ArrayList<Image> contentsOfThisRow = getImages(startingFrom, startingFrom+colCount);
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
	
	public void addImage(Image toAdd) {
		allImages.add(toAdd);
	}
	
	public void clear()
	{
		allImages.clear();
	}
}
