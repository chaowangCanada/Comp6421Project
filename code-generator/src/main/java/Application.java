import java.io.File;

public class Application {

    public static String option = "";

    public static void main(String[] args) {
        File inFile = null;
        if (0 < args.length) {
            try {
                inFile = new File(args[0]);
                CodeGeneratorDriver driver = new CodeGeneratorDriver();
                option = args[1];
                driver.run(inFile, option != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }

    }
}