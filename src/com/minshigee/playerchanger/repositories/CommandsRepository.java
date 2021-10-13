package com.minshigee.playerchanger.repositories;

import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.controllers.GameController;
import com.minshigee.playerchanger.repositories.interfaces.ICommandRepository;
import com.minshigee.playerchanger.repositories.interfaces.NeedPermission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandsRepository implements ICommandRepository {

    @Override
    public void executeHelp(Player player) {
        sendUserCommands(player);
        if(player.isOp())
            sendOpCommands(player);
    }

    @Override
    @NeedPermission
    public void executeStart(Player player) {
        GameController.startGame(player);
    }

    @Override
    @NeedPermission
    public void executeStop(Player player) {
        GameController.stopGame();
    }

    @Override
    public void executeList(Player player) {
        player.sendMessage(ChatColor.AQUA + "==참가자 명단!==");
        PlayInfo.participants.forEach(p -> player.sendMessage(ChatColor.GREEN + p.getName()));
        player.sendMessage(ChatColor.DARK_AQUA + "==관람자 명단!==");
        PlayInfo.leaveParticipants.forEach(p -> player.sendMessage(ChatColor.GOLD + p.getName()));
    }

    @Override
    public void executeReady(Player player) {
        if(PlayInfo.gameStatus != PCH_Status.SETTING){
            player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 현재는 등록이 불가능합니다.");
            return;
        }
        int code = PlayInfo.addParticipant(player);
        switch (code){
            case 0:
                player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 게임에 등록되었습니다.");
                break;
            case 1:
                player.sendMessage(ChatColor.RED + "[PlayerChanger]: 실패. 최대 인원입니다.");
                break;
            case 2:
                player.sendMessage(ChatColor.GOLD + "[PlayerChanger]: 이미 등록된 상태입니다.");
        }
    }

    @Override
    @NeedPermission
    public void executeReadyAll(Player player) {
        player.getServer().getOnlinePlayers().stream()
                .filter(p ->
                        !PlayInfo.isExistParticipant(p)
                ).forEach(p ->
                PlayInfo.addParticipant(p)
        );
    }

    @Override
    public void executeLeave(Player player) {
        if(PlayInfo.gameStatus != PCH_Status.SETTING){
            player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 현재는 퇴장이 불가능합니다.");
            return;
        }
        PlayInfo.delParticipant(player);
        PlayInfo.delLeaveParticipant(player);
    }

    @Override
    public void executeSpectator(Player player) {
        if(PlayInfo.gameStatus != PCH_Status.SETTING){
            player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 현재는 등록이 불가능합니다.");
            return;
        }
        PlayInfo.delParticipant(player);
        PlayInfo.addLeaveParticipant(player);
    }

    @Override
    @NeedPermission
    public void executeLoc(Player player) {
        for(int idx = 0; idx < PlayInfo.startLocations.size(); idx++){
            Location loc = PlayInfo.startLocations.get(idx);
            String msg = String.format("start loc: idx: %d, loc: {%.1f,%.1f,%.1f}",idx,loc.getX(),loc.getY(),loc.getZ());
            player.sendMessage(msg);
        }
    }

    @Override
    @NeedPermission
    public void executeAddLoc(Player player) {
        PlayInfo.addStartLocation(player.getLocation());
    }

    @Override
    @NeedPermission
    public void executeDelLoc(Player player, int idx) {
        PlayInfo.delStartLocation(idx);
    }

    @Override
    public void sendUserCommands(Player player) {
        player.sendMessage(
                ChatColor.GREEN + "[PlayerChanger] : 유저 명령어는 다음과 같습니다. \n" + ChatColor.GRAY +
                        "/pch ready = 게임에 참가합니다.\n" +
                        "/pch spectator = 게임에 관리자로 참가합니다.\n" +
                        "/pch leave = 게임 참가자 명단에서 나갑니다.\n" +
                        "/pch list = 참가자 명단을 확인합니다."
        );
    }

    @Override
    @NeedPermission
    public void sendOpCommands(Player player) {
        player.sendMessage(
                ChatColor.GOLD + "[PlayerChanger] : 관리자 명령어는 다음과 같습니다. \n" + ChatColor.GRAY +
                        "/pch start => \n({비활성화 상태일 때} 게임을 참가자 모집 상태로 만듭니다.),({모집 상태일 때} 모집 상태에서 게임을 시작합니다.)\n" +
                        "/pch stop = 게임을 초기화합니다.\n" +
                        "/pch loc = 시작 좌표 리스트를 조회합니다.\n" +
                        "/pch addloc = 현재 좌표를 시작 좌표 리스트에 추가합니다.\n" +
                        "/pch delloc {num} = num 번호의 좌표를 제거합니다.\n"
        );
    }

}
