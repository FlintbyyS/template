package com.game.dao;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerDAO  {

    Player getPlayer(long id) throws Exception;
    void deletePlayer(long id);
    public void savePlayer(Player player);
    public boolean checkPlayer(Player player) throws Exception;
    public Player updatePlayer(Player player) throws Exception;

}
