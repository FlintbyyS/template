package com.game.utility;

import com.game.entity.Player;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlayerValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Player.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player)target;
        String playerName = player.getName();
        int playerLevel  = player.getLevel();

        if (playerName == null){
            errors.rejectValue("name", "name.required", "Error. Name is missing");
        }
        if (playerName.length() < 2){
            errors.rejectValue("name", "name.anorm", "Error. Name less than 2 symbols");
        }
        if (playerName.length() > 150){
            errors.rejectValue("name", "name.anorm2", "Error. Name more than 150 symbols");
        }
        if (playerLevel < 1){
            errors.rejectValue("level", "level.required", "Error. Level less than 1");
        }

    }
}
