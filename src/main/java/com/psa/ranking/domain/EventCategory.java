package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A EventCategory.
 */
@Entity
@Table(name = "event_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EventCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "split_deck")
    private Boolean splitDeck;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("eventCategories")
    private Event event;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("eventCategories")
    private Category category;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("eventCategories")
    private Format format;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSplitDeck() {
        return splitDeck;
    }

    public EventCategory splitDeck(Boolean splitDeck) {
        this.splitDeck = splitDeck;
        return this;
    }

    public void setSplitDeck(Boolean splitDeck) {
        this.splitDeck = splitDeck;
    }

    public Event getEvent() {
        return event;
    }

    public EventCategory event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Category getCategory() {
        return category;
    }

    public EventCategory category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Format getFormat() {
        return format;
    }

    public EventCategory format(Format format) {
        this.format = format;
        return this;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCategory)) {
            return false;
        }
        return id != null && id.equals(((EventCategory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EventCategory{" +
            "id=" + getId() +
            ", splitDeck='" + isSplitDeck() + "'" +
            "}";
    }
}
