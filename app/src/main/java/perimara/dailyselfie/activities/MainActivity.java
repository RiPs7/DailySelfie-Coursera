package perimara.dailyselfie.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import perimara.dailyselfie.R;
import perimara.dailyselfie.alarm.AlarmHandler;
import perimara.dailyselfie.selfies.Selfie;
import perimara.dailyselfie.selfies.SelfieAdapter;

public class MainActivity extends AppCompatActivity {

    final int IMAGE_CAPTURE_REQUEST_CODE = 1;
    final String ALBUM_NAME = "DailySelfiesAlbum";
    final int FIRST = Menu.FIRST;
    final int SECOND = FIRST + 1;
    final String OPEN_SELFIE = "Open";
    final String DELETE_SELFIE = "Delete";

    SelfieAdapter mSelfieAdapter;

    ActionBar mActionBar;
    ListView mSelfieListView;
    View mCameraButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize SelfieAdapter
        mSelfieAdapter = new SelfieAdapter(MainActivity.this);

        //Load previously taken selfies stored in phone
        loadSelfies();

        //Initialize SelfieListView
        mSelfieListView = (ListView)findViewById(R.id.selfieListView);
        mSelfieListView.setAdapter(mSelfieAdapter);

        //Attach Context Menu to ListView
        registerForContextMenu(mSelfieListView);

        //Setup Action Bar (to include app name and camera icon)
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getResources().getString(R.string.app_name));
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        mCameraButtonView = getLayoutInflater().from(this).inflate(R.layout.custom_action_bar, null);
        mActionBar.setCustomView(mCameraButtonView, lp);
        mActionBar.setDisplayShowCustomEnabled(true);

        //Attach onClickListener to the camera icon view
        mCameraButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        //Handle Alarm
        AlarmHandler alarmHandler = new AlarmHandler(MainActivity.this);
        int TWO_MINUTES_IN_SECONDS = 2 * 60;
        alarmHandler.SetAlarmEveryXSeconds(TWO_MINUTES_IN_SECONDS);
    }

    /**
     * This method handles the intent used for opening the native camera app and starts an
     * activity waiting for a result. If any error occurs while launching Camera app, then
     * a relevant toast message is displayed
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File selfieFile = createPhotoFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(selfieFile));
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
            } catch (IOException ioe){
                Toast.makeText(MainActivity.this, "An error occurred while launching Camera app", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Used to create a temporary file for the native camera app to save the captured image into.
     * The new file is created with a collision-avoiding name using the date and time the file is
     * created.
     * @return A File instance
     * @throws IOException
     */
    private File createPhotoFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String selfieFileName = "JPEG_" + timestamp;
        File album = getAlbumDir();
        File selfieFile = File.createTempFile(selfieFileName, ".jpg", album);
        return selfieFile;
    }

    /**
     * Used to delete a file that is stored in the public application picture directory in the
     * external storage.
     * @param fileName The file name in the album directory
     * @return True if deletion is successfull or false otherwise
     */
    private boolean deletePhotoFile(String fileName){
        File album = getAlbumDir();
        if (album == null){
            return false;
        }
        File selfieFile = new File(album.getAbsolutePath() + "/" + fileName);
        return selfieFile.delete();
    }

    /**
     * Used to get a File object with a reference to the public application picture directory
     * in the external storage. If external storage is not mounted a relevant toast message
     * is displayed.
     * @return A File instance
     */
    private File getAlbumDir(){
        File albumDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            albumDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ALBUM_NAME);
            if (albumDir != null){
                if (!albumDir.mkdir()){
                    if (!albumDir.exists()){
                        return null;
                    }
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "External storage is not mounted", Toast.LENGTH_SHORT).show();
        }
        return albumDir;
    }

    /**
     * Used to load all the selfies stored in the public application picture directory and adds
     * them in the list view adapter to be displayed later.
     */
    private void loadSelfies(){
        mSelfieAdapter.clear();
        File album = getAlbumDir();
        String[] fileNames = album.list();
        for (String fn : fileNames){
            String name = fn;
            String path = album.getAbsolutePath() + "/" + name;
            try {
                Bitmap thumbnail = BitmapFactory.decodeFile(path);
                mSelfieAdapter.add(new Selfie(name, thumbnail, path));
            } catch (NullPointerException npe){
                // In case a null pointer exception is raised, this is probably because of zero-sized
                // file. In such case, the corresponding file is deleted
                new File(path).delete();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE){
            loadSelfies();
        }
    }

    @Override
    protected void onResume() {
        loadSelfies();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.selfieListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(((Selfie)mSelfieAdapter.getItem(info.position)).getName());
            menu.add(Menu.NONE, FIRST, Menu.NONE, OPEN_SELFIE);
            menu.add(Menu.NONE, SECOND, Menu.NONE, DELETE_SELFIE);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case FIRST:
                info.targetView.performClick();
                break;
            case SECOND:
                try {
                    boolean success = deletePhotoFile(((Selfie)mSelfieAdapter.getItem(info.position)).getName());
                    if (!success) {
                        Toast.makeText(MainActivity.this, "Selfie deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Could not delete selfie", Toast.LENGTH_SHORT).show();
                } finally {
                    loadSelfies();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}
