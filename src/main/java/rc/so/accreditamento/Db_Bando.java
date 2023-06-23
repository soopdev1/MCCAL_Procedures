
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.accreditamento;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static rc.so.accreditamento.Accreditamento.formatAL;

/**
 *
 * @author raffaele
 */
public class Db_Bando {

    private static final ResourceBundle rb = ResourceBundle.getBundle("conf.conf");
    private Connection c = null;

    public Db_Bando() {

        String user = rb.getString("db.user");
        String password = rb.getString("db.pass");
        String host = rb.getString("db.host") + ":3306/bandoh8";

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
            ex.printStackTrace();
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
        } catch (SQLException ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    public Db_Bando(Connection conn) {
        try {
            this.c = conn;
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    public Connection getConnectionDB() {
        return c;
    }

    public Connection getConnection() {
        return c;
    }

    public ArrayList<String> getUsername() {
        ArrayList<String> al = new ArrayList<>();
        try {
            String query = "select username from usersvalori where username in (select username from domandecomplete) and username not in (select username from bandoh8) group by username";
            try ( PreparedStatement ps = this.c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);  ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    al.add(rs.getString("username"));
                }
            }
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
        return al;
    }

    public DomandeComplete domandeComplete(String cod, String username) {
        DomandeComplete dc = new DomandeComplete();
        try {
            String sql = "SELECT * FROM usersvalori WHERE codbando = ? AND username = ?";
            PreparedStatement ps = this.c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, cod);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String campo = rs.getString("campo");
                dc.setCodbando("BA0F6");
                dc.setUsername(username);
                System.out.println(rs.getString("valore") + " - " + rs.getString("campo"));
                if (campo.equals("accreditato")) {
                    dc.setAccreditato(rs.getString("valore"));
                }
                if (campo.equals("caricasoc")) {
                    dc.setCarica(rs.getString("valore"));
                }
                if (campo.equals("cell")) {
                    dc.setCellulare(rs.getString("valore"));
                }
                if (campo.equals("cf")) {
                    dc.setCf(rs.getString("valore"));
                }
                if (campo.equals("cognome")) {
                    dc.setCognome(rs.getString("valore"));
                }
                if (campo.equals("comunecciaa")) {
                    dc.setCciaacomune(rs.getString("valore"));
                }
                if (campo.equals("data")) {
                    dc.setData(rs.getString("valore"));
                }
                if (campo.equals("docric1")) {
                    dc.setDocric(rs.getString("valore"));
                }
                if (campo.equals("email")) {
                    dc.setMail(rs.getString("valore"));
                }
                if (campo.equals("idoneo")) {
                    dc.setIdoneo(rs.getString("valore"));
                }
                if (campo.equals("matricolainps")) {
                    dc.setMatricolainps(rs.getString("valore"));
                }
                if (campo.equals("nato")) {
                    dc.setNato_a(rs.getString("valore"));
                }
                if (campo.equals("nome")) {
                    dc.setNome(rs.getString("valore"));
                }
                if (campo.equals("pec")) {
                    dc.setPec(rs.getString("valore"));
                }
                if (campo.equals("piva")) {
                    dc.setPivacf(rs.getString("valore"));
                }
                if (campo.equals("proccciaa")) {
                    dc.setCciaaprovincia(rs.getString("valore"));
                }
                if (campo.equals("rea")) {
                    dc.setRea(rs.getString("valore"));
                }
                if (campo.equals("sedecap")) {
                    dc.setSedecap(rs.getString("valore"));
                }
                if (campo.equals("sedecomune")) {
                    dc.setSedecomune(rs.getString("valore"));
                }
                if (campo.equals("sedeindirizzo")) {
                    dc.setSedeindirizzo(rs.getString("valore"));
                }
                if (campo.equals("sedeprov")) {
                    dc.setSedeprovincia(rs.getString("valore"));
                }
                if (campo.equals("societa")) {
                    dc.setSocieta(rs.getString("valore"));
                }
                if (campo.equals("tipdoc1")) {
                    dc.setTipdoc(rs.getString("valore"));
                }
                String[] valoriDomandaCompleta = getCodiceDomanda(username);
                dc.setCoddomanda(valoriDomandaCompleta[0]);
                dc.setDataconsegna(valoriDomandaCompleta[1]);
            }
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
        return dc;
    }

    public String[] getCodiceDomanda(String username) {
        String query = "select id,timestamp from domandecomplete where username=?";
        String[] valori = new String[2];
        try {
            PreparedStatement ps = this.c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valori[0] = rs.getString("id");
                valori[1] = rs.getString("timestamp");
            }
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
        return valori;
    }

    public boolean insertBandoH8(DomandeComplete dc) {
        String query = "insert into bandoh8 (codbando,username,nome,cognome,nato_a,data,carica,societa,sedecomune,sedeprovincia,sedeindirizzo,sedecap,cf,pivacf,cciaacomune,cciaaprovincia,rea,matricolainps,pec,mail,idoneo,cellulare,tipdoc,docric,coddomanda,dataconsegna,accreditato) value (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, dc.getCodbando());
            ps.setString(2, dc.getUsername());
            ps.setString(3, dc.getNome());
            ps.setString(4, dc.getCognome());
            ps.setString(5, dc.getNato_a());
            ps.setString(6, dc.getData());
            ps.setString(7, dc.getCarica());
            ps.setString(8, dc.getSocieta());
            ps.setString(9, dc.getSedecomune());
            ps.setString(10, dc.getSedeprovincia());
            ps.setString(11, dc.getSedeindirizzo());
            ps.setString(12, dc.getSedecap());
            ps.setString(13, dc.getCf());
            ps.setString(14, dc.getPivacf());
            ps.setString(15, dc.getCciaacomune());
            ps.setString(16, dc.getCciaaprovincia());
            ps.setString(17, dc.getRea());
            ps.setString(18, dc.getMatricolainps());
            ps.setString(19, dc.getPec());
            ps.setString(20, dc.getMail());
            ps.setString(21, dc.getIdoneo());
            ps.setString(22, dc.getCellulare());
            ps.setString(23, dc.getTipdoc());
            ps.setString(24, dc.getDocric());
            ps.setString(25, dc.getCoddomanda());
            System.out.println(dc.getDataconsegna().replace(".0", ""));
            ps.setString(26, dc.getDataconsegna().replace(".0", ""));
            ps.setString(27, dc.getAccreditato());
            int x = ps.executeUpdate();
            return (x > 0);
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    public ArrayList<String[]> getDescDocumenti() {
        ArrayList<String[]> val = new ArrayList<>();
        try {
            String query = "SELECT id,descrizione FROM doc_validi";
            PreparedStatement ps = this.c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] v1 = {rs.getString(1), rs.getString(2)};
                val.add(v1);
            }
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
        return val;
    }

    public void expexcel_completo() {
        ArrayList<String[]> documenti = getDescDocumenti();
        try {
            String sql = "SELECT * FROM bandoh8 where coddomanda <> '-' order by dataconsegna";
            XSSFWorkbook workbook;
            File xl;
            try ( PreparedStatement ps = this.c.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);  ResultSet rs = ps.executeQuery()) {
                workbook = new XSSFWorkbook();
                xl = new File("/mnt/mcprofessioni/bandoba0h8/start_bandoh8.xlsx");
                if (rs.next()) {
                    XSSFSheet sheet = workbook.createSheet("DOMANDE CONSEGNATE");
                    XSSFFont font = workbook.createFont();
                    font.setFontName("Arial");
                    font.setFontHeightInPoints((short) 12);
                    font.setBold(true);
                    XSSFCellStyle style1 = workbook.createCellStyle();
                    style1.setFillBackgroundColor(new XSSFColor());
                    style1.setFillForegroundColor(new XSSFColor(new java.awt.Color(192, 192, 192), workbook.getStylesSource().getIndexedColors()));
                    style1.setFillPattern(FillPatternType.forInt(1));
                    style1.setBorderTop(BorderStyle.THIN);
                    style1.setBorderBottom(BorderStyle.THIN);
                    style1.setBorderLeft(BorderStyle.THIN);
                    style1.setBorderRight(BorderStyle.THIN);
                    style1.setFont(font);
                    XSSFFont font2 = workbook.createFont();
                    font2.setFontName("Arial");
                    font2.setFontHeightInPoints((short) 12);
                    XSSFCellStyle style2 = workbook.createCellStyle();
                    style2.setBorderTop(BorderStyle.THIN);
                    style2.setBorderBottom(BorderStyle.THIN);
                    style2.setBorderLeft(BorderStyle.THIN);
                    style2.setBorderRight(BorderStyle.THIN);
                    style2.setFont(font2);
                    int cntriga = 1;
                    XSSFRow xSSFRow = sheet.createRow((short) cntriga);
                    Cell cl = xSSFRow.createCell(1);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Codice domanda");
                    cl = xSSFRow.createCell(2);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Username");
                    cl = xSSFRow.createCell(3);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("SA giaccreditato");
                    cl = xSSFRow.createCell(4);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Cognome");
                    cl = xSSFRow.createCell(5);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Nome");
                    cl = xSSFRow.createCell(6);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Nato a");
                    cl = xSSFRow.createCell(7);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Data nascita");
                    cl = xSSFRow.createCell(8);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Carica societaria");
                    cl = xSSFRow.createCell(9);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Societa'");
                    cl = xSSFRow.createCell(10);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Comune sede");
                    cl = xSSFRow.createCell(11);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Provincia sede");
                    cl = xSSFRow.createCell(12);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Indirizzo sede");
                    cl = xSSFRow.createCell(13);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("CAP sede");
                    cl = xSSFRow.createCell(14);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Codice fiscale");
                    cl = xSSFRow.createCell(15);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Partita iva/Codice fiscale SA");
                    cl = xSSFRow.createCell(16);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Comune CCIAA");
                    cl = xSSFRow.createCell(17);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Provincia CCIAA");
                    cl = xSSFRow.createCell(18);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Numero REA");
                    cl = xSSFRow.createCell(19);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Matricola Inps");
                    cl = xSSFRow.createCell(20);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("PEC");
                    cl = xSSFRow.createCell(21);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Email");
                    cl = xSSFRow.createCell(22);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Idoneita'");
                    cl = xSSFRow.createCell(23);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Cellulare");
                    cl = xSSFRow.createCell(24);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Tipo documento");
                    cl = xSSFRow.createCell(25);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Numero documento");
                    cl = xSSFRow.createCell(26);
                    cl.setCellStyle((CellStyle) style1);
                    cl.setCellValue("Data consegna");
                    rs.beforeFirst();
                    while (rs.next()) {
                        cntriga++;
                        xSSFRow = sheet.createRow((short) cntriga);
                        cl = xSSFRow.createCell(1);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("coddomanda"));
                        cl = xSSFRow.createCell(2);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("username"));
                        cl = xSSFRow.createCell(3);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("accreditato"));
                        cl = xSSFRow.createCell(4);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("cognome").toUpperCase());
                        cl = xSSFRow.createCell(5);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("nome").toUpperCase());
                        cl = xSSFRow.createCell(6);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("nato_a").toUpperCase());
                        cl = xSSFRow.createCell(7);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("data").toUpperCase());
                        cl = xSSFRow.createCell(8);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("carica").toUpperCase());
                        cl = xSSFRow.createCell(9);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("societa").toUpperCase());
                        cl = xSSFRow.createCell(10);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("sedecomune").toUpperCase());
                        cl = xSSFRow.createCell(11);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("sedeprovincia").toUpperCase());
                        cl = xSSFRow.createCell(12);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("sedeindirizzo").toUpperCase());
                        cl = xSSFRow.createCell(13);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("sedecap").toUpperCase());
                        cl = xSSFRow.createCell(14);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("cf").toUpperCase());
                        cl = xSSFRow.createCell(15);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("pivacf").toUpperCase());
                        cl = xSSFRow.createCell(16);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("cciaacomune").toUpperCase());
                        cl = xSSFRow.createCell(17);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("cciaaprovincia").toUpperCase());
                        cl = xSSFRow.createCell(18);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("rea").toUpperCase());
                        cl = xSSFRow.createCell(19);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("matricolainps").toUpperCase());
                        cl = xSSFRow.createCell(20);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("pec").toUpperCase());
                        cl = xSSFRow.createCell(21);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("mail").toUpperCase());
                        cl = xSSFRow.createCell(22);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("idoneo").toUpperCase());
                        cl = xSSFRow.createCell(23);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("cellulare").toUpperCase());
                        cl = xSSFRow.createCell(24);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(formatAL(rs.getString("tipdoc").toUpperCase(), documenti, 1));
                        cl = xSSFRow.createCell(25);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("docric"));
                        cl = xSSFRow.createCell(26);
                        cl.setCellStyle((CellStyle) style2);
                        cl.setCellValue(rs.getString("dataconsegna").toUpperCase());
                    }
                    for (int co = 1; co < 60; co++) {
                        sheet.autoSizeColumn(co);
                    }
                }
            }

            try ( FileOutputStream out = new FileOutputStream(xl)) {
                workbook.write(out);
            }

            FileUtils.writeByteArrayToFile(new File("/mnt/mcprofessioni/bandoba0h8/bandoh8.xlsx"), FileUtils.readFileToByteArray(xl));

//            String base64 = new String(Base64.encodeBase64(FileUtils.readFileToByteArray(xl)));
//            depositaExcel(xl.getName(), "/mnt/mcprofessioni/bandoba0h8/");
//            return base64;
        } catch (Exception ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
//            return null;
        }
    }

}
