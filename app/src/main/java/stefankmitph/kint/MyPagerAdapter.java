package stefankmitph.kint;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import stefankmitph.model.SQLiteHelper;

/**
 * Created by KumpitschS on 14.04.2015.
 */
public class MyPagerAdapter extends PagerAdapter{
    private SQLiteDatabase database;
    private Typeface typeface;

    public MyPagerAdapter(Context context, SQLiteDatabase database) {
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/palab.ttf");
        this.database = database;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();


        assert database != null;

        Cursor result = database.rawQuery(String.format("select * from content where book_name = 'John' and chapter_nr = %d and verse_nr = 1", position + 1), null);

        SQLiteOpenHelper helper = new SQLiteHelper(context);

        List<String> words = new ArrayList<String>();
        List<String> strongs = new ArrayList<String>();
        List<String> functional = new ArrayList<String>();

        while(result.moveToNext()) {
            words.add(result.getString(5));
            strongs.add(result.getString(7));
            functional.add(result.getString(6));
        }

        FlowLayout layout = new FlowLayout(context, null);
        layout.setLayoutParams(
                new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.MATCH_PARENT,
                        FlowLayout.LayoutParams.MATCH_PARENT
                        ));

        for(int i = 0; i < words.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setPadding(10, 20, 10, 10);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

            final TextView textViewStrongs = new TextView(context);
            textViewStrongs.setText(strongs.get(i));
            textViewStrongs.setTextSize(10.0f);
            textViewStrongs.setTextColor(Color.rgb(0, 146, 242));
            final SQLiteDatabase finalDatabase = database;
            textViewStrongs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textViewStrongs.getText().toString();
                    String[] parts = text.split("\\&");
                    String strongs1 = "";
                    if (parts.length > 1) {

                    } else {
                        Cursor result1 = finalDatabase.rawQuery(String.format("select * from strongs where nr = %s", text), null);
                        while (result1.moveToNext())
                            strongs1 = result1.getString(2);
                    }

                    Toast toast = Toast.makeText(v.getContext(), strongs1, Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            TextView textView2 = new TextView(context);
            textView2.setText(words.get(i));
            textView2.setTypeface(typeface);
            textView2.setTextSize(16.0f);

            TextView textView3 = new TextView(context);
            textView3.setText("line3");


            TextView textViewFunctional = new TextView(context);
            textViewFunctional.setText(functional.get(i));
            textViewFunctional.setTextSize(10.0f);
            textViewFunctional.setTextColor(Color.rgb(0, 146, 242));

            linearLayout.addView(textViewStrongs);
            linearLayout.addView(textView2);
            linearLayout.addView(textView3);
            linearLayout.addView(textViewFunctional);

            layout.addView(linearLayout);
        }
        ((ViewPager) container).addView(layout, 0);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
