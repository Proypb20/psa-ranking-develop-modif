package com.ar.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ar.pbpoints.domain.enumeration.ProfileUser;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ar.pbpoints.domain.Player} entity. This class is used
 * in {@link com.ar.pbpoints.web.rest.PlayerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /players?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ProfileUser
     */
    public static class ProfileUserFilter extends Filter<ProfileUser> {

        public ProfileUserFilter() {
        }

        public ProfileUserFilter(ProfileUserFilter filter) {
            super(filter);
        }

        @Override
        public ProfileUserFilter copy() {
            return new ProfileUserFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ProfileUserFilter profile;

    private LongFilter userId;

    private LongFilter rosterId;

    public PlayerCriteria(){
    }

    public PlayerCriteria(PlayerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.profile = other.profile == null ? null : other.profile.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.rosterId = other.rosterId == null ? null : other.rosterId.copy();
    }

    @Override
    public PlayerCriteria copy() {
        return new PlayerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ProfileUserFilter getProfile() {
        return profile;
    }

    public void setProfile(ProfileUserFilter profile) {
        this.profile = profile;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getRosterId() {
        return rosterId;
    }

    public void setRosterId(LongFilter rosterId) {
        this.rosterId = rosterId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayerCriteria that = (PlayerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(profile, that.profile) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(rosterId, that.rosterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        profile,
        userId,
        rosterId
        );
    }

    @Override
    public String toString() {
        return "PlayerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (profile != null ? "profile=" + profile + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (rosterId != null ? "rosterId=" + rosterId + ", " : "") +
            "}";
    }

}
