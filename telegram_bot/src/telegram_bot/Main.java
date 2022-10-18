package telegram_bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Criacao do objeto bot com as informacoes de acesso.
        TelegramBot bot = new TelegramBot("TOKEN");

        // Objeto responsavel por receber as mensagens.
        GetUpdatesResponse updatesResponse;

        // Objeto responsavel por gerenciar o envio de respostas.
        SendResponse sendResponse;

        // Objeto responsavel por gerenciar o envio de acoes do chat.
        BaseResponse baseResponse;

        // Controle de off-set, isto e, a partir deste ID sera lido as mensagens
        // pendentes na fila.
        int m = 0;

        // Loop infinito pode ser alterado por algum timer de intervalo curto.
        while (true) {
            // Executa comando no Telegram para obter as mensagens pendentes a partir de um
            // off-set (limite inicial).
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

            // Lista de mensagens.
            List<Update> updates = updatesResponse.updates();

            // Analise de cada acao da mensagem.
            for (Update update : updates) {

                // Atualizacao do off-set.
                m = update.updateId() + 1;

                //Capturar nome do usuário
                String user = update.message().chat().firstName();

                System.out.println("Recebendo mensagem: " + update.message().text());

                // Envio de "Escrevendo" antes de enviar a resposta.
                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                // Verificacao de acao de chat foi enviada com sucesso.
                System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

                // Envio da mensagem de resposta.
                // Inclusão de regex para identificar o padrão das perguntas
                if (update.message().text().matches(".*(?i)Oi.*|.*(?i)Olá.*|.*(?i)Ola.*|.*(?i)Bom dia.*|.*(?i)Boa noite.*|.*(?i)Boa tarde.*|")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Olá, " + user + ". Tudo bem com você?"));
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                } else if (update.message().text().matches("^(?i)sim.*|.*(?i)bem.*|.*(?i)ótimo.*|.*(?i)otimo.*")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Bom saber que está bem. Fico muito feliz"));
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                } else if (update.message().text().matches("^(?i)não$|^(?i)nao$|.*(?i)mal.*|.*(?i)péssimo.*")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Poxa! " +
                            "Posso fazer algo por você?"));
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                } else if (update.message().text().matches(".*(?i)pode sim.*|^(?i)pode.*")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Me diga então o que posso fazer por você? "));
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                } else if (update.message().text().matches(".*(?i)não pode.*|.*(?i)nao pode.*|")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Que pena, desejo um ótimo dia pra você"));
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                } else {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Não entendi..."));
                    // Verificacao de mensagem enviada com sucesso.
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                }
            }
        }
    }
}