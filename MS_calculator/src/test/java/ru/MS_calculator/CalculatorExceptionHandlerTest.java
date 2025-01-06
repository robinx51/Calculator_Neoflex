package ru.MS_calculator;

import ru.calculator.MS_calculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = MS_calculator.class)
@AutoConfigureMockMvc
public class CalculatorExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleValidationErrors() throws Exception {
        String requestBody =
                "{" +
                "  \"amount\": 10000," +
                "  \"term\": 5," +
                "  \"firstName\": \"Y\"," +
                "  \"lastName\": \"Sin1\"," +
                "  \"middleName\": \"A\"," +
                "  \"email\": \"Stringmail.ru\"," +
                "  \"birthdate\": \"2014-01-01\"," +
                "  \"passportSeries\": \"12344\"," +
                "  \"passportNumber\": \"12346\"" +
                "}";

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value("Сумма кредита - действительно число, большее или равное 20000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.term").value("Срок кредита - целое число, большее или равное 6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Имя - от 2 до 30 латинских букв"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Фамилия - от 2 до 30 латинских букв"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("must be a well-formed email address"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate").value("Возраст должен быть более 18 и менее 65 лет"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportSeries").value("Серия паспорта - 4 цифры"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportNumber").value("Номер паспорта - 6 цифр"));
    }
}
