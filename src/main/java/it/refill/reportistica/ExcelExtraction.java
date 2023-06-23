/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.reportistica;

import it.refill.entities.Allievi;
import it.refill.entities.Allievi_Pregresso;
import it.refill.entities.Docenti;
import it.refill.entities.Path;
import it.refill.entities.SoggettiAttuatori;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author raf
 */
public class ExcelExtraction {

    private static final SimpleDateFormat sdita = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat sdmysql = new SimpleDateFormat("yyyy-MM-dd");

//    private static boolean excelAllieviFormati() {
//        Entity e = new Entity();
//        String path = e.getPath("estrazione_allievi_formati");
//        try {
//            File out_file = new File(path);
//            FileInputStream inputStream = new FileInputStream(out_file);
//
//            Workbook workbook = WorkbookFactory.create(inputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            CreationHelper createHelper = workbook.getCreationHelper();
//            CellStyle celldata = workbook.createCellStyle();
//            celldata.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
//
//            int riga = 1, colonna = 0;
//            Row row = sheet.createRow(riga);// creo riga
//            List<Allievi> allievi;
//            List<Allievi_Pregresso> allievi_pregresso;
//            HashedMap<String, SoggettiAttuatori> soggetto_map = e.getSoggettiMap();
//            allievi = e.getAllieviConclusi();
//            allievi_pregresso = e.findAll(Allievi_Pregresso.class);
//
//            SoggettiAttuatori s;
//
//            if (!allievi_pregresso.isEmpty()) {
//                for (Allievi_Pregresso a : allievi_pregresso) {
//                    s = new SoggettiAttuatori();
//                    s = soggetto_map.get(a.getCf_sa());
//                    colonna = 0;
//                    row = sheet.createRow(riga);
//                    colonna = writeCell(row, colonna, a.getId().intValue());
//                    colonna = writeCell(row, colonna, a.getCognome());
//                    colonna = writeCell(row, colonna, a.getNome());
//                    colonna = writeCell(row, colonna, a.getCodice_fiscale().toUpperCase());
//                    colonna = writeCell(row, colonna, s == null ? a.getCf_sa() : s.getRagionesociale());
//                    colonna = writeCell(row, colonna, a.getData_di_nascita());
//                    colonna = writeCell(row, colonna, a.getComune_di_nascita());
//                    colonna = writeCell(row, colonna, a.getProvincia_domicilio());
//                    colonna = writeCell(row, colonna, a.getComune_domicilio());
//                    colonna = writeCell(row, colonna, a.getProvincia_sigla());
//                    colonna = writeCell(row, colonna, a.getComune_di_residenza());
//                    colonna = writeCell(row, colonna, a.getCpi_di_competenza());
//                    colonna = writeCell(row, colonna, "-");
//                    colonna = writeCell(row, colonna, a.getEmail());
//                    colonna = writeCell(row, colonna, a.getCellulare());
//                    riga++;
//                }
//                setSizeCell(sheet, row);
//            }
//            if (!allievi.isEmpty()) {
//                for (Allievi a : allievi) {
//                    colonna = 0;
//                    row = sheet.createRow(riga);
//                    colonna = writeCell(row, colonna, a.getId().intValue());
//                    colonna = writeCell(row, colonna, a.getCognome());
//                    colonna = writeCell(row, colonna, a.getNome());
//                    colonna = writeCell(row, colonna, a.getCodicefiscale().toUpperCase());
//                    colonna = writeCell(row, colonna, a.getSoggetto().getRagionesociale());
//                    colonna = writeCell(row, a.getDatanascita(), celldata, colonna);
//                    colonna = writeCell(row, colonna, a.getComune_nascita().getNome());
//                    colonna = writeCell(row, colonna, a.getComune_domicilio().getProvincia());
//                    colonna = writeCell(row, colonna, a.getComune_domicilio().getNome());
//                    colonna = writeCell(row, colonna, a.getComune_residenza().getProvincia());
//                    colonna = writeCell(row, colonna, a.getComune_residenza().getNome());
//                    colonna = writeCell(row, colonna, a.getCpi().getDescrizione());
//                    colonna = writeCell(row, colonna, a.getNeet());
//                    colonna = writeCell(row, colonna, a.getEmail());
//                    colonna = writeCell(row, colonna, a.getTelefono());
//                    riga++;
//                }
//                setSizeCell(sheet, row);
//            }
//            inputStream.close();
//            FileOutputStream out = new FileOutputStream(out_file);
//            workbook.write(out);
//            workbook.close();
//            out.flush();
//            out.close();
//
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            e.insertTracking(null, "BATCH excelDocenti: " + ex.getMessage());
//        } finally {
//            e.close();
//        }
//
//        return false;
//    }
    public static boolean excelDocenti() {
        Entity e = new Entity();
        String path = e.getPath("estrazione_docenti");

        try {
            File out_file = new File(path);
            Workbook workbook;
            try ( FileInputStream inputStream = new FileInputStream(out_file)) {
                workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                CreationHelper createHelper = workbook.getCreationHelper();
                CellStyle celldata = workbook.createCellStyle();
                celldata.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                int riga = sheet.getLastRowNum(), colonna = 0;
                Row row = sheet.getRow(riga);
                Cell cell = row.getCell(0);
                List<Docenti> docenti;
                if (cell.getCellType().toString().equals("STRING")) {
                    docenti = e.findAll(Docenti.class);
                } else {
                    docenti = e.getDocentiIdMaxOf((int) cell.getNumericCellValue());
                }
                System.out.println("docenti: " + docenti.size());
                if (!docenti.isEmpty()) {
                    for (Docenti d : docenti) {
                        riga++;
                        colonna = 0;
                        row = sheet.createRow(riga);
                        colonna = writeCell(row, colonna, d.getId().intValue());
                        colonna = writeCell(row, colonna, d.getNome().toUpperCase());
                        colonna = writeCell(row, colonna, d.getCognome().toUpperCase());
                        colonna = writeCell(row, colonna, d.getCodicefiscale().toUpperCase());
                        colonna = writeCell(row, d.getDatanascita(), celldata, colonna);
                        colonna = writeCell(row, colonna, d.getFascia().getDescrizione().toUpperCase());
                    }
                    setSizeCell(sheet, row);
                }
            }
            try ( FileOutputStream out = new FileOutputStream(out_file)) {
                workbook.write(out);
                workbook.close();
                out.flush();
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            e.insertTracking(null, "BATCH excelDocenti: " + ex.getMessage());
        } finally {
            e.close();
        }

        return false;
    }

//    private static boolean excelAllievi() {
//        Entity e = new Entity();
//        String path = e.getPath("estrazione_allievi");
//        try {
//            File out_file = new File(path);
//            FileInputStream inputStream = new FileInputStream(out_file);
//
//            Workbook workbook = WorkbookFactory.create(inputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            CreationHelper createHelper = workbook.getCreationHelper();
//            CellStyle celldata = workbook.createCellStyle();
//            celldata.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
//
//            int riga = sheet.getLastRowNum(), colonna = 0;
//            Row row = sheet.getRow(riga);
//            Cell cell = row.getCell(0);
//            List<Allievi> allievi;
//            if (cell == null || cell.getCellType().toString().equals("STRING")) {
//                allievi = e.findAll(Allievi.class);
//            } else {
//                allievi = e.getAllieviIdMaxOf((int) cell.getNumericCellValue());
//            }
//
//            if (!allievi.isEmpty()) {
//                for (Allievi a : allievi) {
//                    riga++;
//                    colonna = 0;
//                    row = sheet.createRow(riga);
//                    colonna = writeCell(row, colonna, a.getId().intValue());
//                    colonna = writeCell(row, colonna, a.getCognome());
//                    colonna = writeCell(row, colonna, a.getNome());
//                    colonna = writeCell(row, colonna, a.getCodicefiscale().toUpperCase());
//                    colonna = writeCell(row, colonna, a.getSoggetto().getRagionesociale());
//                    colonna = writeCell(row, a.getDatanascita(), celldata, colonna);
//                    colonna = writeCell(row, colonna, a.getComune_nascita().getNome());
//                    colonna = writeCell(row, colonna, a.getComune_domicilio().getProvincia());
//                    colonna = writeCell(row, colonna, a.getComune_domicilio().getNome());
//                    colonna = writeCell(row, colonna, a.getComune_residenza().getProvincia());
//                    colonna = writeCell(row, colonna, a.getComune_residenza().getNome());
//                    colonna = writeCell(row, colonna, a.getCpi().getDescrizione());
//                    colonna = writeCell(row, colonna, a.getNeet());
//                    colonna = writeCell(row, colonna, a.getEmail());
//                    colonna = writeCell(row, colonna, a.getTelefono());
//                }
//                setSizeCell(sheet, row);
//            }
//            inputStream.close();
//            FileOutputStream out = new FileOutputStream(out_file);
//            workbook.write(out);
//            workbook.close();
//            out.flush();
//            out.close();
//
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            e.insertTracking(null, "BATCH excelDocenti: " + ex.getMessage());
//        } finally {
//            e.close();
//        }
//
//        return false;
//    }
    public static void elencoDocenti_R() {
        Entity e = new Entity();
        try {

            List<Docenti> docenti = e.getDocenti();

            try ( XSSFWorkbook wb = new XSSFWorkbook()) {
                XSSFSheet sh1 = wb.createSheet();

                XSSFColor color = new XSSFColor(new java.awt.Color(43, 150, 150), null);
                XSSFCellStyle cellStyleintest = wb.createCellStyle();
                cellStyleintest.setBorderBottom(BorderStyle.THIN);
                cellStyleintest.setBorderTop(BorderStyle.THIN);
                cellStyleintest.setBorderRight(BorderStyle.THIN);
                cellStyleintest.setBorderLeft(BorderStyle.THIN);
                cellStyleintest.setFillForegroundColor(color);
                cellStyleintest.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyleintest.setAlignment(HorizontalAlignment.CENTER);

                XSSFCellStyle cellStylenorm = wb.createCellStyle();
                cellStylenorm.setBorderBottom(BorderStyle.THIN);
                cellStylenorm.setBorderTop(BorderStyle.THIN);
                cellStylenorm.setBorderRight(BorderStyle.THIN);
                cellStylenorm.setBorderLeft(BorderStyle.THIN);

                XSSFRow intestazione = getRow(sh1, 0);

                for (int ind = 0; ind < 6; ind++) {
                    XSSFCell ce1 = getCell(intestazione, ind);
                    ce1.setCellStyle(cellStyleintest);
                    switch (ind) {
                        case 0:
                            setCell(ce1, "ID");
                            break;
                        case 1:
                            setCell(ce1, "COGNOME");
                            break;
                        case 2:
                            setCell(ce1, "NOME");
                            break;
                        case 3:
                            setCell(ce1, "CODICE FISCALE");
                            break;
                        case 4:
                            setCell(ce1, "DATA DI NASCITA");
                            break;
                        case 5:
                            setCell(ce1, "FASCIA");
                            break;
                        default:
                            break;
                    }
                }

                AtomicInteger indice = new AtomicInteger(1);

                docenti.forEach(d1 -> {
                    XSSFRow rowcontent = getRow(sh1, indice.get());

                    indice.addAndGet(1);
                    for (int ind = 0; ind < 6; ind++) {
                        XSSFCell ce1 = getCell(rowcontent, ind);
                        ce1.setCellStyle(cellStylenorm);
                        switch (ind) {
                            case 0:
                                setCell(ce1, String.valueOf(d1.getId()));
                                break;
                            case 1:
                                setCell(ce1, d1.getCognome().toUpperCase());
                                break;
                            case 2:
                                setCell(ce1, d1.getNome().toUpperCase());
                                break;
                            case 3:
                                setCell(ce1, d1.getCodicefiscale().toUpperCase());
                                break;
                            case 4:
                                setCell(ce1, sdita.format(d1.getDatanascita()));
                                break;
                            case 5:
                                setCell(ce1, d1.getFascia().getDescrizione().toUpperCase());
                                break;
                            default:
                                break;
                        }
                    }
                });
                for (int i = 0; i < 6; i++) {
                    sh1.autoSizeColumn(i);
                }
                String fileout = e.getPath("estrazione_docenti");
                try ( FileOutputStream outputStream = new FileOutputStream(new File(fileout))) {
                    wb.write(outputStream);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            e.insertTracking(null, "BATCH elencoDocenti: " + ex.getMessage());
        } finally {
            e.close();
        }
    }

    public static void elencoAllievi_R() {
        Entity e = new Entity();
        try {
            // in formazione
            // "formato";

            List<String> statiavvio = Arrays.asList(new String[]{"S", "S1"});
            List<String> staticoncluso = Arrays.asList(new String[]{"C", "C1", "CL", "AR"});

            List<Excel_allievi> complete = new LinkedList<>();
            List<Allievi> allievi = e.findAll(Allievi.class);

            List<Allievi_Pregresso> allievi_pregresso = e.findAll(Allievi_Pregresso.class);
            HashedMap<String, SoggettiAttuatori> soggetto_map = e.getSoggettiMap();

            allievi.forEach(a1 -> {
                Excel_allievi a2 = new Excel_allievi();

                a2.setID(String.valueOf(a1.getId()));

                if (a1.getProgetto() == null) {
                    a2.setOrdine(0);
                    a2.setSTATO("IN FASE DI AVVIO");
                    a2.setIDPROGETTOFORMATIVO("-");
                    a2.setCIPPROGETTOFORMATIVO("-");
                    a2.setDATAINIZIOPERCORSOFORMATIVO("-");
                    a2.setDATAFINEPERCORSOFORMATIVO("-");
                } else {
                    if (a1.getStatopartecipazione().getId().equals("01")) {
                        if (statiavvio.contains(a1.getProgetto().getStato().getId())) {
                            a2.setSTATO("IN FORMAZIONE");
                            a2.setOrdine(1);
                        } else if (staticoncluso.contains(a1.getProgetto().getStato().getId())) {
                            a2.setSTATO("FORMATO");
                            a2.setOrdine(2);
                        } else {
                            a2.setSTATO("IN FORMAZIONE");
                            a2.setOrdine(1);
                        }
                    } else {
                        a2.setSTATO("RITIRATO");
                        a2.setOrdine(3);
                    }

                    a2.setIDPROGETTOFORMATIVO(String.valueOf(a1.getProgetto().getId()));
                    a2.setCIPPROGETTOFORMATIVO(a1.getProgetto().getCip() == null ? "-" : a1.getProgetto().getCip());
                    a2.setDATAINIZIOPERCORSOFORMATIVO(
                            a1.getProgetto().getStart() == null ? "-" : sdita.format(a1.getProgetto().getStart()));
                    a2.setDATAFINEPERCORSOFORMATIVO(
                            a1.getProgetto().getEnd() == null ? "-" : sdita.format(a1.getProgetto().getEnd()));
                }

                a2.setCOGNOME(a1.getCognome().toUpperCase());
                a2.setNOME(a1.getNome().toUpperCase());
                a2.setCODICEFISCALE(a1.getCodicefiscale().toUpperCase());
                a2.setDATANASCITA(sdita.format(a1.getDatanascita()));

                a2.setCOMUNENASCITA(a1.getComune_nascita().getNome().toUpperCase());

                a2.setDOMICILIOPROVINCIA(a1.getComune_domicilio().getProvincia().toUpperCase());
                a2.setDOMICILIOCOMUNE(a1.getComune_domicilio().getNome().toUpperCase());
                a2.setRESIDENZAPROVINCIA(a1.getComune_residenza().getProvincia().toUpperCase());
                a2.setRESIDENZACOMUNE(a1.getComune_residenza().getNome().toUpperCase());
                a2.setCPI(a1.getCpi().getDescrizione().toUpperCase());
                a2.setCONDIZIONELAVORATIVAPRECEDENTE(a1.getNeet().toUpperCase());
                a2.setINDIRIZZOEMAIL(a1.getEmail().toLowerCase());
                a2.setNUMERODITELEFONO(a1.getTelefono());
                a2.setSOGGETTOATTUATORE(a1.getSoggetto().getRagionesociale());
                complete.add(a2);
            });

            allievi_pregresso.forEach(a1 -> {
                Excel_allievi a2 = new Excel_allievi();

                a2.setID("P_" + a1.getId());
                a2.setSTATO("FORMATO");
                a2.setOrdine(2);
                a2.setCOGNOME(a1.getCognome().toUpperCase());
                a2.setNOME(a1.getNome().toUpperCase());
                a2.setCODICEFISCALE(a1.getCodice_fiscale().toUpperCase());
                a2.setDATANASCITA(convertDATEMYSQLTOITA(a1.getData_di_nascita()));
                a2.setCOMUNENASCITA(a1.getComune_di_nascita().toUpperCase());

                a2.setCOMUNENASCITA(a1.getComune_di_nascita().toUpperCase());
                a2.setDOMICILIOPROVINCIA(a1.getProvincia_domicilio().toUpperCase());
                a2.setDOMICILIOCOMUNE(a1.getComune_domicilio().toUpperCase());
                a2.setRESIDENZAPROVINCIA(a1.getProvincia_sigla().toUpperCase());
                a2.setRESIDENZACOMUNE(a1.getComune_di_residenza().toUpperCase());
                a2.setCPI(a1.getCpi_di_competenza().toUpperCase());
                a2.setCONDIZIONELAVORATIVAPRECEDENTE(a1.getEsperienza_lavorativa().toUpperCase());
                a2.setINDIRIZZOEMAIL(a1.getEmail().toLowerCase());
                a2.setNUMERODITELEFONO(a1.getCellulare());

                SoggettiAttuatori sa = soggetto_map.get(a1.getCf_sa());
                a2.setSOGGETTOATTUATORE(sa == null ? a1.getCf_sa() : sa.getRagionesociale());

                a2.setIDPROGETTOFORMATIVO("-");
                a2.setCIPPROGETTOFORMATIVO(a1.getId_percorso());
                a2.setDATAINIZIOPERCORSOFORMATIVO(convertDATEMYSQLTOITA(a1.getData_inizio_fase_a()));
                a2.setDATAFINEPERCORSOFORMATIVO(convertDATEMYSQLTOITA(a1.getData_fine_fase_b()));

                complete.add(a2);
            });

            if (!complete.isEmpty()) {

                //String pathIN = "F:\\MCAL\\ESTRAZIONI BASE\\TEMPLATE_ALLIEVI.xlsx";
                String base64IN = e.getPath("TEMPLATE_ALLIEVI");
                //File ing = new File(pathIN);
                // String fileout = e.getPath("estrazione_allievi");
                try ( XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(Base64.decodeBase64(base64IN)))) {
                    XSSFSheet sh1 = wb.getSheetAt(0);
                    AtomicInteger indice = new AtomicInteger(1);

                    Collections.sort(complete, Comparator.comparingInt(Excel_allievi::getOrdine));

                    complete.forEach(allievo -> {
                        XSSFRow row = getRow(sh1, indice.get());

                        setCell(getCell(row, 0), allievo.getID());
                        setCell(getCell(row, 1), allievo.getSTATO());
                        setCell(getCell(row, 2), allievo.getCOGNOME());
                        setCell(getCell(row, 3), allievo.getNOME());
                        setCell(getCell(row, 4), allievo.getCODICEFISCALE());
                        setCell(getCell(row, 5), allievo.getDATANASCITA());
                        setCell(getCell(row, 6), allievo.getCOMUNENASCITA());
                        setCell(getCell(row, 7), allievo.getDOMICILIOPROVINCIA());
                        setCell(getCell(row, 8), allievo.getDOMICILIOCOMUNE());
                        setCell(getCell(row, 9), allievo.getRESIDENZAPROVINCIA());
                        setCell(getCell(row, 10), allievo.getRESIDENZACOMUNE());
                        setCell(getCell(row, 11), allievo.getCPI());
                        setCell(getCell(row, 12), allievo.getCONDIZIONELAVORATIVAPRECEDENTE());
                        setCell(getCell(row, 13), allievo.getINDIRIZZOEMAIL());
                        setCell(getCell(row, 14), allievo.getNUMERODITELEFONO());
                        setCell(getCell(row, 15), allievo.getSOGGETTOATTUATORE());
                        setCell(getCell(row, 16), allievo.getIDPROGETTOFORMATIVO());
                        setCell(getCell(row, 17), allievo.getCIPPROGETTOFORMATIVO());
                        setCell(getCell(row, 18), allievo.getDATAINIZIOPERCORSOFORMATIVO());
                        setCell(getCell(row, 19), allievo.getDATAFINEPERCORSOFORMATIVO());
                        indice.addAndGet(1);
                    });

                    for (int i = 0; i < 19; i++) {
                        sh1.autoSizeColumn(i);
                    }
                    String fileout = e.getPath("estrazione_allievi");
                    try ( FileOutputStream outputStream = new FileOutputStream(new File(fileout))) {
                        wb.write(outputStream);
                    }
                    System.out.println(fileout);

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            e.insertTracking(null, "BATCH elencoAllievi: " + ex.getMessage());
        } finally {
            e.close();
        }
    }

    public static void setCell(XSSFCell cella, String valore) {
        try {
            cella.setCellValue(valore);
        } catch (Exception e) {
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
        } catch (Exception e) {
            riga = null;
        }
        return riga;
    }

    private static String convertDATEMYSQLTOITA(String ing) {
        try {
            return sdita.format(sdmysql.parse(ing));
        } catch (ParseException e) {
        }
        return "";
    }

    private static int writeCell(Row row, int colonna, String dato) {
        Cell cell = row.createCell(colonna);
        cell.setCellValue(dato == null ? "-" : dato);
        return colonna + 1;
    }

    private static int writeCell(Row row, int colonna, int dato) {
        Cell cell = row.createCell(colonna);
        cell.setCellValue(dato);
        return colonna + 1;
    }

    private static int writeCell(Row row, Date dato, CellStyle style, int colonna) {
        Cell cell = row.createCell(colonna);
        cell.setCellValue(dato);
        cell.setCellStyle(style);
        return colonna + 1;
    }

    private static void setSizeCell(Sheet sheet, Row row) {
        Iterator<Cell> cells = row.cellIterator();// fa il resize di tutte le celle
        while (cells.hasNext()) {
            sheet.autoSizeColumn(cells.next().getColumnIndex());
        }

    }

    public static boolean salvatemplate(File ing, String idpath) {
        Entity en = new Entity();
        try {
            String b64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(ing));
            en.begin();
            Path pa = en.getEm().find(Path.class, idpath);
            pa.setUrl(b64);
            en.merge(pa);
            en.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            en.close();
        }
        return false;
    }

    public static void main(String[] args) {
        ExcelExtraction.elencoAllievi_R();

//        salvatemplate(new File("F:\\MCAL\\ESTRAZIONI BASE\\TEMPLATE_ALLIEVI.xlsx"), "TEMPLATE_ALLIEVI");
    }
}
