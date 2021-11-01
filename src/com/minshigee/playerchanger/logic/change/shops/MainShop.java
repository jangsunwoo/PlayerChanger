package com.minshigee.playerchanger.logic.change.shops;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.change.ChangeData;
import com.minshigee.playerchanger.logic.change.shops.domain.Shop;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MainShop extends Shop {
    @Override
    protected void init() {
        inventory = Bukkit.createInventory(null, 8,"Main Shop");
        inventory.setItem(2, Util.createItem("#", Material.GREEN_STAINED_GLASS_PANE, Collections.singletonList("")));
        inventory.setItem(4,Util.createItem("Change", Material.CLOCK, Collections.singletonList(ChatColor.GREEN + "유저들을 Change합니다.")));
        inventory.setItem(6, Util.createItem("Effect Shop", Material.GOLDEN_APPLE, Collections.singletonList(ChatColor.GREEN + "효과를 구입합니다.")));
    }

    @Override
    protected void makeInventory(Participant part) {
        Player player = part.getPlayer();
        super.makeInventory(part);
        GameData.getParticipantAlive(player).ifPresentOrElse(
                participant -> {
                    inventory.setItem(0, Util.createPlayerHead(
                            player,
                            ChatColor.GOLD + player.getName(),
                            Collections.singletonList("체력 : %d\n점수 : %d".formatted((int) player.getHealth(), ChangeData.scoreData.get(participant)))
                    ));

                    inventory.setItem(1, Util.createItem(
                                    ChatColor.GREEN + "점수 : %d".formatted(ChangeData.scoreData.get(participant)),
                                    Material.EMERALD,
                                    Collections.singletonList("")
                            )
                    );
                },
                () -> {
                    inventory.clear(0);
                    inventory.clear(1);
                }
        );

    }
}
