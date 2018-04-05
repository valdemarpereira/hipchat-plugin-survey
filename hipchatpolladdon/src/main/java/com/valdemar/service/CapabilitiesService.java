package com.valdemar.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

import java.util.List;

public interface CapabilitiesService {
    @GET
    Call<String> capabilities(@Url String url);
}
