package de.hunjy.messages;


import java.util.HashMap;

public class MessageManager {
    private HashMap<String, String> messages;

    public MessageManager() {

        messages = new HashMap<>();

        messages.put("PREFIX", "§x§f§b§2§2§e§5§l[§x§e§c§2§b§e§7§lH§x§d§c§3§3§e§9§ly§x§c§d§3§c§e§c§lp§x§b§e§4§5§e§e§le§x§a§e§4§e§f§0§lr§x§9§f§5§6§f§2§lS§x§8§f§5§f§f§4§lt§x§8§0§6§8§f§6§la§x§7§1§7§1§f§9§ln§x§6§1§7§9§f§b§ld§x§5§2§8§2§f§d§l]");

        messages.put("NEED_TO_LOOK_AT_ARMOR_STAND", "§cDu musst einen ArmorStand anschauen!");
        messages.put("COMMAND_NO_PERMISSIONS", "§cDazu hast du keine Rechte!");
        messages.put("COMMAND_DEFAULT_USAGE", "§7Benutze §c/hyperstand halp §7für eine Liste aller Befehle");
        messages.put("COMMAND_SAVE_USAGE", "§cBenutze /hyperstand save <Name> <Beschreibung>");
        messages.put("COMMAND_CREATE_ITEM_GIVEN", "§aDu hast einen ArmorStand erhalten!");
        messages.put("COMMAND_NAME_USAGE", "§cBenutze /hyperstand name <Name>");

        messages.put("HYPERSTAND_ALREADY_IN_USE", "§cDer ArmorStand wird bereits von jemand andern Bearbeitet!");
        messages.put("HYPERSTAND_NOT_EDIBLE", "§cDer ArmorStand kann nicht bearbeitet werden!");
        messages.put("HYPERSTAND_NO_ACCESS", "§cDu kannst den ArmorStand nicht bearbeiten!");
        messages.put("HYPERSTAND_ALLRADY_EXIST", "§cDas ist bereits ein HyperStand");
        messages.put("HYPERSTAND_CREATET", "§aDu hast den HyperStand erstellt!");
        messages.put("create_template", "§7Die Position wird gespeichert...");
        messages.put("create_template_success", "§aDie Position wurde erfolgreich gespeichert!");
    }

    public String get(String key) {
        return messages.get(key);
    }

    public String get(String key, boolean prefix) {
        return ((prefix) ? get("PREFIX") : "") + " §7" + messages.get(key);
    }
}
