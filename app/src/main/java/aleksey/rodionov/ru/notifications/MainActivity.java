package aleksey.rodionov.ru.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String CHANNEL_ID_1 = BuildConfig.APPLICATION_ID + ".CHANNEL_ID_1";
    public final static String REPLY_ACTION = BuildConfig.APPLICATION_ID + ".REPLY_ACTION";
    public final static int NOTIFICATION1_ID = 1;
    public final static int NOTIFICATION2_ID = 2;
    public final static int NOTIFICATION3_ID = 3;

    public final static String CONVERSATION_KEY = "CONVERSATION_KEY";
    public final static int CONVERSATION_ID = 1;

    // Key for the string that's delivered in the action's intent.
    public static final String KEY_TEXT_REPLY = "key_text_reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        createChannel1();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                showNotification1();
                break;

            case R.id.button2:
                showNotification2();
                break;

            case R.id.button3:
                showNotification3();
                break;

        }
    }

    private void showNotification1() {
        String title = getString(R.string.notification_content_title);
        String contentText = getString(R.string.notification_content_text);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_details_black_24dp)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true)
                ;

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(NOTIFICATION1_ID, builder.build());
    }

    private void showNotification2() {
        String title = getString(R.string.notification_content_title);
        String contentText = getString(R.string.notification_content_text);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_details_black_24dp)
                .setContentTitle(title)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true)
        ;

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(NOTIFICATION2_ID, builder.build());
    }

    private void showNotification3() {

        String replyLabel = getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(),
                        CONVERSATION_ID,
                        getMessageReplyIntent(CONVERSATION_ID),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_send_black_24dp, "Reply action", replyPendingIntent
        )
                .addRemoteInput(remoteInput)
                .build();

        Notification newMessageNotification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("New message")
                .setContentText("This is a message")
                .addAction(replyAction)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(NOTIFICATION3_ID, newMessageNotification);
    }

    private void createChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel1_name);
            String description = getString(R.string.channel1_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        return pendingIntent;
    }

    private Intent getMessageReplyIntent(int conversationId) {
        return new Intent(REPLY_ACTION).putExtra(CONVERSATION_KEY, conversationId);
    }
}
