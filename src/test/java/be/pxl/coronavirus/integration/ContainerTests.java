package be.pxl.coronavirus.integration;

import be.pxl.coronavirus.domain.Patient;
import be.pxl.coronavirus.domain.request.DoctorRequest;
import be.pxl.coronavirus.domain.request.PatientRequest;
import be.pxl.coronavirus.domain.response.PatientResponse;
import be.pxl.coronavirus.repository.DoctorRepository;
import be.pxl.coronavirus.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ContainerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PatientRepository patientRepository;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8:0:18");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    public void addPatientToDoctor() throws Exception{
        PatientRequest patientRequest = PatientRequest.builder()
                .name("input")
                .INSZ("110122-12.09").build();
        String requestString = mapper.writeValueAsString(patientRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/doctor/{doctorId}/addPatient", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString)
                        .with(user("doctor").roles("DOCTOR")))
                        .andExpect(status().isCreated());

        Patient patient = patientRepository.findAll().stream().findFirst().orElse(null);

        assertEquals("input", patient.getName());
        assertEquals("110122-12.09", patient.getINSZ());
    }
}
