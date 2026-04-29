package com.sim.management;

import jakarta.persistence.*;

@Entity
@Table(name = "sims")
public class Sim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "org_name")
    private String orgName;

    // ✅ Default constructor (IMPORTANT)
    public Sim() {
    }

    // ✅ Getters
    public Long getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getOrgName() {
        return orgName;
    }

    // ✅ Setters (VERY IMPORTANT)
    public void setId(Long id) {
        this.id = id;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}