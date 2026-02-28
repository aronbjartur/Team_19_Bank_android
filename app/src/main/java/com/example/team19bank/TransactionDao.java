package com.example.team19bank;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// US5: DAO (Data Access Object) - skilgreinir aðgerðir á transaction töflunni
@Dao
public interface TransactionDao {

    // Setja nýja millifærslu í gagnagrunn
    @Insert
    void insert(Transaction transaction);

    // Sækja allar millifærslur fyrir notanda (sent og móttekið), nýjustu fyrst
    @Query("SELECT * FROM transactions WHERE sender = :username OR receiver = :username ORDER BY timestamp DESC")
    List<Transaction> getAllByUser(String username);
}
