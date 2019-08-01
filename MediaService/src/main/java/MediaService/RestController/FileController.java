package MediaService.RestController;

import MediaService.Service.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private AmazonS3ClientService amazonS3ClientService;


    @PostMapping("image")
    public Map<String, String> uploadFile(@RequestPart(value = "file") MultipartFile file)
    {
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
}
