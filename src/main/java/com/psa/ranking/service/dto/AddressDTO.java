package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psa.ranking.domain.Address} entity.
 */
@ApiModel(description = "Address entity.\n@author The JHipster team.")
public class AddressDTO implements Serializable {

    private Long id;

    private String street;

    private String zipCode;


    private Long cityId;

    private Long userExtraId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getUserExtraId() {
        return userExtraId;
    }

    public void setUserExtraId(Long userExtraId) {
        this.userExtraId = userExtraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (addressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), addressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", city=" + getCityId() +
            ", userExtra=" + getUserExtraId() +
            "}";
    }
}
