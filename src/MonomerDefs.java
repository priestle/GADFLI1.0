import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class MonomerDefs {

    // Variables
    ArrayList<Integer> monomerList = new ArrayList<>();

    // Constructor
    public MonomerDefs(String monomerDefinitionFileName) {

        try (Scanner inputF = new Scanner(Paths.get(monomerDefinitionFileName))) {
            while (inputF.hasNextLine()) {
                String row = inputF.nextLine();
                if (!row.isEmpty()) {
                    Integer monomer = Integer.parseInt(row.trim());
                    monomerList.add(monomer);
                }
            }
        }
        catch (Exception e) {
            Assertions.log("FATAL ERROR : Issue with loading monomer defiinitions file.");
            e.printStackTrace();
            System.exit(1);
        }
        Assertions.log("Read in " + monomerList.size() + " monomer definitions.");
    }

    // Gets
    // So that index-1 is in there because the array is zero based
    public int getNPFRMonomerNumber(int index) {
        return monomerList.get(index-1);
    }

    public int getIndexFromNPFRNumber(int npfrMonomer) {
        for (int i = 0; i < monomerList.size(); i++) {
            if (monomerList.get(i) == npfrMonomer) {
                return i;
            }
        }
        Assertions.log("FATAL ERROR : Issue with getting an index from a NPFR monomer number.");
        return -1;
    }

    public int getSize() {
        return this.monomerList.size();
    }

    // Sets

    // Methods
}
