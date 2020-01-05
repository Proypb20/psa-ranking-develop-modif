package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psa.ranking.domain.Person} entity.
 */
@ApiModel(description = "Person entity.\n@author Marcelo Miño")
public class PersonDTO implements Serializable {

    private Long id;

    private String psaId;

    private Instant eraseDate;

    private Boolean active;

    private Instant createDate;

    private Instant updatedDate;

    private String address;

    private String zipCode;


    private Long cityId;

    private Long docTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPsaId() {
        return psaId;
    }

    public void setPsaId(String psaId) {
        this.psaId = psaId;
    }

    public Instant getEraseDate() {
        return eraseDate;
    }

    public void setEraseDate(Instant eraseDate) {
        this.eraseDate = eraseDate;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Long getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Long docTypeId) {
        this.docTypeId = docTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if (personDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), personDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", psaId='" + getPsaId() + "'" +
            ", eraseDate='" + getEraseDate() + "'" +
            ", active='" + isActive() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", address='" + getAddress() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", city=" + getCityId() +
            ", docType=" + getDocTypeId() +
            "}";
    }
}
