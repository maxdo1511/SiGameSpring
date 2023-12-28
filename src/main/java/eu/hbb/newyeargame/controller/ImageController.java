package eu.hbb.newyeargame.controller;

import eu.hbb.newyeargame.configuration.CustomProperty;
import eu.hbb.newyeargame.controller.requests.ImageSaveRequest;
import eu.hbb.newyeargame.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;
    @Autowired
    private CustomProperty customProperty;

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/load-icon")
    ResponseEntity<?> saveImage(@RequestBody ImageSaveRequest request) {
        try {
            imageService.saveImage(request);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get/{id}")
    ResponseEntity<?> getQuestionImage(@PathVariable String id) {
        try {
            long questionId = Long.parseLong(id);
            File img = imageService.getQuestionImage(questionId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(Files.readAllBytes(img.toPath()));
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
