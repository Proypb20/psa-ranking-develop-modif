package com.ar.pbpoints.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Formula.
 */
@Entity
@Table(name = "formula")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Formula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "formula", nullable = false)
    private String formula;

    @NotNull
    @Column(name = "var_1", nullable = false)
    private String var1;

    @Column(name = "var_2")
    private String var2;

    @Column(name = "var_3")
    private String var3;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "example")
    private String example;

    @ManyToOne
    @JsonIgnoreProperties("formulas")
    private Tournament tournament;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormula() {
        return formula;
    }

    public Formula formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getVar1() {
        return var1;
    }

    public Formula var1(String var1) {
        this.var1 = var1;
        return this;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public Formula var2(String var2) {
        this.var2 = var2;
        return this;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public Formula var3(String var3) {
        this.var3 = var3;
        return this;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    public String getDescription() {
        return description;
    }

    public Formula description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public Formula example(String example) {
        this.example = example;
        return this;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Formula tournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formula)) {
            return false;
        }
        return id != null && id.equals(((Formula) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Formula{" +
            "id=" + getId() +
            ", formula='" + getFormula() + "'" +
            ", var1='" + getVar1() + "'" +
            ", var2='" + getVar2() + "'" +
            ", var3='" + getVar3() + "'" +
            ", description='" + getDescription() + "'" +
            ", example='" + getExample() + "'" +
            "}";
    }
}
