package appsheet.client;

import appsheet.dto.User;

import java.util.List;

public interface IUserServiceClient {
    List<User> getYoungestKUsers(int k);
}
