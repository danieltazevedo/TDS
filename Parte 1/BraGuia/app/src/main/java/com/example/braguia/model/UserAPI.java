package com.example.braguia.model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserAPI {
    @GET("user")
    Call<User> getUser();
}
