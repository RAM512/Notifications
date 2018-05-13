package aleksey.rodionov.ru.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

public class ReplyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(ReplyReceiver.class.getSimpleName(), "onReceive()");
        Toast.makeText(context, "Reply is " + getMessageText(intent), Toast.LENGTH_SHORT).show();
        NotificationManagerCompat.from(context).cancel(MainActivity.NOTIFICATION3_ID);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(MainActivity.KEY_TEXT_REPLY);
        }
        return null;
    }
}
