/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.refill.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author rcosco
 */
@NamedQueries(value = {
    @NamedQuery(name = "progetti.ProgettiDocente", query = "SELECT p FROM ProgettiFormativi p WHERE p.docenti=:docente"),})
@Entity
@Table(name = "progetti_formativi")
@JsonIgnoreProperties(value = {"docenti", "allievi", "documenti"})
public class ProgettiFormativi implements Serializable {

    @Id
    @Column(name = "idprogetti_formativi")
    private Long id;
    @Column(name = "data_up")
    @Temporal(TemporalType.DATE)
    private Date data_up;
    @Column(name = "start")
    @Temporal(TemporalType.DATE)
    private Date start;
    @Column(name = "end")
    @Temporal(TemporalType.DATE)
    private Date end;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", name = "timestamp")
    private Date timestamp;
    @Column(name = "descrizione")
    private String descrizione;
    @Column(name = "nome")
    private Long nome;
    @Column(name = "cip")
    private String cip;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "ore")
    private Double ore;
    @Column(name = "ore_svolte")
    private Integer ore_svolte;
    @Column(name = "controllable", columnDefinition = "INT(1) DEFAULT 0")
    private int controllable;
    @Column(name = "archiviabile", columnDefinition = "INT(1) DEFAULT 0")
    private int archiviabile;
    @Column(name = "extract", columnDefinition = "INT(1) DEFAULT 0")
    private int extract;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_fa")
    private Date end_fa;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_fb")
    private Date start_fb;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_fb")
    private Date end_fb;
    @Column(name = "importo")
    private Double importo;
    @ManyToOne
    @JoinColumn(name = "idsoggetti_attuatori")
    SoggettiAttuatori soggetto;

    @ManyToOne
    @JoinColumn(name = "idsede")
    SediFormazione sede;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "progetti_docenti", joinColumns = @JoinColumn(name = "idprogetti_formativi"), inverseJoinColumns = @JoinColumn(name = "iddocenti"))
    List<Docenti> docenti;

    @OneToMany(mappedBy = "progetto", fetch = FetchType.LAZY)
    List<Allievi> allievi;

    @ManyToOne
    @JoinColumn(name = "stato")
    StatiPrg stato;

    public ProgettiFormativi() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData_up() {
        return data_up;
    }

    public void setData_up(Date data_up) {
        this.data_up = data_up;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getNome() {
        return nome;
    }

    public void setNome(Long nome) {
        this.nome = nome;
    }

    public double getOre() {
        return ore;
    }

    public void setOre(double ore) {
        this.ore = ore;
    }

    public int getOre_svolte() {
        return ore_svolte;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setOre_svolte(int ore_svolte) {
        this.ore_svolte = ore_svolte;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public SoggettiAttuatori getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(SoggettiAttuatori soggetto) {
        this.soggetto = soggetto;
    }

    public List<Docenti> getDocenti() {
        List<Docenti> docenti_list = new ArrayList<>();
        docenti_list.addAll(this.docenti);
        return docenti_list;
    }

    public void setDocenti(List<Docenti> docenti) {
        this.docenti = docenti;
    }

    public List<Allievi> getAllievi() {
        List<Allievi> list_allievi = new ArrayList<>();// per fixare il bub dello stream per le lazy list di EclipseLink
        list_allievi.addAll(this.allievi);
        return list_allievi;
    }

    public void setAllievi(List<Allievi> allievi) {
        this.allievi = allievi;
    }

    public SediFormazione getSede() {
        return sede;
    }

    public void setSede(SediFormazione sede) {
        this.sede = sede;
    }

    public int getControllable() {
        return controllable;
    }

    public void setControllable(int controllable) {
        this.controllable = controllable;
    }

    public Date getEnd_fa() {
        return end_fa;
    }

    public void setEnd_fa(Date end_fa) {
        this.end_fa = end_fa;
    }

    public Date getStart_fb() {
        return start_fb;
    }

    public void setStart_fb(Date start_fb) {
        this.start_fb = start_fb;
    }

    public Date getEnd_fb() {
        return end_fb;
    }

    public void setEnd_fb(Date end_fb) {
        this.end_fb = end_fb;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public int getArchiviabile() {
        return archiviabile;
    }

    public void setArchiviabile(int archiviabile) {
        this.archiviabile = archiviabile;
    }

    public int getExtract() {
        return extract;
    }

    public void setExtract(int extract) {
        this.extract = extract;
    }

    public StatiPrg getStato() {
        return stato;
    }

    public void setStato(StatiPrg stato) {
        this.stato = stato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgettiFormativi)) {
            return false;
        }
        ProgettiFormativi other = (ProgettiFormativi) object;
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
