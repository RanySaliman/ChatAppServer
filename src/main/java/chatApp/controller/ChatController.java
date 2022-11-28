package chatApp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@CrossOrigin
public class ChatController {
    @MessageMapping("/hello")
    @SendTo("/topic/mainChat")
    public ChatMessage greeting(HelloMessage message) throws Exception {
        return new ChatMessage("SYSTEM", message.getName() + "joined the chat");
    }
    @MessageMapping("/plain")
    @SendTo("/topic/mainChat")
    public ChatMessage sendPlainMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/sendMessage/{id}")
    @SendTo("/topic/{id}")
    public ChatMessage sendMessage(@PathVariable String id, ChatMessage message) {
        System.out.println(id);
        System.out.println(message);
        return message;
    }
    static class ChatMessage {
        private String sender;
        private String content;
        public ChatMessage() {
        }
        public ChatMessage(String sender, String content) {
            this.sender = sender;
            this.content = content;
        }
        public String getSender() {
            return sender;
        }
        public void setSender(String sender) {
            this.sender = sender;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        @Override
        public String toString() {
            return "ChatMessage{" +
                    "sender='" + sender + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
    static class HelloMessage {
        private String name;
        public HelloMessage() {
        }
        public HelloMessage(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        @Override
        public String toString() {
            return "HelloMessage{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}