package eu.hbb.newyeargame.controller;

import eu.hbb.newyeargame.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audio")
public class AudioController {


    private AudioService audioService;

    @Autowired
    public void setAudioService(AudioService audioService) {
        this.audioService = audioService;
    }

    @GetMapping("/get/music")
    ResponseEntity<?> getMusic() {
        try {
            Resource resource = audioService.getMusicResource();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "file.mp3");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/sound/{id}")
    ResponseEntity<?> getSound(@PathVariable String id) {
        try {
            long questionId = Long.parseLong(id);
            Resource resource = audioService.getAudioResource(questionId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "file.mp3");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
