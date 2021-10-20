package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.mission.domain.Mission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class KillPlayerMission extends Mission {
    public HashMap<Participant,Participant> killMap = new HashMap<>();
    private void initKillMap(){
        killMap.clear();
        List<Participant> tmpParticipants = new ArrayList<>(GameData.getParticipantsAlive());
        Collections.shuffle(tmpParticipants);
        List<Participant> participants = new ArrayList<>(GameData.getParticipantsAlive());
        for(int i = 0; i < participants.size(); i++){
            Participant tmpParticipant = tmpParticipants.get(i);
            Participant participant = participants.get(i);
            if(tmpParticipant.equals(participant)){
                killMap.put(participant,tmpParticipants.get((i + 1)%tmpParticipants.size()));
                continue;
            }
            killMap.put(participant,tmpParticipant);
        }
        viewKillData();
    }

    private void viewKillData(){
        List<Participant> participants = new ArrayList<>(GameData.getParticipantsAlive());
        new BukkitRunnable(){
            @Override
            public void run() {
                if(getClearPlayer() != null){
                    cancel();
                }
                participants.forEach(participant -> {
                    String name = killMap.get(participant.getPlayer()).getPlayer().getName();
                    participant.getPlayer().sendMessage(ChatColor.GREEN + "[Player Changer]: " +
                            ChatColor.GOLD + "당신의 목표는 %s입니다. %s를 죽이세요.".formatted(name,name));
                });
            }
        }.runTaskTimer(PlayerChanger.singleton, 20, 300);
    }

    public KillPlayerMission(int num) {
        super(num);
        this.description = "(채팅 확인)목표를 제거하세요.";
        initKillMap();
    }

    @Override
    public <T> Player updateMission(T event) {
        if(!(event instanceof PlayerDeathEvent))
            return null;
        PlayerDeathEvent ev = (PlayerDeathEvent) event;
        Optional<Participant> killed = GameData.getParticipantAlive(ev.getEntity());
        if(killed.isEmpty())
            return null;
        Optional<Participant> killer = GameData.getParticipantAlive(ev.getEntity().getKiller());
        if(killer.isEmpty())
            return null;
        if(killMap.get(killer.get()) == null)
            return null;
        if(!killMap.get(killer.get()).equals(killed.get()))
            return null;
        setClearPlayer(killer.get().getPlayer());
        return getClearPlayer();
    }
}
