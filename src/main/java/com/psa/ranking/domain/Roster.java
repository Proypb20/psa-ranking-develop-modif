package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.psa.ranking.domain.enumeration.ProfileUser;

/**
 * Roster entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "roster")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Roster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile")
    private ProfileUser profile;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @ManyToOne
    @JsonIgnoreProperties("rosters")
    private Category category;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "roster_player",
               joinColumns = @JoinColumn(name = "roster_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id"))
    private Set<Player> players = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public Roster active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ProfileUser getProfile() {
        return profile;
    }

    public Roster profile(ProfileUser profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(ProfileUser profile) {
        this.profile = profile;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Roster createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public Roster updatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Category getCategory() {
        return category;
    }

    public Roster category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Roster players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Roster addPlayer(Player player) {
        this.players.add(player);
        player.getRosters().add(this);
        return this;
    }

    public Roster removePlayer(Player player) {
        this.players.remove(player);
        player.getRosters().remove(this);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roster)) {
            return false;
        }
        return id != null && id.equals(((Roster) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Roster{" +
            "id=" + getId() +
            ", active='" + isActive() + "'" +
            ", profile='" + getProfile() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
