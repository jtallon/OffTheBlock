package com.zomboto.offtheblock;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import com.zomboto.offtheblock.Fragments.AreaCodeList;
import com.zomboto.offtheblock.Fragments.LogList;

public class ActivityMain extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        switchFragment(new AreaCodeList());

        findViewById(R.id.blockedCodesTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new AreaCodeList());
            }
        });

        findViewById(R.id.logsTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new LogList());
            }
        });
    }

    private void switchFragment(Fragment fragment)
    {
        FragmentTransaction ft;
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment);
        ft.commit();
    }
}
