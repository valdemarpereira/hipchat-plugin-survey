package com.valdemar.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CapabilitiesService {
    @GET
    Call<String> capabilities(@Url String url);
}
