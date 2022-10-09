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
        TelegramBot bot = new TelegramBot("");

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

                System.out.println("Recebendo mensagem: " + update.message().text());

                // Envio de "Escrevendo" antes de enviar a resposta.
                baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                // Verificacao de acao de chat foi enviada com sucesso.
                System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

                // Envio da mensagem de resposta.
                if (update.message().text().startsWith("Boa noite")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Boa noite, Raquel. Como você está?"));
                    System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                } else if (update.message().text().startsWith("Qual a cotação do dólar hoje?")) {
                    sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "A cotação do dólar é R$5,19"));
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