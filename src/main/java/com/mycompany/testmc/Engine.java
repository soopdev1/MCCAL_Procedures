/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testmc;

import static com.mycompany.testmc.ExcelFAD.generatereportFAD_multistanza;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 *
 *
 */
public class Engine {

    private static void manage(int idpr, boolean professioni) {
        try {
            Database db2 = new Database(professioni);
            switch (idpr) {
                case 20:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- Alessia michienzi','-- CATERINA MICHIENZI') "
                            + "WHERE f.room = 'FAD_20_1' AND action LIKE'%-- Alessia michienzi'");
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- Caterina, Alessia','-- CATERINA MICHIENZI') "
                            + "WHERE f.room = 'FAD_20_1' AND action LIKE'%-- Caterina, Alessia'");
                    break;
                case 21:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- Staff','-- CARMELINA TROVATO') "
                            + "WHERE f.room = 'FAD_21_1' AND action LIKE'%-- Staff'");
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- undefined','-- CARMELINA TROVATO') "
                            + "WHERE f.room = 'FAD_21_1' AND date LIKE '2020-11-17%' AND action LIKE'%-- undefined'");
                    break;
                case 22:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- Giulia','-- IULIANA CATALINA HANGANU') "
                            + "WHERE f.room = 'FAD_22_1' AND action LIKE'%-- Giulia'");
                    break;
                case 24:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- Angela','-- ANGELA MESSINA') "
                            + "WHERE f.room = 'FAD_24_1' AND action LIKE'%-- Angela'");
                    break;
                case 29:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'Nisticó','Nisticò') "
                            + "WHERE f.room LIKE 'FAD_29%' AND action LIKE'%Nisticó%'");
                    break;
                case 35:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'Libero scarcello','Libero Vittorio Scarcello') "
                            + "WHERE f.room LIKE 'FAD_35%' AND action LIKE'%Libero scarcello%'");
                    break;
                case 38:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'Elena','ELENA CORVELLO') "
                            + "WHERE f.room LIKE 'FAD_38%' AND action LIKE '%Elena'");
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'Sarà la connessione','FERNANDO CRISPINO') "
                            + "WHERE f.room LIKE 'FAD_38%' AND action LIKE '%connessione'");
                    break;
                case 44:
                    String sql = "UPDATE fad_track f SET ACTION = REPLACE(ACTION,'Solymar Camasca','ELSA SOLYMAR CAMASCA PACHECO') "
                            + "WHERE f.room LIKE 'FAD_44%' AND action LIKE '%Solymar Camasca'";
                    db2.getC().createStatement().executeUpdate(sql);
                default:
                    break;
            }

            db2.closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatefilesEX(int idpr, File ing, boolean professioni) {
        try {
            String base64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(ing));
            if (base64 != null) {
                Report re = new Report(idpr, base64);
                Database db2 = new Database(professioni);
                db2.updateReportFAD(re);
                db2.closeDB();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void createDir(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (Exception e) {
        }
    }

    private static void createANDinsert(int idpr, boolean professioni) {
        File out = generatereportFAD_multistanza(String.valueOf(idpr), false, professioni);
        if (out != null) {
            String base64;
            try {
                base64 = new String(Base64.encodeBase64(FileUtils.readFileToByteArray(out)));
                if (base64 != null) {
                    Report re = new Report(idpr, base64);
                    Database db2 = new Database(professioni);
                    db2.updateReportFAD(re);
                    db2.closeDB();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static List<String> elenco_progetti(boolean professioni) {
        List<String> out = new ArrayList<>();
        try {
            Database db2 = new Database(professioni);
            String sql = "SELECT DISTINCT(room) FROM fad_track f WHERE TIMESTAMP LIKE CONCAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),'%')";

            try (Statement st = db2.getC().createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    String room = rs.getString(1);
                    if (room.contains("_")) {
                        String pf = room.split("_")[1];
                        out.add(pf);
                    }
                }
            }

            db2.closeDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<String> outWithoutDuplicates = out.stream()
                .distinct()
                .collect(Collectors.toList());
        return outWithoutDuplicates;
    }

    public static void sms_primogiorno(boolean professioni) {
        try {
            Database db0 = new Database(professioni);
            String sql0 = "SELECT MIN(f.data),p.idprogetti_formativi,CURDATE() FROM fad_calendar f, progetti_formativi p "
                    + "WHERE p.idprogetti_formativi=f.idprogetti_formativi AND p.stato='FA' "
                    + "GROUP BY f.idprogetti_formativi";
            try (Statement st0 = db0.getC().createStatement(); ResultSet rs0 = st0.executeQuery(sql0)) {
                while (rs0.next()) {
                    String data = rs0.getString(1);
                    String oggi = rs0.getString(3);
                    if (data.equals(oggi)) {
                        int idpr = rs0.getInt(2);
                        
                        
                        
                    }
                }
            }
            db0.closeDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {

////        String pro;
        boolean print = false;
        boolean professioni = false;
////        String id;
////        try {
////            id = args[0];
////            pro = args[1];
////        } catch (Exception e) {
////            id = null;
////            pro = "false";
////        }

//        boolean professioni = (pro == null) || (pro.equals("true"));

        List<String> out = elenco_progetti(professioni);

//        if (id != null) {
//            out.clear();
//            out.add(id);
//        }

        out.forEach(idpr -> {
            File report_temp = generatereportFAD_multistanza(idpr, print, professioni);
            if (report_temp != null) {
                try {
                    String sql1 = "SELECT iddocumenti_progetti FROM documenti_progetti WHERE idprogetto = " + idpr + " AND tipo = 30";
                    Database db2 = new Database(professioni);
                    try (Statement st = db2.getC().createStatement(); ResultSet rs = st.executeQuery(sql1)) {
                        if (rs.next()) {
                            int iddoc = rs.getInt(1);
                            String update1 = "UPDATE documenti_progetti SET path = '" + report_temp.getPath().replace("\\", "/") + "' WHERE iddocumenti_progetti=" + iddoc;
                            try (Statement st1 = db2.getC().createStatement()) {
                                st1.executeUpdate(update1);
                            }
                        } else {
                            String insert1 = "INSERT INTO documenti_progetti (deleted,path,idprogetto,tipo) VALUES (0,'" + report_temp.getPath().replace("\\", "/") + "'," + idpr + ",30)";
                            try (Statement st1 = db2.getC().createStatement()) {
                                st1.execute(insert1);
                            }
                        }
                    }
                    db2.closeDB();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

//        int idpr = 3;
////        manage(idpr);
////        createANDinsert(idpr);
////        String base64 = generatereportFAD_multistanza(String.valueOf(idpr), print, professioni);
//
//        String startpath = "F:\\mnt\\Microcredito\\Cloud\\";
//        if (professioni) {
//            startpath = "F:\\mnt\\mcprofessioni\\Cloud\\";
//        }
//////        //////////////////////////////////////////////////////////////////////
//        updatefilesEX(idpr, new File(startpath + "Progetto_3_report_CFAD_CANTIERI.xlsx"), professioni);
    }

}

class Report {

    int idpr;
    boolean update;
    String base64;

    public Report(int idpr, String base64) {
        this.idpr = idpr;
        this.base64 = base64;
    }

    public Report(int idpr, boolean update) {
        this.idpr = idpr;
        this.update = update;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public int getIdpr() {
        return idpr;
    }

    public void setIdpr(int idpr) {
        this.idpr = idpr;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
