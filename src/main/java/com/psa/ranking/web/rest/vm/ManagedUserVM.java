package com.psa.ranking.web.rest.vm;

import com.psa.ranking.service.dto.UserDTO;

import java.time.LocalDate;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    //Agregado Edu 20191023 UserExtra
    private String phone;
    
    private String numDoc;
    
    private LocalDate bornDate;
    
    private String password;
    

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public LocalDate getBornDate() {
		return bornDate;
	}

	public void setBornDate(LocalDate bornDate) {
		this.bornDate = bornDate;
	}
    
    /*	@Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }*/
    
}
