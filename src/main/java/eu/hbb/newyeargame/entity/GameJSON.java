package eu.hbb.newyeargame.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameJSON {

    private GameEntity game;
    private List<RoundEntity> rounds;
    private List<ThemeEntity> themes;
    private List<QuestionEntity> questions;

}
