package com.robertolopes.agenda.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by roberto.lopes on 22/09/2017.
 */
public class AlunoPreferences {
    public static final String VERSAO_DO_DADO = "versao_do_dado";
    private static final String ALUNO_PREFERENCES = "com.robertolopes.agenda.preferences.AlunoPreferences";
    private Context context;

    public AlunoPreferences(Context context) {

        this.context = context;
    }

    public void salvaVersao(String versao) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSAO_DO_DADO, versao);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(ALUNO_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getVersao() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(VERSAO_DO_DADO, "");
    }

    public boolean temVersao() {
        return !getVersao().isEmpty();
    }
}
