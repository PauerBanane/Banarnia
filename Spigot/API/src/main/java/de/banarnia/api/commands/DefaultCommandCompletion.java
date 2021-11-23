package de.banarnia.api.commands;

/* DefaultCommandCompletion
 * Enum zur Auflistung aller Standard-Completions von Aikar's ACF.
 */
public enum DefaultCommandCompletion {

    DAY("@day"),
    MONTH("@month"),
    YEAR("@year"),
    SERVER("@server"),
    SOUND("@sound"),
    GAMEMODE("@gamemode"),
    BOOLEAN("@boolean"),
    MATERIAL("@material"),
    PROTECTED_REGION("@protected_region"),
    ENTITYTYPE("@entitytype"),
    BANARNIA_PLUGIN("@banarniaplugin");

    private String completionID;

    DefaultCommandCompletion(String completionID) {
        this.completionID = completionID;
    }

    public String get() {
        return completionID + " ";
    }

}
