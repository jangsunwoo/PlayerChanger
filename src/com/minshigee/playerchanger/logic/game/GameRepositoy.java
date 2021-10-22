package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.change.ChangeController;
import com.minshigee.playerchanger.util.ItemGenerate;
import com.minshigee.playerchanger.logic.view.ViewController;
import com.minshigee.playerchanger.util.MessageUtil;
import com.mojang.datafixers.util.Pair;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameRepositoy extends Repository<GameData> {
    public GameRepositoy(GameData localDB, Integer viewCode) {
        super(localDB,viewCode);
    }

    public void excuteGameStart(Player player){
        if(GameData.getGameState().equals(GameState.Disable)){
            MessageUtil.printMsgToAll(ChatMessageType.CHAT, ChatColor.AQUA + "게임이 곧 시작됩니다. 관련 세팅을 시작합니다.");
            resetGame();
            GameData.makeNextGameStatus();
            ///////뒤는 대기 상태에서의 추가 코드입니다.//////
            printWaitMessage();
            scanGameWorldPos();
        }
        else if(GameData.getGameState().equals(GameState.Waitting)){
            if(GameData.spawnBlockVectors.size() < GameData.getParticipantsByRole(Role.Participant).size() || GameData.furnaceBlockVectors.size() < GameData.getParticipantsByRole(Role.Participant).size()/2 || GameData.chestBlockVectors.size() < GameData.getParticipantsByRole(Role.Participant).size() || GameData.craftingBlockVectors.size() < GameData.getParticipantsByRole(Role.Participant).size()/2){MessageUtil.printLogToPlayer(player, ChatColor.RED + "월드에 상자,조합대.화로의 갯수가 부족합니다.");return;}
            if(GameData.getParticipants().size() < 1){MessageUtil.printLogToPlayer(player, ChatColor.RED + "최소 참가자 2명부터 시작이 가능합니다.");return;}
            executeEnable();
        }
        else if(GameData.getGameState().equals(GameState.Enable)){
            GameData.makeNextGameStatus();
            ((ChangeController)PlayerChanger.getInstanceOfClass(ChangeController.class))
                    .changePlayers(null, null);
        }
        else if(GameData.getGameState().equals(GameState.Freezing)){
            GameData.makeNextGameStatus();
        }
    }

    private void executeEnable(){
        //TODO 게임 시작...
        ItemGenerate.addChangedItems(viewCode, localDB.returnDefaultInBoxItems()); // 월드 상자에 기본 아이템 지급 정보 제공
        ItemGenerate.insertItemToChest(viewCode); // 월드 상자에 기본 아이템 공급

        ItemGenerate.addChangedBlocks(viewCode, localDB.returnDefaultNecessaryBlocks()); // 필요한 블럭 정보 제공
        ItemGenerate.makeNecessaryBlock(viewCode); // 필요한 블럭을 월드에 추가

        teleportParticipantsToSpawnpoint(); // 유저들을 spawn 좌표로 랜덤 소환.
        GameData.makeNextGameStatus(); // 게임 State를 Enable로 전환
    }

    private void teleportParticipantsToSpawnpoint(){ArrayList<Participant> tmpParticipants = new ArrayList<>(GameData.getParticipantsAlive());ArrayList<BlockVector> tmpSpawnpoints = new ArrayList<>(GameData.spawnBlockVectors);Collections.shuffle(tmpParticipants);Collections.shuffle(tmpSpawnpoints);for(int i = 0; i < tmpParticipants.size(); i++){Player player = tmpParticipants.get(i).getPlayer();BlockVector pos = tmpSpawnpoints.get(i);player.teleport(new Location(player.getWorld(), pos.getBlockX(),pos.getBlockY() + 1,pos.getBlockZ()));}}

    /*
    게임 참가와 관련된 코드
     */
    public void executeJoin(Player player){GameData.removePlayerFromParticipants(player);GameData.addPlayerToParticipants(player, Role.Participant);}
    public void executeSpectator(Player player){GameData.removePlayerFromParticipants(player);GameData.addPlayerToParticipants(player,Role.Spectator);}
    public void executeLeave(Player player){GameData.removePlayerFromParticipants(player); player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());}

    /*
    월드 스캔과 관련된 코드
     */
    private void resetWorldScanData() {GameData.clearWorldBlockSets();}
    private void scanGameWorldPos() {resetWorldScanData();MessageUtil.printMsgToPlayers(ChatMessageType.CHAT, ChatColor.AQUA + "월드 스캐닝을 시작합니다. 시간이 조금 걸립니다.");final Location pos1 = PlayerChanger.config.getLocation("GameWorld.Size_Pos1");final Location pos2 = PlayerChanger.config.getLocation("GameWorld.Size_Pos2");final BlockVector stV = new BlockVector(Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));final BlockVector edV = new BlockVector(Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));new BukkitRunnable() {@Override public void run() {ArrayList<Pair<Integer, Integer>> chunkPoz = new ArrayList<>();MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, "유효한 청크를 조사 중입니다.");for (int x = stV.getBlockX(); x <= edV.getBlockX(); x += 16) for (int z = stV.getBlockZ(); z <= edV.getBlockZ(); z += 16) chunkPoz.add(new Pair(x >> 4, z >> 4));MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, "청크를 조사 중입니다.");final Pair<Integer, Integer> rRange = new Pair<>(stV.getBlockY(), edV.getBlockY());Integer threadCount = Runtime.getRuntime().availableProcessors();Integer scanSize = chunkPoz.size();Integer scanCount = 0;Chunk chunk = null;ArrayList<ChunkSnapshot> tmpChunks = new ArrayList<>();for (Pair<Integer, Integer> chunkPos : chunkPoz) {if (!GameData.getGameState().equals(GameState.Waitting)) {MessageUtil.printConsoleLog(ChatColor.RED + "맵 스캔이 종료되었습니다.");break;}chunk = pos1.getWorld().getChunkAt(chunkPos.getFirst(), chunkPos.getSecond());tmpChunks.add(chunk.getChunkSnapshot());if (tmpChunks.size() < threadCount) continue;MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, ChatColor.DARK_AQUA + "월드 스캔 %d/%d 진행 중".formatted(scanCount, scanSize));scanSomeChunks(tmpChunks, rRange);tmpChunks.clear();scanCount += threadCount;}scanSomeChunks(tmpChunks, rRange);MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, ChatColor.GREEN + "월드 스캔이 끝났습니다.");MessageUtil.printConsoleLog(ChatColor.GREEN + "%s : [%d], %s : [%d], %s : [%d]".formatted("Chest", (long) GameData.chestBlockVectors.size(), Material.BEDROCK.name(), (long) GameData.spawnBlockVectors.size(), Material.FURNACE.name(), (long) GameData.furnaceBlockVectors.size()));this.cancel();}}.runTaskAsynchronously(PlayerChanger.singleton);}
    private void scanSomeChunks(final ArrayList<ChunkSnapshot> chunks, final Pair<Integer, Integer> yRange) {chunks.parallelStream().forEach(chunk -> getBlocksInChunk(chunk, yRange));}
    private void getBlocksInChunk(final ChunkSnapshot chunk, final Pair<Integer, Integer> yRange) {Set<BlockVector> spawnBlocks = new HashSet<>();Set<BlockVector> craftingBlocks = new HashSet<>();Set<BlockVector> furnaceBlocks = new HashSet<>();Set<BlockVector> chestBlocks = new HashSet<>();final int minX = chunk.getX() << 4;final int maxX = minX | 15;final int minY = yRange.getFirst();final int maxY = yRange.getSecond();final int minZ = chunk.getZ() << 4;final int maxZ = minZ | 15;for (int x = minX; x <= maxX; ++x) for (int y = minY; y <= maxY; ++y) for (int z = minZ; z <= maxZ; ++z) {if (Material.BEDROCK.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) spawnBlocks.add(new BlockVector(x, y, z));if (Material.CRAFTING_TABLE.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) craftingBlocks.add(new BlockVector(x, y, z));if (Material.FURNACE.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) furnaceBlocks.add(new BlockVector(x, y, z));if (Material.CHEST.name().equals(chunk.getBlockType(x & 0xF, y, z & 0xF).name())) chestBlocks.add(new BlockVector(x, y, z));}GameData.spawnBlockVectors.addAll(spawnBlocks);GameData.craftingBlockVectors.addAll(craftingBlocks);GameData.furnaceBlockVectors.addAll(furnaceBlocks);GameData.chestBlockVectors.addAll(chestBlocks);}

    /*
    게임 Stop 관련 코드
     */
    public void executeGameStop(Player player){
        resetGame();
        MessageUtil.printLogToPlayer(player, ChatColor.RED + " 게임이 종료되었습니다.");
    }
    private void resetGame(){
        ItemGenerate.resetWorldBlock();
        ItemGenerate.resetChestBlocks();

        ItemGenerate.clearChangedItems();
        ItemGenerate.clearChangedBlocks();;
        //////위의 내용을 제일 먼저 처리해주세요.//////
        localDB.clearSettingData();
        GameData.clearWorldBlockSets();
        ViewController.singleton.stopViewScoreboard();
        /* 이 뒤는 participants 데이터를 제거합니다. 제일 마지막에 두도록 해주세요. */
        GameData.clearParticipants();
        GameData.offGameState();
    }

    /*
    Setter 관련 Repo
     */
    public void makePlayerSetterOrNot(Player player){if(!localDB.checkPlayerIsSetter(player)){localDB.addSetter(player);MessageUtil.printLogToPlayer(player, ChatColor.AQUA + "세터모드가 되었습니다.");} else{localDB.removeSetter(player);MessageUtil.printLogToPlayer(player, ChatColor.AQUA + "세터모드가 해제되었습니다.");}}
    public void updateGameWorldPos(PlayerInteractEvent event){if(!localDB.checkPlayerIsSetter(event.getPlayer())) return;if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) return;Location location = event.getPlayer().getLocation();if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){PlayerChanger.config.set("GameWorld.Size_Pos1", location);MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos1이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));return;}PlayerChanger.config.set("GameWorld.Size_Pos2", event.getPlayer().getLocation());MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos2이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));}
    public void updateGameCoreBolck(PlayerInteractEvent event){if(!localDB.checkPlayerIsSetter(event.getPlayer())) return;if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ARROW)) return;if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){PlayerChanger.config.set("SpawnBlock", event.getClickedBlock().getBlockData().getMaterial().name());MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 스폰 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));} else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){PlayerChanger.config.set("MissionAssignBlock", event.getClickedBlock().getBlockData().getMaterial().name());MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 미션 완료 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));}}
    public void breakBlockSetter(BlockBreakEvent event){if(!localDB.checkPlayerIsSetter(event.getPlayer())) return;event.setCancelled(true);}

    /*
    Waitting 관련 코드
     */
    private void printWaitMessage(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!GameData.getGameState().equals(GameState.Waitting))
                    cancel();
                Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.printMsgToPlayer(ChatMessageType.ACTION_BAR,
                        player, ChatColor.GOLD + "게임이 곧 시작합니다. /ph join 으로 합류하세요!"));
                GameData.getParticipantsByRole(Role.Participant).forEach(participant -> MessageUtil.printMsgToPlayer(
                        ChatMessageType.ACTION_BAR, participant.getPlayer(), ChatColor.GREEN + "당신은 참가자입니다. /ph leave 로 참여를 포기하실 수 있습니다."
                ));
                GameData.getParticipantsByRole(Role.Spectator).forEach(participant -> MessageUtil.printMsgToPlayer(
                        ChatMessageType.ACTION_BAR, participant.getPlayer(), ChatColor.AQUA + "당신은 관람자입니다. /ph leave 로 참여를 포기하실 수 있습니다."
                ));
            }
        }.runTaskTimerAsynchronously(PlayerChanger.singleton, 0, 40);
    }

}
