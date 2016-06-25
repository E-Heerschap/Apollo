package net.inerva.apollo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 1/28/2016.
 */
public class ApolloServerInfo {

    private int lastUpdate = 0;

    private List<String> players = new ArrayList<>();

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = null;
        this.players = players;
    }
}
