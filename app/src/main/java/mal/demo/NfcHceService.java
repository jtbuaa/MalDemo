package mal.demo;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

/**
 * Created by jingtao on 16-3-15.
 */
public class NfcHceService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        return null;
    }

    @Override
    public void onDeactivated(int reason) {
        Activity1.startActivity(getApplicationContext(), Activity1.BY_NFC);
    }
}