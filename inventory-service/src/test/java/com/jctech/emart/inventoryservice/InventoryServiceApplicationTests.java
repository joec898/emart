package com.jctech.emart.inventoryservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jctech.emart.inventoryservice.repository.InventoryRepository;
import com.jctech.emart.inventoryservice.view.InventoryRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.api.Assertions;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class InventoryServiceApplicationTests {

	/**
	 * mysql properties:
	 * spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
	 * spring.datasource.url=jdbc:mysql://localhost:3306/mart_inventories?createDatabaseIfNotExist=true
	 * spring.datasource.username=myadmin 
	 * spring.datasource.password=LetMeIn
	 * spring.jpa.hibernate.ddl-auto=update
	 */
	
	@Container
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.29").withDatabaseName("mart_inventories")
			.withUsername("myadmin").withPassword("LetMeIn").withExposedPorts(3306).withConnectTimeoutSeconds(10 * 60);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private InventoryRepository inventoryRepo;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",
				() -> String.format("jdbc:mysql://localhost:%d/mart_inventories?createDatabaseIfNotExist=true", 
						mysqlContainer.getFirstMappedPort()));
		registry.add("spring.datasource.username", () -> "myadmin");
		registry.add("spring.datasource.password", () -> "LetMeIn");
	}

	@Test
	void whenPostProduct_shouldCreateProduct() throws Exception {
		InventoryRequest req = mockInventoryRequest();
		String inventoryRequestStr = objMapper.writeValueAsString(req);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory").contentType(MediaType.APPLICATION_JSON)
				.content(inventoryRequestStr)).andExpect(status().isCreated());

		Assertions.assertThat(inventoryRepo.findAll().size()==1);
	}

	private InventoryRequest mockInventoryRequest() {
		return InventoryRequest.builder().skuCode("ipad 9Gen").quantity(20).build();
	}
}
