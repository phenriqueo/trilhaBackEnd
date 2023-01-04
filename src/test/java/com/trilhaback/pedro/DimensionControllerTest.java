package com.trilhaback.pedro;

import com.jayway.jsonpath.JsonPath;
import com.trilhaback.pedro.domain.DataType;
import com.trilhaback.pedro.service.dto.form.DimensionForm;
import com.zaxxer.hikari.HikariConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PedroApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("teste")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DimensionControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Integer produtoDimensionID;
    private static Integer cidadeDimensionID;
    private static Integer marcaDimensionID;

    @Autowired
    private MockMvc mvc;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer<>
            ("postgres:latest")
            .withDatabaseName("mitradb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("initDb.sql");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("teste.datasource.url", container::getJdbcUrl);
        registry.add("teste.datasource.password", container::getPassword);
        registry.add("teste.datasource.username", container::getUsername);
    }

    @BeforeAll
    public static void dataCheck() {
        System.out.println("Connect:");
        System.out.println(container.getJdbcUrl());
        System.out.println(container.getUsername());
        System.out.println(container.getPassword());
        HikariConfig config = new HikariConfig();
        config.setUsername(container.getUsername());
        config.setPassword(container.getPassword());
        config.setJdbcUrl(container.getJdbcUrl());
    }

    @Test
    @Order(1)
    @DisplayName("cria dimensao produto")
    @SneakyThrows
    public void criaDimensaoProduto() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Produto").dataType(DataType.valueOf("INT")).build();
        MvcResult mvcResult = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Produto"))
                .andDo(print())
                .andReturn();

        produtoDimensionID = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(2)
    @DisplayName("altera nome dimensao produto para produto alterado")
    @SneakyThrows
    public void alteraNomeDimensaoProduto() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Produto Alterado").dataType(DataType.valueOf("INT")).id(Long.valueOf(produtoDimensionID)).build();
        MvcResult mvcResult = mvc.perform(post("/dimension/save")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Alterado"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(3)
    @DisplayName("busca produto por ID")
    @SneakyThrows
    public void buscaProdutoPorId() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + produtoDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Alterado"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(4)
    @DisplayName("cria dimensao cidade")
    @SneakyThrows
    public void criaDimensaoCidade() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Cidade").dataType(DataType.valueOf("VARCHAR")).build();
        MvcResult mvcResult = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cidade"))
                .andDo(print())
                .andReturn();

        cidadeDimensionID = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(5)
    @DisplayName("busca todos as dimensoes")
    @SneakyThrows
    public void buscaTodasDimensoes() {
        MvcResult mvcResult = mvc.perform(get("/dimension/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.name").value("Produto Alterado"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(6)
    @DisplayName("deleta dimensao cidade")
    @SneakyThrows
    public void deletaDimensaoProduto() {
        MvcResult mvcResult = mvc.perform(delete("/dimension/" + cidadeDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(7)
    @DisplayName("busca por dimensao cidade deletada")
    @SneakyThrows
    public void buscaDimensaoCidadeDeletada() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + cidadeDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(8)
    @DisplayName("cria dimensao marca")
    @SneakyThrows
    public void criaDimensaoMarca() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Marca").dataType(DataType.valueOf("INT")).build();
        MvcResult mvcResult = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Marca"))
                .andDo(print())
                .andReturn();

        marcaDimensionID = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(9)
    @DisplayName("adiciona dimensao marca como pai de produto")
    @SneakyThrows
    public void adicionaDimensaoMarcaComoPaiDeProduto() {
        DimensionForm dimensionForm = DimensionForm.builder()
                .id(Long.valueOf(marcaDimensionID))
                .name("Marca")
                .dataType(DataType.valueOf("INT"))
                .sonId(Long.valueOf(produtoDimensionID))
                .build();
        MvcResult mvcResult = mvc.perform(put("/dimension/addparent")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.sonID").value(produtoDimensionID))
                .andDo(print())
                .andReturn();
    }
}



