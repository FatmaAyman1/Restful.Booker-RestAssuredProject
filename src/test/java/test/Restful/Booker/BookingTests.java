package test.Restful.Booker;

import Restful.Booker.RestUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BookingTests {

    private String authToken;

    @BeforeClass
    public void authenticate(){

        String authPayload =  "{\n" +
                "    \"username\": \"admin\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";
        Response authResponse = RestUtils.authenticate(authPayload);
        authToken = authResponse.jsonPath().getString("token");

        Assert.assertNotNull(authToken, "Authentication token is null");
        Assert.assertEquals(authResponse.statusCode(), 200, "Expected status code 200");

    }

    @Test (priority = 1)
    public void testCreateBooking(){
        String bookingPayload = "{\n" +
                "    \"firstname\": \"Fatma\",\n" +
                "    \"lastname\": \"Ayman\",\n" +
                "    \"totalprice\": 150,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2023-10-01\",\n" +
                "        \"checkout\": \"2023-10-10\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        Response createResponse = RestUtils.createBooking(bookingPayload);
        Assert.assertEquals(createResponse.statusCode(), 200);

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        Assert.assertTrue(bookingId > 0, "Booking ID is invalid");
    }

    @Test (priority = 2)
    public void testGetBooking(){
        int bookingId = 1;
        Response response = RestUtils.getBookingByID(bookingId);

        Reporter.log("Response for booking ID " + bookingId + ": " + response.asString(), true);

        Assert.assertEquals(response.statusCode(), 200);
        String firstname = response.jsonPath().getString("firstname");
        Assert.assertNotNull(firstname, "Firstname cannot be null");
    }

    @Test (priority = 3)
    public void testUpdateBooking(){
        int bookingId = 1005;
        String updatePayload = "{\n" +
                "    \"firstname\": \"Ayman\",\n" +
                "    \"lastname\": \"Abdelaziz\",\n" +
                "    \"totalprice\": 170,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2024-10-01\",\n" +
                "        \"checkout\": \"2024-10-10\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Dinner\"\n" +
                "}";

        Response updateResponse = RestUtils.updateBooking(bookingId, authToken, updatePayload);
        Assert.assertEquals(updateResponse.statusCode(), 200);
        String updatedFirstname = updateResponse.jsonPath().getString("firstname");
        Assert.assertEquals(updatedFirstname, "Ayman");
    }

    @Test (priority = 4)
    public void testDeleteBooking(){
        int bookingId = 1212;
        Response deleteResponse = RestUtils.deleteBooking(bookingId, authToken);
        Assert.assertEquals(deleteResponse.statusCode(), 201, "Expected status code 201 Created");
    }
}
