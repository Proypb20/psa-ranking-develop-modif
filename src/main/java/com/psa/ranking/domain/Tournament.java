package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.psa.ranking.domain.enumeration.Status;

/**
 * Tournament entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "tournament")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tournament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "close_inscr_days")
    private Integer closeInscrDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "tournament")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("tournaments")
    private UserExtra owner;

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

    public Tournament name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCloseInscrDays() {
        return closeInscrDays;
    }

    public Tournament closeInscrDays(Integer closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
        return this;
    }

    public void setCloseInscrDays(Integer closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
    }

    public Status getStatus() {
        return status;
    }

    public Tournament status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Tournament createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public Tournament updatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Address getAddress() {
        return address;
    }

    public Tournament address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Tournament events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Tournament addEvent(Event event) {
        this.events.add(event);
        event.setTournament(this);
        return this;
    }

    public Tournament removeEvent(Event event) {
        this.events.remove(event);
        event.setTournament(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public UserExtra getOwner() {
        return owner;
    }

    public Tournament owner(UserExtra userExtra) {
        this.owner = userExtra;
        return this;
    }

    public void setOwner(UserExtra userExtra) {
        this.owner = userExtra;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tournament)) {
            return false;
        }
        return id != null && id.equals(((Tournament) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tournament{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", closeInscrDays=" + getCloseInscrDays() +
            ", status='" + getStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
