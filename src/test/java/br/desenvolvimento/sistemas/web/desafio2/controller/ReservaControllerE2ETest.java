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

import java.time.LocalDate;
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
class ReservaControllerE2ETest {

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

    @BeforeEach
    void setUp() throws Exception {
        if (destinoId == null) {
            MvcResult result = mockMvc.perform(post("/destinos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonDestino("Roma", "Cidade eterna", "Italia")))
                    .andExpect(status().isCreated())
                    .andReturn();

            destinoId = extractId(result.getResponse().getContentAsString());
        }
    }

    // -------------------------------------------------------------------------
    // POST /reservas – criar reserva com sucesso
    // -------------------------------------------------------------------------
    @Test
    @Order(1)
    void deveCriarReservaComSucesso() throws Exception {
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReserva("Joao Silva", "joao@email.com", LocalDate.now().plusMonths(2), 3, destinoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nomeCliente").value("Joao Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.numeroPessoas").value(3))
                .andExpect(jsonPath("$.status").value("CONFIRMADA"))
                .andExpect(jsonPath("$.destino.id").value(destinoId));
    }

    // -------------------------------------------------------------------------
    // POST /reservas – email inválido deve retornar 400
    // -------------------------------------------------------------------------
    @Test
    @Order(2)
    void deveRetornar400ParaEmailInvalido() throws Exception {
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReserva("Maria", "email-invalido", LocalDate.now().plusMonths(1), 2, destinoId)))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // POST /reservas – data de viagem no passado deve retornar 400
    // -------------------------------------------------------------------------
    @Test
    @Order(3)
    void deveRetornar400ParaDataDeViagemNoPassado() throws Exception {
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReserva("Carlos", "carlos@email.com", LocalDate.now().minusDays(1), 1, destinoId)))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // POST /reservas – número de pessoas zero deve retornar 400
    // -------------------------------------------------------------------------
    @Test
    @Order(4)
    void deveRetornar400ParaNumeroPessoasZero() throws Exception {
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReserva("Ana", "ana@email.com", LocalDate.now().plusMonths(1), 0, destinoId)))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // POST /reservas – destino inexistente deve retornar 404
    // -------------------------------------------------------------------------
    @Test
    @Order(5)
    void deveRetornar404ParaDestinoInexistente() throws Exception {
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReserva("Pedro", "pedro@email.com", LocalDate.now().plusMonths(1), 2, 99999L)))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // GET /reservas/destino/{destinoId} – listar reservas por destino
    // -------------------------------------------------------------------------
    @Test
    @Order(6)
    void deveListarReservasPorDestino() throws Exception {
        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReserva("Lucia", "lucia@email.com", LocalDate.now().plusMonths(3), 4, destinoId)));

        mockMvc.perform(get("/reservas/destino/{destinoId}", destinoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].destino.id", everyItem(is(destinoId.intValue()))));
    }

    // -------------------------------------------------------------------------
    // GET /reservas/destino/{destinoId} – destino sem reservas retorna lista vazia
    // -------------------------------------------------------------------------
    @Test
    @Order(7)
    void deveRetornarListaVaziaParaDestinoSemReservas() throws Exception {
        MvcResult result = mockMvc.perform(post("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDestino("Toquio", "Capital do Japao", "Japao")))
                .andExpect(status().isCreated())
                .andReturn();

        Long novoDestinoId = extractId(result.getResponse().getContentAsString());

        mockMvc.perform(get("/reservas/destino/{destinoId}", novoDestinoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    // -------------------------------------------------------------------------
    // POST /reservas – nome do cliente em branco deve retornar 400
    // -------------------------------------------------------------------------
    @Test
    @Order(8)
    void deveRetornar400ParaNomeClienteEmBranco() throws Exception {
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReserva("", "valid@email.com", LocalDate.now().plusMonths(1), 2, destinoId)))
                .andExpect(status().isBadRequest());
    }

    private static String jsonDestino(String nome, String descricao, String pais) {
        return String.format("{\"nome\":\"%s\",\"descricao\":\"%s\",\"pais\":\"%s\"}", nome, descricao, pais);
    }

    private static String jsonReserva(String nomeCliente, String email, LocalDate dataViagem, int numeroPessoas, Long destinoId) {
        return String.format(
                "{\"nomeCliente\":\"%s\",\"email\":\"%s\",\"dataViagem\":\"%s\",\"numeroPessoas\":%d,\"destinoId\":%d}",
                nomeCliente, email, dataViagem, numeroPessoas, destinoId
        );
    }

    private static Long extractId(String responseJson) {
        Matcher matcher = Pattern.compile("\"id\":(\\d+)").matcher(responseJson);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not extract id from response: " + responseJson);
        }
        return Long.parseLong(matcher.group(1));
    }
}
