package com.kaassnuvier.dogebolt.objects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SQLQueries {

    CREATE_TABLE_QUERY("CREATE TABLE IF NOT EXISTS `dogebolt`.`player_data`(`uuid` VARCHAR(32) NOT NULL, `games_won` INT NOT NULL, `kills_made` INT NOT NULL, PRIMARY KEY(`uuid`));"),
    SELECT_ALL_QUERY("SELECT * FROM `dogebolt`.`game_data`;"),
    SAVE_GAME_QUERY("INSERT INTO `dogebolt`.`game_data` (`name`, `hub_location`, `red_spawn`, `blue_spawn`, `min_players`, `max_players`) VALUES (?, ?, ?, ?, ?, ?);"),
    DELETE_GAME_QUERY("DELETE FROM `dogebolt`.`game_data` WHERE `name`=?;");

    @Getter private final String query;

}
