package changer.pitagoras.service;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:s3.properties")
public class S3Service implements ContratoS3{

    @Value("${bucketName}")
    private String bucketName;

    private final AmazonS3 s3;

    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String saveArquivo(MultipartFile arquivo, UUID idUsuario) {
        String originalNomeArquivo = arquivo.getOriginalFilename();
        String pasta = idUsuario+"/";
        int count = 0;
        int maxTries = 3;
        while(true) {
            try {
                File arquivo1 = converterMultiPartParaArquivo(arquivo);
                PutObjectResult putObjectResult = s3.putObject(bucketName, pasta+originalNomeArquivo, arquivo1);
                return putObjectResult.getContentMd5();
            } catch (IOException e) {
                if (++count == maxTries) throw new RuntimeException(e);
            }
        }

    }

    @Override
    public byte[] downloadArquivo(String nomeArquivo, UUID idUsuario) {
        String pasta = idUsuario+"/";
        S3Object object = s3.getObject(bucketName, pasta+nomeArquivo);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public String deleteArquivo(String nomeArquivo, UUID idUsuario) {
        s3.deleteObject(bucketName,idUsuario+"/"+nomeArquivo);
        return "Arquivo deletado";
    }

    @Override
    public List<String> listAllArquivos() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());

    }

    private File converterMultiPartParaArquivo(MultipartFile arquivo) throws IOException {
        File convertArquivo = new File(arquivo.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertArquivo);
        fos.write( arquivo.getBytes() );
        fos.close();
        return convertArquivo;
    }

    public String obterUrlPublica(String nomeArquivo,String codigo){
        String path = codigo+"/"+nomeArquivo;
        return s3.getUrl(bucketName,path).toString();
    }
}
