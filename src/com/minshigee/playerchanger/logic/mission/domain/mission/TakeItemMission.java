package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.mission.domain.Mission;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class TakeItemMission extends Mission {

    private Material assignMaterial = Material.LECTERN;
    private Pair<Material,Integer> needItem;
    private static Pair<Material,Integer>[] missionMaterials = new Pair[]{
            new Pair<>(Material.GOLD_INGOT, 10),
            new Pair<>(Material.DIAMOND, 5),
            new Pair<>(Material.CLOCK, 1),
            new Pair<>(Material.COMPASS, 1),
            new Pair<>(Material.NETHER_STAR, 1),
            new Pair<>(Material.DRAGON_EGG, 1),
    };
    private HashMap<Participant, Integer> takeMap = new HashMap<>();

    private void initData(){
        Random random = new Random();
        Pair<Material,Integer> p = missionMaterials[random.nextInt(missionMaterials.length)];
        int cnt = p.getSecond();
        if(cnt > 2)
            cnt = cnt + random.nextInt(cnt/2);
        needItem = new Pair<Material,Integer>(p.getFirst(),cnt);
        GameData.getParticipantsAlive().forEach(participant -> takeMap.put(participant,0));
    }

    public TakeItemMission(int num) {
        super(num);
        initData();
        this.description += "%s %d개를 독서대(상점)에 납품(좌클릭)하세요.".formatted(needItem.getFirst().name().toLowerCase(), needItem.getSecond());
    }

    @Override
    public <T> Player updateMission(T event) {
        if(!(event instanceof PlayerInteractEvent))
            return null;
        PlayerInteractEvent ev = (PlayerInteractEvent) event;
        if(!ev.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return null;
        if(GameData.getParticipantAlive(ev.getPlayer()).isEmpty())
            return null;
        if(!Objects.requireNonNull(ev.getClickedBlock()).getType().equals(assignMaterial))
            return null;
        if(!(ev.getPlayer().getInventory().getItemInMainHand().getType().equals(needItem.getFirst())))
            return null;
        int amt = ev.getPlayer().getInventory().getItemInMainHand().getAmount();
        ev.getPlayer().getInventory().getItemInMainHand().setAmount(amt - 1);
        takeMap.replace(GameData.getParticipantAlive(
                ev.getPlayer()).get(),
                takeMap.get(GameData.getParticipantAlive(ev.getPlayer()).get()) + 1
        );
        if(takeMap.get(GameData.getParticipantAlive(ev.getPlayer()).get()) < needItem.getSecond())
            return null;
        this.setClearPlayer(ev.getPlayer());
        return getClearPlayer();
    }
}
