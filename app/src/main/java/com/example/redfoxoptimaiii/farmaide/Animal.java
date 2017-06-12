package com.example.redfoxoptimaiii.farmaide;

/**
 * Created by REDFOX™ OptimaIII on 3/25/2017.
 */

public class Animal {
    public String animal;
    public String name;
    public String type;
    public float dm_req;
    public float cp_req;
    public float me_req;
    public float ca_req;
    public float p_req;
    public float animal_weight;

    public Animal(String name, String animal, String type, float dm_req, float cp_req, float me_req, float ca_req, float p_req, float animal_weight) {
        this.animal = animal;
        this.name = name;
        this.type = type;
        this.dm_req = dm_req;
        this.cp_req = cp_req;
        this.me_req = me_req;
        this.ca_req = ca_req;
        this.p_req = p_req;
        this.animal_weight = animal_weight;
    }

    public String getAnimal() {
        return animal;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getDm_req() {
        return dm_req;
    }

    public float getCp_req() {
        return cp_req;
    }

    public float getMe_req() {
        return me_req;
    }

    public float getCa_req() {
        return ca_req;
    }

    public float getP_req() {
        return p_req;
    }

    public float getAnimal_weight() {
        return animal_weight;
    }
}
