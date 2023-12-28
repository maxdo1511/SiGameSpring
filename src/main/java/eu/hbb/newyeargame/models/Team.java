package eu.hbb.newyeargame.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {

    private String uuid;
    private String name;
    private String owner;
    private List<String> members;
    private int score;
    private List<Integer> scoreHistory;
    private List<Float> ansTimeHistory;

}
