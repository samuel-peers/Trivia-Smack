package comp4350.triviasmack.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.socket.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import comp4350.triviasmack.R;
import comp4350.triviasmack.application.Main;
import comp4350.triviasmack.business.MultiPlayer;
import comp4350.triviasmack.business.GameController;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private static int otherScore;
    private GameController gameController;
    private MultiPlayer multiPlayer = MultiPlayer.getInstance();
    private Socket socket;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static boolean stillPlaying;
    private static TextView otherScoreText;
    private static TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Main.startUp();

        setContentView(R.layout.activity_main);
        if (isNetworkAvailable()){
            gameController = GameController.getInstance();
            otherScoreText = (TextView) findViewById(R.id.otherScoreText);
            scoreText = (TextView) findViewById(R.id.scoreText);
            displayScores();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void displayScores() {
        if (GameController.getInstance().isStarted()) {
            displayScore();
            if (multiPlayer.isConnected()) {
                displayOtherScore();
            }
        }
    }

    private void makeInvisible() {
        otherScoreText.setVisibility(View.INVISIBLE);
    }

    private void displayScore() {
        int score = gameController.getScore();
        scoreText.setVisibility(View.VISIBLE);
        scoreText.setText("Your Score: " + score);
    }

    private void displayOtherScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otherScoreText.setVisibility(View.VISIBLE);
                if (otherScore != -1) {
                    if(stillPlaying){
                        otherScoreText.setText("Other player is still playing");
                    }else {
                        otherScoreText.setText("Their score: " + otherScore);
                    }
                }else{
                    otherScoreText.setText("The other player disconnected.");
                }
            }
        });
    }

    private Emitter.Listener onOtherPlayerDone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        if (!data.getString("msg").equals("null")) {
                            MainActivity.otherScore = data.getInt("msg");
                        } else {
                            otherScore = -1;
                        }
                        stillPlaying = false;
                        Log.d(LOG_TAG, "onOtherPayerDone" + otherScore);
                        displayScores();
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }
                }
            });
        }
    };

    public void renderSelectCategoryPage(View v) {

        if (!isNetworkAvailable()){
            noInternetDialog();
            return;
        }

        makeInvisible();

        if (multiPlayer.isConnected()) {
            multiPlayer.disconnect();
        }

        Intent SelectCategoryIntent = new Intent(MainActivity.this, SelectCategoryActivity.class);
        MainActivity.this.startActivity(SelectCategoryIntent);
    }

    public void renderRulesPage(View v) {
        Intent RulesPageIntent = new Intent(MainActivity.this, RulesPageActivity.class);
        MainActivity.this.startActivity(RulesPageIntent);
    }

    public void renderMultiPlayerPage(View v) {

        if (!isNetworkAvailable()){
            noInternetDialog();
            return;
        }

        gameController.setCategory("all");
        gameController.start();

        multiPlayer.connect();
        socket = multiPlayer.getSocket();

        stillPlaying = true;
        socket.on("other_player_done", onOtherPlayerDone);
        Intent MultiPlayerPageIntent = new Intent(MainActivity.this, MultiPlayerPageActivity.class);
        MainActivity.this.startActivity(MultiPlayerPageIntent);
    }

    public void renderPracticeModePage(View v){
        gameController.destroy();

        if (!isNetworkAvailable()){
            noInternetDialog();
            return;
        }

        makeInvisible();

        if (multiPlayer.isConnected()) {
            multiPlayer.disconnect();
        }

        Intent PracticeQuestionActivity = new Intent(MainActivity.this, PracticeQuestionActivity.class);
        MainActivity.this.startActivity(PracticeQuestionActivity);
    }

    private void noInternetDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No internet connection!");
        alertDialog.setMessage("You need internet connection to play!");
        alertDialog.show();
    }
}
