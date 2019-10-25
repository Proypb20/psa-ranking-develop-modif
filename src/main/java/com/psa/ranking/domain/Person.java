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

    @Column(name = "names")
    private String names;

    @Column(name = "surnames")
    private String surnames;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "born_date")
    private Instant bornDate;

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

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

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

    public String getNames() {
        return names;
    }

    public Person names(String names) {
        this.names = names;
        return this;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public Person surnames(String surnames) {
        this.surnames = surnames;
        return this;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getEmail() {
        return email;
    }

    public Person email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Person phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getBornDate() {
        return bornDate;
    }

    public Person bornDate(Instant bornDate) {
        this.bornDate = bornDate;
        return this;
    }

    public void setBornDate(Instant bornDate) {
        this.bornDate = bornDate;
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

    public Address getAddress() {
        return address;
    }

    public Person address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
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
            ", names='" + getNames() + "'" +
            ", surnames='" + getSurnames() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bornDate='" + getBornDate() + "'" +
            ", psaId='" + getPsaId() + "'" +
            ", eraseDate='" + getEraseDate() + "'" +
            ", active='" + isActive() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
