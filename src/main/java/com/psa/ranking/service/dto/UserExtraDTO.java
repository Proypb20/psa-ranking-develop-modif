package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.psa.ranking.domain.UserExtra} entity.
 */
@ApiModel(description = "UserExtra entity.\n@author Marcelo Miño")
public class UserExtraDTO implements Serializable {

    private Long id;

    private String numDoc;

    private String phone;

    private LocalDate bornDate;
    
    @Lob
    private byte[] picture;

    private String pictureContentType;

    private Long docTypeId;

    private Long userId;

    private String userLogin;

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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Long getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Long docTypeId) {
        this.docTypeId = docTypeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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
            ", picture='" + getPicture() + "'" +
            ", docType=" + getDocTypeId() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
