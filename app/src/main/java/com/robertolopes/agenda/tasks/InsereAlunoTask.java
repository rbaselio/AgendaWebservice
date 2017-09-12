package com.robertolopes.agenda.tasks;

import android.os.AsyncTask;

import com.robertolopes.agenda.converter.AlunoConverter;
import com.robertolopes.agenda.modelo.Aluno;
import com.robertolopes.agenda.web.WebClient;

public class InsereAlunoTask extends AsyncTask {
    private final Aluno aluno;

    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String json = new AlunoConverter().converteParaJSONCompleto(aluno);
        new WebClient().insere(json);
        return null;
    }
}
