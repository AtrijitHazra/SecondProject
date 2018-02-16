package firebase.demo.firebaseapplicationdemo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PdfDisplay extends AppCompatActivity {

    String TAG = "PdfDisplay";

    private ImageView img;
    private int currentpage = 0;
    private Button next, previous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);

        currentpage++;
        render();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpage++;
                render();
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpage--;
                render();
            }
        });


    }

    private void render() {

        Log.d(TAG, "render: curr page " + currentpage);
        try {

            Log.d(TAG, "render: enter try");
            img = (ImageView) findViewById(R.id.image_pdf);

            int width = img.getWidth();
            int height = img.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

            String FILE = Environment.getExternalStorageDirectory() + File.separator
                    + "HelloWorld.pdf";
            File file = new File(FILE);

            Log.d(TAG, "render: file exist " + file.exists());
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

            Log.d(TAG, "render: renderer.getPageCount() " + renderer.getPageCount());
            if (currentpage < 0) {
                currentpage = 0;

            } else {

                Log.d(TAG, "render: enter else ");

                currentpage = renderer.getPageCount() - 1;

                Matrix matrix = img.getImageMatrix();

                Rect rect = new Rect(0, 0, width, height);

                renderer.openPage(currentpage).render(bitmap, rect, matrix, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                img.setImageMatrix(matrix);
                img.setImageBitmap(bitmap);
                img.invalidate();

            }


        } catch (Exception e) {

            e.printStackTrace();
            Log.d(TAG, "render: error " + e);

        }

    }
}
