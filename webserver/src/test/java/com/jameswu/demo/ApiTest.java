package com.jameswu.demo;

import static com.jameswu.demo.utils.GzTexts.BEARER_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiTest {

	private final String APPLICATION_JSON = "application/json";
	private final String CONTENT_TYPE = "Content-Type";
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	void loginApiTest() {
		Assertions.assertFalse(login().isBlank());
	}

	@SneakyThrows
	private String login() {
		String responseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
						.content(parseJson(
								Map.ofEntries(Map.entry("username", "TestUser7"), Map.entry("password", "TestUser7"))))
						.contentType(APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.message.access-token").isNotEmpty())
				.andReturn()
				.getResponse()
				.getContentAsString();
		return JsonPath.read(responseBody, "$.message.access-token");
	}

	@SneakyThrows
	@Test
	void diagramApi() {
		String accessToken = login();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/management/diagram")
						.param("id", "1")
						.header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath(
								"message.childrenProfiles[0].childrenProfiles[0].userProfile.userId")
						.value(4));
	}

	@SneakyThrows
	@Test
	void registerUserApi() {
		String accessToken = login();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/public/register")
						.content(parseJson(getNewUser(0, 65)))
						.contentType(APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.userId").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.recommenderId")
						.value(1));
	}

	@SneakyThrows
	@Test
	void addProductApi() {
		String accessToken = login();
		Map<String, Object> payload = Map.ofEntries(
				Map.entry("title", "A Product"),
				Map.entry("description", "A product des."),
				Map.entry("price", 1000),
				Map.entry("quantity", 1));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/management/add")
						.content(parseJson(payload))
						.contentType(APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.productId").isNumber());
	}

	@SneakyThrows
	@Test
	void createOrderApi() {
		String accessToken = login();
		Map<String, Integer> coupon = new HashMap<>();
		coupon.put("quantity", 9);
		coupon.put("couponId", null);
		coupon.put("productId", 10);
		var orderPayload = List.of(coupon);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/create")
						.content(parseJson(orderPayload))
						.contentType(APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.orderId").value(1));
	}

	@SneakyThrows
	public String parseJson(Object payload) {
		return objectMapper.writeValueAsString(payload);
	}

	private Map<String, String> getNewUser(int increasedId, int baseId) {
		int id = increasedId + baseId;
		return Map.ofEntries(
				Map.entry("username", "TestUser" + id),
				Map.entry("password", "TestUser" + id),
				Map.entry("email", "TestUser" + id + "@gmail.com"),
				Map.entry("nickname", "NikcnameTest" + id),
				Map.entry("address", "Taipei"),
				Map.entry("recommenderId", "1"));
	}
}
