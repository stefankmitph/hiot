package stefankmitph.kint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;


import java.lang.reflect.Type;
import java.util.List;

import stefankmitph.model.BookNavigator;
import stefankmitph.model.DatabaseManager;
import stefankmitph.model.Word;

/**
 * Created by KumpitschS on 14.04.2015.
 */
public class VerseFragment extends Fragment {

    private String book;
    private int chapter;
    private int verse;
    private List<Word> words;
    private BookNavigator navigator;
    private Typeface typeface;
    private SQLiteDatabase database;
    private ActivityObjectProvider provider;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private View contentView;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static VerseFragment newInstance(String book, int chapter, int verse) {
        VerseFragment fragment = new VerseFragment();
        fragment.setRetainInstance(true);
        Bundle args = new Bundle();

        args.putString("book", book);
        args.putInt("chapter", chapter);
        args.putInt("verse", verse);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try
        {
            provider = (ActivityObjectProvider) activity;
            typeface = provider.getTypeface();
        } catch(ClassCastException e) {
            throw new RuntimeException("it ain't a Provider");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        book = getArguments().getString("book");
        chapter = getArguments().getInt("chapter");
        verse = getArguments().getInt("verse");

        database = provider.getDatabase();
        navigator = new BookNavigator(database);

        FragmentActivity activity= getActivity();

        progressBar = (ProgressBar)((MainActivity)activity).findViewById(R.id.progressBar1);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser) {
            FragmentActivity activity = getActivity();
            if(activity != null)
            {
                ((MainActivity)activity).setActionBarTitle(String.format("%s %d:%d", book, chapter, verse));
                //progressBar.setVisibility(View.VISIBLE);

            }
        }
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_fragment);
        // dialog.setMessage(Message);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();
        contentView = inflater.inflate(R.layout.activity_fragment, null);

        words = provider.getWords(verse);

        FlowLayout layout = new FlowLayout(context, null);
        layout.setLayoutParams(
                new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.MATCH_PARENT,
                        FlowLayout.LayoutParams.MATCH_PARENT
                ));
        layout.setPadding(10, 10, 10, 10);

        int count = provider.getWords(verse).size();
        for(int i = 0; i < count; i++) {

            LinearLayout linearLayout = getLayout(context, i);


            layout.addView(linearLayout);
        }
        ((ViewGroup)contentView).addView(layout);
        return contentView; // super.onCreateView(inflater, container, savedInstanceState);
    }

    private LinearLayout getLayout(Context context, int index) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(10, 20, 10, 10);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

        final TextView textViewStrongs = new TextView(context);
        textViewStrongs.setTag("textViewStrongs" + index);
        textViewStrongs.setTextAppearance(context, android.R.style.TextAppearance_Small);
        textViewStrongs.setTextColor(Color.rgb(0, 146, 242));
        //final SQLiteDatabase finalDatabase = database;
        textViewStrongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*String text = textViewStrongs.getText().toString();
                    String[] parts = text.split("\\&");
                    String strongs1 = "";
                    if (parts.length > 1) {

                    } else {
                        //Cursor result1 = finalDatabase.rawQuery(String.format("select * from strongs where nr = %s", text), null);
                        //while (result1.moveToNext())
                        //    strongs1 = result1.getString(2);
                    }

                    Toast toast = Toast.makeText(v.getContext(), strongs1, Toast.LENGTH_LONG);
                    toast.show();*/
            }
        });

        TextView textViewWord = new TextView(context);
        textViewWord.setTextAppearance(context, android.R.style.TextAppearance_Large);
        textViewWord.setTypeface(typeface);
        textViewWord.setTextColor(Color.rgb(61, 76, 83));
        textViewWord.setTag("textViewWord" + index);

        TextView textViewConcordance = new TextView(context);
        textViewConcordance.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        textViewConcordance.setTextColor(Color.rgb(230, 74, 69));
        textViewConcordance.setTag("textViewConcordance" + index);

        TextView textViewFunctional = new TextView(context);
        textViewFunctional.setTextAppearance(context, android.R.style.TextAppearance_Small);
        textViewFunctional.setTextColor(Color.rgb(77, 179, 179));
        textViewFunctional.setTag("textViewFunctional" + index);

        textViewStrongs.setText(words.get(index).getStrongs());
        textViewWord.setText(words.get(index).getWord());
        textViewFunctional.setText(words.get(index).getFunctional());
        textViewConcordance.setText(words.get(index).getConcordance());

        linearLayout.addView(textViewStrongs);
        linearLayout.addView(textViewWord);
        linearLayout.addView(textViewConcordance);
        linearLayout.addView(textViewFunctional);

        return linearLayout;
    }
}
