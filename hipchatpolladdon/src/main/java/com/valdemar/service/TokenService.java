package com.valdemar.service;

import com.valdemar.model.Token;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface TokenService {
    @FormUrlEncoded
    @POST
    Call<Token> token(@Url String url, @Header("Authorization") String token, @Field("grant_type") String grantType);
}
