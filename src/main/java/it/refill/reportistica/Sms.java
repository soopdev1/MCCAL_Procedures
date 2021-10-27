/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.reportistica;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static it.refill.reportistica.Sms.MESSAGE_MEDIUM_QUALITY;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rcosco
 */
public class Sms {

    private static final String BASEURL = "https://api.skebby.it/API/v1.0/REST/";
    //private static final String MESSAGE_HIGH_QUALITY = "GP";
    public static final String MESSAGE_MEDIUM_QUALITY = "TI";
    //private static final String MESSAGE_LOW_QUALITY = "SI";

    public static boolean sendSMS2021(String cell, String msg, Database db1) {
        try {
            String skebbyuser = db1.getPathtemp("skebbyuser");
            String skebbyPwd = db1.getPathtemp("skebbyPwd");
            String[] authKeys = login(skebbyuser, skebbyPwd);
            SendSMSRequest sendSMS = new SendSMSRequest();
            sendSMS.setMessage(msg);
            sendSMS.setMessageType(MESSAGE_MEDIUM_QUALITY);
            sendSMS.addRecipient("+39" + cell);
            sendSMS.setSender("YISU");
            boolean es = sendSMS(authKeys, sendSMS);
            return es;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String[] login(String username, String password) throws IOException {
        URL url = new URL(BASEURL + "/login?username=" + username + "&password=" + password);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = "";
        String output;
        while ((output = br.readLine()) != null) {
            response += output;
        }
        conn.disconnect();
        String[] parts = response.split(";");
        return parts;
    }

    private static boolean sendSMS(String[] authKeys, SendSMSRequest sendSMS) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        URL url = new URL(BASEURL + "/sms");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("user_key", authKeys[0]);
        conn.setRequestProperty("Session_key", authKeys[1]);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setDoOutput(true);
        String payload = gson.toJson(sendSMS);
        OutputStream os = conn.getOutputStream();
        os.write(payload.getBytes());
        os.flush();
        if (conn.getResponseCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = "";
        String output;
        while ((output = br.readLine()) != null) {
            response += output;
        }
        conn.disconnect();
        SendSMSResponse responseObj = gson.fromJson(response, SendSMSResponse.class);
        return responseObj.isValid();
    }

}

class CreditSMS{
    
    String money,email;
    List<Smsvalue> sms;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Smsvalue> getSms() {
        return sms;
    }

    public void setSms(List<Smsvalue> sms) {
        this.sms = sms;
    }
    
    

}


class Smsvalue {
    String type;
    int quantity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
            
}


class SendSMSRequest {

    /**
     * The message body
     */
    private String message;

    /**
     * The message type
     */
    private String message_type = MESSAGE_MEDIUM_QUALITY;

    /**
     * Should the API return the remaining credits?
     */
    private boolean returnCredits = false;

    /**
     * The list of recipients
     */
    private List<String> recipient = new ArrayList<>();

    /**
     * The sender Alias (TPOA)
     */
    private String sender = null;

    /**
     * Postpone the SMS message sending to the specified date
     */
    private Date scheduled_delivery_time = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return message_type;
    }

    public void setMessageType(String messageType) {
        this.message_type = messageType;
    }

    public boolean isReturnCredits() {
        return returnCredits;
    }

    public void setReturnCredits(boolean returnCredits) {
        this.returnCredits = returnCredits;
    }

    public List<String> getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getScheduledDeliveryTime() {
        return scheduled_delivery_time;
    }

    public void setScheduledDeliveryTime(Date scheduled_delivery_time) {
        this.scheduled_delivery_time = scheduled_delivery_time;
    }

    public void addRecipient(String recipient) {
        this.recipient.add(recipient);
    }
}

/**
 * This class represents the API Response. It is automatically created starting
 * from the JSON object returned by the server, using GSon
 */
class SendSMSResponse {

    private String result;
    private String order_id;
    private int total_sent;
    private int remaining_credits;
    private String internal_order_id;

    public String getResult() {
        return result;
    }

    public String getOrderId() {
        return order_id;
    }

    public int getTotalSent() {
        return total_sent;
    }

    public int getRemainingCredits() {
        return remaining_credits;
    }

    public String getInternalOrderId() {
        return internal_order_id;
    }

    public boolean isValid() {
        return "OK".equals(result);
    }
}
