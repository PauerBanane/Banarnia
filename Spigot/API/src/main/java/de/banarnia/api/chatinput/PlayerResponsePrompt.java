package de.banarnia.api.chatinput;

import de.banarnia.api.config.ApiConfig;
import de.banarnia.api.util.UtilString;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.List;

/* PlayerResponsePrompt Klasse
 * Ermöglicht Konfigurationsmöglichkeiten für die Responses.
 */
public class PlayerResponsePrompt extends StringPrompt {

    // Dazugehöriger ChatInput
    private ChatInput input;

    // Nachricht, die bei Beginn der Conversation angezeigt wird
    private String message;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public PlayerResponsePrompt(ChatInput input) {
        this.input      = input;
        this.message    = input.message;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Override Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Nachricht, die bei Beginn der Conversation angezeigt wird
    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }

    // Wird ausgeführt, wenn der Spieler die Nachricht eingegeben hat
    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String message) {
        // Variable um auszuwerten, ob die Eingabe abgebrochen wurde
        boolean canceled = message == null;

        // Abfrage, ob die Eingabe überhaupt abgebrochen werden kann - wird vom ChatInput vorgegeben
        if (input.cancelable) {
            // Liste von Wörtern, die einen Abbruch der Eingabe bedeuten
            List<String> cancelWords = ApiConfig.CONVERSATION_CANCEL_WORDS();

            // Überprüfen, ob die Eingabe einem Abbruch ähnelt - z.B. "Abbrechen"
            canceled = UtilString.containsIgnoreCase(cancelWords, message);
        }

        // Erhalt der Nachricht bestätigen
        input.onResponse(message, canceled);

        // Instanz zurückgeben
        return this;
    }
}
