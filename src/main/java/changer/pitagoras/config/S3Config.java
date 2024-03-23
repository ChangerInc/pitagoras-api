package changer.pitagoras.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:s3.properties")
public class S3Config {

    @Value("${accessKey}")
    private String accessKey;
    @Value("${secret}")
    private String secret;
    @Value("${region}")
    private String region;
    @Value("${bucketName}")
    private String bucketName;

    @Bean
    public AmazonS3 s3(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secret);
        return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
}
