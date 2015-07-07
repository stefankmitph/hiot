package stefankmitph.hiot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import stefankmitph.model.DatabaseManager;
import stefankmitph.model.PosParser;
import stefankmitph.model.Word;

public class VerseFragment extends Fragment {

    private String book;
    private int chapter;
    private int verse;
    private List<Word> words;
    private Typeface typeface;
    private ActivityObjectProvider provider;
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser) {
            FragmentActivity activity = getActivity();
            if(activity != null)
            {
                ((MainActivity)activity).setActionBarTitle(String.format("%s %d:%d", book, chapter, verse));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();
        contentView = inflater.inflate(R.layout.activity_fragment, null);

        typeface = provider.getTypeface();
        words = provider.getWords(verse);

        FlowLayout layout = new FlowLayout(context, null);
        layout.setLayoutParams(
                new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.MATCH_PARENT,
                        FlowLayout.LayoutParams.WRAP_CONTENT
                ));
        layout.setPadding(10, 10, 10, 10);

        List<Word> words = provider.getWords(verse);
        if(words == null) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(32, 32, 32, 32);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            TextView textViewErrorText = new TextView(context);
            textViewErrorText.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            textViewErrorText.setText(context.getString(R.string.missing_verse_text));

            TextView textViewErrorLink = new TextView(context);
            //textViewErrorLink.setText(context.getString(R.string.missing_verse_link));
            textViewErrorLink.setText(Html.fromHtml("For more information <a href='http://en.wikipedia.org/wiki/List_of_Bible_verses_not_included_in_modern_translations'>this link</a>"));
            textViewErrorLink.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            textViewErrorLink.setMovementMethod(LinkMovementMethod.getInstance());

            linearLayout.addView(textViewErrorText);
            linearLayout.addView(textViewErrorLink);

            ((ViewGroup) contentView).addView(linearLayout);
        }
        else {
            int count = words.size();
            for (int i = 0; i < count; i++) {

                LinearLayout linearLayout = getLayout(context, i);

                layout.addView(linearLayout);
            }
            ((ViewGroup) contentView).addView(layout);
        }
        return contentView; // super.onCreateView(inflater, container, savedInstanceState);
    }

    private LinearLayout getLayout(Context context, int index) {

        Bundle prefs = provider.getPreferences();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(10, 20, 30, 40);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

        final TextView textViewStrongs = new TextView(context);
        textViewStrongs.setTag("textViewStrongs" + index);
        textViewStrongs.setTextAppearance(context, android.R.style.TextAppearance_Small);
        textViewStrongs.setGravity(Gravity.RIGHT);
        textViewStrongs.setTextColor(Color.rgb(77, 179, 179));
        textViewStrongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textViewStrongs.getText().toString();
                String[] parts = text.split("\\&");

                DatabaseManager manager = DatabaseManager.getInstance();
                String strongs = manager.getStrongs(parts);

                Toast toast = Toast.makeText(v.getContext(), strongs, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        String fontSizeWord = prefs.getString("font_size_word", "2");
        int fontResId = 0;

        switch(fontSizeWord) {
            case "0": // small
                fontResId = android.R.style.TextAppearance_Small;
                break;
            case "1": // medium
                fontResId = android.R.style.TextAppearance_Medium;
                break;
            case "2": // medium
                fontResId = android.R.style.TextAppearance_Large;
                break;
        }

        TextView textViewWord = new TextView(context);
        textViewWord.setTextAppearance(context, fontResId);
        textViewWord.setTypeface(typeface);
        textViewWord.setTextColor(Color.rgb(61, 76, 83));
        textViewWord.setTag("textViewWord" + index);

        TextView textViewConcordance = new TextView(context);
        textViewConcordance.setTextAppearance(context, android.R.style.TextAppearance_Holo_Small);
        textViewConcordance.setGravity(Gravity.RIGHT);
        textViewConcordance.setTextColor(Color.rgb(230, 74, 69));
        textViewConcordance.setTag("textViewConcordance" + index);

        TextView textViewFunctional = new TextView(context);
        textViewFunctional.setTextAppearance(context, android.R.style.TextAppearance_Small);
        textViewFunctional.setTextColor(Color.rgb(77, 179, 179));
        textViewFunctional.setGravity(Gravity.RIGHT);
        textViewFunctional.setTag("textViewFunctional" + index);
        textViewFunctional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = PosParser.get((String) ((TextView) v).getText());

                Toast toast = Toast.makeText(v.getContext(), result, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        textViewStrongs.setText(words.get(index).getStrongs());
        textViewWord.setText(words.get(index).getWord());
        textViewFunctional.setText(words.get(index).getTranslit());
        textViewConcordance.setText(words.get(index).getConcordance());

        boolean showStrongs = prefs.getBoolean("show_strongs", false);
        if(showStrongs)
            linearLayout.addView(textViewStrongs);

        linearLayout.addView(textViewWord);

        boolean showConcordance = prefs.getBoolean("show_concordance", false);
        if(showConcordance)
            linearLayout.addView(textViewConcordance);

        boolean showFunctional = prefs.getBoolean("show_functional", false);
        if(showFunctional)
            linearLayout.addView(textViewFunctional);

        return linearLayout;
    }
}
