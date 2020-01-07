public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            System.out.println("Normalizing entries in file " + args[0] + "...");
        } else {
            throw new IllegalArgumentException("One filepath argument required");
        }


        final Normalizer normalizer = new Normalizer();
        normalizer.createNormalizeEntries(args[0]);

        System.out.println("normalized_entries.csv");
    }

}
