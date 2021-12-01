package com.github.iamtakagi.tcore.team;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Team<T extends TeamPlayer> {

    private T leader;
    private List<T> teamPlayers;

    public Team(T t) {
        this.leader = t;
        this.teamPlayers = new ArrayList<>();
        this.teamPlayers.add(this.leader);
    }

    public boolean isLeader(Player player) {
        return this.leader.getUuid().equals(player.getUniqueId());
    }

    public boolean containsPlayer(Player player) {
        for (TeamPlayer playerInfo : this.teamPlayers) {
            if (playerInfo.getUuid().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        this.teamPlayers.forEach(matchPlayer -> {
            Player player = matchPlayer.toPlayer();

            if (player != null) {
                players.add(player);
            }
        });

        return players;
    }

    /**
     * Returns a list of objects that extend {@link TeamPlayer} whose
     * {@link TeamPlayer#isAlive()} returns true.
     *
     * @return A list of team players that are alive.
     */
    public List<T> getAliveTeamPlayers() {
        List<T> alive = new ArrayList<>();

        this.teamPlayers.forEach(teamPlayer -> {
            if (teamPlayer.isAlive()) {
                alive.add(teamPlayer);
            }
        });

        return alive;
    }

    /**
     * Returns an integer that is incremented for each {@link TeamPlayer}
     * element in the {@code teamPlayers} list whose {@link TeamPlayer#isAlive()}
     * returns true.
     *
     * Use this method rather than calling {@link List#size()} on
     * the result of {@code getAliveTeamPlayers}.
     *
     * @return The count of team players that are alive.
     */
    public int getAliveCount() {
        if (this.teamPlayers.size() == 1) {
            return this.leader.isAlive() ? 1 : 0;
        } else {
            int alive = 0;

            for (TeamPlayer teamPlayer : this.teamPlayers) {
                if (teamPlayer.isAlive()) {
                    alive++;
                }
            }

            return alive;
        }
    }

    /**
     * Returns a list of objects that extend {@link TeamPlayer} whose
     * {@link TeamPlayer#isAlive()} returns false.
     *
     * @return A list of team players that are dead.
     */
    public List<T> getDeadTeamPlayers() {
        List<T> dead = new ArrayList<>();

        this.teamPlayers.forEach(teamPlayer -> {
            if (!teamPlayer.isAlive()) {
                dead.add(teamPlayer);
            }
        });

        return dead;
    }

    /**
     * Subtracts the result of {@code getAliveCount} from the size
     * of the {@code teamPlayers} list.
     *
     * @return The count of team players that are dead.
     */
    public int getDeadCount() {
        return this.teamPlayers.size() - this.getAliveCount();
    }

    public void broadcast(String messages) {
        this.getPlayers().forEach(player -> player.sendMessage(messages));
    }

    public void broadcast(List<String> messages) {
        this.getPlayers().forEach(player -> messages.forEach(player::sendMessage));
    }

    public void broadcastComponents(List<BaseComponent[]> components) {
        this.getPlayers().forEach(player -> components.forEach(array -> player.spigot().sendMessage(array)));
    }

}
