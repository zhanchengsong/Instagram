package MediaService.Service;

import MediaService.Domin.S3Image;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {
    void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess);
    String uploadFileToS3BucketwithPath(MultipartFile multipartFile, boolean enablePublicReadAccess);
    void deleteFileFromS3Bucket(String fileName);
    S3Image getImageFromS3Bucket(String fileName);


}
