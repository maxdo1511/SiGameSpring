package eu.hbb.newyeargame.models;

import eu.hbb.newyeargame.entity.GameEntity;
import eu.hbb.newyeargame.entity.QuestionEntity;
import eu.hbb.newyeargame.entity.RoundEntity;
import eu.hbb.newyeargame.entity.ThemeEntity;
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
