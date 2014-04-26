package com.zomboto.offtheblock.Helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import com.zomboto.offtheblock.R;

public class NavigationHelper {
    public static void NavigateTo(FragmentManager manager, Fragment fragment)
    {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
