package stefankmitph.kint;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
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
import java.util.Map;

import stefankmitph.model.Concordance;
import stefankmitph.model.DatabaseManager;
import stefankmitph.model.Word;

public class MainActivity extends ActionBarActivity implements ActivityObjectProvider, FragmentSelection.OnFragmentInteractionListener {

    private SQLiteDatabase database;
    private Bundle bundle;
    private String book;
    private int chapter;
    private int verse;
    private HashMap<Integer, List<Word>> map;
    private Typeface typeface;

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void initializeData(List<Word> words) {
        map = new HashMap<Integer, List<Word>>();
        for(Word word : words) {
            if(!map.containsKey(word.getVerse_nr()))
                map.put(word.getVerse_nr(), new ArrayList<Word>());

            map.get(word.getVerse_nr()).add(word);
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

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

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
    public void onFragmentInteraction(Uri uri) {

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
