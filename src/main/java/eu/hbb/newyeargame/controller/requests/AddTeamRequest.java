package eu.hbb.newyeargame.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTeamRequest {

    private String uuid;
    private String name;
    private List<String> members;

}
