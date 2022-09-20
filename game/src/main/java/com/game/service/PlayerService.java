package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerService  {

    List<Player> getListPlayers(
            String name,
            String title,
            Race race,
            Profession profession,
            Long after,
            Long before,
            Boolean banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel);
    List<Player> sortPlayers(List<Player> players, PlayerOrder order);
    List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize);
    Player getPlayer(long id) throws Exception;
    void deletePlayer(long id);
    public void savePlayer(Player player);
    public boolean checkPlayer(Player player) throws Exception;
    public Player updatePlayer(Player player) throws Exception;
}
