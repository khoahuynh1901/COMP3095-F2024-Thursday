package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

//Tell SpringBoot to look for main configuration class (@SpringBoot application)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
	//this annotation is used in combination with TestContainer to automatically configure the connection
	//the test MongoDBContainer
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@LocalServerPort
	private Integer port;
	//http://localhost:port/api/product


	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost:";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void createProductTest(){
		String requestBody = """
				{
					"name" : "Samsung Tv",
					"description" : "Samsung TV 60inch Retina display",
					"price" : "2000"
				}
				""";


		//BDD -0 Behavioural Driven Development (Given, When , Then)
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Samsung Tv"))
				.body("description", Matchers.equalTo("Samsung TV 60inch display"))
				.body("price", Matchers.equalTo("2000"));
	}


	@Test
	void getAllProductsTest(){

		String requestBody = """
				{
					"name" : "Samsung Tv",
					"description" : "Samsung TV 60inch Retina display",
					"price" : "2000"
				}
				""";
		//BDD -0 Behavioural Driven Development (Given, When , Then)
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Samsung Tv"))
				.body("description", Matchers.equalTo("Samsung TV 60inch display"))
				.body("price", Matchers.equalTo("2000"));

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/product")
				.then()
				.log().all()
				.statusCode(200)
				.body("size()", Matchers.greaterThan(0))
				.body("[0].name", Matchers.equalTo("Samsung Tv"))
				.body("[0].description", Matchers.equalTo("Samsung TV 60inch display"))
				.body("[0].price", Matchers.equalTo("2000"));

	}
}
