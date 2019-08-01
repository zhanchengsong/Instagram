package MediaService.Domin;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;



@Data
@RequiredArgsConstructor
public class S3Image {
    @NonNull
    public String contentTye;
    @NonNull
    public byte[] data;
}
