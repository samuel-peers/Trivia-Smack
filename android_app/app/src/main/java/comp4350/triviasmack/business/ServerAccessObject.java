package comp4350.triviasmack.business;

import android.util.Log;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import comp4350.triviasmack.application.Constants;
import comp4350.triviasmack.application.Services;
import comp4350.triviasmack.objects.Question;

public class ServerAccessObject implements ServerAccess {


    private static final String baseUrl = Constants.SERVER_URL;

    private String questionUrl;
    private URL url;
    private final String LOG_TAG = ServerAccessObject.class.getSimpleName();

    public ServerAccessObject() {
        questionUrl = baseUrl + "api/question_data/";
    }

    public void getRandomQuestions(ArrayList<Question> questions, int numQuestions, String category) {
        try {
            if (numQuestions < 0) {
                throw new IllegalArgumentException("Number of questions cannot be less than 0");
            }
            url = new URL((questionUrl + numQuestions + "/" + category));

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException", e);
        }

        JSONObject result = Services.createAsyncFacade().executeTask(url);

        questions.addAll(ParseJSON.parseJSONQuestions(result));
    }
}
