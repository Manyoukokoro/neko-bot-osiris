package org.nekotori.persistence.sqlite;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class GroupPersistence {

    private Connection connection;

    public static void main(String[] args) {
        GroupPersistence groupPersistence = new GroupPersistence();
    }


    public GroupPersistence(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:bot.db");
            connection.setAutoCommit(false);
        }catch (SQLException se){
            log.error("init sqlite error: {}",se.getMessage());
        }
    }

    public void save(){
        try {
            var sql = "INSERT INTO chat_history(name,value) VALUES (?,?)";
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"name");
            preparedStatement.setString(2,"value");
            preparedStatement.executeUpdate();
        }catch (SQLException se){

        }
    }
}
