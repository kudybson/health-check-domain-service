package pl.akh.model.rq;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Groups;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CreateUserRQ implements Serializable {

    private boolean enabled;
    private List<Groups> groups;
    private String username;
    private String email;
    private String firstName;
    private String lastName;


}
