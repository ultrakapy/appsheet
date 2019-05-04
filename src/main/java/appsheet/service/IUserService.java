package appsheet.service;

import appsheet.dto.User;

import java.util.List;

public interface IUserService {
    List<User> getYoungestKUsers(int k);
}
