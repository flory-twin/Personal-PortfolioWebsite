package kflory.web.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//The following is a meta-annotation combining:
//@Configuration (provides source of bean configuration)
//@EnableAutoConfiguration (look on classpath, ?(project folder), etc. (?) for bean defs/props files/etc.
//@EnableWebMvc
//@ComponentScan: Check for other components/configuration/services in this package 
@SpringBootApplication
public class PortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApplication.class, args);
	}

}
