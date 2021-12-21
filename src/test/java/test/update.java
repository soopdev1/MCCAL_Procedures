/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import it.refill.engine.Engine;
import it.refill.reportistica.Database;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Administrator
 */
public class update {

    public static void setCell(XSSFCell cella, String valore) {
        try {
            cella.setCellValue(valore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCell(XSSFCell cella, XSSFCellStyle style, String valore, boolean integervalue, boolean doublevalue) {
        try {
            cella.setCellStyle(style);

            if (integervalue) {
                cella.setCellValue(Integer.parseInt(valore));
            } else if (doublevalue) {
                cella.setCellValue(Double.parseDouble(valore));
            } else {
                cella.setCellValue(valore);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static XSSFCell getCell(XSSFRow riga, int indice) {
        XSSFCell cell1;
        try {
            cell1 = riga.getCell(indice);
            if (cell1 == null) {
                cell1 = riga.createCell(indice);

            }
        } catch (Exception e) {
            e.printStackTrace();
            cell1 = null;
        }
        return cell1;
    }

    public static XSSFRow getRow(XSSFSheet foglio, int indice) {
        XSSFRow riga;
        try {
            riga = foglio.getRow(indice);
            if (riga == null) {
                riga = foglio.createRow(indice);
            }
            riga.setHeight((short) -1);
        } catch (Exception e) {
            e.printStackTrace();
            riga = null;
        }
        return riga;
    }

    public static String getCellValue(XSSFCell cella) {
        try {
            switch (cella.getCellType()) {
                case STRING:
                    return cella.getRichStringCellValue().getString().toUpperCase().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cella)) {
                        final DataFormatter df = new DataFormatter();
                        return df.formatCellValue(cella).toUpperCase().trim();
                    } else {
                        return String.valueOf(cella.getNumericCellValue()).trim();
                    }
                case BOOLEAN:
                    return String.valueOf(cella.getBooleanCellValue()).toUpperCase().trim();
                case FORMULA:
                    return (cella.getCellFormula()).toUpperCase().trim();
                default:
                    return "";
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static void main(String[] args) {

        String fileing = "C:\\Users\\Administrator\\Desktop\\da caricare\\Copia di cittadinanza.xlsx";

        try {
            List<Obj1> l1 = new ArrayList<>();
            try (XSSFWorkbook wb = new XSSFWorkbook(new File(fileing))) {
                AtomicInteger inizio = new AtomicInteger(4);
                XSSFRow row = getRow(wb.getSheetAt(0), inizio.get());
                while (!getCellValue(getCell(row, 0)).equals("")) {
                    l1.add(new Obj1(getCellValue(getCell(row, 0)).trim(), getCellValue(getCell(row, 1)).trim()));
                    inizio.addAndGet(1);
                    row = getRow(wb.getSheetAt(0), inizio.get());
                }
            }

            Database db = new Database(false);
            String select = "SELECT idcomune,istat,nome FROM comuni WHERE cittadinanza=1 AND istat<>000";

            try (Statement st = db.getC().createStatement(); ResultSet rs = st.executeQuery(select)) {
                while (rs.next()) {
                    String nome = rs.getString("nome").toLowerCase().trim();

                    Obj1 o1 = l1.stream().filter(n1 -> n1.getNome().toLowerCase().equals(nome)).findAny().orElse(null);
                    if (o1 != null) {
                        if (rs.getString("istat").equals(o1.getCodice())) {
                            System.out.println(nome + " INVARIATO");
                        } else {
                            String update = "UPDATE comuni SET istat = '" + o1.getCodice() + "' WHERE idcomune = "
                                    + rs.getInt("idcomune") + " AND istat = '" + rs.getString("istat") + "' AND cittadinanza=1";
//                            System.out.println(nome + " - VECCHIO " + rs.getString("istat") + " - NUOVO " + o1.getCodice());

                            try (Statement st2 = db.getC().createStatement()) {
                                System.out.println(nome + " " + (st2.executeUpdate(update) > 0));
                            }

                        }

                    } else {
                        System.out.println(nome + " NON TROVATO");
                    }

                }
            }

            db.closeDB();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        Engine.manage(71, false);
//        Engine.manage(16, true);
    }
}

class Obj1 {

    String codice, nome;

    public Obj1(String codice, String nome) {
        this.codice = codice;
        this.nome = nome;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
