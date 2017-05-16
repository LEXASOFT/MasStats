package com.github.lexasoft.masstats.api;

import com.github.lexasoft.masstats.models.Coincidence;
import com.github.lexasoft.masstats.models.Person;
import com.github.lexasoft.masstats.models.Site;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("sites")
    Call<List<Site>> getSites();
    @GET("persons/{id}")
    Call<List<Person>> getPersons(@Path("id") int siteId);
    @GET("persons")
    Call<List<Person>> getPersons();
    @GET("persons/{dates}/{site}/{person}")
    Call<List<Coincidence>> getPersons(@Path("dates") String dates, @Path("site") int siteId, @Path("person") int personId);
}
