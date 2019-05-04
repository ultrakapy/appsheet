package appsheet;

import appsheet.client.IUserServiceClient;
import appsheet.client.UserServiceClientImpl;
import appsheet.dto.User;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        IUserServiceClient userServiceClient = new UserServiceClientImpl();
        try {
            List<User> top5YoungestUsers = userServiceClient.getYoungestKUsers(5);
            if (top5YoungestUsers != null && !top5YoungestUsers.isEmpty()) {
                for (User user : top5YoungestUsers) {
                    System.out.println(user);
                }
            } else {
                System.out.println("No users available.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
