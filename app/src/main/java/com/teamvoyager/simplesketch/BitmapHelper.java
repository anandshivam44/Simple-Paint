package com.teamvoyager.simplesketch;

import android.graphics.Bitmap;

public class BitmapHelper {
    private Bitmap bitmap =null;

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    private boolean isSaved=false;
    private static final BitmapHelper instance=new BitmapHelper();


    public BitmapHelper() {

    }

    public static BitmapHelper getInstance() {
        return instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
