package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psa.ranking.domain.Location} entity.
 */
@ApiModel(description = "Location entity.\n@author Marcelo Miño\nDatos traidos desde https:")
public class LocationDTO implements Serializable {

    private Long id;

    private String name;


    private Long provinceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (locationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", province=" + getProvinceId() +
            "}";
    }
}
