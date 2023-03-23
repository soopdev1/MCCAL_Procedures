package rc.so.engine;

import com.mailjet.client.ClientOptions;
import static com.mailjet.client.ClientOptions.builder;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.sms.SmsSend;
import java.util.logging.Level;
import static rc.so.engine.MainSelector.conf;
import static rc.so.engine.MainSelector.estraiEccezione;
import static rc.so.engine.MainSelector.log;

/**
 *
 * @author Administrator
 */
public class SMS_MJ {

    public static boolean sendSMS2022(String cell, String msg) {
        try {
            ClientOptions options = builder().bearerAccessToken(conf.getString("mj.sms.token")).build();
            MailjetClient client = new MailjetClient(options);
            MailjetRequest request = new MailjetRequest(SmsSend.resource)
                    .property(SmsSend.FROM, conf.getString("mj.sms.name"))
                    .property(SmsSend.TO, "+39" + cell)
                    .property(SmsSend.TEXT, msg);
            MailjetResponse response = client.post(request);
            if (response.getStatus() == 200) {
                return true;
            }
            log.log(Level.INFO, "sendSMS2022: {0}", response.getStatus());
            log.log(Level.INFO, "sendSMS2022: {0}", response.toString());
        } catch (Exception e) {
            log.severe(estraiEccezione(e));
        }
        return false;
    }

//    public static void main(String[] args) {
//        sendSMS2022("3286137172", "testing message");
//    }
}
