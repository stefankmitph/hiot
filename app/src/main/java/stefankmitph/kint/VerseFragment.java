package stefankmitph.kint;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;


import stefankmitph.model.BookNavigator;
import stefankmitph.model.Word;

/**
 * Created by KumpitschS on 14.04.2015.
 */
public class VerseFragment extends ProgressFragment {

    private String book;
    private int chapter;
    private int verse;
    private Word[] words;
    private BookNavigator navigator;
    private Typeface typeface;
    private SQLiteDatabase database;
    private ActivityObjectProvider provider;
    //private ProgressBar bar;

    private View contentView;

    private Handler mHandler;
    private Runnable mShowContentRunnable = new Runnable() {

        @Override
        public void run() {
            setContentShown(true);
        }

    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup content view
        setContentView(contentView);
        // Setup text for empty content
        setEmptyText(R.string.empty);
        obtainData();
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

        //bar = (ProgressBar) activity.findViewById(R.id.progressBar);

        this.typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/palab.ttf");

        try
        {
            provider = (ActivityObjectProvider) activity;
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }

    private void obtainData() {
        // Show indeterminate progress
        setContentShown(false);

        mHandler = new Handler();
        mHandler.postDelayed(mShowContentRunnable, 3000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();

        contentView = inflater.inflate(R.layout.activity_fragment, null);


        final ViewGroup c = container;

        AsyncTask<Object, Void, Word[]> loadTask = new AsyncTask<Object, Void, Word[]>() {

            @Override
            protected void onPreExecute(){
            //    bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Word[] doInBackground(Object... params) {
                return navigator.getVerse(book, chapter, verse);
            }

            @Override
            protected void onPostExecute(Word[] result) {
                words = result;
                for(Word word : words) {
                    TextView textViewStrongs = (TextView) c.findViewWithTag("textViewStrongs" + (word.getWord_nr() - 1));
                    textViewStrongs.setText(word.getStrongs());

                    TextView textViewWord = (TextView) c.findViewWithTag("textViewWord" + (word.getWord_nr() - 1));
                    textViewWord.setText(word.getWord());
                }
                //bar.setVisibility(View.GONE);
                setContentShown(true);
            }
        };

        loadTask.execute(book, chapter, verse);

        FlowLayout layout = new FlowLayout(context, null);
        layout.setLayoutParams(
                new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.MATCH_PARENT,
                        FlowLayout.LayoutParams.MATCH_PARENT
                ));
        layout.setPadding(10, 10, 10, 10);

        int count =  navigator.getVerseCount(book, chapter);
        for(int i = 0; i < count; i++) {
            //Word word = words[i];

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setPadding(10, 20, 10, 10);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

            final TextView textViewStrongs = new TextView(context);
            textViewStrongs.setTag("textViewStrongs" + i);
            //textViewStrongs.setText(word.getStrongs());
            textViewStrongs.setTextSize(10.0f);
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
            //textViewWord.setText(word.getWord());
            textViewWord.setTypeface(typeface);
            textViewWord.setTextSize(16.0f);
            textViewWord.setTag("textViewWord" + i);

            TextView textViewConcordance = new TextView(context);
            //textViewConcordance.setText(word.getConcordance());
            textViewConcordance.setTextColor(Color.rgb(213, 85, 0));
            textViewConcordance.setTextSize(14.0f);

            TextView textViewFunctional = new TextView(context);
            //textViewFunctional.setText(word.getFunctional());
            textViewFunctional.setTextSize(10.0f);
            textViewFunctional.setTextColor(Color.rgb(0, 146, 242));

            linearLayout.addView(textViewStrongs);
            linearLayout.addView(textViewWord);
            linearLayout.addView(textViewConcordance);
            linearLayout.addView(textViewFunctional);

            layout.addView(linearLayout);
        }
        ViewGroup viewGroup = (ViewGroup)contentView.findViewById(R.id.content_container);
        viewGroup.addView(layout);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mShowContentRunnable);
        if (contentView != null) {
            ViewGroup parentViewGroup = (ViewGroup) contentView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }
}
