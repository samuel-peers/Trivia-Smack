package comp4350.triviasmack.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import comp4350.triviasmack.R;
import comp4350.triviasmack.application.MultiPlayer;
import comp4350.triviasmack.business.GameController;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MultiPlayerPageActivity extends AppCompatActivity {
    private MultiPlayer multiPlayer = MultiPlayer.getInstance();
    private GameController gameController = GameController.getInstance();
    private Socket socket;
    private final String TAG = "MultiPlayerPageActivity";
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_player_page_activity);
        tv = (TextView) findViewById(R.id.otherScore);
        socket = multiPlayer.getSocket();
        socket.on("join_waiting", onJoinWaiting);
        socket.on("other_player_ready", onOtherPlayerReady);
    }

    public void onDestroy() {
        super.onDestroy();
        socket.off("join_waiting", onJoinWaiting);
        socket.off("other_player_ready", onOtherPlayerReady);

    }

    private Emitter.Listener onOtherPlayerReady = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MultiPlayerPageActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Intent QuestionIntent = new Intent(MultiPlayerPageActivity.this, QuestionPageActivity.class);
                    MultiPlayerPageActivity.this.startActivity(QuestionIntent);
                }
            });
        }
    };

    private Emitter.Listener onJoinWaiting = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MultiPlayerPageActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    TextView tv = (TextView) findViewById(R.id.otherScore);
                    tv.setText("Waiting for other players to connect...");
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit game?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                gameController.start();
                Intent ExitGameIntent = new Intent(MultiPlayerPageActivity.this, MainActivity.class);
                ExitGameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                MultiPlayerPageActivity.this.startActivity(ExitGameIntent);
                multiPlayer.disconnect();
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



