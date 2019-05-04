package appsheet.client;

import appsheet.controller.IUserController;
import appsheet.controller.UserControllerImpl;
import appsheet.dto.User;
import appsheet.repository.IUserRepository;
import appsheet.repository.UserRepositoryImpl;
import appsheet.service.IUserService;
import appsheet.service.UserServiceImpl;

import java.util.List;

/*
Any consumers/users of the UserService will access it's methods through the UserServiceClient.
We could package the UserServiceClient into it's own jar so that it can be used by any other internal
services that need to access the UserService.
 */

public class UserServiceClientImpl implements IUserServiceClient {
    public UserServiceClientImpl() {
        // default constructor
    }

    // ideally the various objects would be created using dependency injection
    // i'm explicitly creating the objects here for simplicity
    private IUserService getUserService() {
        IUserRepository userRepository = new UserRepositoryImpl();
        IUserController userController = new UserControllerImpl(userRepository);
        return new UserServiceImpl(userController);
    }

    @Override
    public List<User> getYoungestKUsers(int k) {
        return getUserService().getYoungestKUsers(k);
    }
}
