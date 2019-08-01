package MediaService.Service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {
    void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess);
    String uploadFileToS3BucketwithPath(MultipartFile multipartFile, boolean enablePublicReadAccess);
    void deleteFileFromS3Bucket(String fileName);
}
