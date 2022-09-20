package com.game.dao;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.utility.NoSuchPlayerException;
import com.game.utility.PlayerNameNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

    private PlayerRepository playerRepository;
    @Autowired
    private EntityManagerFactory sessionFactory;


    @Override
    @Transactional
    public Player getPlayer(long id) throws Exception {
        EntityManager entityManager = sessionFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Player player = new Player();
        if(id>0) {
            player = session.get(Player.class,id);// добавить проверку id
        }
        else{
            throw new Exception();
        }
        return player;
    }

    @Override
    @Transactional
    public void deletePlayer(long id) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Transaction txn = session.beginTransaction();
        Query<Player> query = session.createQuery("delete from Player where id =:playerId");
        query.setParameter("playerId",id);
        query.executeUpdate();
        txn.commit();
    }

    @Override
    @Transactional
    public void savePlayer(Player player) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        int lvl = (int) ((Math.sqrt(2500+200*player.getExperience())-50)/100);
        player.setLevel(lvl);
        int n = 50*(lvl+1)*(lvl+2)-player.getExperience();
        player.setUntilNextLevel(n);
        session.save(player);
    }

    @Override
    public boolean checkPlayer(Player player) throws Exception {
        if(player.getBanned() == null){
            player.setBanned(false);
        }
        Date date;
        Date date1;
        Date date2;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = player.getBirthday();
            date1 = format.parse("2000-01-01");
            date2 = format.parse("3000-01-01");
        } catch (Exception e) {
            throw new Exception();
        }
        if (player.getName().toCharArray().length > 12 || player.getTitle().toCharArray().length > 30 || player.getName().equals("")
                || player.getExperience() < 0 || player.getExperience() > 10000000
                || player.getBirthday().getTime() < 0 || date.before(date1) || date.after(date2)) {
            throw new Exception("exception");
        }
        return true;
    }

    @Override
    @Transactional
    public Player updatePlayer(Player player) throws Exception {
        EntityManager entityManager = sessionFactory.createEntityManager();
        Player playerFromDb = new Player();
        if (player.getId()>0)
            playerFromDb = entityManager.find(Player.class,player.getId());
        else
            throw new Exception();
        if (playerFromDb == null){
         throw new NoSuchPlayerException("There is no player with ID = " + player.getId() + " in Database");
        }
        entityManager.detach(playerFromDb);
        if (player.getName() != null && !playerFromDb.getName().equalsIgnoreCase(player.getName())){
            playerFromDb.setName(player.getName());
        }
        if (player.getTitle() != null && !playerFromDb.getTitle().equalsIgnoreCase(player.getTitle())){
            playerFromDb.setTitle(player.getTitle());
        }
        if (player.getRace() != null && !playerFromDb.getRace().equals(player.getRace())){
            playerFromDb.setRace(player.getRace());
        }
        if (player.getProfession() != null && !playerFromDb.getProfession().equals(player.getProfession())){
            playerFromDb.setProfession(player.getProfession());
        }
        Date date = player.getBirthday();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse("2000-01-01");
        Date date2 = format.parse("3000-01-01");
        if (date != null) {
            if (date.before(date1) || date.after(date2)) throw new Exception();
            if (!playerFromDb.getBirthday().equals(player.getBirthday())) {
                playerFromDb.setBirthday(player.getBirthday());
            }
        }
        Integer experience = player.getExperience();
        if (experience != null ) {
            if (experience <0 || experience >10000000) throw new Exception();
            if (playerFromDb.getExperience() != player.getExperience()){
                playerFromDb.setExperience(player.getExperience());
                int lvl = (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
                playerFromDb.setLevel(lvl);
                int n = 50 * (lvl + 1) * (lvl + 2) - player.getExperience();
                playerFromDb.setUntilNextLevel(n);
            }
        }
        Boolean banned = player.getBanned();
        if (playerFromDb.getBanned() != player.getBanned()){
            playerFromDb.setBanned(player.getBanned());
        }
        entityManager.getTransaction().begin();
        entityManager.merge(playerFromDb);
        entityManager.getTransaction().commit();
        return playerFromDb;
    }
}
