package appsheet.controller;

import appsheet.dto.User;

import java.util.List;

public interface IUserController {
    List<User> getYoungestKUsers(int k);
}
