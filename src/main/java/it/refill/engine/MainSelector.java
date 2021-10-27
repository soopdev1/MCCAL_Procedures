/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.refill.engine;

import static it.refill.engine.Engine.elenco_progetti;
import static it.refill.engine.Engine.sms_primogiorno;
import it.refill.reportistica.Database;
import it.refill.reportistica.ExcelExtraction;
import static it.refill.reportistica.ExcelFAD.generatereportFAD_multistanza;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MainSelector {

    public static void main(String[] args) {

        int select_action;
        try {
            select_action = Integer.parseInt(args[0].trim());
        } catch (Exception e) {
            select_action = 0;
        }

        boolean print = false;
        boolean professioni = false;
        //ARGS[0] OPERAZIONE 1 - 3
        //1 - GESTIONE - REPORTISTICA
        //2 - GESTIONE - FAD
        //3 - GESTIONE - SMS
        switch (select_action) {
            case 1:
                ExcelExtraction.elencoAllievi_R();
                ExcelExtraction.elencoDocenti_R();
                break;
            case 2:
                List<String> out = elenco_progetti(professioni);
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
                break;
            case 3:
                sms_primogiorno(professioni);
                break;
            default:
                break;

        }

    }
}
