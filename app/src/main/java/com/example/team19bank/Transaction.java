package com.example.team19bank;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// US5: Room entity klasi - tafla í SQLite gagnagrunninum
// Geymir upplýsingar um hverja millifærslu (sendandi, móttakandi, upphæð, tilvísun, tímasetning)
@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public int id; // Sjálfvirkt auðkenni

    public String sender;    // Notandanafn sendanda
    public String receiver;  // Móttakandi (reikningur/nafn)
    public long amount;      // Upphæð í ISK
    public String reference; // Tilvísun (valfrjálst)
    public long timestamp;   // Tímasetning í millisekúndum

    public Transaction(String sender, String receiver, long amount, String reference, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.reference = reference;
        this.timestamp = timestamp;
    }
}
