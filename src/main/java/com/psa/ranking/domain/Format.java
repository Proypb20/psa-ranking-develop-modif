package com.psa.ranking.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Format.
 */
@Entity
@Table(name = "format")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Format implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "coeficient", nullable = false)
    private Float coeficient;

    @Column(name = "players_qty")
    private Integer playersQty;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Format name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Format description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getCoeficient() {
        return coeficient;
    }

    public Format coeficient(Float coeficient) {
        this.coeficient = coeficient;
        return this;
    }

    public void setCoeficient(Float coeficient) {
        this.coeficient = coeficient;
    }

    public Integer getPlayersQty() {
        return playersQty;
    }

    public Format playersQty(Integer playersQty) {
        this.playersQty = playersQty;
        return this;
    }

    public void setPlayersQty(Integer playersQty) {
        this.playersQty = playersQty;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Format)) {
            return false;
        }
        return id != null && id.equals(((Format) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Format{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", coeficient=" + getCoeficient() +
            ", playersQty=" + getPlayersQty() +
            "}";
    }
}
