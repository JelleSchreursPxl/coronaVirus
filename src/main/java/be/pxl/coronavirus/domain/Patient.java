package be.pxl.coronavirus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String INSZ;
    private boolean Quarantined = false;

    @OneToMany(fetch = FetchType.EAGER)
    private List<VirusTest> virusTests = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "doctor_id")
//    private Doctor doctor;

}