package be.pxl.coronavirus.service;

import be.pxl.coronavirus.domain.Doctor;
import be.pxl.coronavirus.domain.Patient;
import be.pxl.coronavirus.domain.VirusTest;
import be.pxl.coronavirus.domain.request.PatientRequest;
import be.pxl.coronavirus.domain.request.VirusTestRequest;
import be.pxl.coronavirus.domain.response.PatientQuarantinedResponse;
import be.pxl.coronavirus.domain.response.PatientResponse;
import be.pxl.coronavirus.domain.response.VirusTestResponse;
import be.pxl.coronavirus.exception.DoctorException;
import be.pxl.coronavirus.repository.DoctorRepository;
import be.pxl.coronavirus.repository.PatientRepository;
import be.pxl.coronavirus.repository.VirusTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImplDoctorService implements be.pxl.coronavirus.service.contracts.DoctorService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VirusTestRepository virusTestRepository;

    @Override
    @Secured("ROLE_DOCTOR")
    public PatientResponse addPatientToDoctor(Long doctorId, PatientRequest patientRequest) {
        Doctor doctor = findDoctorById(doctorId);
        Patient newPatient = Patient.builder().name(patientRequest.getName()).INSZ(patientRequest.getINSZ()).build();

        if(!doctor.addPatientToDoctor(newPatient)){
            throw new DoctorException("This patient is already a patient of this doctor");
        } else {
            patientRepository.save(newPatient);
        }
        return new PatientResponse(newPatient.getName(), newPatient.getINSZ(), newPatient.isQuarantined(), new ArrayList<>());
    }

    @Override
    @Secured("ROLE_DOCTOR")
    public PatientResponse getPatientByDoctorAndId(Long doctorId, Long patientId) {
        Doctor doctor = findDoctorById(doctorId);
        Patient patient = findPatientById(patientId);

        Patient foundPatient = findPatientBasedOnDoctor(doctor, patient);
        List<VirusTestResponse> virusTestByPatient = getVirustTestByPatient(foundPatient);

        return new PatientResponse(foundPatient.getName(), foundPatient.getINSZ(), foundPatient.isQuarantined(), virusTestByPatient);
    }

    @Override
    @Secured("ROLE_DOCTOR")
    public List<PatientQuarantinedResponse> getPatientsWithPositiveVirusTest(Long doctorId) {
        Doctor doctor = findDoctorById(doctorId);
        List<PatientQuarantinedResponse> quarantinedPatients =
                getQuarantinedPatients(doctor);

        if(quarantinedPatients.isEmpty()){
            throw new DoctorException("This doctor has no quarantined patients");
        } else {
            return quarantinedPatients;
        }
    }

    @Override
    @Secured("ROLE_DOCTOR")
    public String deletePatientFromDoctor(Long doctorId, Long patientId) {
        Doctor doctor = findDoctorById(doctorId);
        Patient patient = doctor.getPatients().stream().filter(p -> Objects.equals(p.getId(), patientId))
                .findFirst().orElseThrow(() -> new DoctorException("No patient found"));

        doctor.getPatients().remove(patient);
        patientRepository.delete(patient);

        return MessageFormat.format("Patient {0} no longer with doctor {1}", patient.getName(), doctor.getName());
    }

    @Override
    @Secured("ROLE_DOCTOR")
    public PatientResponse updatePatientVirusTests(Long patientId, VirusTestRequest virusTestRequest) {
        Patient patient = findPatientById(patientId);
        VirusTest virusTest = VirusTest.builder()
                .virusName(virusTestRequest.getVirusName())
                .testResult(virusTestRequest.isTestResult()).build();

        List<VirusTest> virusTests = patient.getVirusTests();
        virusTests.add(virusTest);
        virusTestRepository.save(virusTest);

        patient.setVirusTests(virusTests);
        patient.setQuarantined(virusTest.isTestResult());
        List<VirusTestResponse> testResponses = getVirustTestByPatient(patient);

        return new PatientResponse(patient.getName(), patient.getINSZ(), patient.isQuarantined(), testResponses);
    }

    @Override
    @Secured("ROLE_PATIENT")
    public List<VirusTestResponse> getVirusTestsByPatient(Long patientId) {
        Patient patient = findPatientById(patientId);
        List<VirusTestResponse> testResponses = new ArrayList<>();

        for (var testResult : getVirustTestByPatient(patient)) {
            testResponses.add(VirusTestResponse.builder()
                    .virusName(testResult.getVirusName())
                    .testResult(testResult.isTestResult())
                    .build());
        }
        return testResponses;
    }


    /*** REFINEMENT METHODS ********************/
    private Doctor findDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).stream().findFirst().orElseThrow(() -> new DoctorException("No doctor found"));
    }

    private Patient findPatientById(Long patientId) {
        return patientRepository
                .findById(patientId).stream()
                .findFirst().orElseThrow(() -> new DoctorException("No patient has been found"));
    }
    private static Patient findPatientBasedOnDoctor(Doctor doctor, Patient patient) {
        return doctor.getPatients().stream()
                .filter(p -> Objects.equals(p.getINSZ(), patient.getINSZ()))
                .findFirst()
                .orElseThrow(() -> new DoctorException(MessageFormat
                        .format("This doctor has no patient with INSZ: {0}", patient.getINSZ())));
    }

    private static List<VirusTestResponse> getVirustTestByPatient(Patient foundPatient) {
        return foundPatient.getVirusTests().stream()
                .map(v -> new VirusTestResponse(v.getVirusName(), v.isTestResult())).toList();
    }

    private static List<PatientQuarantinedResponse> getQuarantinedPatients(Doctor doctor) {
        return doctor.getPatients().stream().filter(
                        Patient::isQuarantined)
                .map(p -> new PatientQuarantinedResponse(p.getName(), p.getINSZ(), p.isQuarantined())).toList();
    }
}
