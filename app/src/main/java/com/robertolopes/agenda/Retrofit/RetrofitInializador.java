package com.robertolopes.agenda.Retrofit;

import com.robertolopes.agenda.Services.AlunoService;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInializador {
    private final Retrofit retrofit;

    public RetrofitInializador() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.10.1.76:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public AlunoService getAlunoService() {
        return retrofit.create(AlunoService.class);
    }
}
