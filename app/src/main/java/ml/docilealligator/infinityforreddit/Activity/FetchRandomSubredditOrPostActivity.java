package ml.docilealligator.infinityforreddit.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.docilealligator.infinityforreddit.BottomSheetFragment.RandomBottomSheetFragment;
import ml.docilealligator.infinityforreddit.CustomTheme.CustomThemeWrapper;
import ml.docilealligator.infinityforreddit.Infinity;
import ml.docilealligator.infinityforreddit.Post.FetchPost;
import ml.docilealligator.infinityforreddit.R;
import retrofit2.Retrofit;

public class FetchRandomSubredditOrPostActivity extends BaseActivity {

    public static final String EXTRA_RANDOM_OPTION = "ERO";

    @BindView(R.id.relative_layout_fetch_random_subreddit_or_post_activity)
    RelativeLayout relativeLayout;
    @BindView(R.id.lottie_animation_view_fetch_random_subreddit_or_post_activity)
    LottieAnimationView lottieAnimationView;
    @Inject
    @Named("no_oauth")
    Retrofit mRetrofit;
    @Inject
    @Named("default")
    SharedPreferences mSharedPreferences;
    @Inject
    CustomThemeWrapper mCustomThemeWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((Infinity) getApplicationContext()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_random_subreddit_or_post);

        ButterKnife.bind(this);

        int option = getIntent().getIntExtra(EXTRA_RANDOM_OPTION, RandomBottomSheetFragment.RANDOM_SUBREDDIT);

        FetchPost.fetchRandomPost(mRetrofit, option == RandomBottomSheetFragment.RANDOM_NSFW_SUBREDDIT
                || option == RandomBottomSheetFragment.RANDOM_NSFW_POST, new FetchPost.FetchRandomPostListener() {
            @Override
            public void fetchRandomPostSuccess(String postId, String subredditName) {
                switch (option) {
                    case RandomBottomSheetFragment.RANDOM_SUBREDDIT:
                    case RandomBottomSheetFragment.RANDOM_NSFW_SUBREDDIT: {
                        Intent intent = new Intent(FetchRandomSubredditOrPostActivity.this, ViewSubredditDetailActivity.class);
                        intent.putExtra(ViewSubredditDetailActivity.EXTRA_SUBREDDIT_NAME_KEY, subredditName);
                        startActivity(intent);
                    }
                    break;
                    case RandomBottomSheetFragment.RANDOM_POST:
                    case RandomBottomSheetFragment.RANDOM_NSFW_POST:
                        Intent intent = new Intent(FetchRandomSubredditOrPostActivity.this, ViewPostDetailActivity.class);
                        intent.putExtra(ViewPostDetailActivity.EXTRA_POST_ID, postId);
                        startActivity(intent);
                        break;
                }

                finish();
            }

            @Override
            public void fetchRandomPostFailed() {
                Toast.makeText(FetchRandomSubredditOrPostActivity.this, R.string.fetch_random_thing_failed, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected SharedPreferences getDefaultSharedPreferences() {
        return mSharedPreferences;
    }

    @Override
    protected CustomThemeWrapper getCustomThemeWrapper() {
        return mCustomThemeWrapper;
    }

    @Override
    protected void applyCustomTheme() {
        relativeLayout.setBackgroundColor(mCustomThemeWrapper.getBackgroundColor());
    }
}