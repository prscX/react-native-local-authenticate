
package prscx.localauthenticate;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableNativeArray;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import com.facebook.react.bridge.ReadableMap;
import android.telecom.Call;


public class RNLocalAuthenticateModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private final FingerprintManagerCompat mFingerprintManager;
  private boolean mIsAuthenticating = false;
  private CancellationSignal mCancellationSignal;
  private Callback mCallback;

  public RNLocalAuthenticateModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    mFingerprintManager = FingerprintManagerCompat.from(reactContext);
  }

  @Override
  public String getName() {
    return "RNLocalAuthenticate";
  }

  private static final int AUTHENTICATION_TYPE_FINGERPRINT = 1;

  private final FingerprintManagerCompat.AuthenticationCallback mAuthenticationCallback =
          new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
              mIsAuthenticating = false;
              // Bundle successResult = new Bundle();
              // successResult.putBoolean("success", true);
              safeResolve(true);
            }

            @Override
            public void onAuthenticationFailed() {
              mIsAuthenticating = false;
              Bundle failResult = new Bundle();
              // failResult.putBoolean("success", false);
              // failResult.putString("error", "authentication_failed");
              safeResolve(false);
              // Failed authentication doesn't stop the authentication process, stop it anyway so it works
              // with the promise API.
              safeCancel();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
              mIsAuthenticating = false;
              Bundle errorResult = new Bundle();
              // errorResult.putBoolean("success", false);
              // errorResult.putString("error", convertErrorCode(errMsgId));
              // errorResult.putString("message", errString.toString());
              safeResolve(false);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
              mIsAuthenticating = false;
              Bundle helpResult = new Bundle();
              // helpResult.putBoolean("success", false);
              // helpResult.putString("error", convertHelpCode(helpMsgId));
              // helpResult.putString("message", helpString.toString());
              safeResolve(false);
              // Help doesn't stop the authentication process, stop it anyway so it works with the
              // promise API.
              safeCancel();
            }
          };

  @ReactMethod
  public void SupportedAuthenticationTypes(final Callback callback) {
    boolean hasHardware = mFingerprintManager.isHardwareDetected();
    List<Integer> results = new ArrayList<>();
    if (hasHardware) {
      results.add(AUTHENTICATION_TYPE_FINGERPRINT);
    }

    callback.invoke(results.toArray());
  }


  @ReactMethod
  public void HasHardware(final Callback callback) {
    boolean hasHardware = mFingerprintManager.isHardwareDetected();
    callback.invoke(hasHardware);
  }

  @ReactMethod
  public void IsEnrolled(final Callback callback) {
    boolean isEnrolled = mFingerprintManager.hasEnrolledFingerprints();
    callback.invoke(isEnrolled);
  }

  @ReactMethod
  public void Authenticate(final String reason, final Callback callback) {
    // FingerprintManager callbacks are invoked on the main thread so also run this there to avoid
    // having to do locking.
    final Activity activity = this.getCurrentActivity();
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mIsAuthenticating) {
          // Bundle cancelResult = new Bundle();
          // cancelResult.putBoolean("success", false);
          // cancelResult.putString("error", "app_cancel");
          safeResolve(false);
          mCallback = callback;
          return;
        }

        mIsAuthenticating = true;
        mCallback = callback;
        mCancellationSignal = new CancellationSignal();
        mFingerprintManager.authenticate(null, 0, mCancellationSignal, mAuthenticationCallback, null);
      }
    });
  }


  @ReactMethod
  public void CancelAuthenticate(final Callback callback) {
    final Activity activity = this.getCurrentActivity();
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        safeCancel();
      }
    });
  }

  private void safeCancel() {
    if (mCancellationSignal != null) {
      mCancellationSignal.cancel();
      mCancellationSignal = null;
    }
  }


  private void safeResolve(boolean bundle) {
    if (mCallback != null) {
      // mCallback.invoke(Arguments.makeNativeMap(bundle));
      mCallback.invoke(bundle);
      mCallback = null;
    }
  }

  private static String convertErrorCode(int code) {
    switch (code) {
      case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
        return "user_cancel";
      case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
        return "not_available";
      case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
        return "lockout";
      case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
        return "no_space";
      case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
        return "timeout";
      case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
        return "unable_to_process";
      default:
        return "unknown";
    }
  }

  private static String convertHelpCode(int code) {
    switch (code) {
      case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
        return "imager_dirty";
      case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
        return "insufficient";
      case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
        return "partial";
      case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
        return "too_fast";
      case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
        return "too_slow";
      default:
        return "unknown";
    }
  }
}