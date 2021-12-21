package commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingCommand extends ListenerAdapter {

    private final String prefix = "!";

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(msg.getAuthor().isBot()){
            return;
        }
        if(msg.getContentRaw().equalsIgnoreCase(prefix+"ping")){
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(resp -> {
                        resp.editMessageFormat("Ping: " + (System.currentTimeMillis() - time)  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
                    });
        }

    }
}
