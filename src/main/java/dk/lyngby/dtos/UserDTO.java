package dk.lyngby.dtos;

import dk.lyngby.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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

    public UserDTO(User user) {
        this.username = user.getUserName();
        this.roles = user.getRolesAsStrings();
    }

    public static List<UserDTO> toUserDTOList(List<User> users) {
        List<UserDTO> userDTOList =  new ArrayList<>();
        for (User user : users) {
            userDTOList.add(new UserDTO(user.getUserName(), user.getRolesAsStrings().toArray(new String[0])));
        }
        return userDTOList;

    }

    @Override
    public String getName() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public User toUser() {
        return new User(username, roles.toString());
    }
}
