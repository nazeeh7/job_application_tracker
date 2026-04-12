package com.nazeeh.jobapplicationtracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private CompanyAddress address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public CompanyAddress getAddress() {
		return address;
	}

	public void setAddress(CompanyAddress address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
    
    
}
