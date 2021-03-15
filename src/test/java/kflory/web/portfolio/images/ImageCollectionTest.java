package kflory.web.portfolio.images;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

public class ImageCollectionTest {
	ImageCollection ic;
	
	@Value("${art.rowCount}")
	private int rowCount;
	
	@Value("${art.colCount}")
	private int colCount;
	
	@BeforeEach
	public void initImages() {
		ic = new ImageCollection();
		ic.addImage(new Image("/art/20140516-2.jpeg"));
		ic.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ic.addImage(new Image("/art/20140526-wildwood-5.jpg"));
	}
	
	@AfterEach
	public void clearImages() {
		ic.clear();
	}
	
	@Test
	public void testIndexStart() {
		assertThat(ic.getImages(-1, -2)).hasSize(0);
		assertThat(ic.getImages(0, 0)).hasSize(1);
		//Currently, collection returns a static 3 images.
		assertThat(ic.getImages(0, 1)).hasSize(2);
		assertThat(ic.getImages(0, 5)).hasSize(3);
		assertThat(ic.getImages(-5, 80)).hasSize(3);
		//Verify that all elements have SOME path set.
		assertThat(ic.getImages(0, 2))
			.allMatch(img -> img.getPath().length() > 0);
	}
	
	@Test
	public void testIndexSafety() {
		assertThat(ic.getImages(-1, -2)).hasSize(0);
		assertThat(ic.getImages(0, 0)).hasSize(1);
		//Currently, collection returns a static 3 images.
		assertThat(ic.getImages(0, 1)).hasSize(2);
		assertThat(ic.getImages(0, 5)).hasSize(3);
		assertThat(ic.getImages(-5, 80)).hasSize(3);
		//Verify that all elements have SOME path set.
		assertThat(ic.getImages(0, 2))
			.allMatch(img -> img.getPath().length() > 0);
	}
	
	@Test
	public void testArrayCorrectness() {
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
	}
}
