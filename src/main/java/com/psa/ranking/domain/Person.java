package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Person entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "psa_id")
    private String psaId;

    @Column(name = "erase_date")
    private Instant eraseDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Column(name = "address")
    private String address;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    @JoinColumn(unique = true)
    private City city;

    @ManyToOne
    @JsonIgnoreProperties("people")
    private DocType docType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPsaId() {
        return psaId;
    }

    public Person psaId(String psaId) {
        this.psaId = psaId;
        return this;
    }

    public void setPsaId(String psaId) {
        this.psaId = psaId;
    }

    public Instant getEraseDate() {
        return eraseDate;
    }

    public Person eraseDate(Instant eraseDate) {
        this.eraseDate = eraseDate;
        return this;
    }

    public void setEraseDate(Instant eraseDate) {
        this.eraseDate = eraseDate;
    }

    public Boolean isActive() {
        return active;
    }

    public Person active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Person createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public Person updatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public Person address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Person zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public City getCity() {
        return city;
    }

    public Person city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public DocType getDocType() {
        return docType;
    }

    public Person docType(DocType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", psaId='" + getPsaId() + "'" +
            ", eraseDate='" + getEraseDate() + "'" +
            ", active='" + isActive() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", address='" + getAddress() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            "}";
    }
}
