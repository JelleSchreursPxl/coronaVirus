package be.pxl.coronavirus.service.contracts;

import be.pxl.coronavirus.domain.Patient;
import be.pxl.coronavirus.domain.request.PatientRequest;
import be.pxl.coronavirus.domain.request.VirusTestRequest;
import be.pxl.coronavirus.domain.response.PatientQuarantinedResponse;
import be.pxl.coronavirus.domain.response.PatientResponse;
import be.pxl.coronavirus.domain.response.VirusTestResponse;

import java.util.List;

public interface IDoctorService {
    PatientResponse addPatientToDoctor(Long doctorId, PatientRequest patientRequest);
    PatientResponse getPatientByDoctorAndId(Long doctorId, Long patientId);

    List<PatientQuarantinedResponse> getPatientsWithPositiveVirusTest(Long doctorId);
    String deletePatientFromDoctor(Long doctorId, Long patientId);
    PatientResponse updatePatientVirusTests(Long patientId, VirusTestRequest virusTestRequest);

    List<VirusTestResponse> getVirusTestsByPatient(Long patientId);
}
