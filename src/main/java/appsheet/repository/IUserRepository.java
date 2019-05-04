package appsheet.repository;

import appsheet.dto.User;
import org.apache.http.HttpException;

import java.util.List;

public interface IUserRepository {
    List<User> getYoungestKUsers(int k);
}
