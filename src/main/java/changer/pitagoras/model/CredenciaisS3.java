package changer.pitagoras.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredenciaisS3 {
    private String accessKey;
    private String secret;
    private String region;
    private String bucketName;
}
