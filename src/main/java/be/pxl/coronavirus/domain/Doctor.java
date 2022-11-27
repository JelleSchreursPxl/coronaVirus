package be.pxl.coronavirus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Patient> patients = new ArrayList<>();

    public boolean addPatientToDoctor(Patient patient){
        List<String> patientNames = patients.stream().map(Patient::getINSZ).toList();
        if(!patientNames.contains(patient.getINSZ())){
            patients.add(patient);
            return true;
        } else {
            return false;
        }
    }
}