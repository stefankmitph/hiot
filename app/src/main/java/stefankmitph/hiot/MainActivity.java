package stefankmitph.hiot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stefankmitph.model.DatabaseManager;
import stefankmitph.model.Word;

public class MainActivity extends ActionBarActivity implements ActivityObjectProvider {

    private SQLiteDatabase database;
    private Bundle bundle;
    private String book;
    private int chapter;
    private int verse;
    private HashMap<Integer, List<Word>> map;
    private Typeface typeface;
    MyPagerAdapter myPagerAdapter;

    private static final int RESULT_SETTINGS = 1;

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void initializeData(List<Word> words) {
        map = new HashMap<>();
        for(Word word : words) {
            if(!map.containsKey(word.getVerse()))
                map.put(word.getVerse(), new ArrayList<Word>());

            map.get(word.getVerse()).add(word);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Cardo104s.ttf");

        DatabaseManager.init(this);

        bundle = getIntent().getExtras();
        book = bundle.getString("book");
        chapter = bundle.getInt("chapter");
        verse = bundle.getInt("verse");

        DatabaseManager manager = DatabaseManager.getInstance();
        List<Word> words = manager.getChapter(book, chapter);

        initializeData(words);

        setActionBarTitle(String.format("%s %d:%d", book, this.chapter, verse));

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        final ViewPager myPager = (ViewPager) findViewById(R.id.home_panels_pager);
        myPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myPager.setOffscreenPageLimit(5);
        myPager.setAdapter(myPagerAdapter);
        myPager.setCurrentItem(verse - 1);
        myPager.setOnPageChangeListener(new CircularViewPagerHandler(myPager));
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    @Override
    public List<Word> getWords(int verse) {
        assert map != null;
        if(map.containsKey(verse))
            return map.get(verse);
        return null;
    }

    @Override
    public Typeface getTypeface() {
        return typeface;
    }

    @Override
    public Bundle getPreferences() {

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        Bundle bundle = new Bundle();
        bundle.putBoolean("show_strongs", sharedPrefs.getBoolean("show_strongs", true));
        bundle.putBoolean("show_concordance", sharedPrefs.getBoolean("show_concordance", true));
        bundle.putBoolean("show_functional", sharedPrefs.getBoolean("show_functional", true));
        bundle.putBoolean("show_lemma", sharedPrefs.getBoolean("show_lemma", true));
        bundle.putString("font_size_word", sharedPrefs.getString("font_size_word", "2"));

        return bundle;
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return VerseFragment.newInstance(book, chapter, position + 1);
        }

        @Override
        public int getCount() {
            return map.keySet().size(); // myPager -> scroll
        }

        private String getFragmentName(int viewPagerId, int index) {
            return "android:switcher:" + viewPagerId + ":" + index;
        }
    }

    public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        private ViewPager   mViewPager;
        private int         mCurrentPosition;
        private int         mScrollState;

        public CircularViewPagerHandler(final ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onPageSelected(final int position) {
            mCurrentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            handleScrollState(state);
            mScrollState = state;
        }

        private void handleScrollState(final int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                setNextItemIfNeeded();
            }
        }

        private void setNextItemIfNeeded() {
            if (!isScrollStateSettling()) {
                handleSetNextItem();
            }
        }

        private boolean isScrollStateSettling() {
            return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
        }

        private void handleSetNextItem() {
            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if(mCurrentPosition == 0) {
                mViewPager.setCurrentItem(lastPosition, false);
            } else if(mCurrentPosition == lastPosition) {
                mViewPager.setCurrentItem(0, false);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(false); // disable the button
            supportActionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            supportActionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                reloadFragment();
                break;

        }
    }

    private void reloadFragment() {
        ViewPager pager = (ViewPager) findViewById(R.id.home_panels_pager);
        String fragmentId = myPagerAdapter.getFragmentName(R.id.home_panels_pager, pager.getCurrentItem());

        int currentItem = pager.getCurrentItem();
        pager.setAdapter(myPagerAdapter);
        pager.setCurrentItem(currentItem);
    }
}
