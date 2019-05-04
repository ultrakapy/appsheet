package appsheet.service;

import appsheet.controller.IUserController;
import appsheet.controller.UserControllerImpl;
import appsheet.dto.User;

import java.util.List;
/*
UserService handles request from the client through the UserService client
 */
public class UserServiceImpl implements IUserService {
    private IUserController userController;

    public UserServiceImpl(IUserController userController) {
        this.userController = userController;
    }

    @Override
    public List<User> getYoungestKUsers(int k) {
        return userController.getYoungestKUsers(k);
    }
}
