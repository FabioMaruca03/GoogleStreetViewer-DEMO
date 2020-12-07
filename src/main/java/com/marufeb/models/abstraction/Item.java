package com.marufeb.models.abstraction;

import java.io.IOException;
import java.net.URI;

public class Item extends AbstractItem {

    private String image = null;

    public Item(URI location) throws IOException {
        super(location);
    }

    public Item(URI location, String image) throws IOException {
        super(location);
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean collect(Backpack backpack) {
        if (this.backpack == null) {
            this.backpack = backpack;
            return true;
        } else return false;
    }

    @Override
    public boolean drop() {
        if (backpack != null) {
            backpack = null;
            return true;
        } else return false;
    }
}
