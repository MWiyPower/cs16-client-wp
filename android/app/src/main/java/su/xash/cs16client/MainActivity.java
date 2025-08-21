package wp.xash.cs16client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.util.TypedValue;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.splash);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        int sizeInDp = 200;
        int sizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp, getResources().getDisplayMetrics());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sizeInPx, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        FrameLayout layout = new FrameLayout(this);
        layout.addView(imageView, params);
        setContentView(layout);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);
        imageView.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
            fadeOut.setDuration(500);
            fadeOut.setFillAfter(true);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationEnd(Animation animation) { launchXash(); }
                @Override public void onAnimationRepeat(Animation animation) {}
            });
            imageView.startAnimation(fadeOut);
        }, 5000);
    }

    private void launchXash() {
        String pkg = "su.xash.engine.test";

        try { 
            getPackageManager().getPackageInfo(pkg, 0); 
        } catch (PackageManager.NameNotFoundException e) {
            try { 
                pkg = "su.xash.engine"; 
                getPackageManager().getPackageInfo(pkg, 0); 
            } catch (PackageManager.NameNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/FWGS/xash3d-fwgs/releases/tag/continuous"))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
                return;
            }
        }

        startActivity(new Intent().setComponent(new ComponentName(pkg, "su.xash.engine.XashActivity"))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("gamedir", "cstrike")
                .putExtra("gamelibdir", getApplicationInfo().nativeLibraryDir)
                .putExtra("argv", "-dev 2 -log -dll @yapb")
                .putExtra("package", getPackageName()));
        finish();
    }
}