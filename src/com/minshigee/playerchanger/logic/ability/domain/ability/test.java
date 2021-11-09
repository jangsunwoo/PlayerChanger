package com.minshigee.playerchanger.logic.ability.domain.ability;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class test extends Abilities {

    public test()
    {
        super(Ability.GoldenPig);
        abilityHelp.put(Ability.GoldenPig,
                ChatColor.GREEN + "\n──────[능력 정보]──────\n" +
                        ChatColor.AQUA + "황금돼지 " + ChatColor.WHITE + "[" + ChatColor.GRAY + "능력 활성화" + ChatColor.WHITE + "]" + ChatColor.AQUA + "C 등급" + ChatColor.WHITE + "인간\n" +
                        "상대방에게 타격을 받을 경우 그 자리에 금, 사과, 돼지고기를 뿌린다.\n" +
                        ChatColor.GREEN + "─────────────────\n"
        );
    }

    @Override
    public <T> Player updateAbility(T event) {
        Bukkit.getConsoleSender().sendMessage(super.partsAbility.toString());
        if(!(event instanceof EntityDamageByEntityEvent))
            return null;
        if(!(((EntityDamageByEntityEvent) event).getEntity() instanceof Player)) return null;
        Player ev = ((Player) ((EntityDamageByEntityEvent) event).getEntity()).getPlayer();
        ArrayList<Participant> thisAbilityParts = getThisAbilityParts(ability);
        for(Participant participant : thisAbilityParts)
        {
            Player player = participant.getPlayer();
            if(ev.equals(player))
            {
                player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.APPLE, 1));
                player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.GOLD_NUGGET, 1));
                player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.PORKCHOP, 1));
            }
        }
        return null;
    }
}
//  숙제:  철괴를 우클릭 했을 경우 우클릭한 플레이어한테 메세지가 전달이 되도록 (메세지가 뜨게끔.)