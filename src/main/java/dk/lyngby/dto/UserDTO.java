package dk.lyngby.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.Principal;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class UserDTO implements Principal {

    private String username;
    private Set<String> roles;

    public UserDTO(String username, String[] roles) {
        this.username = username;
        this.roles = Set.of(roles);
    }

    @Override
    public String getName() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
