package MediaService.Service.Impl;

import MediaService.Service.AmazonS3ClientService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {

    private String awsS3ImageBucket;
    private AmazonS3 amazonS3;

    @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3ImageBucket) {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(
                        awsCredentialsProvider
                )
                .withRegion(awsRegion.getName())
                .build();
        this.awsS3ImageBucket = awsS3ImageBucket;
    }

    @Async
    @Override
    public void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess) {
        String fileName = multipartFile.getOriginalFilename();
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3ImageBucket, fileName, file );
            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            PutObjectResult result = this.amazonS3.putObject(putObjectRequest);
            file.delete();

        } catch (IOException | AmazonServiceException ex) {
            log.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
    }

    @Async
    @Override
    public String uploadFileToS3BucketwithPath(MultipartFile multipartFile, boolean enablePublicReadAccess){
        String fileName = multipartFile.getOriginalFilename();
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3ImageBucket, fileName, file);
            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            PutObjectResult result = this.amazonS3.putObject(putObjectRequest);
            file.delete();
            String filePath = this.amazonS3.getBucketLocation(this.awsS3ImageBucket) + "/" + fileName;
            return filePath;


        } catch (IOException | AmazonServiceException ex) {
            log.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
        return null;
    }

    @Async
    @Override
    public void deleteFileFromS3Bucket(String fileName)
    {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(this.awsS3ImageBucket, fileName));
        } catch (AmazonServiceException ex) {
            log.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }
    }
}
