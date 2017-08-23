package com.pqt.core.entities.product;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Category implements Serializable{
    private int id;
    private String name;

    public Category() {
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!this.getClass().isInstance(obj))
            return false;

        Category other = Category.class.cast(obj);
        return this.id == other.id
                && Objects.equals(this.name, other.name);
    }
}
