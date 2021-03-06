package comp4350.triviasmack.business;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comp4350.triviasmack.objects.Question;

public class ParseJSON {

    public static ArrayList<Question> parseJSONQuestions(JSONObject jsonResult) {
        Question questionObj;
        JSONObject jsonQuestionObject;
        JSONArray jsonQuestions, jsonOptions;
        String question;
        String[] options;
        String category;
        final String LOG_TAG=ParseJSON.class.getSimpleName();
        int answer;

        ArrayList<Question> questions = new ArrayList<>();

        try {
            jsonQuestions = jsonResult.getJSONArray("questions");
            for (int i = 0; i < jsonQuestions.length(); i++) {
                jsonQuestionObject = (JSONObject) jsonQuestions.get(i);
                question = jsonQuestionObject.getString("question");
                jsonOptions = jsonQuestionObject.getJSONArray("options");
                category = jsonQuestionObject.getString("category");

                options = new String[jsonOptions.length()];

                for (int j = 0; j < jsonOptions.length(); j++) {
                    options[j] = jsonOptions.get(j).toString();
                }
                answer = jsonQuestionObject.getInt("answer");
                questionObj = new Question(question, options, answer, category);
                questions.add(questionObj);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with JSON:", e);
            questions = null;
        }

        return questions;
    }
}
