package com.robertolopes.agenda.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.robertolopes.agenda.retrofit.RetrofitInializador;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by roberto.lopes on 18/09/2017.
 */
public class AgendaInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN FIREBASE", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        enviaTokenParaServidor(refreshedToken);
    }

    private void enviaTokenParaServidor(final String token) {

        Call<Void> enviaToken = new RetrofitInializador().getDispositivoService().enviaToken(token);
        enviaToken.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("TOKEN FIREBASE", "Token enviado: " + token);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TOKEN FIREBASE", "Token não enviado: " + t.getMessage());
            }
        });
    }
}
