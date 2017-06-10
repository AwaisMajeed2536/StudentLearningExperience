package iqra.shabeer.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.adapter.QuantitativeAnalysisAdapter;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.QuantitativeAnalysisModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Devprovider on 20/04/2017.
 */

public class QuantitativeAnalysisFragment extends Fragment {
    private static final String TAG = "qaf";
    private LinearLayout rootView;
    private ListView analysisTableListview;
    private QuantitativeAnalysisAdapter adapter;
    private List<QuantitativeAnalysisModel> dataList = new ArrayList<>();
    private ArrayList<ArrayList<Long>> scoreDataList;
    private ArrayList<String> questionsList = new ArrayList<>();

    private DatabaseReference scoreRef;
    private DatabaseReference questionRef;

    private Bitmap pdfImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilHelper.showWaitDialog(getActivity(), "Creating Report", "please wait...");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quantitative_analysis, container, false);
        storeTempData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scoreRef = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://student-evaluation-system.firebaseio.com/root/analysisData/quantitative/" + "dbd");
        questionRef = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://student-evaluation-system.firebaseio.com/root/surveyQuestions/quantitative");
        scoreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreDataList = (ArrayList<ArrayList<Long>>) dataSnapshot.getValue();
                questionRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        questionsList = (ArrayList<String>) dataSnapshot.getValue();
                        dataList = convertList();
                        adapter = new QuantitativeAnalysisAdapter(getActivity(), dataList);
                        analysisTableListview.setAdapter(adapter);
                        UtilHelper.dismissWaitDialog();
                        pdfImage = getWholeListViewItemsToBitmap();
                        storeImage(pdfImage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createPDF() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(getActivity(), "Permission not Granted", Toast.LENGTH_SHORT).show();
        }

//Create a directory for your PDF
        File pdfDir = new File(Environment.getExternalStorageDirectory(), "MyApp");
        if (!pdfDir.exists()){
            pdfDir.mkdir();
        }

        File pdfFile = new File(pdfDir, "myPdfFile.pdf");

        try {
            Document  document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pdfImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addImage(document,byteArray);
            document.close();
            Toast.makeText(getActivity(), "PDF Created!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private List<QuantitativeAnalysisModel> convertList() {
        List<QuantitativeAnalysisModel> returnList = new ArrayList<>();
        QuantitativeAnalysisModel model;
        for (int i = 0; i < scoreDataList.size(); i++) {
            ArrayList<Long> scores = scoreDataList.get(i);
            Long[] convertArray = new Long[scores.size()];
            model = new QuantitativeAnalysisModel();
            model.setQuestionNumber((i + 1) + "");
            model.setQuestion(questionsList.get(i));
            model.setScoreCount(new int[]{scores.get(0).intValue(), scores.get(1).intValue(), scores.get(2).intValue(),
                    scores.get(3).intValue(), scores.get(4).intValue()});
            double mean = UtilHelper.findMean(scores);
            model.setMean(mean);
            model.setMedian(UtilHelper.findMedian(scores.toArray(convertArray)));
            model.setStdDev(UtilHelper.findStdDev(mean, scores));
            returnList.add(model);

        }
        return returnList;
    }

    private void initView(View view) {
        rootView = (LinearLayout) view.findViewById(R.id.root_view);
        analysisTableListview = (ListView) view.findViewById(R.id.quantitative_analysis_table_listview);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.analysis_list_header, null);
        analysisTableListview.addHeaderView(listHeader);
        adapter = new QuantitativeAnalysisAdapter(getActivity(), dataList);
        analysisTableListview.setAdapter(adapter);
    }


    private void storeTempData() {
        QuantitativeAnalysisModel model;
        for (int i = 0; i < 5; i++) {
            model = new QuantitativeAnalysisModel();
            model.setQuestionNumber((i + 1) + "");
            model.setQuestion("This is a simple question, generated for test purpose?");
            model.setScoreCount(new int[]{16, 12, 8, 7, 15});
            model.setMean(4.5);
            model.setMedian(5.3);
            model.setStdDev(0.6);
            dataList.add(model);
        }
    }

//    private void createPDF(Bitmap bmp) {
//        //First Check if the external storage is writable
//        String state = Environment.getExternalStorageState();
//        if (!Environment.MEDIA_MOUNTED.equals(state)) {
//            Toast.makeText(getActivity(), "External storage is not writeAble!", Toast.LENGTH_LONG).show();
//        }
//
//        //Create a directory for your PDF
//        pdfDir = new File(Environment.getExternalStorageDirectory().toURI() + "SLE");
//        if (!pdfDir.exists()) {
//            fileCreated = pdfDir.mkdir();
//        }
//
//        File pdfFile = new File(Environment.getExternalStorageDirectory().toString() + "/myPDFnew.pdf");
//        if (fileCreated)
//            pdfFile = new File(pdfDir, "thePdfFile.pdf");
//        else
//            Toast.makeText(getActivity(), "Directory wasn't created!", Toast.LENGTH_LONG).show();
//
//        try {
//            Document document = new Document();
//
//            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
//            document.open();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//            addImage(document, byteArray);
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void addImage(Document document, byte[] byteArray) {
        Image image = null;
        try {
            image = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try {
            document.add(image);
            Toast.makeText(getActivity(), "Image added to document", Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public Bitmap getWholeListViewItemsToBitmap() {

        ListView listview = analysisTableListview;
        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView = adapter.getView(i, null, listview);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }

        Bitmap bigbitmap = Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();

            bmp.recycle();
            bmp = null;
        }


        return bigbitmap;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(getActivity(), "Image saved...", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getActivity().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.create_analysis_pdf_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        createPDF();
        return super.onOptionsItemSelected(item);
    }
}
