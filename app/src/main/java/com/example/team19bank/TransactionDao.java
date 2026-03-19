package com.example.team19bank;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// US5: DAO (Data Access Object) - skilgreinir aðgerðir á transaction töflunni
@Dao
public interface TransactionDao {

    // OnConflictStrategy.REPLACE kemur í veg fyrir crash ef sama færsla kemur aftur
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Transaction transaction);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Transaction> transactions);

    // LAGAÐ: Notum núna sourceAccount og destinationAccount!
    @Query("SELECT * FROM transactions WHERE sourceAccount = :accountNumber OR destinationAccount = :accountNumber ORDER BY id DESC")
    List<Transaction> getAllByAccount(String accountNumber);

    // Gott að hafa ef þú vilt hreinsa út gamlar færslur
    @Query("DELETE FROM transactions")
    void clearAll();
}