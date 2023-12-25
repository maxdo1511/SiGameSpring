package eu.hbb.newyeargame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ConfigReaderService {

    private static final String encoding = "UTF-8";

    public <T> T readObject(Class<T> obj, String fileName) {
        return readObjectFromPath(obj, fileName);
    }

    /**
     * @param path должен иметь / в конце, например "data/config/"
     * @return созданый объект, если что-то пошло не так возвращает пустой объект
     */
    public <T> T readObject(Class<T> obj, String fileName, String path) {
        return readObjectFromPath(obj, path + fileName);
    }

    private String readFile(String path) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path)))) {
            String string_data = "";
            while(bufferedReader.ready())

            {
                string_data = string_data + bufferedReader.readLine();
            }
            return string_data;
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    private  <T> T readObjectFromPath(Class<T> obj, String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(readFile(path), obj);
        } catch (JsonProcessingException e) {
            try {
                Object o = obj.getConstructors()[0].newInstance();
                return (T) o;
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
