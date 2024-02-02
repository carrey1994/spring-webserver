import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Disabled("test for local only")
class ApiIntegrationTest {
    private final String APPLICATION_JSON = "application/json";
    private final String CONTENT_TYPE = "Content-Type";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private final MediaType jsonMediaType = MediaType.parse(APPLICATION_JSON);

    @SneakyThrows
    public String loginApi() {

        var payload = Map.ofEntries(Map.entry("username", "testuser7"), Map.entry("password", "testuser7"));
        RequestBody body = RequestBody.create(jsonMediaType, parseJson(payload));
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/api/v1/login")
                .method(HttpMethod.POST.name(), body)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();
        Response response = client.newCall(request).execute();
        Assertions.assertEquals(HttpStatus.OK.value(), response.code());
        JsonNode resultNode = objectMapper.readTree(response.body().string());
        String accessToken = resultNode.get("message").get("access-token").asText();
        Assertions.assertFalse(accessToken.isBlank());
        return accessToken;
    }

    @SneakyThrows
    @Test
    void diagramApi() {
        String accessToken = loginApi();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/api/v1/user/diagram?id=1")
                .get()
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
        Response response = client.newCall(request).execute();
        JsonNode resultNode = objectMapper.readTree(response.body().string());
        Assertions.assertEquals(HttpStatus.OK.value(), response.code());

        long userId = resultNode
                .get("message")
                .get("childrenProfiles")
                .get(0)
                .get("childrenProfiles")
                .get(0)
                .get("userProfile")
                .get("userId")
                .asLong();
        Assertions.assertEquals(4L, userId);
    }

    @SneakyThrows
    @Test
    void addUserApi() {
        String accessToken = loginApi();
        Map<String, String> payload = getNewUser(0, 64);
        RequestBody body = RequestBody.create(jsonMediaType, parseJson(payload));
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/api/v1/user/management/add")
                .method(HttpMethod.POST.name(), body)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        Response response = doRequest(request);
        Assertions.assertEquals(HttpStatus.OK.value(), response.code());

        JsonNode resultNode = objectMapper.readTree(response.body().string());
        Assertions.assertFalse(
                Strings.isNullOrEmpty(resultNode.get("message").get("userId").asText()));
        Assertions.assertEquals(
                "1", resultNode.get("message").get("recommenderId").asText());
        Assertions.assertTrue(resultNode.get("ok").asBoolean());
    }

    @SneakyThrows
    @Test
    void concurrentlyAddUserApi() {
        String accessToken = loginApi();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 200; i++) {
            int increasedId = i;
            cachedThreadPool.execute(() -> {
                Map<String, String> payload = getNewUser(increasedId, 65);
                RequestBody body = RequestBody.create(jsonMediaType, parseJson(payload));
                Request request = new Request.Builder()
                        .url("http://127.0.0.1:8080/api/v1/user/management/add")
                        .method(HttpMethod.POST.name(), body)
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .build();
                Response response = doRequest(request);
                Assertions.assertEquals(HttpStatus.OK.value(), response.code());
                System.out.println("finish new user registration" + increasedId);
            });
        }
        cachedThreadPool.shutdown();
        cachedThreadPool.awaitTermination(1, TimeUnit.MINUTES);
    }

    @SneakyThrows
    public Response doRequest(Request request) {
        return client.newCall(request).execute();
    }

    @SneakyThrows
    public String parseJson(Object payload) {
        return objectMapper.writeValueAsString(payload);
    }

    private Map<String, String> getNewUser(int increasedId, int baseId) {
        int id = increasedId + baseId;
        return Map.ofEntries(
                Map.entry("username", "testuser" + id),
                Map.entry("password", "testuser" + id),
                Map.entry("email", "testuser" + id + "@gmail.com"),
                Map.entry("address", "Taipei"),
                Map.entry("date", "2024-01-23"),
                Map.entry("recommenderId", "1"));
    }
}
