package eu.hbb.newyeargame.controller.response;

import eu.hbb.newyeargame.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundInfoResponse {

    private String name;
    private String ansTeam;
    private Map<QuestionEntity, Boolean> questionEntities;

}
