package appsheet.repository;

import appsheet.dto.User;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/*
UserRepository is responsible for accessing data from various data sources (e.g., database, web service).
In this case we are calling a web service.
 */
public class UserRepositoryImpl implements IUserRepository {
    private final String APPSHEET_SERVICE_ENDPOINT = "https://appsheettest1.azurewebsites.net/sample";
    // May want to move this regex to a Util class/method so it can be reusable elsewhere
    private final String PHONE_REGEX = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport(); // ideally use DI in real project
    private final JsonFactory JSON_FACTORY = new JacksonFactory(); // ideally use DI in real project

    private HttpRequestFactory requestFactory;

    public UserRepositoryImpl() {
        requestFactory = HTTP_TRANSPORT.createRequestFactory(request ->
                request.setParser(new JsonObjectParser(JSON_FACTORY)));
    }

    private String getUserListUrl() {
        return String.format("%s/list", APPSHEET_SERVICE_ENDPOINT);
    }

    private String getUserListUrlByToken(String token) {
        return String.format("%s/list?token=%s", APPSHEET_SERVICE_ENDPOINT, token);
    }

    private String getUserDetailUrl(int userId) {
        return String.format("%s/detail/%d", APPSHEET_SERVICE_ENDPOINT, userId);
    }

    @Override
    public List<User> getYoungestKUsers(int k) {
        // topUsersPQ orders users from oldest to youngest
        // so that we can efficiently (in constant time) remove the oldest user & adding users is O(Log(k))
        // for each user and O(N*Log(k)) overall, where N is the total number of users with valid phone numbers.
        // Below I make sure the size of topUsersPQ never exceeds k+1, so it's also space efficient
        PriorityQueue<User> topUsersPQ = new PriorityQueue<>(k, (user1, user2) -> user2.getAge() - user1.getAge());

        List<Integer> userIdList = getUserIdList();

        // Iterate through all User IDs, get details, then add user to the top K priority queue
        // if they have a validate phone number
        if (userIdList != null && userIdList.size() > 0) {
            for (Integer userId : userIdList) {
                try {
                    HttpRequest userDetailRequest =
                            requestFactory.buildGetRequest(new GenericUrl(getUserDetailUrl(userId)));
                    User user = userDetailRequest.execute().parseAs(User.class);
                    if (isValidPhoneNumber(user.getNumber())) {
                        topUsersPQ.offer(user);
                        if (topUsersPQ.size() > k) {
                            topUsersPQ.poll(); // remove extra user to keep just the top k users
                        }
                    }
                } catch (Exception e) {
                    // As noted in assumptions: I would log this exception
                    // Continue fetching User details
                }
            }
        }

        List<User> topUsersList = new ArrayList<>(topUsersPQ);
        // sort top uses by name, which takes O(k*Log(k))
        topUsersList.sort((user1, user2) -> user1.getName().compareTo(user2.getName()));
        return topUsersList;
    }

    private List<Integer> getUserIdList() {
        ArrayList<Integer> userIdList = new ArrayList<>();

        try {
            HttpRequest userListRequest = requestFactory.buildGetRequest(new GenericUrl(getUserListUrl()));
            UserListResponse userListResponse = userListRequest.execute().parseAs(UserListResponse.class);

            // Get all User IDs, making multiple service calls as long as there are more IDs available
            // (denoted by the existence of a token)
            while (userListResponse != null
                    && userListResponse.result != null && userListResponse.result.length > 0) {
                userIdList.addAll(Arrays.stream(userListResponse.result).boxed().collect(Collectors.toList()));

                // If more User IDs are available, make another service call
                if (userListResponse.token != null && !userListResponse.token.isEmpty()) {
                    try {
                        HttpRequest userListNextPageRequest =
                                requestFactory.buildGetRequest(
                                        new GenericUrl(getUserListUrlByToken(userListResponse.token)));
                        userListResponse = userListNextPageRequest.execute().parseAs(UserListResponse.class);
                    } catch (Exception e) {
                        // As noted in assumptions: I would log this exception
                        // Continue fetching any available User IDs
                    }
                } else {
                    userListResponse = null; // Signal we are done getting User IDs
                }
            }
        } catch (IOException e) {
            // As noted in assumptions: LOG "IO exepctions (e.g., unable to build request URL, etc)";
        }

        return userIdList;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches(PHONE_REGEX);
    }
}
