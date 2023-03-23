/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.entities;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 *
 * @author rcosco
 */
@Entity
@Table(name = "selfiemployment_prestiti")
@NamedQueries(value = {
    @NamedQuery(name = "se_p.Elenco", query = "select se_p from Selfiemployment_Prestiti se_p ORDER BY se_p.id")
})
public class Selfiemployment_Prestiti implements Serializable {

    @Column(name = "id")
    @Id
    private Long id;

    @Column(name="descrizione")
    private String descrizione;
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Selfiemployment_Prestiti() {
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Selfiemployment_Prestiti)) {
            return false;
        }
        Selfiemployment_Prestiti other = (Selfiemployment_Prestiti) object;
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
