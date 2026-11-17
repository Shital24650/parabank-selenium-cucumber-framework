package unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;
import org.testng.annotations.Test;
import utils.ApiResponseParser;

public class ApiResponseParserTest {

    @Test
    public void customerIdIsReadFromASuccessfulLogin() {
        Response response = mock(Response.class);
        JsonPath json = mock(JsonPath.class);
        when(response.statusCode()).thenReturn(200);
        when(response.jsonPath()).thenReturn(json);
        when(json.getInt("id")).thenReturn(12212);

        assertEquals(ApiResponseParser.customerId(response), 12212);
    }

    @Test
    public void customerIdIsZeroAndBodyIsNotParsedWhenLoginFails() {
        Response response = mock(Response.class);
        when(response.statusCode()).thenReturn(400);

        assertEquals(ApiResponseParser.customerId(response), 0);
        verify(response, never()).jsonPath();
    }

    @Test
    public void accountIdsAreConvertedToStrings() {
        Response response = mock(Response.class);
        JsonPath json = mock(JsonPath.class);
        when(response.jsonPath()).thenReturn(json);
        when(json.getList("id")).thenReturn(List.of(13344, 13455));

        assertEquals(ApiResponseParser.accountIds(response), List.of("13344", "13455"));
    }
}
