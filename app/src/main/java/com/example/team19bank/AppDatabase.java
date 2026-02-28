package com.example.team19bank;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// US5: Room gagnagrunnur - singleton mynstur svo aðeins eitt tilvik sé til í einu
@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE; // Eitt tilvik af gagnagrunni

    public abstract TransactionDao transactionDao(); // Aðgangur að millifærslu aðgerðum

    // Sækja (eða búa til) gagnagrunn tilvik
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "team19bank_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
