package ppk.perpus.service;

import ppk.perpus.dto.UserDto;

public interface UserService {
    public UserDto createUser(UserDto user);
    public UserDto getUserByEmail(String email);
}
