package com.minshigee.playerchanger.domain.interface_;

import com.minshigee.playerchanger.domain.Ability;
import com.minshigee.playerchanger.domain.Role;
import org.bukkit.entity.Player;

public interface IParticipant {

    public Player getPlayer();
    public void setPlayer(Player player);

    public Role getRole();
    public void setRole(Role role);

    public Ability getAbility();
    public void setAbility(Ability ability);

    public boolean getIsDeath();
    public void swapIsDeath();
}
