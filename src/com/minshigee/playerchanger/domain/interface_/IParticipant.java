package com.minshigee.playerchanger.domain.interface_;

import com.minshigee.playerchanger.constant.Role;
import org.bukkit.entity.Player;

public interface IParticipant {

    public Player getPlayer();
    public void setPlayer(Player player);

    public Role getRole();
    public void setRole(Role role);

    public boolean getIsDeath();
    public void swapIsDeath();
}
