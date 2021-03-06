package comp4350.triviasmack.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import comp4350.triviasmack.R;
import comp4350.triviasmack.business.GameController;
import android.widget.Button;

public class SelectCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private GameController gameController;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        category = "all";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        gameController = GameController.getInstance();
        setButtons();
    }

    public void setButtons() {
        Button all = (Button)findViewById(R.id.all);
        Button geography = (Button)findViewById(R.id.geography);
        Button history = (Button)findViewById(R.id.history);
        Button math_and_science = (Button)findViewById(R.id.math_and_science);
        Button pop_culture = (Button)findViewById(R.id.pop_culture);
        Button other = (Button)findViewById(R.id.other);

        Button[] buttons = {all, geography, history, math_and_science, pop_culture, other};

        for(int i = 0; i < buttons.length; i++){
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v){
        gameController.setCategory(v.getTag().toString());
        renderQuestionPage(v);
    }

    public void renderQuestionPage(View v) {
        gameController.start();
        Intent QuestionPageIntent = new Intent(SelectCategoryActivity.this, QuestionPageActivity.class);
        SelectCategoryActivity.this.startActivity(QuestionPageIntent);
    }
}
