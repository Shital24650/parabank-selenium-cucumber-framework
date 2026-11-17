package utils;

import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;

/** Pulls values out of ParaBank API responses. Kept free of test state so it can be unit tested with mocks. */
public final class ApiResponseParser {

    private ApiResponseParser() {
    }

    /** Customer id from a login response, or 0 when the login did not return 200. */
    public static int customerId(Response response) {
        if (response.statusCode() != 200) {
            return 0;
        }
        return response.jsonPath().getInt("id");
    }

    /** Account ids as strings, so they compare directly with the ids read from the UI. */
    public static List<String> accountIds(Response response) {
        List<Integer> ids = response.jsonPath().getList("id");
        return ids.stream().map(String::valueOf).collect(Collectors.toList());
    }
}
