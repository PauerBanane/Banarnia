package de.banarnia.api.util;

import org.bukkit.entity.Player;

public class Title {

    // Haupttitel
    private String title = "";

    // Subtitel
    private String subTitle = "";

    // Zeit bis vollständig eingeblendet
    private int fadeInTicks     = 10;

    // Zeit bis fadeOut startet
    private int stayTicks       = 100;

    // Zeit bis vollständig verschwunden
    private int fadeOutTicks    = 10;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Static Aufruf des Konstruktors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Konstruktor mit Haupttitel und Anzeigezeit
    public static Title of(String title, int stayTicks) {
        return new Title(title, stayTicks);
    }

    // Konstruktor mit Haupttitel, Anzeigezeit und Fade-Zeiten
    public static Title of(String title, int stayTicks, int fadeInTicks, int fadeOutTicks) {
        return new Title(title, stayTicks, fadeInTicks, fadeOutTicks);
    }

    // Konstruktor mit Haupttitel, Subtitel und Anzeigezeit
    public static Title of(String title, String subTitle, int stayTicks) {
        return new Title(title, subTitle, stayTicks);
    }

    // Konstruktor mit Haupttitel, Subtitel, Anzeigezeit und Fade-Zeiten
    public static Title of(String title, String subTitle, int stayTicks, int fadeInTicks, int fadeOutTicks) {
        return new Title(title, subTitle, stayTicks);
    }

    // Konstruktor mit dem Haupttitel
    public static Title of(String title) {
        return new Title(title);
    }

    // Konstruktor mit Haupttitel und Subtitel
    public static Title of(String title, String subTitle) {
        return new Title(title, subTitle);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Leerer Konstruktor
    private Title() {}

    // Konstruktor mit Haupttitel und Anzeigezeit
    private Title(String title, int stayTicks) {
        this.title(title);
        this.stayTicks(stayTicks);
    }

    // Konstruktor mit Haupttitel, Anzeigezeit und Fade-Zeiten
    private Title(String title, int stayTicks, int fadeInTicks, int fadeOutTicks) {
        this.title(title);
        this.stayTicks(stayTicks);
        this.fadeInTicks(fadeInTicks);
        this.fadeOutTicks(fadeOutTicks);
    }

    // Konstruktor mit Haupttitel, Subtitel und Anzeigezeit
    private Title(String title, String subTitle, int stayTicks) {
        this.title(title);
        this.subTitle(subTitle);
        this.stayTicks(stayTicks);
    }

    // Konstruktor mit Haupttitel, Subtitel, Anzeigezeit und Fade-Zeiten
    private Title(String title, String subtitle, int stayTicks, int fadeInTicks, int fadeOutTicks) {
        this.title(title);
        this.subTitle(subTitle);
        this.stayTicks(stayTicks);
        this.fadeInTicks(fadeInTicks);
        this.fadeOutTicks(fadeOutTicks);
    }

    // Konstruktor mit dem Haupttitel
    private Title(String title) {
        this(title, null);
        this.title(title);
    }

    // Konstruktor mit Haupttitel und Subtitel
    private Title(String title, String subTitle) {
        this.title(title);
        this.subTitle(subTitle);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Senden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Title senden
    public Title send(Player... players) {
        // Title an alle Spieler senden
        for (Player player : players)
            player.sendTitle(title, subTitle, fadeInTicks, stayTicks, fadeOutTicks);

        // Instanz zurückgeben
        return this;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Chain-Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Titel ändern
    public Title title(String title) {
        // Titel setzen
        this.title = title == null ? "" : title;

        // Instanz zurückgeben
        return this;
    }

    // Subtitel ändern
    public Title subTitle(String subTitle) {
        // Subtitel setzen
        this.subTitle = subTitle == null ? "" : subTitle;

        // Instanz zurückgeben
        return this;
    }

    // Stay-Zeit ändern
    public Title stayTicks(int stayTicks) {
        // Stay-Zeit setzen
        this.stayTicks = stayTicks < 0 ? 0 : stayTicks;

        // Instanz zurückgeben
        return this;
    }

    // FadeIn-Zeit ändern
    public Title fadeInTicks(int fadeInTicks) {
        // FadeIn-Zeit setzen
        this.fadeInTicks = fadeInTicks < 0 ? 0 : fadeInTicks;

        // Instanz zurückgeben
        return this;
    }

    // FadeOut-Zeit ändern
    public Title fadeOutTicks(int fadeOutTicks) {
        // FadeOut-Zeit setzen
        this.fadeOutTicks = fadeOutTicks < 0 ? 0 : fadeOutTicks;

        // Instanz zurückgeben
        return this;
    }

}
