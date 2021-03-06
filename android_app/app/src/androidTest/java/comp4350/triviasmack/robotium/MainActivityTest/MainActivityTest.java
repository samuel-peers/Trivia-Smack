package comp4350.triviasmack.robotium.MainActivityTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp4350.triviasmack.R;
import comp4350.triviasmack.presentation.MainActivity;
import comp4350.triviasmack.presentation.MultiPlayerPageActivity;
import comp4350.triviasmack.presentation.PracticeQuestionActivity;
import comp4350.triviasmack.presentation.SelectCategoryActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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
    public void pressPlayBtn() throws Exception {
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.one_player));

        solo.assertCurrentActivity("wrong activity", SelectCategoryActivity.class);
    }

    @Test
    public void pressTwoPlayerBtn() throws Exception {
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.two_player));

        solo.assertCurrentActivity("wrong activity", MultiPlayerPageActivity.class);
    }

    @Test
    public void pressPracticeModeBtn() throws Exception {
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.practice_mode));

        solo.assertCurrentActivity("wrong activity", PracticeQuestionActivity.class);
    }
}
