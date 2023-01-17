package com.trilhaback.pedro;

import com.jayway.jsonpath.JsonPath;
import com.trilhaback.pedro.domain.DataType;
import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.NodeContent;
import com.trilhaback.pedro.service.dto.form.DimensionContentForm;
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

import javax.print.attribute.standard.Media;

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
    private static Integer taskDimensionID;
    private static Integer taskDeletadaDimensionID;
    private static Integer projetoDimensionID;
    private static Integer statusTaskDimensionID;
    private static Integer tipoProjetoDimensionID;
    private static Integer itemTaskID = 1;
    private static Integer itemTaskID2 = 2;
    private static Integer itemProjetoID = 1;
    private static Integer itemTipoProjetoID = 1;

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
    @DisplayName("cria dimensao tarefa")
    @SneakyThrows
    public void criaDimensaoTarefa() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Tarefa").dataType(DataType.valueOf("INT")).build();
        MvcResult mvcResult = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tarefa"))
                .andDo(print())
                .andReturn();

        taskDimensionID = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(2)
    @DisplayName("altera nome dimensao tarefa para task")
    @SneakyThrows
    public void alteraNomeDimensaoTask() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Task").dataType(DataType.valueOf("INT")).id(Long.valueOf(taskDimensionID)).build();
        MvcResult mvcResult = mvc.perform(post("/dimension/save")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Task"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(3)
    @DisplayName("busca dimensao task por ID")
    @SneakyThrows
    public void buscaDimensaoTaskPorId() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + taskDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Task"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(4)
    @DisplayName("cria dimensao task deletada")
    @SneakyThrows
    public void criaDimensaoTaskDeletada() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Task Deletada").dataType(DataType.valueOf("VARCHAR")).build();
        MvcResult mvcResult = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Task Deletada"))
                .andDo(print())
                .andReturn();

        taskDeletadaDimensionID = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
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
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(6)
    @DisplayName("deleta dimensao task deletada")
    @SneakyThrows
    public void deletaDimensaoTaskDeletada() {
        MvcResult mvcResult = mvc.perform(delete("/dimension/" + taskDeletadaDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(7)
    @DisplayName("busca por dimensao task deletada")
    @SneakyThrows
    public void buscaDimensaoTaskDeletada() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + taskDeletadaDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(8)
    @DisplayName("cria dimensao projeto")
    @SneakyThrows
    public void criaDimensaoProjeto() {
        DimensionForm dimensionForm = DimensionForm.builder().name("Projeto").dataType(DataType.valueOf("INT")).build();
        MvcResult mvcResult = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Projeto"))
                .andDo(print())
                .andReturn();

        projetoDimensionID = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(9)
    @DisplayName("adiciona dimensao projeto como pai de task")
    @SneakyThrows
    public void adicionaDimensaoProjetoComoPaiDeTask() {
        DimensionForm dimensionForm = DimensionForm.builder()
                .id(Long.valueOf(projetoDimensionID))
                .sonId(Long.valueOf(taskDimensionID))
                .build();
        MvcResult mvcResult = mvc.perform(put("/dimension/addparent")
                        .content(objectMapper.writeValueAsString(dimensionForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(10)
    @DisplayName("busca por projeto com dimensao filha task")
    @SneakyThrows
    public void buscaPorProdjtoComDimensaoFilhaTask() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + projetoDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sonId").value(taskDimensionID))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(11)
    @DisplayName("adiciona dimensao status task e adiciona como pai de task")
    @SneakyThrows
    public void adicionaDimensaoStatusTaskEAdicionaComoPaiDeTask() {
        DimensionForm dimensionFormStatusTask = DimensionForm.builder()
                .name("Status Task")
                .dataType(DataType.valueOf("INT"))
                .build();

        MvcResult mvcResultCriaDimensao = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionFormStatusTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Status Task"))
                .andDo(print())
                .andReturn();

        statusTaskDimensionID = JsonPath.read(mvcResultCriaDimensao.getResponse().getContentAsString(), "$.id");

        DimensionForm dimensionFormAddParentTask = DimensionForm.builder()
                .id(Long.valueOf(statusTaskDimensionID))
                .sonId(Long.valueOf(taskDimensionID))
                .build();

        MvcResult mvcResultAssociaDimensao = mvc.perform(put("/dimension/addparent")
                        .content(objectMapper.writeValueAsString(dimensionFormAddParentTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(12)
    @DisplayName("adiciona dimensao tipo projeto e adiciona como pai de projeto")
    @SneakyThrows
    public void adicionaDimensaoTipoProjetoEAdicionaComoPaiDeProjeto() {
        DimensionForm dimensionFormTipoProjeto = DimensionForm.builder()
                .name("Tipo Projeto")
                .dataType(DataType.valueOf("INT"))
                .build();

        MvcResult mvcResultCriaDimensao = mvc.perform(post("/dimension")
                        .content(objectMapper.writeValueAsString(dimensionFormTipoProjeto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tipo Projeto"))
                .andDo(print())
                .andReturn();

        tipoProjetoDimensionID = JsonPath.read(mvcResultCriaDimensao.getResponse().getContentAsString(), "$.id");

        DimensionForm dimensionFormAddParentProjeto = DimensionForm.builder()
                .id(Long.valueOf(tipoProjetoDimensionID))
                .sonId(Long.valueOf(projetoDimensionID))
                .build();

        MvcResult mvcResultAssociaDimensao = mvc.perform(put("/dimension/addparent")
                        .content(objectMapper.writeValueAsString(dimensionFormAddParentProjeto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(13)
    @DisplayName("busca por tipo projeto com dimensao filha projeto")
    @SneakyThrows
    public void buscaPorTipoProjetoComDimensaoFilhaProjeto() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + tipoProjetoDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sonId").value(projetoDimensionID))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(14)
    @DisplayName("busca por arvore dimensao task")
    @SneakyThrows
    public void buscaPorArvoreDimensaoTask() {
        MvcResult mvcResult = mvc.perform(get("/dimension/tree/" + taskDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parent[0].id").value(projetoDimensionID))
                .andExpect(jsonPath("$.parent[1].id").value(statusTaskDimensionID))
                .andExpect(jsonPath("$.parent[0].parent[0].id").value(tipoProjetoDimensionID))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(15)
    @DisplayName("deleta relacao entre task e status task")
    @SneakyThrows
    public void deletaRelacaoEntreTaskEStatusTask() {
        DimensionForm dimensionFormStatusTask = DimensionForm.builder()
                .id(Long.valueOf(statusTaskDimensionID))
                .sonId(Long.valueOf(taskDimensionID))
                .build();

        MvcResult mvcResult = mvc.perform(put("/dimension/removeSonId/")
                        .content(objectMapper.writeValueAsString(dimensionFormStatusTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(16)
    @DisplayName("busca por dimensao status task com campo sonid null")
    @SneakyThrows
    public void buscaPorDimensaoStatusTaskComCampoSonIdNull() {
        MvcResult mvcResult = mvc.perform(get("/dimension/" + statusTaskDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sonId").value(0))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(20)
    @DisplayName("inserir item task na dimensao task")
    @SneakyThrows
    public void inserirItemTsakNaDimensaoTask() {
        DimensionContentForm dimensionContentForm = DimensionContentForm.builder()
                .id(String.valueOf(itemTaskID))
                .name("Criar arvore dimensoes")
                .build();

        MvcResult mvcResult = mvc.perform(post("/dimensionContent/" + taskDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(21)
    @DisplayName("alterar nome do 'Criar arvore dimensoes' para 'Alterar arvore dimensoes'")
    @SneakyThrows
    public void alterarNomeItemTask() {
        DimensionContentForm dimensionContentForm = DimensionContentForm.builder()
                .id(String.valueOf(itemTaskID))
                .name("Alterar arvore dimensoes")
                .build();

        MvcResult mvcResult = mvc.perform(put("/dimensionContent/" + taskDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(22)
    @DisplayName("busca por conteudo dimensao")
    @SneakyThrows
    public void buscaPorConteudoDimensao() {
        MvcResult mvcResult = mvc.perform(get("/dimensionContent/" + taskDimensionID + "/" + itemTaskID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alterar arvore dimensoes"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(23)
    @DisplayName("deleta conteudo dimensao")
    @SneakyThrows
    public void deletaConteudoDimensao() {
        MvcResult mvcResult = mvc.perform(delete("/dimensionContent/" + taskDimensionID + "/" + itemTaskID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        MvcResult mvcResult2 = mvc.perform(get("/dimensionContent/" + taskDimensionID + "/" + itemTaskID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(24)
    @DisplayName("adiciona membro nas dimensoes task, projeto, tipo projeto e lista todos os itens da dimensao task")
    @SneakyThrows
    public void adicionaMembroNasDimensoesTaskProjetoTipoProjeto() {
        DimensionContentForm dimensionContentFormTask = DimensionContentForm.builder()
                .id(String.valueOf(itemTaskID))
                .name("Criar arvore dimensoes")
                .build();

        DimensionContentForm dimensionContentFormTask2 = DimensionContentForm.builder()
                .id(String.valueOf(itemTaskID2))
                .name("Estudar sobre design patterns")
                .build();

        DimensionContentForm dimensionContentFormProjeto = DimensionContentForm.builder()
                .id(String.valueOf(itemProjetoID))
                .name("Trilha Back Jr")
                .build();

        DimensionContentForm dimensionContentFormTipoProjeto = DimensionContentForm.builder()
                .id(String.valueOf(itemTipoProjetoID))
                .name("Industria")
                .build();

        MvcResult mvcResult = mvc.perform(post("/dimensionContent/" + taskDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentFormTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        MvcResult mvcResult2 = mvc.perform(post("/dimensionContent/" + taskDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentFormTask2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        MvcResult mvcResult3 = mvc.perform(post("/dimensionContent/" + projetoDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentFormProjeto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        MvcResult mvcResult4 = mvc.perform(post("/dimensionContent/" + tipoProjetoDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentFormTipoProjeto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        MvcResult mvcResult5 = mvc.perform(get("/dimensionContent/" + taskDimensionID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(25)
    @DisplayName("relaciona item task Criar Arvores Dimensoes com projeto Trilha Back Jr")
    @SneakyThrows
    public void relacionaItemTaskCriarArvoresDimensoesComProjetoTrilhaBackJr() {
        DimensionContentForm dimensionContentFormTask = DimensionContentForm.builder()
                .id(String.valueOf(itemTaskID))
                .nodeContent(NodeContent.builder()
                        .dimensionId(Long.valueOf(projetoDimensionID))
                        .dimensionContentId(String.valueOf(itemProjetoID))
                        .build())
                .build();

        MvcResult mvcResult = mvc.perform(put("/dimensionContent/addRelationship/" + taskDimensionID)
                        .content(objectMapper.writeValueAsString(dimensionContentFormTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }


    @Test
    @Order(100)
    @DisplayName("the last of us")
    public void the_last_of_us() {
        System.out.println(container.getJdbcUrl());
        System.out.println("breakpoint ");
    }
}



