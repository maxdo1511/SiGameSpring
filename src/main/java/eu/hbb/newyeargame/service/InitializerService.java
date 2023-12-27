package eu.hbb.newyeargame.service;

import eu.hbb.newyeargame.configuration.CustomProperty;
import eu.hbb.newyeargame.models.Game;
import eu.hbb.newyeargame.models.GameJSON;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InitializerService {

    public String PATH;
    private List<Game> loadedGames = new ArrayList<>();
    @Autowired
    private ConfigReaderService configReaderService;
    @Autowired
    private GameService gameService;

    public InitializerService(@Autowired CustomProperty customProperty) {
        this.PATH = customProperty.getJson();
    }

    @PostConstruct
    private void postConstruct() {
        loadAllJSON(PATH);
        saveAllJSON();
    }

    /**
     * @param path передать PATH поле класса
     */
    public void loadAllJSON(String path) {
        File dir = new File(path);
        createDirIfNotExists(dir);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().toLowerCase().endsWith(".json")) {
                if (file.isFile()) {
                    GameJSON gameJSON = configReaderService.readObject(GameJSON.class, file.getPath());
                    loadedGames.add(gameService.parseGameJSONToGame(gameJSON));
                }
                if (file.isDirectory()) {
                    loadAllJSON(file.getPath());
                }
            }
        }
    }

    private void saveAllJSON() {
        for (Game game : loadedGames) {
            gameService.saveGameToDatabaseIfNotExists(game);
        }
    }

    private void createDirIfNotExists(File dir) {
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

}
