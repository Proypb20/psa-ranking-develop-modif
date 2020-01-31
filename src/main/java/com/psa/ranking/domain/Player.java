package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.psa.ranking.domain.enumeration.ProfileUser;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile")
    private ProfileUser profile;

    @ManyToMany(mappedBy = "players")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Roster> rosters = new HashSet<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileUser getProfile() {
        return profile;
    }

    public Player profile(ProfileUser profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(ProfileUser profile) {
        this.profile = profile;
    }

    public Set<Roster> getRosters() {
        return rosters;
    }

    public Player rosters(Set<Roster> rosters) {
        this.rosters = rosters;
        return this;
    }

    public Player addRoster(Roster roster) {
        this.rosters.add(roster);
        roster.getPlayers().add(this);
        return this;
    }

    public Player removeRoster(Roster roster) {
        this.rosters.remove(roster);
        roster.getPlayers().remove(this);
        return this;
    }

    public void setRosters(Set<Roster> rosters) {
        this.rosters = rosters;
    }

    public User getUser() {
        return user;
    }

    public Player user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", profile='" + getProfile() + "'" +
            "}";
    }
}
