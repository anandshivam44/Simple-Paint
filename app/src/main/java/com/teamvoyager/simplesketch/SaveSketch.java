package com.teamvoyager.simplesketch;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveSketch extends AppCompatActivity {

    private static final String TAG = "MyTag";
    @BindView(R.id.button_1)
    Button button;
    @BindView(R.id.button_2)
    Button button2;
    @BindView(R.id.button_3)
    Button button3;
    @BindView(R.id.saveImage)
    ImageView imageView;
    @BindView(R.id.button_4)
    Button button4;
    @BindView(R.id.button_5)
    Button button5;

    private AdView add;
    File fname;
    String path;
    CheckBox cbPng;
    CheckBox cbjpeg;
    String extension = ".jpeg";

    final int REQUEST_BUTTON1 = 10;
    final int REQUEST_BUTTON2 = 11;
    final int REQUEST_BUTTON3 = 12;
    final int REQUEST_BUTTON4 = 13;
    final int REQUEST_BUTTON5 = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);

        cbPng = findViewById(R.id.checkBox2);
        cbjpeg = findViewById(R.id.checkBox1);

        cbjpeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    extension = ".jpeg";
                    cbPng.setChecked(false);
                }

            }
        });
        cbPng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    extension = ".png";
                    cbjpeg.setChecked(false);
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView.setImageBitmap(BitmapHelper.getInstance().getBitmap());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        add = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        add.loadAd(adRequest);

    }

    @OnClick(R.id.button_1)
    public void onViewClicked() {
        handlePermission(REQUEST_BUTTON1);
        if (BitmapHelper.getInstance().isSaved() == false) {
            saveScreenshot();
        }
        else {
            Toast.makeText(this, "Already Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.button_2)
    public void onButton2Clicked() {
        handlePermission(REQUEST_BUTTON2);
        if (BitmapHelper.getInstance().isSaved() == false) {
            saveScreenshot();
        }
        if (hasPermission(55)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(path); // a directory
            intent.setDataAndType(uri, "*/*");
            startActivity(Intent.createChooser(intent, "Open folder"));
        }

    }

    @OnClick(R.id.button_3)
    public void onButton3Clicked() {
        handlePermission(REQUEST_BUTTON3);
        if (BitmapHelper.getInstance().isSaved() == false) {
            saveScreenshot();
        }
        if (hasPermission(55)) {
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", fname);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.button_4)
    public void onButton4Clicked() {
        handlePermission(REQUEST_BUTTON4);
        if (BitmapHelper.getInstance().isSaved() == false) {
            saveScreenshot();
        }
        if (hasPermission(55)) {
            Uri imgUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", fname);
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            whatsappIntent.setType("image/jpeg");
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @OnClick(R.id.button_5)
    public void onButton5Clicked() {

        handlePermission(REQUEST_BUTTON5);
        if (BitmapHelper.getInstance().isSaved() == false) {
            saveScreenshot();
        }
        if (hasPermission(55)) {

            Uri imgUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", fname);
            Intent Intent = new Intent(android.content.Intent.ACTION_SEND);
            Intent.putExtra(Intent.EXTRA_STREAM, imgUri);
            Intent.setType("image/jpeg");
            Intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(Intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void saveScreenshot() {


        if (ActivityCompat.checkSelfPermission(SaveSketch.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences sharedPreferences = getSharedPreferences("fileName", MODE_PRIVATE);
            int index = sharedPreferences.getInt("index", 0);
            Bitmap bb;
            String filename = Environment.getExternalStorageDirectory() + "/Pictures/Sketch/Sketch_" + String.valueOf(index) + extension;
            path = Environment.getExternalStorageDirectory() + "/Pictures/Sketch/";
            Toast.makeText(this, "Saved as " + filename, Toast.LENGTH_SHORT).show();
            bb = BitmapHelper.getInstance().getBitmap();
            File file = new File(filename);
            file.getParentFile().mkdirs();

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bb.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                BitmapHelper.getInstance().setSaved(true);
                fname = file;
                index++;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("index", index);
                editor.apply();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        BitmapHelper.getInstance().setSaved(false);
        finish();
        return true;
    }

    private void handlePermission(int REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SaveSketch.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }
        }
    }

    private boolean hasPermission(int REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SaveSketch.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == REQUEST_BUTTON1 || requestCode == REQUEST_BUTTON2 || requestCode == REQUEST_BUTTON3 || requestCode == REQUEST_BUTTON4 || requestCode == REQUEST_BUTTON5) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Read Storage DECLINED");
//
//            }
//
//        }
        switch (requestCode) {

            case REQUEST_BUTTON1:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (BitmapHelper.getInstance().isSaved() == false) {
                        saveScreenshot();
                    }

                } else {
                    Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Read Storage DECLINED");
                }

                break;
            case REQUEST_BUTTON2:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (BitmapHelper.getInstance().isSaved() == false) {
                        saveScreenshot();
                    }
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(path); // a directory
                    intent.setDataAndType(uri, "*/*");
                    startActivity(Intent.createChooser(intent, "Open folder"));


                } else {
                    Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Read Storage DECLINED");
                }

                break;
            case REQUEST_BUTTON3:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (BitmapHelper.getInstance().isSaved() == false) {
                        saveScreenshot();
                    }
                    Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", fname);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(uri, "image/*");
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Read Storage DECLINED");
                }

                break;
            case REQUEST_BUTTON4:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (BitmapHelper.getInstance().isSaved() == false) {
                        saveScreenshot();
                    }
                    Uri imgUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", fname);
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                    whatsappIntent.setType("image/jpeg");
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Read Storage DECLINED");
                }

                break;
            case REQUEST_BUTTON5:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (BitmapHelper.getInstance().isSaved() == false) {
                        saveScreenshot();
                    }

                    Uri imgUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", fname);
                    Intent Intent = new Intent(android.content.Intent.ACTION_SEND);
                    Intent.putExtra(Intent.EXTRA_STREAM, imgUri);
                    Intent.setType("image/jpeg");
                    Intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(Intent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Read Storage DECLINED");
                }

                break;
            default:
//                if (requestCode == REQUEST_BUTTON1 || requestCode == REQUEST_BUTTON2 || requestCode == REQUEST_BUTTON3 || requestCode == REQUEST_BUTTON4 || requestCode == REQUEST_BUTTON5) {
//                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "Can't save without Permission Granted", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "Read Storage DECLINED");
//
//                    }
//
//                }
                break;

        }
    }

}