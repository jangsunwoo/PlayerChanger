package com.minshigee.playerchanger.repositories.interfaces;

import org.bukkit.entity.Player;

public interface ICommandRepository {
    public void executeHelp(Player player);
    @NeedPermission
    public void executeStart(Player player);
    @NeedPermission
    public void executeStop(Player player);

    public void executeList(Player player);
    public void executeReady(Player player);
    @NeedPermission
    public void executeReadyAll(Player player);
    public void executeLeave(Player player);
    public void executeSpectator(Player player);

    @NeedPermission
    public void executeLoc(Player player);
    @NeedPermission
    public void executeAddLoc(Player player);
    @NeedPermission
    public void executeDelLoc(Player player, int idx);

    public void sendUserCommands(Player player);
    @NeedPermission
    public void sendOpCommands(Player player);
}