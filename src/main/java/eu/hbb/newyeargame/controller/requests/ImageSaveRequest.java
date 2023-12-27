package eu.hbb.newyeargame.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageSaveRequest {

    private byte[] image;
    private long questionId;

}
