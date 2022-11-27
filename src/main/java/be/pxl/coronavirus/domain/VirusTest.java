package be.pxl.coronavirus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "virus_test")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VirusTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String virusName;
    private boolean testResult;

    public VirusTest(String _virusName, boolean _testResult) {
        virusName = _virusName;
        testResult = _testResult;
    }

//    @ManyToOne
//    @JoinColumn(name = "patient_id")
//    private Patient patient;

}