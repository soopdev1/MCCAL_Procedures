/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.reportistica;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import rc.so.engine.MainSelector;
import static rc.so.engine.MainSelector.conf;
import static rc.so.engine.MainSelector.estraiEccezione;
import static rc.so.engine.MainSelector.log;

/**
 *
 * @author rcosco
 */
public class Database {

    public Connection c = null;

    public Database() {

        String user = conf.getString("db.user");
        String password = conf.getString("db.pass");

        /*NUOVO MC NAZIONALE*/
        String host = conf.getString("db.host") + ":3306/" + conf.getString("db.name");
//        String host = conf.getString("db.host") + ":3306/microcredito";//MICROCREDITO
//        if (professioni) {
//            host = conf.getString("db.host") + ":3306/professioni";//PROFESSIONI
//        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Properties p = new Properties();
            p.put("user", user);
            p.put("password", password);
            p.put("characterEncoding", "UTF-8");
            p.put("passwordCharacterEncoding", "UTF-8");
            p.put("useSSL", "false");
            p.put("connectTimeout", "1000");
            p.put("useUnicode", "true");
            p.put("serverTimezone", "UTC");
            p.put("zeroDateTimeBehavior", "convertToNull");
            this.c = DriverManager.getConnection("jdbc:mysql://" + host, p);
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
            if (this.c != null) {
                try {
                    this.c.close();
                } catch (SQLException ex1) {
                }
            }
            this.c = null;
        }
    }

    public void closeDB() {
        try {
            if (c != null) {
                this.c.close();
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
    }

    public Connection getC() {
        return c;
    }

    public void setC(Connection c) {
        this.c = c;
    }

    public boolean updatePath(String id, String url) {
        try {
            String upd = "UPDATE path SET url = ? WHERE id = ? ";
            boolean ok;
            try ( PreparedStatement ps = this.c.prepareStatement(upd)) {
                ps.setString(1, url);
                ps.setString(2, id);
                ok = ps.executeUpdate() > 0;
            }
            return ok;
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return false;
    }

    public String getPathtemp(String id) {
        String p1 = "";
        try {
            String sql = "SELECT url FROM path WHERE id = ?";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p1 = rs.getString(1);
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return p1;
    }

    public String getNomePR_F(String id) {
        String p1 = "Progetto Formativo";
        try {
            String sql = "SELECT descrizione FROM progetti_formativi WHERE idprogetti_formativi = ?";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p1 = rs.getString(1);
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return p1;
    }

    public String getSAPR_F(String id) {
        String p1 = "0";
        try {
            String sql = "SELECT idsoggetti_attuatori FROM progetti_formativi WHERE idprogetti_formativi = ?";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p1 = rs.getString(1);
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return p1;
    }

    public boolean updateReportFAD(Report re) {
        try {
            String sql = "SELECT idprogetti_formativi FROM fad_report WHERE idprogetti_formativi = " + re.getIdpr();
            Statement st = this.c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int x;
                try ( Statement st1 = this.c.createStatement()) {
                    String update = "UPDATE fad_report SET base64 = '" + re.getBase64() + "' WHERE idprogetti_formativi = " + re.getIdpr();
                    x = st1.executeUpdate(update);
                }
                rs.close();
                st.close();
                return x > 0;
            } else {
                try ( Statement st1 = this.c.createStatement()) {
                    String insert = "INSERT INTO fad_report VALUES (" + re.getIdpr() + ",'" + re.getBase64() + "','Y')";
                    st1.execute(insert);
                }
                rs.close();
                st.close();
                return true;
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return false;
    }

    public List<Report> fadreport_dafare() {
        List<Report> out = new ArrayList<>();
        try {
            String sql1 = "SELECT DISTINCT(f.idprogetti_formativi) FROM fad_calendar f";
            try ( Statement st1 = this.c.createStatement();  ResultSet rs1 = st1.executeQuery(sql1)) {
                while (rs1.next()) {
                    int idpr = rs1.getInt(1);
                    String sql2 = "SELECT f.definitivo FROM fad_report f WHERE f.idprogetti_formativi = " + idpr;
                    try ( Statement st2 = this.c.createStatement();  ResultSet rs2 = st2.executeQuery(sql2)) {
                        if (rs2.next()) {
                            if (rs2.getString(1).equals("N")) {
                                out.add(new Report(idpr, true));
                            }
                        } else {
                            out.add(new Report(idpr, false));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return out;
    }

    public List<String> getMailFromConference(String nomestanza) {
        List<String> out = new ArrayList<>();
        try {
            String sql = "SELECT partecipanti FROM fad_micro WHERE idfad = " + nomestanza;
            try ( Statement st = this.c.createStatement();  ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    out = Arrays.asList(new Gson().fromJson(rs.getString(1).toUpperCase().trim(), String[].class));
                }
            }
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return out;
    }

}
