package net.inerva.playersearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 1/17/2016.
 */
public class SearchPlayer {

    public SearchPlayer(String playerName) {
        this.playerName = playerName;
    }

    private String playerName;

    private List<String> checkedServers = new ArrayList<String>();

    public String getPlayerName() {
        return playerName;
    }

    public List<String> getCheckedServers() {
        return checkedServers;
    }
}
