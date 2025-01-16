package ro.pub.cs.systems.eim.practicaltest02v9;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AnagramApiService {
    @GET("all/:{word}")
    Call<AnagramResponse> getAnagrams(@Path("word") String word);
}

