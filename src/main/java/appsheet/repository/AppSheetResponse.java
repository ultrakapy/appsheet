package appsheet.repository;

import com.google.api.client.util.Key;

public class AppSheetResponse {
    @Key
    public int[] result;
    @Key
    public String token;
}
