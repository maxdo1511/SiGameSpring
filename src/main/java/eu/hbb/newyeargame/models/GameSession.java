package eu.hbb.newyeargame.models;

import eu.hbb.newyeargame.entity.QuestionEntity;
import eu.hbb.newyeargame.entity.RoundEntity;
import eu.hbb.newyeargame.enums.GameStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSession {

    private String uuid;
    private GameStage stage;
    private Map<String, Team> teams;
    private Deque<String> teamQueue;
    private int maxQuestionAnsTime;
    private long startQuestionAns;
    private long game;
    private Map<QuestionEntity, Boolean> questions;
    private RoundEntity currentRound;
    private QuestionEntity currentQuestion;

}
