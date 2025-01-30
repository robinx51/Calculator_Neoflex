package ru.statement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.library.dto.LoanOfferDto;
import ru.library.dto.LoanStatementRequestDto;
import ru.statement.controller.StatementController;
import ru.statement.service.StatementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatementController.class)
public class StatementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatementService statementService;

    private LoanOfferDto offer;
    private LoanStatementRequestDto request;

    @BeforeEach
    void setUp() {
        offer = LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(BigDecimal.valueOf(200000))
                .totalAmount(BigDecimal.valueOf(210000)) // Пример значения
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(17500)) // Пример значения
                .rate(BigDecimal.valueOf(5.00)) // Пример значения
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        request = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(20000))
                .term(6)
                .firstName("John")
                .lastName("Doe")
                .middleName("Smith")
                .email("smith@example.com")
                .birthdate(LocalDate.now().minusYears(18))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }

    @Test
    void testInitialRegistration() throws Exception {
        when(statementService.processClient(request)).thenReturn(Collections.singletonList(offer));

        MvcResult result = mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].requestedAmount").value(offer.getRequestedAmount()))
                .andExpect(jsonPath("$[0].term").value(offer.getTerm()))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<LoanOfferDto> offers = objectMapper.readValue(jsonResponse, new TypeReference<List<LoanOfferDto>>() {});
        assertNotNull(offers);
        assertEquals(1, offers.size());
        assertEquals(offer.getRequestedAmount(), offers.get(0).getRequestedAmount());
        assertEquals(offer.getTerm(), offers.get(0).getTerm());
    }

    @Test
    void testSelectOffer() throws Exception {
        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk());

        verify(statementService, times(1)).selectOffer(offer);
    }

    @Test
    void testInitialRegistration_InvalidRequest() throws Exception {
        LoanStatementRequestDto invalidRequest = LoanStatementRequestDto.builder().build();

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}