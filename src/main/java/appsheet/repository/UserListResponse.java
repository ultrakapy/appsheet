package appsheet.repository;

import com.google.api.client.util.Key;

public class UserListResponse {
    @Key
    public int[] result;
    @Key
    public String token;
}
