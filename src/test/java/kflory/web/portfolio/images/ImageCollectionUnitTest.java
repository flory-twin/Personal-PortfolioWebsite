package kflory.web.portfolio.images;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.TestPropertySource;

/* 
 * Note potential use of:
 * @TestPropertySource(properties = {
	    "art.relativeResourceFolder=resources/static/art/",
	    "art.filetypes=.jpeg,.jpg,.png",
	})
 */
public class ImageCollectionUnitTest {
	ImageCollection ic = new ImageCollection();
	
	public void smallArray() {
		ImageCollection.clear();
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));
	}
	
	public void arrayOf15() {
		ImageCollection.clear();
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));
	}
	
	@Test
	public void testIndexStart() {
		smallArray();
		assertThat(ic.getImages(-1, -2)).hasSize(0);
		assertThat(ic.getImages(0, 0)).hasSize(1);
		//Currently, collection returns a static 3 images.
		assertThat(ic.getImages(0, 1)).hasSize(2);
		assertThat(ic.getImages(0, 5)).hasSize(3);
		assertThat(ic.getImages(-5, 80)).hasSize(3);
		//Verify that all elements have SOME path set.
		assertThat(ic.getImages(0, 2))
			.allMatch(img -> img.getRelativeResourcePath().length() > 0);
	}
	
	@Test
	public void testIndexSafety() {
		smallArray();
		assertThat(ic.getImages(-1, -2)).hasSize(0);
		assertThat(ic.getImages(0, 0)).hasSize(1);
		//Currently, collection returns a static 3 images.
		assertThat(ic.getImages(0, 1)).hasSize(2);
		assertThat(ic.getImages(0, 5)).hasSize(3);
		assertThat(ic.getImages(-5, 80)).hasSize(3);
		//Verify that all elements have SOME path set.
		assertThat(ic.getImages(0, 2))
			.allMatch(img -> img.getRelativeResourcePath().length() > 0);
	}
	
	@Test
	public void testArrayCorrectness() {
		smallArray();
		int rowCount = 3;
		int colCount = 3;
		Image[][] pathsArray = ic.getImageArray(0, rowCount, colCount);
		assertThat(pathsArray.length).isEqualTo(rowCount);
		for(Image[] row : pathsArray){
			assertThat(row.length).isEqualTo(colCount);
		}
		assertThat(pathsArray[0]).allSatisfy(
				img -> assertThat(img).isNotNull());
		assertThat(pathsArray[1]).allSatisfy(
				img -> assertThat(img).isNull());
		assertThat(pathsArray[2]).allSatisfy(
				img -> assertThat(img).isNull());
		
		pathsArray = ic.getImageArray(1, rowCount, colCount);
		assertThat(pathsArray.length).isEqualTo(rowCount);
		for(Image[] row : pathsArray){
			assertThat(row.length).isEqualTo(colCount);
		}
		assertThat(pathsArray[0][0]).isNotNull();
		assertThat(pathsArray[0][1]).isNotNull();
		assertThat(pathsArray[0][2]).isNull();
		assertThat(pathsArray[1]).allSatisfy(
				img -> assertThat(img).isNull());
		assertThat(pathsArray[2]).allSatisfy(
				img -> assertThat(img).isNull());
		
		pathsArray = ic.getImageArray(2, rowCount, colCount);
		assertThat(pathsArray.length).isEqualTo(rowCount);
		for(Image[] row : pathsArray){
			assertThat(row.length).isEqualTo(colCount);
		}
		assertThat(pathsArray[0][0]).isNotNull();
		assertThat(pathsArray[0][1]).isNull();
		assertThat(pathsArray[0][2]).isNull();
		assertThat(pathsArray[1]).allSatisfy(
				img -> assertThat(img).isNull());
		assertThat(pathsArray[2]).allSatisfy(
				img -> assertThat(img).isNull());
		
		pathsArray = ic.getImageArray(3, rowCount, colCount);
		assertThat(pathsArray.length).isEqualTo(rowCount);
		for(Image[] row : pathsArray){
			assertThat(row.length).isEqualTo(colCount);
		}
		assertThat(pathsArray[0]).allSatisfy(
				img -> assertThat(img).isNull());
		assertThat(pathsArray[1]).allSatisfy(
				img -> assertThat(img).isNull());
		assertThat(pathsArray[2]).allSatisfy(
				img -> assertThat(img).isNull());
		
		arrayOf15();
		
		pathsArray = ic.getImageArray(3, rowCount, colCount);
		assertThat(pathsArray.length).isEqualTo(rowCount);
		for(Image[] row : pathsArray){
			assertThat(row.length).isEqualTo(colCount);
		}
		assertThat(pathsArray[0]).allSatisfy(
				img -> assertThat(img).isNotNull());
		assertThat(pathsArray[1]).allSatisfy(
				img -> assertThat(img).isNotNull());
		assertThat(pathsArray[2]).allSatisfy(
				img -> assertThat(img).isNotNull());
	}
	
	//Should I resolve how to configure a FileSystemXmlApplicationContext into the system so that I can use an autowired field to inject a mock, I'd like the following tests at minimum on .loadFromFileSystem(...):
	//if none of type or none at location, none load.
	//if location dne, exception
	//incidental: not-default
}
