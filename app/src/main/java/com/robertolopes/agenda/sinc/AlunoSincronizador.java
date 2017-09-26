package com.robertolopes.agenda.sinc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.robertolopes.agenda.dao.AlunoDAO;
import com.robertolopes.agenda.dto.AlunoSync;
import com.robertolopes.agenda.event.AtualizaListaAlunoEvent;
import com.robertolopes.agenda.modelo.Aluno;
import com.robertolopes.agenda.preferences.AlunoPreferences;
import com.robertolopes.agenda.retrofit.RetrofitInializador;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {
    private final Context context;
    private EventBus bus = EventBus.getDefault();
    private AlunoPreferences preferences;

    public AlunoSincronizador(Context context) {
        this.context = context;
        preferences = new AlunoPreferences(context);
    }

    public void buscaTodos() {
        if (preferences.temVersao()) {
            buscaNovos();
        } else {
            buscaAlunos();
        }
    }

    private void buscaNovos() {
        String versao = preferences.getVersao();
        Call<AlunoSync> call = new RetrofitInializador().getAlunoService().novos(versao);
        call.enqueue(buscaAlunosCallBack());
    }

    private void buscaAlunos() {
        Call<AlunoSync> call = new RetrofitInializador().getAlunoService().lista();
        call.enqueue(buscaAlunosCallBack());
    }

    public void sincronizaAlunosInternos() {
        final AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.listaNaoSincronizados();
        Call<AlunoSync> call = new RetrofitInializador().getAlunoService().atualiza(alunos);
        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {

            }
        });
    }

    @NonNull
    private Callback<AlunoSync> buscaAlunosCallBack() {
        return new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                String versao = alunoSync.getMomentoDaUltimaModificacao();
                preferences.salvaVersao(versao);

                List<Aluno> alunos = alunoSync.getAlunos();
                AlunoDAO alunoDAO = new AlunoDAO(context);
                alunoDAO.sincroniza(alunos);
                alunoDAO.close();

                Log.i("VERSAO", preferences.getVersao());
                bus.post(new AtualizaListaAlunoEvent());
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                bus.post(new AtualizaListaAlunoEvent());
                Log.e("ALUNOS FALHOU CHAMADO", "DEU BO AQUI");
            }
        };
    }

    public void deleta(final Aluno aluno) {
        Call<Void> delete = new RetrofitInializador().getAlunoService().delete(aluno.getId());
        delete.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AlunoDAO alunoDAO = new AlunoDAO(context);
                alunoDAO.deleta(aluno);
                alunoDAO.close();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}