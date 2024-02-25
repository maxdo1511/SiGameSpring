package eu.hbb.newyeargame.service;

import eu.hbb.newyeargame.configuration.CustomProperty;
import eu.hbb.newyeargame.enums.QuestionType;
import eu.hbb.newyeargame.repo.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Queue;

@Service
public class AudioService {

    @Autowired
    private CustomProperty customProperty;
    private QuestionRepository questionRepository;
    private Deque<String> musicQueue;

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    private void fillQueue() {
        musicQueue = new ArrayDeque<>();
        File files = new File(customProperty.getMusic());
        for (File file : Objects.requireNonNull(files.listFiles())) {
            if (file.isFile()) {
                musicQueue.add(file.getName());
            }
        }
    }

    public Resource getMusicResource() {
        String music = musicQueue.poll();
        musicQueue.add(music);
        String audioFilePath = customProperty.getMusic() + music;
        Path path = Paths.get(audioFilePath);

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Не удалось загрузить аудио файл: " + e.getMessage());
        }
        return resource;
    }

    public Resource getAudioResource(long questionId) {
        String attribute = questionRepository.findQuestionEntityById(questionId).orElseThrow().getQuestionAttribute();
        if (!QuestionType.equalsAttributeType(attribute, QuestionType.SOUND)) {
            throw new RuntimeException("questionType is " + attribute.split("_")[0] + " but must be " + QuestionType.SOUND.toString());
        }
        String audioFilePath = customProperty.getSound() + attribute.split("_", 2)[1] + ".mp3";

        Path path = Paths.get(audioFilePath);

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Не удалось загрузить аудио файл: " + e.getMessage());
        }
        return resource;
    }


}
