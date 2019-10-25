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

    private String names;

    private String surnames;

    private String email;

    private String phone;

    private Instant bornDate;

    private String psaId;

    private Instant eraseDate;

    private Boolean active;

    private Instant createDate;

    private Instant updatedDate;


    private Long addressId;

    private Long docTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getBornDate() {
        return bornDate;
    }

    public void setBornDate(Instant bornDate) {
        this.bornDate = bornDate;
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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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
            ", names='" + getNames() + "'" +
            ", surnames='" + getSurnames() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bornDate='" + getBornDate() + "'" +
            ", psaId='" + getPsaId() + "'" +
            ", eraseDate='" + getEraseDate() + "'" +
            ", active='" + isActive() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", address=" + getAddressId() +
            ", docType=" + getDocTypeId() +
            "}";
    }
}
