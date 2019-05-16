package com.gmail.sirojudinag.belajarkamera;

//TODO 1 : package yang perlu diimport untuk menjalankan program
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //TODO 2 : pembuatan Parameter dan variabel yang dibutuhkan
    Button btnCamera;
    //TODO 2.1 : variabel imageview
    ImageView imageView;
    //TODO 2.2 : variabel URI
    Uri file;

    //TODO 3 : override pembuatan bundle untuk ditampilkan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 4 : pemberian nilai pada variabel dengan id yang telah ditentukan
        btnCamera = (Button) findViewById(R.id.buttonCamera);
        imageView = (ImageView) findViewById(R.id.imageView);


        //TODO 5 : proses pengecekan permission untuk mengakses kamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //TODO 5.1 : ketika hak akses belum didapat button camera didisable
            btnCamera.setEnabled(false);
            //TODO 5.2 : merequest permission untuk mengakses kamera  kepada user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    //TODO 6 : method untuk melakukan request permission kamera
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //TODO 6.1 : proses pengecekan menggunakan reques code jika sudah memiliki permission maka button camera akan dienable
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnCamera.setEnabled(true);
            }
        }
    }

    //TODO 7 : method untuk mengambil gambar
    public void takePicture(View view) {
        //TODO 7.1 : proses pemanggilan intent untuk membuka kamera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //TODO 7.2 : membuat file untuk menyimpan data dari file gambar dari method getOutputMediaFile
        file = Uri.fromFile(getOutputMediaFile());
        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        //TODO 7.3 : set the image file name, biasa menyebabkan force close dibeberapa android
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        //TODO 7.4 : menjalankan intent dengan nilai request code 0
        startActivityForResult(intent,0);
    }

    /*
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
*/
    //TODO 8 : method activity hasil pengambilan gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO 8.1 : mengecek request code dan result untuk menampilkan gambar tersebut pada image view
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                //imageView.setImageURI(file);
                //TODO 8.2 : menampilkan pada imageView menggunakan bitmap
                imageView.setImageBitmap(imageBitmap);
                //TODO 8.3 : memberikan toast sebagai notifikasi kepengguna
                Toast.makeText(getApplicationContext(), "Tersimpan", Toast.LENGTH_SHORT).show();
            }
            //TODO 8.4 : jika user membatalkan take picture maka akan memunculkan toast cancel
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO 9 : method untuk menyimpan hasil foto yang diambil
    private static File getOutputMediaFile() {
        //TODO 9.1 : mengambil direktori pictures dari penyimpanan perangkat dengan nama directory Camera
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera");

        //TODO 9.2 : mengecek direktori pictures apakah ada atau tidak
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //TODO 9.3 : mwemberikan log / catatan
                Log.d("Sirojudin", "failed to create directory");
                return null;
            }
        }

        //TODO 9.4 : membuat time stamp pada gambar hasil foto
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //TODO 9.5 : pembuatan file baru dan  menyimpan hasil foto pada direktori dengan nama dan format yang telah ditentukan
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
}

    // batas komentar ----------------------------------------------------------------------------------------------------------------------------------------------
    /*
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    File mediaFile;
    if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
            } else {
            return null;
            }
            return mediaFile;
            }
      */
    //source
    //https://androidkennel.org/android-camera-access-tutorial
    //https://stackoverflow.com/questions/16348757/mediastore-extra-output-renders-data-null-other-way-to-save-photos
    //tidak semua smartphone mendukung, masih terdapat beberapa bug, sebagai contoh dihp sony xperia z4 saya foto tersimpan, namun pada xiaomi redmi 4x saya tidak