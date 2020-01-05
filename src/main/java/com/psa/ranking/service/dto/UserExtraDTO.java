package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psa.ranking.domain.UserExtra} entity.
 */
@ApiModel(description = "UserExtra entity.\n@author Marcelo Miño")
public class UserExtraDTO implements Serializable {

    private Long id;

    private String numDoc;

    private String phone;

    private LocalDate bornDate;

    private Long userId;

    private Long docTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

        UserExtraDTO userExtraDTO = (UserExtraDTO) o;
        if (userExtraDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userExtraDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserExtraDTO{" +
            "id=" + getId() +
            ", numDoc='" + getNumDoc() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bornDate='" + getBornDate() + "'" +
            ", user=" + getUserId() +
            ", docType=" + getDocTypeId() +
            "}";
    }
}
