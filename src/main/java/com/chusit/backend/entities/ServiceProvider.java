package com.chusit.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "service_providers")
public class ServiceProvider {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String name;
    private String email;
//    private String businessName;
//    private String serviceType;



    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
//    public String getname(){
//        return name;
//    }
//    public void setName(String name){
//        this.name=name;
//    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
//    public String getBusinessName(){
//        return businessName;
//    }
//    public void setBusinessName(String businessName){
//        this.businessName=businessName;
//    }
//    public String getServiceType(){
//        return serviceType;
//    }
//    public void setServiceType(String serviceType){
//        this.serviceType=serviceType;
//    }
}
