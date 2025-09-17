package edu.pjatk.planista.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pjatk.planista.company.controllers.CompanyController;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.company.services.CompanyService;
import edu.pjatk.planista.security.JwtAuthenticationFilter;
import edu.pjatk.planista.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompanyControllerTest {
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    CompanyService service;

    private CompanyResponse sampleResponse() {
        return new CompanyResponse(
                1L,
                "ABC",
                "ABC Sp. z o.o.",
                "1234567890",
                "00-001",
                "Main St",
                "12",
                "34",
                "+48123456789",
                "info@abc.pl",
                "www.abc.pl",
                "Test comments",
                10L,
                20L,
                30L,
                40L,
                50L
        );
    }

    @Test
    void getReturnsCompany() throws Exception {
        var response = sampleResponse();
        Mockito.when(service.get(1L)).thenReturn(response);

        mvc.perform(get("/api/v1/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.shortName").value("ABC"))
                .andExpect(jsonPath("$.fullName").value("ABC Sp. z o.o."))
                .andExpect(jsonPath("$.userId").value(10L));
    }

    @Test
    void listReturnsPage() throws Exception {
        var response = sampleResponse();
        var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        Mockito.when(service.list(any())).thenReturn(page);

        mvc.perform(get("/api/v1/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].shortName").value("ABC"))
                .andExpect(jsonPath("$.content[0].districtId").value(30L));
    }

    @Test
    void createReturnsCreatedCompany() throws Exception {
        var request = new CompanyRequest(
                "ABC",
                "ABC Sp. z o.o.",
                "1234567890",
                "00-001",
                "Main St",
                "12",
                "34",
                "+48123456789",
                "info@abc.pl",
                "www.abc.pl",
                "Test comments",
                10L,
                20L,
                30L,
                40L,
                50L
        );
        var response = sampleResponse();

        Mockito.when(service.create(any())).thenReturn(response);

        mvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("info@abc.pl"));
    }

    @Test
    void updateReturnsUpdatedCompany() throws Exception {
        var request = new CompanyRequest(
                "XYZ",
                "XYZ S.A.",
                "9876543210",
                "11-111",
                "Broadway",
                "99",
                "1",
                "+48987654321",
                "contact@xyz.pl",
                "www.xyz.pl",
                "Updated comments",
                11L,
                21L,
                31L,
                41L,
                51L
        );

        var updated = new CompanyResponse(
                1L,
                "XYZ",
                "XYZ S.A.",
                "9876543210",
                "11-111",
                "Broadway",
                "99",
                "1",
                "+48987654321",
                "contact@xyz.pl",
                "www.xyz.pl",
                "Updated comments",
                11L,
                21L,
                31L,
                41L,
                51L
        );

        Mockito.when(service.update(eq(1L), any())).thenReturn(updated);

        mvc.perform(put("/api/v1/companies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortName").value("XYZ"))
                .andExpect(jsonPath("$.email").value("contact@xyz.pl"));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/companies/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }
}