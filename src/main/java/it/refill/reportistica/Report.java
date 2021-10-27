/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.refill.reportistica;

/**
 *
 * @author Administrator
 */
public class Report {

    int idpr;
    boolean update;
    String base64;

    public Report(int idpr, String base64) {
        this.idpr = idpr;
        this.base64 = base64;
    }

    public Report(int idpr, boolean update) {
        this.idpr = idpr;
        this.update = update;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public int getIdpr() {
        return idpr;
    }

    public void setIdpr(int idpr) {
        this.idpr = idpr;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
