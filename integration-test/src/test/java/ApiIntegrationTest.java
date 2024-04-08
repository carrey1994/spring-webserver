import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.util.HashMap;
import java.util.Map;
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

		var payload = Map.ofEntries(
				Map.entry("username", "testuser7"), Map.entry("password", "testuser7"));
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
				.url("http://127.0.0.1:8080/api/v1/user/management/diagram?id=1")
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
				.asInt();
		Assertions.assertEquals(4L, userId);
	}

	@SneakyThrows
	@Test
	void registerUserApi() {
		String accessToken = loginApi();
		Map<String, String> payload = getNewUser(0, 65);
		RequestBody body = RequestBody.create(jsonMediaType, parseJson(payload));
		Request request = new Request.Builder()
				.url("http://127.0.0.1:8080/api/v1/public/register")
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
	void addProductApi() {
		String accessToken = loginApi();
		Map<String, Object> payload = Map.ofEntries(
				Map.entry("title", "A Product"),
				Map.entry("description", "A product des."),
				Map.entry("price", 1000),
				Map.entry("quantity", 1));
		RequestBody body = RequestBody.create(jsonMediaType, parseJson(payload));
		Request request = new Request.Builder()
				.url("http://127.0.0.1:8080/api/v1/product/management/add")
				.method(HttpMethod.POST.name(), body)
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.addHeader(CONTENT_TYPE, APPLICATION_JSON)
				.build();

		Response response = doRequest(request);
		Assertions.assertEquals(HttpStatus.OK.value(), response.code());

		JsonNode resultNode = objectMapper.readTree(response.body().string());
		Assertions.assertNotEquals(0, resultNode.get("message").get("productId").asInt());
		Assertions.assertNotEquals(
				"A Product", resultNode.get("message").get("title").asInt());
	}

	@SneakyThrows
	@Test
	void createOrderApi() {
		String accessToken = loginApi();
		Map coupon = new HashMap<>();
		coupon.put("quantity", 1);
		coupon.put("couponId", null);
		var orderPayload = Map.of("2", coupon);
		RequestBody body = RequestBody.create(jsonMediaType, parseJson(orderPayload));
		Request request = new Request.Builder()
				.url("http://127.0.0.1:8080/api/v1/order/create")
				.method(HttpMethod.POST.name(), body)
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.addHeader(CONTENT_TYPE, APPLICATION_JSON)
				.build();

		Response response = doRequest(request);
		Assertions.assertEquals(HttpStatus.OK.value(), response.code());

		JsonNode resultNode = objectMapper.readTree(response.body().string());
		Assertions.assertEquals(1, resultNode.get("message").get("orderId").asInt());
		Assertions.assertTrue(resultNode.get("ok").asBoolean());
	}

	@SneakyThrows
	@Test
	void systemApi() {

		String accessToken = loginApi();
		Request request = new Request.Builder()
				.url("http://127.0.0.1:8080/api/v1/user/management/system")
				.get()
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.build();
		Response response = client.newCall(request).execute();
		Assertions.assertEquals(HttpStatus.OK.value(), response.code());
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
				Map.entry("nickname", "NikcnameTest" + id),
				Map.entry("address", "Taipei"),
				Map.entry("date", "2017-08-14 12:17:47"),
				Map.entry("recommenderId", "1"));
	}
}
