package be.pxl.coronavirus.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse implements Serializable {
    private String name;
    private String INSZ;
    private boolean quarantined;
    private List<VirusTestResponse> virusTests;
}