package eu.frlab.helper;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CliHelper {

    private final Options options;
    private final ParsedOptions parsedOptions;

    public CliHelper(String[] args) {
        Options optionsTemp = new Options();
        optionsTemp.addOption(new Option("h", "help", false, "Εκτύπωση αυτού του μηνύματος"));
        optionsTemp.addOption(new Option("g", "gsis", true, "Αρχείο με διαπιστευτήρια (κωδικούς) ΓΓΠΣΔΔ (προεπιλογή: $ΗΟΜΕ/.gsis)"));
        optionsTemp.addOption(new Option("i", "issue", false, "Έκδοση πιστοποιητικού (όχι προεπιλεγμένο)"));
        optionsTemp.addOption(new Option("p", "print", false, "Εκτύπωση πιστοποιητικού, υπονοείται η έκδοση (όχι προεπιλεγμένο)"));
        optionsTemp.addOption(new Option("f", "firefox", false, "Χρήση Firefox, προαπαιτείται εγκατεστημένος GeckoDriver"));
        optionsTemp.addOption(new Option("c", "chrome", false, "Χρήση Chrome, προεπιλογή (προαπαιτείται εγκατεστημένος ChromeDriver)"));
        optionsTemp.addOption(new Option("w", "wait", true, "Αναμονή μεταξύ δικτυακών κλήσεων σε millisecond (προεπιλογή: 500)"));
        optionsTemp.addOption(new Option("v", "verbose", false, "Επιπλέον διαγνωστικές πληροφορίες κατά την εκτέλεση"));
        this.options = optionsTemp;

        this.parsedOptions = ParsedOptions.create(this.getOptions(), args);
    }

    public Options getOptions() {
        return this.options;
    }

    public ParsedOptions getParsedOptions() {
        return this.parsedOptions;
    }

    public void handleHelp() {
        if (this.parsedOptions.isHelp()) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar schoolcard.jar [επιλογές] <αρχείο_δεδομένων_παιδιού>", "", getOptions(), "\nΑναλυτικές πληροφορίες στο: https://github.com/yannisf/schoolcard/blob/master/README.md");
            System.exit(0);
        }
    }

}
