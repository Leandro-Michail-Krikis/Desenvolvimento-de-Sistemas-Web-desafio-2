package br.desenvolvimento.sistemas.web.desafio2.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DestinoControllerE2ETest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Long destinoId;

    @BeforeAll
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @Order(0)
    void usuarioComumNaoDeveCriarDestino() throws Exception {
        mockMvc.perform(post("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDestino("Recife", "Teste", "Brasil")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(1)
    void deveCriarDestinoComSucesso() throws Exception {
        MvcResult result = mockMvc.perform(post("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDestino("Paris", "A cidade luz", "França")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Paris"))
                .andExpect(jsonPath("$.pais").value("França"))
                .andReturn();

        destinoId = extractId(result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    void deveRetornar400AoCriarDestinoSemNome() throws Exception {
        mockMvc.perform(post("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDestino("", "Sem nome", "Brasil")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void deveListarTodosOsDestinos() throws Exception {
        mockMvc.perform(get("/destinos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    @Order(4)
    void deveBuscarDestinoPorId() throws Exception {
        mockMvc.perform(get("/destinos/{id}", destinoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(destinoId))
                .andExpect(jsonPath("$.nome").value("Paris"));
    }

    @Test
    @Order(5)
    void deveRetornar404ParaDestinoInexistente() throws Exception {
        mockMvc.perform(get("/destinos/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void deveBuscarDestinoPorNome() throws Exception {
        mockMvc.perform(get("/destinos/buscar").param("nome", "Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nome", hasItem(containsString("Paris"))));
    }

    @Test
    @Order(7)
    void deveBuscarDestinoPorPais() throws Exception {
        mockMvc.perform(get("/destinos/buscar").param("pais", "França"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].pais", hasItem("França")));
    }

    @Test
    @Order(8)
    void deveAtualizarDestino() throws Exception {
        mockMvc.perform(put("/destinos/{id}", destinoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDestino("Paris Atualizado", "Nova descrição", "França")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Paris Atualizado"));
    }

    @Test
    @Order(9)
    void deveAvaliarDestino() throws Exception {
        mockMvc.perform(patch("/destinos/{id}/avaliar", destinoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAvaliacao(8)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNotas").value(greaterThan(0)))
                .andExpect(jsonPath("$.notaMedia").value(greaterThan(0.0)));
    }

    @Test
    @Order(10)
    void deveRetornar400ParaAvaliacaoAcimaDoMaximo() throws Exception {
        mockMvc.perform(patch("/destinos/{id}/avaliar", destinoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAvaliacao(11)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    void deveRetornar400ParaAvaliacaoAbaixoDoMinimo() throws Exception {
        mockMvc.perform(patch("/destinos/{id}/avaliar", destinoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAvaliacao(0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    void deveDeletarDestino() throws Exception {
        MvcResult created = mockMvc.perform(post("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDestino("Destino para Deletar", "Sera deletado", "Teste")))
                .andExpect(status().isCreated())
                .andReturn();

        Long idParaDeletar = extractId(created.getResponse().getContentAsString());

        mockMvc.perform(delete("/destinos/{id}", idParaDeletar))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/destinos/{id}", idParaDeletar))
                .andExpect(status().isNotFound());
    }

    private static String jsonDestino(String nome, String descricao, String pais) {
        return String.format("{\"nome\":\"%s\",\"descricao\":\"%s\",\"pais\":\"%s\"}", nome, descricao, pais);
    }

    private static String jsonAvaliacao(int valor) {
        return String.format("{\"valor\":%d}", valor);
    }

    private static Long extractId(String responseJson) {
        Matcher matcher = Pattern.compile("\"id\":(\\d+)").matcher(responseJson);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not extract id from response: " + responseJson);
        }
        return Long.parseLong(matcher.group(1));
    }
}
