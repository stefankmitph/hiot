package stefankmitph.kint;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import stefankmitph.model.SQLiteHelper;


public class MainActivity extends ActionBarActivity {

    final boolean doInsert = false;
    final boolean createStrongs = false;
    final boolean insertStrongs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/palab.ttf");

        SQLiteOpenHelper helper = new SQLiteHelper(this);
        final SQLiteDatabase database = helper.getWritableDatabase();

        //database.execSQL("drop table if exists content");

        if(createStrongs) {
            String[] strings = getResources().getStringArray(R.array.createStrongs);
            String listString = "";

            for (String s : strings)
            {
                listString += s;
            }

            database.execSQL(listString);
        }

        if(insertStrongs) {
            database.execSQL("delete from strongs");


            try {
                InputStream stream = getAssets().open("nestle/smallstrongs.sql");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while((line = reader.readLine()) != null) {
                    database.execSQL(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(doInsert)
            database.execSQL("delete from content");

        if(doInsert) {
            try {
                InputStream stream = getAssets().open("nestle/nestle1904.sql");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while((line = reader.readLine()) != null) {
                    database.execSQL(line + ";");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Cursor result = database.rawQuery("select * from content where book_name = 'John' and chapter_nr = 1 and verse_nr = 1", null);

        List<String> words = new ArrayList<String>();
        List<String> strongs = new ArrayList<String>();
        List<String> functional = new ArrayList<String>();

        while(result.moveToNext()) {
            words.add(result.getString(5));
            strongs.add(result.getString(7));
            functional.add(result.getString(6));
        }

        int count = result.getCount();

        ViewGroup flowContainer = (ViewGroup) findViewById(R.id.flow_container);

        for(int i = 0; i < words.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setPadding(10, 20, 10, 10);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

            final TextView textViewStrongs = new TextView(this);
            textViewStrongs.setText(strongs.get(i));
            textViewStrongs.setTextSize(10.0f);
            textViewStrongs.setTextColor(Color.rgb(0, 146, 242));
            textViewStrongs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textViewStrongs.getText().toString();
                    String[] parts = text.split("\\&");
                    String strongs = "";
                    if(parts.length > 1) {

                    } else {
                        Cursor result = database.rawQuery(String.format("select * from strongs where nr = %s", text), null);
                        while(result.moveToNext())
                            strongs = result.getString(2);
                    }

                    Toast toast = Toast.makeText(v.getContext(), strongs, Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            TextView textView2 = new TextView(this);
            textView2.setText(words.get(i));
            textView2.setTypeface(typeface);
            textView2.setTextSize(16.0f);

            TextView textView3 = new TextView(this);
            textView3.setText("line3");


            TextView textViewFunctional = new TextView(this);
            textViewFunctional.setText(functional.get(i));
            textViewFunctional.setTextSize(10.0f);
            textViewFunctional.setTextColor(Color.rgb(0, 146, 242));

            linearLayout.addView(textViewStrongs);
            linearLayout.addView(textView2);
            linearLayout.addView(textView3);
            linearLayout.addView(textViewFunctional);

            flowContainer.addView(linearLayout);
        }

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
