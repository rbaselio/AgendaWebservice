package com.robertolopes.agenda.firebase;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.robertolopes.agenda.dao.AlunoDAO;
import com.robertolopes.agenda.dto.AlunoSync;
import com.robertolopes.agenda.event.AtualizaListaAlunoEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

public class AgendaMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> mensagem = remoteMessage.getData();
        Log.i("MENSAGEM RECEBIDA", String.valueOf(mensagem));

        converteParaAluno(mensagem);
    }

    private void converteParaAluno(Map<String, String> mensagem) {

        String chaveDeAcesso = "alunoSync";
        if (mensagem.containsKey(chaveDeAcesso)) {
            String json = mensagem.get(chaveDeAcesso);
            ObjectMapper mapper = new ObjectMapper();
            try {
                AlunoSync alunoSync = mapper.readValue(json, AlunoSync.class);
                AlunoDAO alunoDAO = new AlunoDAO(this);
                alunoDAO.sincroniza(alunoSync.getAlunos());
                alunoDAO.close();
                EventBus eventBus = EventBus.getDefault();
                eventBus.post(new AtualizaListaAlunoEvent());
            } catch (IOException e) {
                Log.e("ERRO RECEBER", e.getMessage());
            }
        }
    }
}
