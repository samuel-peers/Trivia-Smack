package comp4350.triviasmack.business;

import java.util.ArrayList;

import comp4350.triviasmack.application.Constants;
import comp4350.triviasmack.application.Services;
import comp4350.triviasmack.objects.Question;
import java.util.Arrays;

public class AccessQuestions {

    private ServerAccess serverAccess;
    private String[] validCategories = Constants.categories;
    private int num_all_questions = Constants.TOTAL_NUM_QUESTIONS;

    public AccessQuestions() {
        serverAccess = Services.getServerAccess();
    }

    public void getRandomQuestions(ArrayList<Question> questions, int numQuestions, String category) {
        if (numQuestions < 0) {
            throw new IllegalArgumentException("Number of questions cannot be less than 0");
        }

        if (questions == null) {
            throw new NullPointerException("questions is null");
        }

       if(!Arrays.asList(validCategories).contains(category)){
           throw new IllegalArgumentException("Invalid category");
       }

        serverAccess.getRandomQuestions(questions, numQuestions, category);
    }

    public void getAllQuestions(ArrayList<Question> questions){
        getRandomQuestions(questions, num_all_questions, "all");
    }
}
