package com.github.newsbeautifier;

import android.content.Intent;

import com.github.cooperj.newsbeautifier.activities.HomeActivity;

/**
 * Created by james_000 on 2/29/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        HomeActivity activity = Robolectric.setupActivity(HomeActivity.class);
        activity.findViewById(R.id.login).performClick();

        Intent expectedIntent = new Intent(activity, HomeActivity.class);
        assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
    }
}
