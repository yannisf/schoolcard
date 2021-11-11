package eu.frlab.helper;

import org.apache.commons.cli.*;

import java.util.Optional;

public class ParsedOptions {

    private final boolean help;
    private final boolean issue;
    private final boolean print;
    private final boolean chrome;
    private final boolean firefox;
    private final long wait;
    private final Optional<String> credentials;
    private final Optional<String> kid;

    private ParsedOptions(boolean help, boolean issue, boolean print, boolean chrome, boolean firefox, String wait, String credentials, String kid) {
        this.help = help;
        this.issue = issue || print;
        this.print = print;
        this.chrome = chrome;
        this.firefox = firefox;
        this.wait = (wait == null || wait.isEmpty()) ? 500 : Long.parseLong(wait);
        this.credentials = Optional.ofNullable(credentials);
        this.kid = Optional.ofNullable(kid);
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isIssue() {
        return issue;
    }

    public boolean isPrint() {
        return print;
    }

    public boolean isChrome() {
        return chrome;
    }

    public boolean isFirefox() {
        return firefox;
    }

    public long getWait() {
        return wait;
    }

    public Optional<String> getCredentials() {
        return credentials;
    }

    public Optional<String> getKid() {
        return kid;
    }

    @Override
    public String toString() {
        return "ProgramOptions{" +
                "help=" + help +
                ", issue=" + issue +
                ", print=" + print +
                ", creds='" + credentials + '\'' +
                ", kid='" + kid + '\'' +
                '}';
    }

    public static ParsedOptions create(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return new ParsedOptions(
                cmd.hasOption("h"),
                cmd.hasOption("i") || cmd.hasOption("p"),
                cmd.hasOption("p"),
                cmd.hasOption("c") || !cmd.hasOption("f"),
                cmd.hasOption("f"),
                cmd.getOptionValue("w"),
                cmd.getOptionValue("g"),
                !cmd.getArgList().isEmpty() ? cmd.getArgList().get(0) : null
        );
    }

}
