# Έκδοση σχολικής κάρτας COVID19

Πρόκειται για μια μικρή εφαρμογή που αναλαμβάνει να αυτοματοποιήσει την έκδοση σχολικής κάρτας COVID19.
Χρησιμοποιεί τους κωδικούς TaxisNet (GSIS ή ΓΓΠΣ) του γονέα ή κηδεμόνα και στην συνέχεια εισάγει τα δεδομένα του παιδιού.
Εκδίδει πάντα ΑΡΝΗΤΙΚΟ αποτέλεσμα COVID19, καθώς αυτή είναι η συνηθέστερη περίπτωση.

Στηρίζεται σε Selenium και χρησιμοποιεί ChromeDriver. 
Απαιτείται η ύπαρξη Java και Chrome ή Chromium στον υπολογιστή σας, και η εγκατάσταση του [ChromeDriver](https://chromedriver.chromium.org/downloads).

## Περιβάλλον

Η εφαρμογή χρειάζεται τους κωδικούς TaxisNet του προσώπου που εκδίδει τη σχολική κάρτα.
Οι κωδικοί πρέπει να βρίσκονται στο κατάλογο `HOME` του τρέχοντα χρήστη, 
σε ένα αρχείο με το όνομα `.gsis` (προσέξτε την τελεία μπροστά) σε μια γραμμή με τη μορφή:
```
username:password
```
**Σημείωση**:
- Πιθανώς οι Windows χρήστες θα δυσκολευτούν να βρουν το `HOME` και να φτιάξουν αρχείο που αρχίζει από `.`.
- Αν το username ή το password περιλαμβάνει `:` δε θα δουλέψει η εφαρμογή.

Τα στοιχεία του παιδιού πρέπει και αυτά να είναι σέ ένα αρχείο το οποίο θα πρέπει να τα περιλαμβάνει με την εξής μορφή:

```
ΟΝΟΜΑ:ΕΠΙΘΕΤΟ:ΟΝΟΜΑ_ΠΑΤΕΡΑ:ΟΝΟΜΑ_ΜΗΤΕΡΑΣ:ΗΜΕΡΑ_ΓΕΝΝΗΣΗΣ:ΜΗΝΑΣ_ΓΕΝΝΗΣΗΣ:ΧΡΟΝΙΑ_ΓΕΝΗΣΣΗΣ:ΑΜΚΑ
```

#### Παράδειγμα
Αρχείο: `/home/user/document/giorgos.covid`

```
ΓΙΩΡΓΟΣ:ΠΑΠΑΔΟΠΟΥΛΟΣ:ΝΙΚΟΛΑΟΣ:ΜΑΡΙΑ:10:01:2005:10010501234
```

## Εκτέλεση

    $ java -jar schoolcard.jar /home/user/document/giorgos.covid

Αν ο ChromeDriver είναι δεν είναι στο PATH, πριν την παραπάνω εντολή δηλώστε στη μεταβλητή περιβάλλοντος `CHROME_DRIVER` που βρίσκεται:

    $ export CHROME_DRIVER=/home/user/bin/chromedriver

## Δημιουργία jar (για προγραμματιστές)

    $ mvn package

Το εκτελέσιμο jar θα δημιουργηθεί στο project directory με όνομα `schoolcard-jar-with-dependencies.jar`. 