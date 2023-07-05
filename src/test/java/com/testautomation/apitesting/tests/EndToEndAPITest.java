package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import io.restassured.http.Header;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.testautomation.apitesting.utils.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.testautomation.apitesting.utils.RestAPIHelper;
import com.testautomation.apitesting.utils.FileNameConstants;

public class EndToEndAPITest extends BaseTest {

	private static final Logger logger = LogManager.getLogger(EndToEndAPITest.class);
	int bookingId;
	RestAPIHelper helper;

	String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),
			"UTF-8");
	String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),
			"UTF-8");
	String putAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),
			"UTF-8");
	String patchAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),
			"UTF-8");

	public EndToEndAPITest() throws IOException {
		logger.info("e2eAPIRequest test execution started...");
		helper = new RestAPIHelper();
	}

	@Test
	public void postRequestTest() throws JsonProcessingException {
		// post api call
		Header header = new Header("Content-Type","application/json");
		helper.sendRequest(postAPIRequestBody,"POST");
		Assert.assertEquals(helper.getBookingResponse().getBooking().getFirstname(), "api testing");
		bookingId = helper.getBookingResponse().getBookingid();
	}
	@Test (dependsOnMethods = "postRequestTest")
	public void getRequestTest() throws JsonProcessingException {
		// get api call
		Header header = new Header("Content-Type","application/json");
		helper.sendRequest("","GET",bookingId);
		Assert.assertEquals(helper.getBooking().getFirstname(), "api testing");
	}
	@Test (dependsOnMethods = "getRequestTest")
	public void putRequestTest() throws JsonProcessingException {
		// put api call
		Header header = new Header("Content-Type","application/json");
		helper.sendRequest(putAPIRequestBody,"PUT",bookingId);
		Assert.assertEquals(helper.getBooking().getFirstname(), "Specflow");
	}
	@Test (dependsOnMethods = "putRequestTest")
	public void patchRequestTest() throws JsonProcessingException {
		// patch api call
		Header header = new Header("Content-Type","application/json");
		helper.sendRequest(patchAPIRequestBody,"PATCH",bookingId);
		Assert.assertEquals(helper.getBooking().getFirstname(), "Testers Talk");
	}
	@Test (dependsOnMethods = "patchRequestTest")
	public void deleteRequestTest() throws JsonProcessingException {
		// delete api call
		Header header = new Header("Content-Type","application/json");
		helper.sendRequest("","DELETE",bookingId);
		Assert.assertEquals(helper.response.getStatusCode(),201);
	}

}
