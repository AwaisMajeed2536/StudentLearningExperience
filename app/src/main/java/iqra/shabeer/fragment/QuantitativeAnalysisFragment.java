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
import android.view.LayoutInflater;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
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

    private LinearLayout rootView;
    private ListView analysisTableListview;
    private QuantitativeAnalysisAdapter adapter;
    private List<QuantitativeAnalysisModel> dataList = new ArrayList<>();
    private ArrayList<ArrayList<Long>> scoreDataList;
    private ArrayList<String> questionsList = new ArrayList<>();

    private DatabaseReference scoreRef;
    private DatabaseReference questionRef;

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

    private List<QuantitativeAnalysisModel> convertList() {
        List<QuantitativeAnalysisModel> returnList = new ArrayList<>();
        QuantitativeAnalysisModel model;
        for (int i = 0; i < scoreDataList.size(); i++) {
            ArrayList<Long> scores = scoreDataList.get(i);
            model = new QuantitativeAnalysisModel();
            model.setQuestionNumber((i + 1) + "");
            model.setQuestion(questionsList.get(i));
            model.setScoreCount(new int[]{scores.get(0).intValue(), scores.get(1).intValue(), scores.get(2).intValue(),
                    scores.get(3).intValue(), scores.get(4).intValue()});
            double mean = UtilHelper.findMean(scores);
            model.setMean(mean);
            model.setMedian(5.3);
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


    int allitemsheight = 0;
    Bitmap bigbitmap;
    Canvas bigcanvas;
    List<Bitmap> bmps = new ArrayList<Bitmap>();
    Bitmap screen = null;
    File pdfDir;
    boolean fileCreated = false;

    private void createPDF() {
        //First Check if the external storage is writable
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(getActivity(), "External storage is not writeAble!", Toast.LENGTH_LONG).show();
        }

        //Create a directory for your PDF
        pdfDir = new File(Environment.getExternalStorageDirectory().toURI() + "SLE");
        if (!pdfDir.exists()) {
            fileCreated = pdfDir.mkdir();
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        //root = (LinearLayout) inflater.inflate(R.layout.activity_view_analysis, null);
        //root.setDrawingCacheEnabled(true);
        View v = getActivity().getWindow().findViewById(R.id.quantitative_analysis_table_listview).getRootView();
        //v.setDrawingCacheEnabled(true);
        //v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        //View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        //v.buildDrawingCache(true);


        final ListView listview = (ListView) getActivity().getWindow().findViewById(R.id.quantitative_analysis_table_listview);
        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();

        for (int i = 0; i < itemscount; i++) {

            View childView = adapter.getView(i, null, listview);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache(true);
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }


        listview.post(new Runnable() {
            @Override
            public void run() {
                bigbitmap = Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
                bigcanvas = new Canvas(bigbitmap);
                Paint paint = new Paint();
                int iHeight = 0;

                for (int i = 0; i < bmps.size(); i++) {
                    Bitmap bmp = bmps.get(i);
                    bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
                    iHeight += bmp.getHeight();

                    bmp.recycle();
                    bmp = null;
                }
                screen = bigbitmap;
                File pdfFile = new File(Environment.getExternalStorageDirectory().toString() + "/myPDFnew.pdf");
                if (fileCreated)
                    pdfFile = new File(pdfDir, "thePdfFile.pdf");
                else
                    Toast.makeText(getActivity(), "Directory wasn't created!", Toast.LENGTH_LONG).show();

                try {
                    Document document = new Document();

                    PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                    document.open();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    addImage(document, byteArray);
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //Bitmap screen;
//        View v1 = rootView.getRootView();
//        v1.setDrawingCacheEnabled(true);
//        v1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());
//
//        v1.buildDrawingCache(true);
//        //screen = Bitmap.createBitmap(v1.getDrawingCache());
//        v1.setDrawingCacheEnabled(false);

    }

    private static void addImage(Document document, byte[] byteArray) {
        Image image = null;
        try {
            image = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try {
            document.add(image);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public Bitmap getBitmapFromViewNew(View pView) {

        pView.setDrawingCacheEnabled(true);
        pView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        pView.layout(0, 0, pView.getWidth(), pView.getHeight());
        pView.buildDrawingCache(true);

        Bitmap bitmap = Bitmap.createScaledBitmap(pView.getDrawingCache(), pView.getWidth(), pView.getHeight(), false);
        pView.setDrawingCacheEnabled(false);

        return bitmap;

    }
}
