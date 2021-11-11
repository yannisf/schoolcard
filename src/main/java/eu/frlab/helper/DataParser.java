package eu.frlab.helper;

import eu.frlab.model.KidDataModel;
import eu.frlab.model.CredentialsDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class DataParser {

    private static final Logger LOG = LoggerFactory.getLogger(DataParser.class);

    private DataParser() {}

    public static KidDataModel parseKidData(Optional<String> kidsDataLocation) {
        KidDataModel kidDataModel = null;
        try {

            if (kidsDataLocation.isPresent()) {
                Path kidsDataPath = Paths.get(kidsDataLocation.get());
                if (Files.exists(kidsDataPath)) {
                    if (kidDataChecks(kidsDataPath)) {
                        String[] school = Files.readAllLines(kidsDataPath).get(0).split(":");
                        String[] kid = Files.readAllLines(kidsDataPath).get(1).split(":");
                        kidDataModel = new KidDataModel(kid, school);
                    } else {
                        LOG.error("Tο αρχείο δεδομένων του παιδιού δεν έχει την αναμενόμενη μορφή");
                        System.exit(1);
                    }
                } else {
                    LOG.error("Δε βρέθηκε το αρχείο δεδομένων του παιδιού στη θέση [{}]", kidsDataPath);
                    System.exit(1);
                }
            } else {
                LOG.error("Λείπει η παράμετρος με το αρχείο δεδομένων του παιδιού");
                System.exit(1);
            }
        } catch (IOException ioe) {
            LOG.error("Γενικό σφάλμα", ioe);
            System.exit(1);
        }

        return kidDataModel;
    }

    private static boolean kidDataChecks(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        return (lines.size() == 2 && lines.get(0).split(":").length == 6 && lines.get(1).split(":").length == 6);
    }

    public static CredentialsDataModel parseCredentialsData(Optional<String> credentialsDataLocation) {
        CredentialsDataModel credentialsDataModel = null;
        try {
            if (credentialsDataLocation.isPresent()) {
                Path credentialsDataPath = Paths.get(credentialsDataLocation.get());
                if (Files.exists(credentialsDataPath)) {
                    LOG.info("Χρησιμοποίηση αρχείου διαπιστευτηρίων [{}]", credentialsDataLocation.get());
                    if (credentialsDataChecks(credentialsDataPath)) {
                        String[] credentials = Files.readAllLines(credentialsDataPath).get(0).split(":");
                        credentialsDataModel = new CredentialsDataModel(credentials);
                    } else {
                        LOG.error("Tο αρχείο των διαπιστευτηρίων ΓΓΠΣ (TaxisNet) δεν έχει την αναμενόμενη μορφή");
                        System.exit(1);
                    }
                } else {
                    LOG.error("Δε βρέθηκε το αρχείο των διαπιστευτηρίων στη θέση [{}]", credentialsDataPath);
                    System.exit(1);
                }
            } else {
                String defaultLocation = String.format("%s%s%s", System.getenv("HOME"), File.separator, ".gsis");
                credentialsDataModel = parseCredentialsData(Optional.of(defaultLocation));
            }
        } catch (IOException ioe) {
            LOG.error("Γενικό σφάλμα", ioe);
            System.exit(1);
        }

        return credentialsDataModel;
    }

    private static boolean credentialsDataChecks(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        return (lines.size() == 1 && lines.get(0).split(":").length == 2);
    }

}
