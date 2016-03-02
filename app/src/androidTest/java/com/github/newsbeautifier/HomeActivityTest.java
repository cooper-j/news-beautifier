package com.github.newsbeautifier;

import android.content.Intent;

import com.github.newsbeautifier.activities.HomeActivity;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
