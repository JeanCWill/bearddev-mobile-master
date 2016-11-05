package com.beardev.findrestaurant;

import android.graphics.Bitmap;

/**
 * Created by Jean on 03/11/2016.
 */

public class PhotoItem {
    private Bitmap photo;

    public PhotoItem(Bitmap photo, String s) {
        super();
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
