package eu.hbb.newyeargame.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "customproprty")
public class CustomProperty {

    private String json;
    private String image;
    private String sound;
    private String music;
    private int roundStartTime;

}
