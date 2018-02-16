package firebase.demo.firebaseapplicationdemo.firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;


import java.util.List;

import firebase.demo.firebaseapplicationdemo.R;

/**
 * Created by Atrijit on 14-09-2017.
 */

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }


//    public void showNotificationMessage(final String title, final String message, Intent intent, Uri defaultSoundUri) {
//        // Check for empty push message
//        if (TextUtils.isEmpty(message))
//            return;
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
//        showSmallNotification(mBuilder, title, message, resultPendingIntent, defaultSoundUri);
//    }
//
//
//    private void showSmallNotification(NotificationCompat.Builder mBuilder, String title, String message, PendingIntent resultPendingIntent, Uri defaultSoundUri) {
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.addLine(message);
//
//        Log.d(TAG, "showSmallNotification: " + title + message);
//        RemoteViews mContentView = new RemoteViews(mContext.getPackageName(), R.layout.notification_layout);
//        Bitmap bitmap_title = textAsBitmapLarge(title);
//        Bitmap bitmap_message = textAsBitmapSmall(message);
//
//        mContentView.setImageViewResource(R.id.notification_image, R.mipmap.notify_all);
//        mContentView.setImageViewBitmap(R.id.notification_task, bitmap_title);
//        mContentView.setImageViewBitmap(R.id.notification_assign, bitmap_message);
//
//
//        Notification notification;
//        notification = mBuilder.setTicker(title)
//                .setAutoCancel(true)
//                .setContent(mContentView)
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setStyle(inboxStyle)
//                .setWhen(System.currentTimeMillis())
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.notify_all)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.notify_all))
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(101, notification);
//    }

    public void playNotificationSound() {
        try {
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, defaultSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public Bitmap textAsBitmapSmall(String text) {

        Typeface bahu_thin = Typeface.createFromAsset(mContext.getAssets(), "fonts/bauhaus_thin.ttf");
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setTypeface(bahu_thin);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public Bitmap textAsBitmapLarge(String text) {

        Typeface bahu_bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/bauhaus_bold.ttf");
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setTypeface(bahu_bold);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

}