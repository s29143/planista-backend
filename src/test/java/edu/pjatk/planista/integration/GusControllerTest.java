package edu.pjatk.planista.integration;

import edu.pjatk.planista.config.filters.JwtAuthenticationFilter;
import edu.pjatk.planista.config.security.JwtService;
import edu.pjatk.planista.integration.controllers.GusController;
import edu.pjatk.planista.integration.models.CompanyFromRegonDto;
import edu.pjatk.planista.integration.services.GusRegonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GusController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GusControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @MockitoBean
    GusRegonService gusRegonService;

    @Test
    void getNip() throws Exception {
        CompanyFromRegonDto dto = new CompanyFromRegonDto();
        dto.setNip("1234567890");
        dto.setName("ACME");

        when(gusRegonService.findByNip("1234567890")).thenReturn(dto);

        mvc.perform(get("/api/v1/integrations/gus/regon/by-nip/1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.nip").value("1234567890"))
                .andExpect(jsonPath("$.name").value("ACME"));

        verify(gusRegonService).findByNip("1234567890");
        verifyNoMoreInteractions(gusRegonService);
    }
}
