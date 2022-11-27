package be.pxl.coronavirus.domain.response;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link be.pxl.coronavirus.domain.Patient} entity
 */
@Data
public class PatientQuarantinedResponse implements Serializable {
    private final String name;
    private final String INSZ;
    private final boolean Quarantined;
}