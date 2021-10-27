/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import it.refill.reportistica.Database;
import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author rcosco
 */
public class mail {
    public static void main(String[] args) {
        try {
//            Database db = new Database();
//            String b64 = db.getPathtemp("fadmail");
//            db.closeDB();
//            FileUtils.writeByteArrayToFile(new File("alunno.html"), Base64.decodeBase64(b64));



            File start = new File("alunno.html");
            String b64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(start));
            Database db = new Database(false);
            boolean es = db.updatePath("fadmail", b64);
            db.closeDB();
            System.out.println("test.mail.main() "+es);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
