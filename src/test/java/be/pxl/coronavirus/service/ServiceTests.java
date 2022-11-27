package be.pxl.coronavirus.service;

import be.pxl.coronavirus.domain.Doctor;
import be.pxl.coronavirus.domain.Patient;
import be.pxl.coronavirus.domain.VirusTest;
import be.pxl.coronavirus.domain.request.PatientRequest;
import be.pxl.coronavirus.domain.request.VirusTestRequest;
import be.pxl.coronavirus.domain.response.PatientQuarantinedResponse;
import be.pxl.coronavirus.domain.response.PatientResponse;
import be.pxl.coronavirus.exception.DoctorException;
import be.pxl.coronavirus.repository.DoctorRepository;
import be.pxl.coronavirus.repository.PatientRepository;
import be.pxl.coronavirus.repository.VirusTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private VirusTestRepository virusTestRepository;

    @InjectMocks
    private ImplDoctorService doctorService;

    private String name;
    private Doctor doctor;
    private Patient patient;
    private VirusTest virusTest;

    private PatientRequest patientRequest;
    private VirusTestRequest virusTestRequest;

    @BeforeEach
    public void Setup(){
        Random random = new Random();
        name = UUID.randomUUID().toString();
        doctor = Doctor.builder()
                .name(UUID.randomUUID().toString())
                .patients(new ArrayList<>())
                .id(1L)
                .build();

        patient = Patient.builder()
                .name(UUID.randomUUID().toString())
                .INSZ(UUID.randomUUID().toString())
                .Quarantined(random.nextBoolean())
                .virusTests(new ArrayList<>())
                .id(1L)
                .build();

        virusTest = VirusTest.builder()
                .virusName(UUID.randomUUID().toString())
                .testResult(random.nextBoolean())
                .id(1L)
                .build();

        virusTestRequest = VirusTestRequest.builder()
                .virusName(virusTest.getVirusName())
                .testResult(virusTest.isTestResult()).build();

        patientRequest = PatientRequest.builder()
                .name(patient.getName())
                .INSZ(patient.getINSZ()).build();
    }

    @Test
    void addPatientToDoctor_WillThrowException_WhenDoctorIsNotFound(){
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(DoctorException.class,
                () -> doctorService.addPatientToDoctor(anyLong(), patientRequest));

        assertEquals("No doctor found", exception.getMessage());
    }

    @Test
    void removePatientFromDoctor_WillReturnMessageAfterDelete(){
        Patient secondPatient = Patient.builder()
                .name("Smith S.")
                .INSZ("111101-121.28")
                .Quarantined(true)
                .virusTests(new ArrayList<>())
                .build();

        doctor.addPatientToDoctor(patient);
        doctor.addPatientToDoctor(secondPatient);

        String resultMessage = MessageFormat.format("Patient {0} no longer with doctor {1}", patient.getName(), doctor.getName());
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(doctor));
        String result = doctorService.deletePatientFromDoctor(doctor.getId(), patient.getId());

        assertEquals(result, resultMessage);
        assertEquals(1, doctor.getPatients().size());
    }

    @Test
    void getQuarantinedPatientsFromDoctor_WillReturnListOfPatients(){
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(doctor));
        patient.setQuarantined(true);
        doctor.addPatientToDoctor(patient);

        List<PatientQuarantinedResponse> result = doctorService.getPatientsWithPositiveVirusTest(anyLong());

        assertEquals(1, result.size());
        assertEquals(patient.getName(), result.get(0).getName());
    }

    @Test
    void updatePatient_shouldReturnUpdatedPatientQuarantineResult(){
        VirusTestRequest updatedPatientVirus =
                VirusTestRequest.builder().virusName("updated")
                        .testResult(true).build();

        when(patientRepository.findById(anyLong())).thenReturn(Optional.ofNullable(patient));
        PatientResponse result = doctorService.updatePatientVirusTests(patient.getId(), updatedPatientVirus);

        assertEquals(patient.isQuarantined(), result.isQuarantined());
        assertEquals(patient.getName(), result.getName());
    }
}
