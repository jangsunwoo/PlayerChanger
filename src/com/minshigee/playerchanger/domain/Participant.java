package com.minshigee.playerchanger.domain;

import com.minshigee.playerchanger.constant.Role;
import com.minshigee.playerchanger.domain.interface_.IParticipant;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Participant implements IParticipant {

    private Player player;
    private Role role;
    private boolean death = false;

    public Participant(Player _player){
        player = _player;
        role = Role.None;
    }

    public Participant(Player _player, Role _role){
        player = _player;
        role = _role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, role, death);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player _player) {
        player = _player;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role _role) {
        role = _role;
    }

    @Override
    public boolean getIsDeath() {
        return death;
    }

    @Override
    public void swapIsDeath() {
        death = !death;
    }
}
