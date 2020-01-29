package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * UserExtra entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "user_extra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_doc")
    private String numDoc;

    @Column(name = "phone")
    private String phone;

    @Column(name = "born_date")
    private LocalDate bornDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("userExtras")
    private DocType docType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public UserExtra numDoc(String numDoc) {
        this.numDoc = numDoc;
        return this;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public UserExtra phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public UserExtra bornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
        return this;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public User getUser() {
        return user;
    }

    public UserExtra user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DocType getDocType() {
        return docType;
    }

    public UserExtra docType(DocType docType) {
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
        if (!(o instanceof UserExtra)) {
            return false;
        }
        return id != null && id.equals(((UserExtra) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "UserExtra [id=" + id + ", numDoc=" + numDoc + ", phone=" + phone + ", bornDate=" + bornDate + ", user="
				+ user + ", docType=" + docType + "]";
	}


}
