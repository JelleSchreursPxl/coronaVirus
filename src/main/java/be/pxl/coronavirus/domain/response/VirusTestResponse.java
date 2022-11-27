package be.pxl.coronavirus.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VirusTestResponse implements Serializable {
    private String virusName;
    private boolean testResult;
}