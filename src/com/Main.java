package com;

import javax.imageio.ImageIO;
import javax.swing.plaf.nimbus.State;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        String userName = "root";
        String password = "sql123";
        String connectionUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
      //  Class.forName("com.mysql.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(connectionUrl, userName, password);
             Statement stat = conn.createStatement()) {

            stat.execute ("drop table IF EXISTS Books");
            stat.executeUpdate("CREATE TABLE Books(id MEDIUMINT NOT NULL AUTO_INCREMENT,name CHAR(30) NOT NULL,img BLOB, PRIMARY KEY(id))");

            BufferedImage image= ImageIO.read(new File("pr.jpg"));
            Blob blob = conn.createBlob();
          try(  OutputStream os=blob.setBinaryStream(1)){
            ImageIO.write(image,"jpg",os);}
            PreparedStatement statement=conn.prepareStatement("insert into Books(name,img)values(?,?)");
            statement.setString(1,"inferno");
            statement.setBlob(2,blob);
            statement.execute();

            ResultSet resultSet=stat.executeQuery("select * from Books");
            while (resultSet.next()){
                Blob blob2=resultSet.getBlob("img");
                blob2.getBinaryStream();
                BufferedImage image2=ImageIO.read(blob.getBinaryStream());
                File outputFile=new File("saved.jpg");
                ImageIO.write(image2,"jpg",outputFile);
            }
        }
    }
}


// CREATE TABLE IF NOT EXISTS Book (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))























