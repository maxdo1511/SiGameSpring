package eu.hbb.newyeargame.service;

import eu.hbb.newyeargame.configuration.CustomProperty;
import eu.hbb.newyeargame.controller.requests.ImageSaveRequest;
import eu.hbb.newyeargame.entity.QuestionEntity;
import eu.hbb.newyeargame.enums.QuestionType;
import eu.hbb.newyeargame.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private CustomProperty customProperty;
    private QuestionRepository questionRepository;

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void saveImageToDatabase(long id, String uuid) {
        QuestionEntity questionEntity = questionRepository.findById(id).orElseThrow();
        if (!questionEntity.getQuestionType().equals(QuestionType.IMAGE.toString())) {
            questionEntity.setQuestionType(QuestionType.IMAGE.toString());
        }
        questionEntity.setQuestionAttribute(questionEntity.getQuestionType() + "_" + uuid);
        questionRepository.save(questionEntity);
    }

    public String saveImage(ImageSaveRequest request) {
        String iconID = UUID.randomUUID().toString();
        saveImageToDatabase(request.getQuestionId(), iconID);
        try {
            saveImage(request.getImage(), customProperty.getImage() + iconID + ".png");
            return iconID;
        } catch (IOException e) {
            throw new RuntimeException("Image couldn't be save!");
        }
    }

    public void saveImage(byte[] bytes, String outputPath) throws IOException {
        if (outputPath == null) {
            outputPath = UUID.randomUUID().toString() + ".png";
        }
        Path path = Paths.get(outputPath);
        Files.write(path, bytes);
    }

    public void removeImage(String dir, String fileName) throws IOException {
        String removePath = dir + fileName;
        Path path = Paths.get(removePath);
        Files.delete(path);
    }

    public File getQuestionImage(long questionId) throws IOException {
        String attribute = questionRepository.findQuestionEntityById(questionId).orElseThrow().getQuestionAttribute();
        if (!QuestionType.equalsAttributeType(attribute, QuestionType.IMAGE)) {
            throw new RuntimeException("questionType is " + attribute.split("_")[0] + " but must be " + QuestionType.IMAGE.toString());
        }
        String imagePath = customProperty.getImage() + attribute.split("_", 2)[1] + ".png";
        return new File(imagePath);
    }
}
