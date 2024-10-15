package Restful.Booker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtils {

    private static final String BASE_URL = "https://restful-booker.herokuapp.com";

    public static RequestSpecification getRequestSpec(){
        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .log().all();
    }

    public static Response createBooking(String bookingPayload){
        return getRequestSpec()
                .body(bookingPayload)
                .post("/booking")
                .then().log().all()
                .extract().response();
    }

    public static Response getBookingByID(int bookingId){
        return getRequestSpec()
                .get("/booking/" + bookingId)
                .then().log().all()
                .extract().response();
    }

    public static Response updateBooking(int bookingId, String authToken, String bookingPayload){
        return getRequestSpec()
                .header("Cookie", "token=" + authToken)
                .body(bookingPayload)
                .put("/booking/" + bookingId)
                .then().log().all()
                .extract().response();
    }

    public static Response deleteBooking(int bookingId, String authToken){
        return getRequestSpec()
                .header("Cookie", "token=" + authToken)
                .delete("/booking/" + bookingId)
                .then().log().all()
                .extract().response();
    }

    public static Response authenticate(String authPayload){
        return getRequestSpec()
                .body(authPayload)
                .post("/auth")
                .then().log().all()
                .extract().response();
    }

}
