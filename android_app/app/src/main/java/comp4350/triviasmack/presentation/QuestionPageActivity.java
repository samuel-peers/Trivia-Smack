package comp4350.triviasmack.presentation;

import android.content.Intent;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;

import comp4350.triviasmack.R;
import comp4350.triviasmack.business.GameController;
import comp4350.triviasmack.objects.Question;

public class QuestionPageActivity extends AppCompatActivity {

    private GameController gameController = GameController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);

        Question questionObj = gameController.getNextQuestion();
        TextView questionTitle = (TextView) findViewById(R.id.questionText);

        questionTitle.setText(questionObj.getQuestion());
        showOptions(questionObj.getOptions());
    }

    public void showOptions(String options[]) {

        for (int i = 0; i < options.length; i++) {
            String buttonID = "optionBtn" + (i + 1);
            int id = getResources().getIdentifier(buttonID, "id", "comp4350.triviasmack");
            Button optionButton = (Button) findViewById(id);
            optionButton.setVisibility(View.VISIBLE);
            optionButton.setText("• " + options[i]);
        }
    }

    public void processOptionButton(View v) {
        boolean result;
        String optionText;

        optionText = ((Button) v).getText() + "";
        optionText = optionText.substring(2);
        result = gameController.evaluateAnswer(optionText);

        if (result) {
            ((Button) v).setText("• RIGHT!");
            v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.nice_green));
            gameController.increaseScore();
        } else {
            v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.nice_red));
        }

        if (gameController.finished()) {
            Intent MainPageIntent = new Intent(QuestionPageActivity.this, MainActivity.class);
            MainPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            QuestionPageActivity.this.startActivity(MainPageIntent);
        } else {
            startActivity(getIntent());
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit game?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                gameController.start();
                Intent ExitGameIntent = new Intent(QuestionPageActivity.this, MainActivity.class);
                ExitGameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                QuestionPageActivity.this.startActivity(ExitGameIntent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}