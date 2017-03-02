package perimara.dailyselfie.selfies;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by periklismaravelias on 26/02/17.
 */
public class Selfie {

    String mName, mPath;
    Bitmap mThumbnail;

    public Selfie(String name, Bitmap thumbnail, String path){
        mName = name;

        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);

        mThumbnail = thumbnail;

        mPath = path;
    }

    public Bitmap getThumbnail(){
        return mThumbnail;
    }

    public String getName(){
        return mName;
    }

    public String getPath(){
        return mPath;
    }

}