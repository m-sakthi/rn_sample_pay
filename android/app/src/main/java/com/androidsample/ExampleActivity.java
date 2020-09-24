package com.androidsample;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.squareup.sdk.pos.ChargeRequest;
import com.squareup.sdk.pos.CurrencyCode;
import com.squareup.sdk.pos.PosClient;
import com.squareup.sdk.pos.PosSdk;

public class ExampleActivity extends ReactActivity {
    private static final String APPLICATION_ID = "sq0idp-NHW0BYyK9LRo-Cw15YgSfQ";

    private PosClient posClient;
    private static final int CHARGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        Intent intent = getIntent();
        String currency = intent.getStringExtra("currency");

        Toast.makeText(this, "currency onCreate" + currency, Toast.LENGTH_LONG).show();

        posClient = PosSdk.createClient(this, APPLICATION_ID);
        startTransaction(currency);
    }


    public void startTransaction(String currency) {

        Log.d("ExampleActivity", "currency startTransaction" + currency);
        ChargeRequest request = new ChargeRequest.Builder(
                100,
                CurrencyCode.USD)
                .build();
        try {
            Intent intent = posClient.createChargeIntent(request);
            startActivityForResult(intent, CHARGE_REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
//            AlertDialogHelper.showDialog(
//                    this,
//                    "Error",
//                    "Square Point of Sale is not installed"
//            );
            Log.d("ExampleActivity", "Some custom");
            Toast.makeText(this, "Some custom", Toast.LENGTH_LONG).show();

            posClient.openPointOfSalePlayStoreListing();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle unexpected errors
        if (data == null || requestCode != CHARGE_REQUEST_CODE) {
            Toast.makeText(this, "Error: unknown", Toast.LENGTH_LONG).show();
            Log.d("ExampleActivity", "Error: unknown");
//            AlertDialogHelper.showDialog(this,
//                    "Error: unknown",
//                    "Square Point of Sale was uninstalled or stopped working");
            return;
        }

        // Handle expected results
        if (resultCode == Activity.RESULT_OK) {
            // Handle success
            ChargeRequest.Success success = posClient.parseChargeSuccess(data);
            Log.d("ExampleActivity", "Success" + success.clientTransactionId);
            EventEmitterModule.emitEvent("ExampleActivity" + success.clientTransactionId);
            Toast.makeText(this, "Success" + success.clientTransactionId, Toast.LENGTH_LONG).show();
//            AlertDialogHelper.showDialog(this,
//                    "Success",
//                    "Client transaction ID: "
//                            + success.clientTransactionId);
        } else {
            // Handle expected errors
            ChargeRequest.Error error = posClient.parseChargeError(data);
            EventEmitterModule.emitEvent("PaymentFailure" + error.code + error.debugDescription);
            Toast.makeText(this, "Success" + "Error" + error.code + error.debugDescription, Toast.LENGTH_LONG).show();
//            AlertDialogHelper.showDialog(this,
//                    "Error" + error.code,
//                    "Client transaction ID: "
//                            + error.debugDescription);
        }
        finish();
//        return;
    }
}
