/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@NamedQueries(value = {
    @NamedQuery(name = "user.UsernamePwd", query = "SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
    ,
    @NamedQuery(name = "user.byEmail", query = "SELECT u FROM User u WHERE u.username=:username")
})
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "iduser")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "stato")
    private int stato = 2;
    @Column(name = "tipo")
    private int tipo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idsoggetti_attuatori")
    private SoggettiAttuatori soggettoAttuatore;

    public User() {
    }

    public User(Long id, String username, String password, int stato, int tipo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.stato = stato;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public SoggettiAttuatori getSoggettoAttuatore() {
        return soggettoAttuatore;
    }

    public void setSoggettoAttuatore(SoggettiAttuatori soggettoAttuatore) {
        this.soggettoAttuatore = soggettoAttuatore;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
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
