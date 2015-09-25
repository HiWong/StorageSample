package imallen.wang.filesample;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG=MainActivity.class.getSimpleName();

    private Button getFilesDirBtn,getCacheDirBtn,externalPublicBtn,getExternalFilesDirBtn;


    private void initView()
    {
        getFilesDirBtn=(Button)findViewById(R.id.getFilesDirBtn);
        getCacheDirBtn=(Button)findViewById(R.id.getCacheDirBtn);
        externalPublicBtn=(Button)findViewById(R.id.externalPublicBtn);
        getExternalFilesDirBtn=(Button)findViewById(R.id.getExternalFilesDirBtn);

        getFilesDirBtn.setOnClickListener(this);
        getCacheDirBtn.setOnClickListener(this);
        externalPublicBtn.setOnClickListener(this);
        getExternalFilesDirBtn.setOnClickListener(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.getFilesDirBtn:
                getFilesDirSample(MainActivity.this);
                break;
            case R.id.getCacheDirBtn:
                getCacheDirSample(MainActivity.this);
                break;
            case R.id.externalPublicBtn:
                externalPublicSample();
                break;
            case R.id.getExternalFilesDirBtn:
                externalPrivateSample(MainActivity.this);
                break;
        }
    }

    private void getFilesDirSample(Context context)
    {
        String fileName="myfile.txt";
        String content="Hello world";

        Log.d(TAG,"Environment.getDataDirectory():"+Environment.getDataDirectory().getAbsolutePath());
        File file=new File(context.getFilesDir(),fileName);
        Log.d(TAG,"getFilesDirSample,file absolutePath:"+file.getAbsolutePath()+",file path:"+file.getPath());
        FileOutputStream outputStream;
        try
        {
            outputStream=openFileOutput(fileName,Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //then we try to mkdirs under /data/data/imallen.wang.filesample/
        File file2=new File("/data/data/imallen.wang.filesample","bannerimages");
        if(file2.mkdirs())
        {
            Log.d(TAG,"dir bannerimages created successfully");
        }

    }

    /**
     * usually we only put temp file in cacheDir
     * @param context
     */
    private File getCacheDirSample(Context context)
    {
        File file=null;
        try
        {
            file=File.createTempFile("miss.txt",null,context.getCacheDir());
            Log.d(TAG,"getCacheDirSample,file absolutePath:"+file.getAbsolutePath()+",file path:"+file.getPath());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return file;
    }

    public void externalPublicSample()
    {
        boolean isExternWritable=isExternalStorageWritable();
        boolean isExternReadable=isExternalStorageReadable();
        Log.d(TAG, "isExternalStorageWritable:"+isExternWritable);
        Log.d(TAG,"isExternReadable:"+isExternReadable);

        File externalStorageDir=Environment.getExternalStorageDirectory();
        Log.d(TAG,"externalStorageDirectory-->absolutePath:"+externalStorageDir.getAbsolutePath()+",path:"+externalStorageDir.getPath());
        File externPublicPicDir=Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        Log.d(TAG,"externalPublicPicDir-->absolutePath:"+externPublicPicDir.getAbsolutePath()+"," +
                "path:"+externPublicPicDir.getPath());

        String albumName="AllenPic";
        if(isExternWritable)
        {
            File file=new File(externPublicPicDir,albumName);
            if(!file.mkdirs())
            {
                Log.e(TAG,"directory not created");
            }
        }

        ///////////////////then we will create a file under externalStorageDir
        String fileName="allenTest.txt";
        File allenTestFile=new File(externalStorageDir,fileName);
        try
        {
            allenTestFile.createNewFile();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        if(allenTestFile.exists())
        {
            Log.d(TAG,"created succceed");
        }

    }

    public void externalPrivateSample(Context context)
    {
        Log.d(TAG,"getExternalCacheDir:"+context.getExternalCacheDir());
        String albumName="AllenPrivateAlbum";
        File externalPrivatePicDir=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(null!=externalPrivatePicDir)
        {
            Log.d(TAG,"externalPrivatePicDir-->absolutePath:"+externalPrivatePicDir.getAbsolutePath()
                    +",path:"+externalPrivatePicDir.getPath());
        }

        File file=new File(externalPrivatePicDir,albumName);
        if(!file.mkdirs())
        {
            Log.e(TAG,"Directory not created");
        }
        else
        {
            Log.d(TAG,"Directory created succeed");
        }

    }



    public boolean isExternalStorageWritable()
    {
        String state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable()
    {
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)
                ||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return true;
        }
        return false;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
