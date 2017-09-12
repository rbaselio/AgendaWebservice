package com.robertolopes.agenda.web;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by renan on 20/01/16.
 */
public class WebClient {
    public String post(String json) {
        String endereco = "https://www.caelum.com.br/mobile";
        String resposta = realizaConexao(json, endereco);
        if (resposta != null) return resposta;
        return null;
    }

    @Nullable
    private String realizaConexao(String json, String spec) {
        try {
            URL url = new URL(spec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            PrintStream output = new PrintStream(connection.getOutputStream());
            output.println(json);

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());
            String resposta = scanner.next();
            return resposta;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insere(String json) {
        String endereco = "http://10.10.1.76:8080/api/aluno";
        Log.d("EXECUTANDO", realizaConexao(json, endereco));


    }
}
