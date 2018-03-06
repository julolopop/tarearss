package com.example.julolopop.tarearss.api;

import com.example.julolopop.tarearss.pojo.Email;
import com.example.julolopop.tarearss.pojo.Tarea;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Julolopop on 06/03/2018.
 */

public interface ApiServices {
    @GET("api/tareas")
    Call<ArrayList<Tarea>> getTareas();


    @POST("api/tareas")
    Call<Tarea> createSite(@Body Tarea tarea);

    @PUT("api/tareas/{id}")
    Call<Tarea> updateSite(@Body Tarea tarea, @Path("id") int id);

    @DELETE("api/tareas/{id}")
    Call<ResponseBody> deleteSite(@Path("id") int id);

    @POST("api/email")
    Call<ResponseBody> sendEmail(@Body Email email);
}
