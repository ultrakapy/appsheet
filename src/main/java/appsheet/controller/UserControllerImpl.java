package appsheet.controller;

import appsheet.dto.User;
import appsheet.repository.IUserRepository;

import java.util.List;
/*
UserController is responsible for most validation and business logic
 */
public class UserControllerImpl implements IUserController {
    private IUserRepository userRepository;

    public UserControllerImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getYoungestKUsers(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Number of users must be greater than zero.");
        }
        return userRepository.getYoungestKUsers(k);
    }
}
