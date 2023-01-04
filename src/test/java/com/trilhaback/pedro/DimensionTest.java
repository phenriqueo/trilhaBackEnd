package com.trilhaback.pedro;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = PedroApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DimensionTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer<>
            ("postgres:latest")
            .withDatabaseName("mitradb")
            .withUsername("mitraecp")
            .withPassword("mitra@123")
            .withInitScript("initDb.sql");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("teste.datasource.url", container::getJdbcUrl);
        registry.add("teste.datasource.password", container::getPassword);
        registry.add("teste.datasource.username", container::getUsername);
    }

    @Test
    @Order(1)
    @DisplayName("cria dimensao produto")
    @SneakyThrows
    public void primeiro_teste_com_o_figa() {

        MvcResult mvcResult = mvc.perform();

        System.out.println("teste deu bão");
    }
}



