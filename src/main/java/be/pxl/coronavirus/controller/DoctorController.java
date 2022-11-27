package be.pxl.coronavirus.controller;

import be.pxl.coronavirus.domain.Patient;
import be.pxl.coronavirus.domain.request.PatientRequest;
import be.pxl.coronavirus.domain.request.VirusTestRequest;
import be.pxl.coronavirus.domain.response.PatientQuarantinedResponse;
import be.pxl.coronavirus.domain.response.PatientResponse;
import be.pxl.coronavirus.domain.response.VirusTestResponse;
import be.pxl.coronavirus.service.contracts.IDoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor                                                                    // autowiring wordt voorzien
@Slf4j
public class DoctorController {

    private final IDoctorService doctorService;

    @PostMapping("/doctor/{doctorId}/addPatient")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse addPatientToDoctor(@PathVariable Long doctorId,
                                              @RequestBody PatientRequest patientRequest){
        return doctorService.addPatientToDoctor(doctorId, patientRequest);
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponse getPatientAndInformation(@PathVariable Long doctorId,
                                                    @PathVariable Long patientId){
        return doctorService.getPatientByDoctorAndId(doctorId, patientId);
    }

    @GetMapping("/doctor/{doctorId}/quarantinedPatients")
    @ResponseStatus(HttpStatus.OK)
    public List<PatientQuarantinedResponse> getQuarantinedPatients(@PathVariable Long doctorId){
        return doctorService.getPatientsWithPositiveVirusTest(doctorId);
    }

    @PutMapping("/patient/{patientId}/addTestResult")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse updatePatientQuarantineTests(@PathVariable Long patientId,
                                                                   @RequestBody VirusTestRequest virusTestRequest){
        return doctorService.updatePatientVirusTests(patientId, virusTestRequest);
    }

    @GetMapping("/patient/{patientId}/testResults")
    @ResponseStatus(HttpStatus.OK)
    public List<VirusTestResponse> getTestResultsByPatient(@PathVariable Long patientId){
        return doctorService.getVirusTestsByPatient(patientId);
    }

    @DeleteMapping("/doctor/{doctorId}/patient/{patientId}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePatientFromDoctor(@PathVariable Long patientId,
                                          @PathVariable Long doctorId){
        return doctorService.deletePatientFromDoctor(doctorId, patientId);
    }
}
