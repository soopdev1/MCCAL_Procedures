/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.refill.engine;

import it.refill.reportistica.Database;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Administrator
 */
public class Sms {
    public static void sms_primogiorno(boolean professioni) {
        try {
            Database db0 = new Database(professioni);
            String sql0 = "SELECT MIN(f.data),p.idprogetti_formativi,CURDATE(),f.orainizio,f.orafine FROM fad_calendar f, progetti_formativi p "
                    + "WHERE p.idprogetti_formativi=f.idprogetti_formativi AND p.stato='FA' "
                    + "GROUP BY f.idprogetti_formativi";
            try (Statement st0 = db0.getC().createStatement(); ResultSet rs0 = st0.executeQuery(sql0)) {
                while (rs0.next()) {
                    String datalezione1 = rs0.getString(1);
                    String oggi = rs0.getString(3);
                    if (datalezione1.equals(oggi)) {
                        int idpr = rs0.getInt(2);
                        String orainizio = rs0.getString("f.orainizio");
                        String orafine = rs0.getString("f.orafine");
                        String sql1 = "SELECT a.idallievi,a.nome,a.telefono FROM allievi a WHERE a.id_statopartecipazione='01' AND a.idprogetti_formativi=" + idpr;
                        try (Statement st1 = db0.getC().createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {
                            if (rs1.next()) {
                                String nome = rs1.getString("nome").toUpperCase();
                                String telefono = rs1.getString("telefono").toUpperCase();
                                String msg = "Ciao " + nome + " ti ricordiamo che oggi ci sarà la prima lezione del tuo PF, dalle " + orainizio + " alle " + orafine + ". Controlla la tua mail.";
                                boolean es = it.refill.reportistica.Sms.sendSMS2021(telefono, msg, db0);
                                if (es) {
                                    System.out.println("SMS INVIATO AD ID " + idpr);
                                } else {
                                    System.out.println("KO SMS INVIATO AD ID " + idpr);
                                }
                            }
                        }
                    }
                }
            }
            db0.closeDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    
    public static void main(String[] args) {
        boolean professioni;
        try {
            professioni = Boolean.valueOf(args[0].trim());
        } catch (Exception e) {
            professioni = false;
        }
        
        sms_primogiorno(professioni);
    }
}
