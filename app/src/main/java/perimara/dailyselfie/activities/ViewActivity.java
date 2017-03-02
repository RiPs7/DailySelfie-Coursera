package perimara.dailyselfie.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import perimara.dailyselfie.R;

public class ViewActivity extends AppCompatActivity {

    ImageView fullSelfieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //Initialize fullSelfieView
        fullSelfieView = (ImageView)findViewById(R.id.fullSelfieView);

        //Gather Intent data
        Uri data = getIntent().getData();
        try {
            //Load the image
            Bitmap bitmap = BitmapFactory.decodeFile(data.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //Set the fullSelfieView source to the loaded image
            fullSelfieView.setImageBitmap(bitmap);
        } catch (Exception e){
            //If an error occurs while loading the image, then a relevant toast message is displayed
            //and the Activity finishes
            Toast.makeText(ViewActivity.this, "Could not load image!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
