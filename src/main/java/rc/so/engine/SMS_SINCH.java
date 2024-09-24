/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rc.so.engine;

import com.sinch.xms.ApiConnection;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsResult;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import static rc.so.engine.MainSelector.conf;
import static rc.so.engine.MainSelector.estraiEccezione;
import static rc.so.engine.MainSelector.log;

/**
 *
 * @author Administrator
 */
public class SMS_SINCH {

    public static boolean sendSMS2024(String cell, String msg) {
        try (ApiConnection conn = ApiConnection.builder()
                .servicePlanId(conf.getString("si.sms.spid"))
                .token(conf.getString("si.sms.token"))
                .start()) {
            String[] recipients = {"39" + StringUtils.replace(cell, "+39", "")};
            MtBatchTextSmsResult batch
                    = conn.createBatch(
                            SinchSMSApi.batchTextSms()
                                    .sender(conf.getString("si.sms.sender"))
                                    .addRecipient(recipients)
                                    .body(msg)
                                    .build());
            log.log(Level.INFO, "SMS OK -> {0} -- ID:{1}", new Object[]{cell, batch.id()});
            return true;
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
        }
        return false;
    }
}
