package comp4350.triviasmack.robotium.TwoPlayerTest;

import com.robotium.solo.Solo;

import comp4350.triviasmack.application.Main;
import comp4350.triviasmack.R;
import comp4350.triviasmack.business.MultiPlayer;
import comp4350.triviasmack.presentation.MainActivity;
import comp4350.triviasmack.presentation.MultiPlayerPageActivity;
import comp4350.triviasmack.presentation.QuestionPageActivity;
import comp4350.triviasmack.presentation.SelectCategoryActivity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TwoPlayersTest{
    private static final int numQuestions= Main.numQuestions;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    public Solo solo;


    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), mActivityRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void twoPlayerTest() throws Exception {
        MultiPlayer dummyPlayer = MultiPlayer.getInstance();

        solo.assertCurrentActivity("Expected activity: MainActivity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.two_player));

        solo.assertCurrentActivity("Expected activity: MultiPlayerPageActivity", MultiPlayerPageActivity.class);

        solo.sleep(1000);
        dummyPlayer.connect();
        for(int i =0; i < numQuestions; i++) {
            solo.assertCurrentActivity("Expected activity: QuestionPageActivity", QuestionPageActivity.class);
            solo.sleep(1000);
            solo.clickOnView(solo.getView(R.id.optionBtn1));
        }

        solo.waitForActivity(MultiPlayerPageActivity.class);
        solo.assertCurrentActivity("Expected activity: MainActivity", MainActivity.class);

        int scoreVis = solo.getView(R.id.scoreText).getVisibility();
        assertEquals(scoreVis, View.VISIBLE);
    }

}
