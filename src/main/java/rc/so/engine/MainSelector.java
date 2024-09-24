/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rc.so.engine;

import rc.so.reportistica.Database;
import rc.so.reportistica.ExcelExtraction;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import static rc.so.engine.SMS_SINCH.sendSMS2024;
import static rc.so.reportistica.ExcelFAD.generatereportFAD_multistanza;
import rc.so.reportistica.Report;

/**
 *
 * @author Administrator
 */
public class MainSelector {

    public static final ResourceBundle conf = ResourceBundle.getBundle("conf.conf");
    public static final Logger log = createLog(conf.getString("name.log"), conf.getString("path.log"), conf.getString("date.log"));

    public static void main(String[] args) {

        String id;
        int select_action;
        try {
            select_action = Integer.parseInt(args[0].trim());
        } catch (Exception e) {
            select_action = 1;
        }

        try {
            id = args[1];
        } catch (Exception e) {
            id = null;
        }

        boolean print = false;

        //ARGS[0] OPERAZIONE 1 - 3
        //1 - GESTIONE - REPORTISTICA
        //2 - GESTIONE - FAD
        //3 - GESTIONE - SMS
        //--------//
        //4 - GESTIONE - REPORT ON DEMAND
        //5 - ACCREDITAMENTO - DOMANDE
        switch (select_action) {
            case 1:
                ExcelExtraction.elencoAllievi_R();
                ExcelExtraction.elencoDocenti_R();
                break;
            case 2:
                List<String> out = elenco_progetti();
                out.forEach(idpr -> {
                    File report_temp = generatereportFAD_multistanza(idpr, print);
                    if (report_temp != null) {
                        try {
                            String sql1 = "SELECT iddocumenti_progetti FROM documenti_progetti WHERE idprogetto = " + idpr + " AND tipo = 30";
                            Database db2 = new Database();
                            try ( Statement st = db2.getC().createStatement();  ResultSet rs = st.executeQuery(sql1)) {
                                if (rs.next()) {
                                    int iddoc = rs.getInt(1);
                                    String update1 = "UPDATE documenti_progetti SET path = '" + report_temp.getPath().replace("\\", "/") + "' WHERE iddocumenti_progetti=" + iddoc;
                                    try ( Statement st1 = db2.getC().createStatement()) {
                                        st1.executeUpdate(update1);
                                    }
                                } else {
                                    String insert1 = "INSERT INTO documenti_progetti (deleted,path,idprogetto,tipo) VALUES (0,'" + report_temp.getPath().replace("\\", "/") + "'," + idpr + ",30)";
                                    try ( Statement st1 = db2.getC().createStatement()) {
                                        st1.execute(insert1);
                                    }
                                }
                            }
                            db2.closeDB();

                        } catch (Exception ex1) {
                            log.severe(estraiEccezione(ex1));
                        }
                    }
                });
                break;
            case 3:
                sms_primogiorno();
                break;
            case 4:
                List<String> out2 = elenco_progetti();
                if (id != null) {
                    out2.clear();
                    out2.add(id);
                }
                out2.forEach(idpr -> {
                    File report_temp = generatereportFAD_multistanza(idpr, print);
                    if (report_temp != null) {
                        try {
                            String sql1 = "SELECT iddocumenti_progetti FROM documenti_progetti WHERE idprogetto = " + idpr + " AND tipo = 30";
                            Database db2 = new Database();
                            try ( Statement st = db2.getC().createStatement();  ResultSet rs = st.executeQuery(sql1)) {
                                if (rs.next()) {
                                    int iddoc = rs.getInt(1);
                                    String update1 = "UPDATE documenti_progetti SET path = '" + report_temp.getPath().replace("\\", "/") + "' WHERE iddocumenti_progetti=" + iddoc;
                                    try ( Statement st1 = db2.getC().createStatement()) {
                                        st1.executeUpdate(update1);
                                    }
                                } else {
                                    String insert1 = "INSERT INTO documenti_progetti (deleted,path,idprogetto,tipo) VALUES (0,'" + report_temp.getPath().replace("\\", "/") + "'," + idpr + ",30)";
                                    try ( Statement st1 = db2.getC().createStatement()) {
                                        st1.execute(insert1);
                                    }
                                }
                            }
                            db2.closeDB();
                        } catch (Exception ex1) {
                            log.severe(estraiEccezione(ex1));
                        }
                    }
                });
                break;
            case 5:
//                Accreditamento.engine();
                break;
            default:
                break;

        }

    }

    public static void manage(int idpr) {
        try {
            Database db2 = new Database();
            switch (idpr) {
                case 16:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'-- GIACOMO DEROSE','-- GIACOMO DE ROSE') "
                            + "WHERE f.room LIKE '%PR_16%' AND action LIKE'%-- GIACOMO DEROSE'");
                    break;
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
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'Solymar Camasca','ELSA SOLYMAR CAMASCA PACHECO') "
                            + "WHERE f.room LIKE 'FAD_44%' AND action LIKE '%Solymar Camasca'");
                    break;
                case 71:
                    db2.getC().createStatement().executeUpdate("UPDATE fad_track f SET ACTION = REPLACE(ACTION,'PEPE EVARISTO FRANCESCO PIO','EVARISTO FRANCESCO PIO PEPE') "
                            + "WHERE f.room LIKE 'FAD_71%' AND action LIKE '%PEPE EVARISTO FRANCESCO PIO'");
                    break;

                default:
                    break;
            }

            db2.closeDB();
        } catch (Exception ex1) {
            log.severe(estraiEccezione(ex1));
        }
    }

    public static void updatefilesEX(int idpr, File ing) {
        try {
            String base64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(ing));
            if (base64 != null) {
                Report re = new Report(idpr, base64);
                Database db2 = new Database();
                db2.updateReportFAD(re);
                db2.closeDB();
            }
        } catch (Exception ex1) {
            log.severe(estraiEccezione(ex1));
        }
    }

    public static void createDir(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (Exception e) {
        }
    }

    public static void createANDinsert(int idpr) {
        File out = generatereportFAD_multistanza(String.valueOf(idpr), false);
        if (out != null) {
            String base64;
            try {
                base64 = new String(Base64.encodeBase64(FileUtils.readFileToByteArray(out)));
                if (base64 != null) {
                    Report re = new Report(idpr, base64);
                    Database db2 = new Database();
                    db2.updateReportFAD(re);
                    db2.closeDB();
                }
            } catch (Exception ex1) {
                log.severe(estraiEccezione(ex1));
            }
        }
    }

    public static List<String> elenco_progetti() {
        List<String> out = new ArrayList<>();
        try {
            Database db2 = new Database();
            String sql = "SELECT f.idprogetti_formativi FROM fad_calendar f WHERE f.data = DATE_SUB(CURDATE(),INTERVAL 1 DAY)";
            try ( Statement st = db2.getC().createStatement();  ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out.add(rs.getString(1));
                }
            }

            db2.closeDB();
        } catch (Exception ex1) {
            log.severe(estraiEccezione(ex1));
        }
        List<String> outWithoutDuplicates = out.stream()
                .distinct()
                .collect(Collectors.toList());
        return outWithoutDuplicates;
    }

    public static void sms_primogiorno() {
        try {
            Database db0 = new Database();
            String sql0 = "SELECT MIN(f.data),p.idprogetti_formativi,CURDATE(),f.orainizio,f.orafine FROM fad_calendar f, progetti_formativi p "
                    + "WHERE p.idprogetti_formativi=f.idprogetti_formativi AND p.stato='FA' "
                    + "GROUP BY f.idprogetti_formativi";
            try ( Statement st0 = db0.getC().createStatement();  ResultSet rs0 = st0.executeQuery(sql0)) {
                while (rs0.next()) {
                    String datalezione1 = rs0.getString(1);
                    String oggi = rs0.getString(3);
                    if (datalezione1.equals(oggi)) {
                        int idpr = rs0.getInt(2);
                        String orainizio = rs0.getString("f.orainizio");
                        String orafine = rs0.getString("f.orafine");
                        String sql1 = "SELECT a.idallievi,a.nome,a.telefono FROM allievi a WHERE a.id_statopartecipazione='01' AND a.idprogetti_formativi=" + idpr;
                        try ( Statement st1 = db0.getC().createStatement();  ResultSet rs1 = st1.executeQuery(sql1)) {
                            if (rs1.next()) {
                                String nome = rs1.getString("nome").toUpperCase();
                                String cell = rs1.getString("telefono").toUpperCase();
                                String msg = "Ciao " + nome + " ti ricordiamo che oggi ci sarà la prima lezione del tuo PF, dalle " + orainizio + " alle " + orafine + ". Controlla la tua mail.";
                                boolean es = sendSMS2024(cell, msg);
                                if (es) {
                                    log.log(Level.WARNING, "SMS INVIATO AD ID {0}", idpr);
                                } else {
                                    log.log(Level.SEVERE, "KO SMS INVIATO AD ID {0}", idpr);
                                }
                            }
                        }
                    }
                }
            }
            db0.closeDB();
        } catch (Exception ex1) {
            log.severe(estraiEccezione(ex1));
        }

    }

    public static String estraiEccezione(Exception ec1) {
        try {
            String stack_nam = ec1.getStackTrace()[0].getMethodName();
            String stack_msg = ExceptionUtils.getStackTrace(ec1);
            return stack_nam + " - " + stack_msg;
        } catch (Exception e) {
        }
        return ec1.getMessage();

    }

    private static Logger createLog(String appname, String folderini, String patterndatefolder) {
        Logger LOGGER = Logger.getLogger(appname);
        try {
            DateTime dt = new DateTime();
            String filename = appname + "-" + dt.toString("HHmmssSSS") + ".log";
            File dirING = new File(folderini);
            dirING.mkdirs();
            if (patterndatefolder != null) {
                File dirINGNew = new File(dirING.getPath() + File.separator + dt.toString(patterndatefolder));
                dirINGNew.mkdirs();
                filename = dirINGNew.getPath() + File.separator + filename;
            } else {
                filename = dirING.getPath() + File.separator + filename;
            }
            Handler fileHandler = new FileHandler(filename);
            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception localIOException) {
        }

        return LOGGER;
    }

}
