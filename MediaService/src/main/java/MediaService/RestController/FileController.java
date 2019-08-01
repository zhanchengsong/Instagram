package MediaService.RestController;

import MediaService.Domin.S3Image;
import MediaService.Service.AmazonS3ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private AmazonS3ClientService amazonS3ClientService;


    @PostMapping("image")
    public Map<String, String> uploadFile(@RequestPart(value = "file") MultipartFile file)
    {
        log.info("ContentType " + file.getContentType());

        String url = this.amazonS3ClientService.uploadFileToS3BucketwithPath(file, false);

        Map<String, String> response = new HashMap<>();

        response.put("message", "file [" + file.getOriginalFilename() + "] uploading request submitted successfully.");

        response.put("url",url);
        return response;
    }

    @DeleteMapping("image")
    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
    {
        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "file [" + fileName + "] removing request submitted successfully.");

        return response;
    }

    @GetMapping("image")
    public ResponseEntity<Resource> getFile(@RequestParam("file_name") String fileName, HttpServletRequest reuqest) {
        S3Image s3Image = amazonS3ClientService.getImageFromS3Bucket(fileName);
        if (s3Image != null) {
            Resource resource = new ByteArrayResource(s3Image.getData());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(s3Image.getContentTye()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
                            .body(resource);
        }
        return ResponseEntity.noContent().build();
    }
}
