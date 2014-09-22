package com.droidcon.nabil.pluginsandbox;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.Properties;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MyActivity extends Activity {

    @InjectView(R.id.author)
    TextView mTxtAuthor;

    @InjectView(R.id.commit)
    TextView mTxtCommit;

    @InjectView(R.id.flavour)
    TextView mTxtFlavour;

    AssetManager mAssetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);

        mAssetManager = getAssets();

        populate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mAssetManager){
            mAssetManager.close();
        }
    }

    private void populate () {
        try {
            Properties properties = new Properties();
            properties.load(mAssetManager.open("about.properties"));

            mTxtAuthor.setText(getSpannableString (AboutKeys.AUTHOR.key, properties.getProperty(AboutKeys.AUTHOR.key)));
            mTxtCommit.setText(getSpannableString(AboutKeys.COMMIT.key, properties.getProperty(AboutKeys.COMMIT.key)));
            mTxtFlavour.setText(getSpannableString(AboutKeys.FLAVOUR.key, properties.getProperty(AboutKeys.FLAVOUR.key)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SpannableString getSpannableString (String field, String value) {
        String separator = ": ";
        SpannableString text = new SpannableString (field + separator + value);
        text.setSpan(new RelativeSizeSpan(1.5f), 0, field.length() + separator.length(), 0);
        return text;
    }

    enum AboutKeys {
        AUTHOR ("author"),
        COMMIT ("commit"),
        FLAVOUR ("flavour");

        String key;

        AboutKeys (String propKey) {
                key = propKey;
        }
    }
}
