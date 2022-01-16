package de.jrninj.serverapi.listener;

import de.jrninj.serverapi.mysql.MySQL;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void postLogin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

        try {

            PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT USERNAME FROM players WHERE UUID = ?;");


            statement.setString(1, player.getUniqueId().toString());
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {

                if (!rs.getString("USERNAME").equals(player.getName())) {
                    PreparedStatement statement1 = MySQL.getConnection().prepareStatement("UPDATE players SET USERNAME = '" + player.getName() + "' WHERE UUID ='" + player.getUniqueId() + "';");
                    statement1.executeUpdate();
                }


            } else {

                Long datetime = System.currentTimeMillis();
                Date timestamp = new Date(datetime);
                PreparedStatement statement1 = MySQL.getConnection().prepareStatement("INSERT INTO players (ID,USERNAME,UUID,COINS,EUROS,FIRST_JOINED) VALUES (default,'" + player.getName() + "','" + player.getUniqueId() + "',10,20,'" + timestamp + "');");
                statement1.executeUpdate();

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}