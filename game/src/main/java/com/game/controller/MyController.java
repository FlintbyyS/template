package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.utility.NoSuchPlayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MyController {
    @Autowired
    PlayerService playerService;

    @GetMapping("/rest/players")
    public List<Player> showAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false) PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize){

            List<Player> allPlayers = playerService.getListPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
            List<Player> sortedPlayers = playerService.sortPlayers(allPlayers,order);
            return playerService.getPage(sortedPlayers,pageNumber,pageSize);
    }
    @GetMapping("/rest/players/count")
    public Integer showPlayersCount(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "race", required = false) Race race,
                                    @RequestParam(value = "profession", required = false) Profession profession,
                                    @RequestParam(value = "after", required = false) Long after,
                                    @RequestParam(value = "before", required = false) Long before,
                                    @RequestParam(value = "banned", required = false) Boolean banned,
                                    @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                    @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                    @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                    @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                    @RequestParam(value = "order", required = false) PlayerOrder order,
                                    @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize){

            List<Player> allPlayers = playerService.getListPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
            return allPlayers.size();
    }
    @GetMapping("/rest/players/{id}")
    public  Player getPlayer(@PathVariable String id) throws Exception {
        String regex = "^[+-]?[0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(id);
        if (!(m.find() && m.group().equals(id))){
            throw new Exception();
        }
        Player player = playerService.getPlayer(Long.parseLong(id));

        if(player == null){
            throw new NoSuchPlayerException("There is no player with ID = " + id + " in Database");
        }

        return player;
    }
    @DeleteMapping("/rest/players/{id}")
    public String deleteEmployee(@PathVariable long id) throws Exception {

        Player player = playerService.getPlayer(id);
        if (player == null){
            throw new NoSuchPlayerException("There is no player with ID = " + id + " in Database");
        }

        playerService.deletePlayer(id);

        return "Employee with ID = " + id + " was deleted";
    }
    @PostMapping("/rest/players")
    public Player addNewEmployee(@RequestBody Player player) throws Exception {
        if (player.getName() != null && player.getTitle() != null
                && player.getRace() != null && player.getProfession() != null
                && player.getBirthday() != null && player.getExperience() != null) {
            if (playerService.checkPlayer(player)) {
                playerService.savePlayer(player);
            } else {
                throw new Exception();
            }
        }
        else throw  new Exception();
        return player;
    }
    @PostMapping("/rest/players/{id}")
    public Player updateEmployee(@RequestBody Player player,@PathVariable long id) throws Exception {
        Date date = player.getBirthday();
        Integer experience = player.getExperience();
        Boolean banned = player.getBanned();
        if(banned == null && player.getName() == null
                && player.getTitle() == null && player.getRace() == null
                && player.getProfession() == null  && date == null && experience == null){
            return playerService.getPlayer(id);
        }
        if (banned == null) player.setBanned(false);
        player.setId(id);
        return playerService.updatePlayer(player);
    }
}
