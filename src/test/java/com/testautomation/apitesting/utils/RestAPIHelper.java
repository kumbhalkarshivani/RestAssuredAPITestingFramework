package com.testautomation.apitesting.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.listener.RestAssuredListener;

import static io.restassured.RestAssured.*;

import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class RestAPIHelper {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private  BookingResponse bookingResponse;
	private  Booking booking;
	public Response response;
	String token;
	String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),
			"UTF-8");

	public RestAPIHelper() throws IOException {
	}

	public  void setBookingResponse(BookingResponse bookingResponse) {
		this.bookingResponse = bookingResponse;
	}

	public BookingResponse getBookingResponse() {
		return bookingResponse;
	}
	public  Booking getBooking() {
		return booking;
	}

	public  void setBooking(Booking booking) {
		this.booking = booking;
	}

	public void sendRequest(String request, String method, int... bookingid) throws JsonProcessingException {
		baseURI = "https://restful-booker.herokuapp.com";
		token = getAuthToken();

		RequestSpecification requestSpecification = given()
					.filter(new RestAssuredListener())
					.contentType(ContentType.JSON)
					.header("Cookie","token="+token) // generating auth token
					.body(request);
		if(bookingid.length == 0){
			this.getResponse(requestSpecification,method);
		}else {
			this.getResponse(requestSpecification,method,bookingid[0]);
		}
	}
	public void getResponse(RequestSpecification requestSpecification, String method, int... id) throws JsonProcessingException {

		int bookingid = 0;
		if (id.length != 0) {
			bookingid = id[0];
		}

		switch (method){
			case "POST":
				response = requestSpecification.when().post("/booking").then().extract().response();
				this.bookingResponse = objectMapper.readValue(response.body().prettyPrint(),BookingResponse.class);
				break;
			case "PUT":
				response = requestSpecification.when().put("/booking/{bookingid}",bookingid).then().extract().response();
				this.booking = objectMapper.readValue(response.body().prettyPrint(),Booking.class);
				break;
			case "PATCH":
				response = requestSpecification.when().patch("/booking/{bookingid}",bookingid).then().extract().response();
				this.booking = objectMapper.readValue(response.body().prettyPrint(),Booking.class);
				break;
			case "DELETE":
				response = requestSpecification.when().delete("/booking/{bookingid}",bookingid).then().extract().response();
				break;
			default:
				response = requestSpecification.when().get("/booking/{bookingid}",bookingid).then().extract().response();
				this.booking = objectMapper.readValue(response.body().prettyPrint(),Booking.class);
		}
	}
	public String getAuthToken(){
		Response response = given()
					.filter(new RestAssuredListener())
					.contentType(ContentType.JSON)
					.body(tokenAPIRequestBody)
				.when()
					.post("/auth")
				.then()
					.extract().response();
		String token = JsonPath.read(response.body().asString(), "$.token");
		System.out.println("Token Id : " + token);
		return token;
	}

}
