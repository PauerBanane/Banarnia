package de.banarnia.api.config;

import com.google.common.collect.Lists;
import de.banarnia.api.BanarniaAPI;
import de.banarnia.api.util.FileLoader;

import java.util.List;

public class ApiConfig {

    // Config initialisieren
    static {
        config = FileLoader.of(BanarniaAPI.getInstance().getDataFolder(), "ApiConfig.yml");
    }

    // Config-Datei
    private static FileLoader config;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static List<String> CONVERSATION_CANCEL_WORDS() {
        return config.getOrElseSet("Conversation.Cancel-words", Lists.newArrayList("Abbrechen"));
    }

}
