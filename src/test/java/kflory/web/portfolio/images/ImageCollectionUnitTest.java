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

// Note that none of the following tests actually depend on files existing at these paths, within the project or at any point in the underlying filesystem.
public class ImageCollectionUnitTest {
	ImageCollection ic = new ImageCollection();
	
	public void smallArray() {
		ImageCollection.clear();
		ImageCollection.addImage(new Image("string1"));
		ImageCollection.addImage(new Image("string2"));
		ImageCollection.addImage(new Image("string3"));
	}
	
	public void arrayOf15() {
		ImageCollection.clear();
		ImageCollection.addImage(new Image("string4"));
		ImageCollection.addImage(new Image("string5"));
        ImageCollection.addImage(new Image("string6"));
        ImageCollection.addImage(new Image("string7"));
        ImageCollection.addImage(new Image("string8"));
        ImageCollection.addImage(new Image("string9"));
        ImageCollection.addImage(new Image("string10"));
        ImageCollection.addImage(new Image("string11"));
        ImageCollection.addImage(new Image("string12"));
        ImageCollection.addImage(new Image("string13"));
        ImageCollection.addImage(new Image("string14"));
        ImageCollection.addImage(new Image("string15"));
        ImageCollection.addImage(new Image("string16"));
        ImageCollection.addImage(new Image("string17"));
        ImageCollection.addImage(new Image("string18"));
	}
	
	//Static array, does not use file loading
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
	
	//Static array, does not use file loading
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
	
	//Static array, does not use file loading
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
	
    @Test
    public void testCount() {
        ImageCollection.clear();
        assertThat(ic.count()).isEqualTo(0);
        
        smallArray();
        assertThat(ic.count()).isEqualTo(3);
        
        arrayOf15();
        assertThat(ic.count()).isEqualTo(15);
    }
}
