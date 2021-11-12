# Έκδοση σχολικής κάρτας COVID19

Πρόκειται για μια μικρή εφαρμογή που αναλαμβάνει να αυτοματοποιήσει την έκδοση σχολικής κάρτας COVID19.
Χρησιμοποιεί τους κωδικούς TaxisNet (ΓΓΠΣΔΔ) του γονέα ή κηδεμόνα και στη συνέχεια εισάγει τα δεδομένα του παιδιού.
Εκδίδει πάντα ΑΡΝΗΤΙΚΟ αποτέλεσμα COVID19 με την τρέχουσα ημερομηνία, καθώς αυτή είναι η συνηθέστερη περίπτωση.
Φτάνει μέχρι το στάδιο της εκτύπωσης.

Στηρίζεται σε Selenium και χρησιμοποιεί ChromeDriver ή GeckoDriver. 
Απαιτείται η ύπαρξη Java και Chrome ή Chromium ή Firefox στον υπολογιστή σας, 
και η εγκατάσταση του [ChromeDriver](https://chromedriver.chromium.org/downloads)
ή του [GeckoDriver](https://github.com/mozilla/geckodriver/releases).

Μπορείτε να κατεβάσετε ένα προετοιμασμένο εκτελέσιμο εδω: https://github.com/yannisf/schoolcard/releases/download/v3.1/schoolcard.jar

**Σημαντικό**: Τα προσωπικά δεδομένα σας και του παιδιού μένουν **ΠΑΝΤΑ** στον υπολογιστή σας και δεν αποθηκεύονται 
σε καμιά περίπτωση **ΠΟΥΘΕΝΑ** αλλού. Ο κώδικας είναι ανοιχτός, μπορεί να επιθεωρηθεί σχετικά και να χτιστεί τοπικά.

## Περιβάλλον

Η εφαρμογή χρειάζεται τα διαπιστευτήρια (κωδικούς) TaxisNet του προσώπου που εκδίδει τη σχολική κάρτα.
Οι κωδικοί πρέπει να βρίσκονται σε ένα αρχείο σε μια γραμμή με τη μορφή:
```
username:password
```

Τα στοιχεία του σχολείου και του παιδιού πρέπει και αυτά να είναι σέ ένα αρχείο, σε μια γραμμή, το καθένα με την εξής μορφή:

```
ΠΕΡΙΦΕΡΕΙΑ:ΠΕΡΙΦΕΡΙΑΚΗ ΕΝΟΤΗΤΑ:ΔΗΜΟΣ:ΚΑΤΗΓΟΡΙΑ:ΤΥΠΟΣ:ΣΧΟΛΕΙΟ
ΟΝΟΜΑ:ΕΠΙΘΕΤΟ:ΗΜΕΡΑ_ΓΕΝΝΗΣΗΣ:ΜΗΝΑΣ_ΓΕΝΝΗΣΗΣ:ΧΡΟΝΙΑ_ΓΕΝΝΗΣΗΣ:ΑΜΚΑ
```

### Παράδειγμα

**Αρχείο κωδικών**: `/home/user/taxisnet.txt`
```
myuser:secret123
```

**Αρχείο παιδιού**: `/home/user/document/giorgos.covid`

```
ΑΤΤΙΚΗΣ:ΚΕΝΤΡΙΚΟΥ ΤΟΜΕΑ ΑΘΗΝΩΝ:ΑΘΗΝΑΙΩΝ:Γυμνάσια:Ημερήσιο Γυμνάσιο:1ο ΗΜΕΡΗΣΙΟ ΓΥΜΝΑΣΙΟ ΑΘΗΝΩΝ
ΓΙΩΡΓΟΣ:ΠΑΠΑΔΟΠΟΥΛΟΣ:10:01:2005:10010501234
```

Τα στοιχεία του σχολείου πρέπει να αναγράφονται **ΑΚΡΙΒΩΣ** όπως παρουσιάζονται στην φόρμα του edupass.gov.gr. 

## Εκτέλεση

    $ java -jar schoolcard.jar -g /home/user/taxisnet.txt /home/user/document/giorgos.covid

Αν ο ChromeDriver δεν είναι στο PATH, πριν την παραπάνω εντολή δηλώστε στη μεταβλητή περιβάλλοντος `CHROME_DRIVER` που βρίσκεται:

    $ export CHROME_DRIVER=/home/user/bin/chromedriver

### Περισσότερες επιλογές

Η εκτέλεση της εφαρμογής μπορεί να παραμετροποιηθεί από τη γραμμή εντολών με τις παρακάτω επιλογές:

* `-g`: Το αρχείο (πλήρης διαδρομή) που περιέχει τα διαπιστευτήρια. Σε περίπτωση που δε δοθεί, αναζητούνται στο `$ΗΟΜΕ/.gsis`. 
* `-f`: Ενεργοποιεί τον GeckoDriver για χρήση με Firefox. Προαπαιτείται η ύπαρξη Firefox, η εγκατάσταση του GeckoDriver και η ύπαρξή τους στο PATH.
* `-c`: Ενεργοποιεί τον ChromeDriver. Πρόκειται για την προεπιλογή, οπότε δε θα χρειαστεί να χρησιμοποιήσετε αυτή την επιλογή αν θέλετε Chrome. Απλά φροντίστε να έχετε στο PATH τον ChromeDriver.
* `-i`: Η προεπιλογή της εφαρμογής είναι να συμπληρώνει τη σχετική φόρμα, αλλά να μην την εκδίδει,
καθώς ίσως θέλετε να αναθεωρήσετε κάποια επιλογή (π.χ. ημερομηνία test, ΘΕΤΙΚΟ αντί για αρνητικό κτλ). 
Ενεργοποιώντας αυτή την επιλογή, η φόρμα υποβάλλεται και το πιστοποιητικό εκδίδεται.
* `-p`: Υποβάλει και εκτυπώνει το πιστοποιητικό. 
* `-v`: Παρέχει διαγνωστικές πληροφορίες σχετικά με την εκτέλεση της εφαρμογής.
* `-h`: Παρουσιάζει μια σύνοψη των επιλογών της εφαρμογής
* `-w`: Καθώς η εφαρμογή πραγματοποιεί δικτυακές κλήσεις, 
κάποιες ίσως καθυστερήσουν περισσότερο από το αναμενόμενο και η εφαρμογή επιστρέψει σφάλμα.
Στην περίπτωση αυτή, μπορείτε να ξαναδοκιμάσετε χρησιμοποιώντας αυτή την επιλογή και δίνοντας 
ένα διάστημα αναμονής μεταξύ κλήσεων μεγαλύτερο από το προκαθορισμένο, που είναι 500 (millisecond).
Για παράδειγμα, δοκιμάστε 5000, δηλαδή 5 δευτερόλεπτα. Όλα θα είναι πιο αργά, 
αλλά είναι πιθανότερο να πάρετε αποτέλεσμα.

#### Σύνθετο παράδειγμα

    $ java -jar schoolcard.jar -f -w 2000 -p /home/user/document/giorgos.covid

Η παραπάνω εντολή:
* θα ενεργοποιήσει τον GeckoDriver για εκτέλεση με Firefox (`-f`)
* θα αναζητήσει κωδικούς στο $HOME/.gsis (δεν έχει οριστεί θέση `-g`)
* θα περιμένει 2 δευτερόλεπτα μεταξύ δικτυακών κλήσεων (`-w 2000`)
* θα εκδόσει και εκτυπώσει πιστοποιητικό (`-p`)

## Προσοχή: Μετάβαση από παλιότερες εκδόσεις
Πλέον δεν είναι απαραίτητο το πατρώνυμο και το μητρώνυμο στα στοιχεία του παιδιού και έχουν αφαιρεθεί από το σχετικό αρχείο. Ενημερώστε κατάλληλα. 

## Δημιουργία jar (για προγραμματιστές)

    $ mvn package

Το εκτελέσιμο jar θα δημιουργηθεί στο project directory με όνομα `schoolcard-jar-with-dependencies.jar`. 
