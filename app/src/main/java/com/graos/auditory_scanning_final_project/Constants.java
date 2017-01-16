package com.graos.auditory_scanning_final_project;

import android.provider.BaseColumns;

/**
 * Created by GG on 16/01/2017.
 */

// **************** CREATE CONTACTS TABLE *********************
public final class Constants
{
    private Constants(){
        throw  new AssertionError(" Can't create constants class");
    }

    public static abstract  class Items implements BaseColumns {
        public static final String TABLE_NAME = "itemTable";
        public static final String ITEM = "item";
    }
}
