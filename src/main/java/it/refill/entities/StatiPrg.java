package it.refill.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author rcosco
 */
@Entity
@Table(name = "stati_progetto")
public class StatiPrg implements Serializable {

    @Id
    @Column(name = "idstati_progetto")
    private String id;
    @Column(name = "descrizione")
    private String descrizione;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "de_tipo")
    private String de_tipo;
    @Column(name = "ordine")
    private int ordine;
    @Column(name = "controllare")
    private int controllare;
    @Column(name = "modificabile")
    private int modificabile;
    @Column(name = "modifica_doc")
    private int modifica_doc;
    @Column(name = "errore")
    private int errore;
    @Column(name = "ordine_processo")
    private int ordine_processo;

    public StatiPrg() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public int getControllare() {
        return controllare;
    }

    public void setControllare(int controllare) {
        this.controllare = controllare;
    }

    public int getErrore() {
        return errore;
    }

    public void setErrore(int errore) {
        this.errore = errore;
    }

    public String getDe_tipo() {
        return de_tipo;
    }

    public void setDe_tipo(String de_tipo) {
        this.de_tipo = de_tipo;
    }

    public int getModificabile() {
        return modificabile;
    }

    public void setModificabile(int modificabile) {
        this.modificabile = modificabile;
    }

    public int getModifica_doc() {
        return modifica_doc;
    }

    public void setModifica_doc(int modifica_doc) {
        this.modifica_doc = modifica_doc;
    }

    public int getOrdine_processo() {
        return ordine_processo;
    }

    public void setOrdine_processo(int ordine_processo) {
        this.ordine_processo = ordine_processo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StatiPrg)) {
            return false;
        }
        StatiPrg other = (StatiPrg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

}
