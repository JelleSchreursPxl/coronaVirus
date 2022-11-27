package be.pxl.coronavirus.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VirusTestRequest implements Serializable {
    private String virusName;
    private boolean testResult;
}