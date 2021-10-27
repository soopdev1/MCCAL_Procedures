/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import it.refill.reportistica.Database;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author rcosco
 */
public class conference {

    public static void main(String[] args) {
        Database db1 = new Database(false);
        List<String> partecipanti = new ArrayList<>();
        try {
            ResultSet rs = db1.getC().createStatement().executeQuery("SELECT * FROM fad_track f WHERE room='26NOVEMBREORE9.30ACCREDITAMENTODOCENTIYESISTARTUPCALABRIA' ORDER BY f.date");
            while (rs.next()) {
                if (rs.getString("action").contains("NUOVO PARTECIPANTE")) {

                    partecipanti.add(rs.getString("action").split("--")[1].trim());
                    System.out.println(rs.getString("date") + " (-) " + rs.getString("action").split("--")[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db1.closeDB();

        partecipanti = partecipanti.stream().distinct().sorted().collect(Collectors.toList());

        partecipanti.forEach(
                p -> {
                    System.out.println(p.toLowerCase());
                }
        );

    }
}
