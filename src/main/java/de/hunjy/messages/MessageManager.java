package de.hunjy.messages;


import de.hunjy.HyperStand;

import java.util.HashMap;

public class MessageManager {
    private HashMap<String, String> messages;

    public MessageManager() {

        messages = new HashMap<>();


        for (String cfg : HyperStand.getInstance().getConfig().getConfigurationSection("messgae").getKeys(false)) {
            messages.put(cfg, HyperStand.getInstance().getConfig().getString("messgae." + cfg).replaceAll("&", "§"));
        }
    }

    public String get(String key) {
        String msg = messages.get(key);
        msg = msg.replace("{prefix}", messages.get("PREFIX"));
        return msg;
    }

    public String get(String key, String... placeholder) {
        return setPlaceHolders(messages.get(key), placeholder);
    }

    private String setPlaceHolders(String message, String... placeholder) {
        String msg = message;
        msg = msg.replace("{prefix}", messages.get("PREFIX"));
        for (int i = 0; i < placeholder.length; i++) {
            msg = msg.replace("{" + i + "}", placeholder[i]);
        }

        return msg;
    }
}
