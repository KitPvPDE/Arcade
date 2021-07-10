package net.kitpvp.plugins.arcade.chat;

import net.kitpvp.chat.api.Connection;
import net.kitpvp.chat.api.MsgFormat;
import net.kitpvp.network.chat.ChatFormats;
import net.kitpvp.plugins.kitpvpcore.modules.chat.SpigotChat;
import org.jetbrains.annotations.PropertyKey;

public class Chat {

    public static void localeResponse(Connection sender,
                                      @PropertyKey(resourceBundle = "locales.arcade.arcade") String languageKey, Object... args) {
        net.kitpvp.chat.Chat.localeResponse(sender, ChatFormats.ARCADE, languageKey, args);
    }

    public static void commandResponse(Connection sender, String text, Object... args) {
        net.kitpvp.chat.Chat.commandResponse(sender, ChatFormats.ARCADE, text, args);
    }

    public static void localeAnnounce(@PropertyKey(resourceBundle = "locales.arcade.arcade") String languageKey, Object... args) {
        SpigotChat.announce(ChatFormats.ARCADE, languageKey, args);
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, @PropertyKey(resourceBundle = "locales.arcade.arcade") String languageKey, Object... args) {
        net.kitpvp.chat.Chat.localeAnnounce(connections, ChatFormats.ARCADE, languageKey, args);
    }

    public static void commandAnnounce(String text, Object... args) {
        SpigotChat.commandAnnounce(ChatFormats.ARCADE, text, args);
    }

    public static void localeResponse(Connection sender, MsgFormat format,
                                      @PropertyKey(resourceBundle = "locales.arcade.arcade") String languageKey, Object... args) {
        net.kitpvp.chat.Chat.localeResponse(sender, format, languageKey, args);
    }

    public static void commandResponse(Connection sender, MsgFormat format, String text, Object... args) {
        net.kitpvp.chat.Chat.commandResponse(sender, format, text, args);
    }

    public static void localeAnnounce(MsgFormat format, @PropertyKey(resourceBundle = "locales.arcade.arcade") String languageKey, Object... args) {
        SpigotChat.announce(format, languageKey, args);
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, MsgFormat format, @PropertyKey(resourceBundle = "locales.arcade.arcade") String languageKey, Object... args) {
        net.kitpvp.chat.Chat.localeAnnounce(connections, format, languageKey, args);
    }

    public static void commandAnnounce(MsgFormat format, String text, Object... args) {
        SpigotChat.commandAnnounce(format, text, args);
    }
}
