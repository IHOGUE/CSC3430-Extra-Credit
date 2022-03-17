import java.net.URISyntaxException;
import java.util.*;
import java.io.File;  // Import the File class <- necessary?
import java.io.FileNotFoundException;  // Import this class to handle errors <- necessary?


class Job{
    public int timeStart;
    public int timeEnd;
    public int weight;
    public String name;
    public Job p; // first compatible job available
    public Job maxWeightJob; // keeps track of what job has the maximum weight for the currently selected job (the job one index below or the current job + job.p)
    public boolean usesSelf = false; // tracks whether or not the current job is included in it's maximum weight
    public Job (int a, int b, int c){
        this.timeStart = a;
        this.timeEnd = b;
        this.weight = c;
    }

    public Job() {

    }

    public void Print(){
        System.out.println(name + ", " + timeStart + ", " + timeEnd + ", " + weight);
    }
    public int GetEnd(){
        return timeEnd;
    }
    public String GetName(){return name;}
}
class GridVariables{
    public  String fileName;
    public  int numJobs = 0;
    public  int timeMax = 20;
    public  int weightMax = 10;
    public  Stack<Job> jobInput = new Stack<>(); // able to take jobs from file to store in array later
    public boolean inputFromFile = false; // bool to check if jobs are being read or random generated
}

public class WeightedIntervalS {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    public static void main(String[] args) {
        GridVariables gridVariables = new GridVariables();
        Map<String, String> settingsMap = new HashMap<>();
        String[] settingNames = new String[] {"DISPLAY_GRID", "GRID_COLOR","PRINT_JOBS","LOAD_FILE_NAME","NUMBER_OF_JOBS","MAXIMUM_TIME","MAXIMUM_WEIGHT"};
        for (String i : settingNames){
            settingsMap.put(i,null);
        }

        Random rand = new Random();
        boolean color = false;
        boolean displayGrid = true;
        boolean printJobs = false;


        try{
            String name = "settings.txt";
            File local = new File(WeightedIntervalS.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String source = local.getParent() + File.separator + name;
            File address = new File(source);
            String scannerInput;
            Scanner fileRead = new Scanner(address);

            while (fileRead.hasNextLine()) {

                scannerInput = fileRead.nextLine();
                Scanner lineRead = new Scanner(scannerInput);
                String currentName = "";
                lineRead.useDelimiter("=");

                while (lineRead.hasNextLine()){
                    try{
                        scannerInput = lineRead.next();
                    }
                    catch (Exception e) {
                        Print("Error: setting " + currentName + " left blank");
                        System.exit(1);
                    }
                    scannerInput = scannerInput.strip();
                    if (settingsMap.containsKey(scannerInput)){
                        currentName = scannerInput;
                    }
                    else if (scannerInput.startsWith("//")){
                        continue;
                    }
                    if (!settingsMap.containsKey(scannerInput)){
                        if (scannerInput.equals("\"\"")){
                            settingsMap.put(currentName,null);
                        }
                        else if (scannerInput.contains("\"")){
                            scannerInput = scannerInput.replaceAll("\"","");
                            settingsMap.put(currentName,scannerInput);
                        }
                        else{
                            settingsMap.put(currentName,scannerInput);
                        }
                    }
                }
            }
            fileRead.close();
        }catch(FileNotFoundException e) {
            System.out.println("Error: settings.txt not found");
        } catch (URISyntaxException e) {
            System.out.println("URI Syntax Exeption");
            e.printStackTrace();
        }

        for (String i : settingNames){
            switch (i){ // should i convert to "enhanced" switch?
                case "DISPLAY_GRID":
                    if (Objects.equals(settingsMap.get(i), "false") || Objects.equals(settingsMap.get(i), "true")){
                        displayGrid = Boolean.parseBoolean((settingsMap.get(i)));
                    }
                    else{
                        Print("Error: value for setting " + i + " \"" + settingsMap.get(i) + "\" must be true or false");
                        System.exit(1);
                    }
                    break;
                case "GRID_COLOR":
                    if (Objects.equals(settingsMap.get(i), "false") || Objects.equals(settingsMap.get(i), "true")){
                        color = Boolean.parseBoolean((settingsMap.get(i)));
                    }
                    else{
                        Print("Error: value for setting " + i + " \"" + settingsMap.get(i) + "\" must be true or false");
                        System.exit(1);
                    }
                    break;
                case "PRINT_JOBS":
                    if (Objects.equals(settingsMap.get(i), "false") || Objects.equals(settingsMap.get(i), "true")){
                        printJobs = Boolean.parseBoolean((settingsMap.get(i)));
                    }
                    else{
                        Print("Error: value for setting " + i + " \"" + settingsMap.get(i) + "\" must be true or false");
                        System.exit(1);
                    }
                    break;
                case "LOAD_FILE_NAME":
                    gridVariables.fileName = settingsMap.get(i);
                    if (gridVariables.fileName != null){
                        gridVariables.inputFromFile = true;
                    }
                    break;
                case "NUMBER_OF_JOBS":
                    try{
                        gridVariables.numJobs = Integer.parseInt(settingsMap.get(i));
                    } catch (NumberFormatException e) {
                        Print("Error: value for setting " + i + " \"" + settingsMap.get(i) + "\" is not a number");
                        System.exit(1);
                    }
                    break;
                case "MAXIMUM_TIME":
                    try{
                        gridVariables.timeMax = Integer.parseInt(settingsMap.get(i));
                    } catch (NumberFormatException e) {
                        Print("Error: value for setting " + i + " \"" + settingsMap.get(i) + "\" is not a number");
                        System.exit(1);
                    }
                    break;
                case "MAXIMUM_WEIGHT":
                    try{
                        gridVariables.weightMax = Integer.parseInt(settingsMap.get(i));
                    } catch (NumberFormatException e) {
                        Print("Error: value for setting " + i + " \"" + settingsMap.get(i) + "\" is not a number");
                        System.exit(1);
                    }
                    break;
            }
            if (gridVariables.inputFromFile){
                break;
            }
        }

        if (settingsMap.get("LOAD_FILE_NAME") != null){
            FileRead(gridVariables);
        }

        Job[] Jobs = new Job[gridVariables.numJobs];

        int counter = 0;
        while (!gridVariables.jobInput.empty()){
            Jobs[counter] = gridVariables.jobInput.pop();
            counter++;
        }

       if (settingsMap.get("LOAD_FILE_NAME") == null){
           // easy random job initiation
           String name;
           for (int i = 0; i <  gridVariables.numJobs; i++) {
               if ( gridVariables.numJobs > 26){
                   name = "1";
               } else{
                   name = "";
               }
               int intToChar = 65 + i;
               int wrapAround = (26 * (i/26)); // 90 is Z, this wraps around to A after Z
               if (wrapAround > 0){
                   intToChar = intToChar - wrapAround;
                   wrapAround = wrapAround - 25*(i/26);
                   name = String.valueOf(wrapAround + 1);
               }
               char letter = (char)intToChar;
               name += letter;

               // random start time for a
               int a = rand.nextInt( gridVariables.timeMax);
               // generates random length using the subtraction of the start time from maximum time
               int len = rand.nextInt( gridVariables.timeMax - a) + 1;
               // start time plus random length equals end time
               int b = a + len;
               // random weight
               int c = rand.nextInt( gridVariables.weightMax) + 1;
               Job temp = new Job(a,b,c); // 2*i, 2*i+3, i+1
               temp.name = name;
               // add job to array of jobs
               Jobs[i] = temp;
           }
       }
       
        Job[] unsortedJobs = Jobs.clone(); // I assume 0(n)
        Arrays.sort(unsortedJobs, Comparator.comparing(Job::GetName)); // O(nlog(n))
        long startTime = System.currentTimeMillis();
        Arrays.sort(Jobs, Comparator.comparing(Job::GetEnd)); // O(nlog(n))
        // -------------------------find p-------------------------------------------------------------------
        // better way to do this?
        // I think O(n^2) but in practice will be way less, closer to O(n)
        for(int i = 0; i <  gridVariables.numJobs; i++){
            int start = Jobs[i].timeStart;
            for(int j = i ; j > -1; j--){
                if (Jobs[j].timeEnd < start){   // breaks at first compatible index found
                    Jobs[i].p = Jobs[j];
                    break;
                } // otherwise no compatible index was found
            }
        }
        //  heart of the algorithm: this builds bottom to top the max weight possible for each job into MaxMap
        HashMap<Job, Integer> MaxMap = new HashMap<>( gridVariables.numJobs);
        int numMax = 0;
        Job jobMax = null;
        List<String> solutionPath = new ArrayList<>(); // keeps track of jobs to take for maximum weight
        MaxMap.put(null, 0); // if null is sent into map, returns max value of 0
        int max1; // current job value + job.p  = max value
        int max2 = 0; // 1 index lower than current job's max value
        for (int j = 1; j <  gridVariables.numJobs + 1; j++){
            if (MaxMap.get(Jobs[j-1].p) == null){ // max is zero if j doesn't have a compatible index
                max1 = 0;
            } else{
                max1 = MaxMap.get(Jobs[j-1].p);
            }
            if (j >=2){
                max2 = MaxMap.get(Jobs[j-2]);
            }
            int sum;
//------------------------------this goes through each job's max, and if it includes itself, marks it
            if (Jobs[j-1].weight + max1 > max2){
                sum = Jobs[j-1].weight + max1;
                Jobs[j-1].maxWeightJob = Jobs[j-1].p;
                Jobs[j-1].usesSelf = true;
            } else{
                sum = max2;
                Jobs[j-1].maxWeightJob = Jobs[j-2];
            }
            // records highest max so far
            MaxMap.put(Jobs[j-1],sum);
            if(sum > numMax){
                numMax = sum;
                jobMax = Jobs[j-1];
            }
        }
        long endTime = System.currentTimeMillis();
        long fin = endTime - startTime;
        String message = "";
        //------------------------------------------------------------------------- print jobs
        Print("For " + gridVariables.numJobs + " jobs:");
        Map<String,Job> jobMap = new HashMap<>();
        for (Job i : Jobs){
            if (printJobs){
                i.Print();
            }
            jobMap.put(i.name, i);
        }
        // temp traverses the path of greatest weight
        Job temp = jobMax;
        while(temp != null){
            if (temp.usesSelf) {        // complexity O(n)
                solutionPath.add(temp.name);
            }
            temp = temp.maxWeightJob;
        }
        message += "\n" + numMax + " is the highest weight sum possible by taking job";
        if (solutionPath.size() > 1){
            message += 's';
        }
        //Collections.sort(solutionPath); // sort alphabetically [time complexity?]
        Print(message + " " + solutionPath + "\n");
        for (String s : solutionPath) {
            jobMap.get(s).Print();
        }


        Print("\n"+fin+" milliseconds to sort jobs by end time, calculate every p value, and find value of max weight"); //-------------------------------------   time

        //                  Grid Printer
        if (displayGrid){
            String[][] gridArray = new String[ gridVariables.timeMax+1][ gridVariables.numJobs+1];
            // initialize bottom row of grid, the x axis of time
            for (int i = 0; i <= gridVariables.timeMax ; i++){
                gridArray[i][0] = String.valueOf(i);
            }
            int count =  gridVariables.numJobs;
            for (Job j: unsortedJobs){
                gridArray[j.timeEnd][count] = String.valueOf(j.name);
                gridArray[j.timeStart][count] = String.valueOf(j.name);
                for (int i = j.timeStart + 1; i < j.timeEnd; i++){
                    gridArray[i][count] = String.valueOf(j.weight);
                }
                count--;
            }

            StringBuilder gridDisplay = new StringBuilder();
            if (!color){
                GridBuilder(gridVariables.numJobs,gridVariables.timeMax,gridArray,gridDisplay);
            } else{
                GridBuilderColor(gridVariables.numJobs,gridVariables.timeMax,gridArray,gridDisplay, solutionPath);
            }
            StringBuilder border = new StringBuilder();
            border.append("-".repeat(Math.max(0, (gridVariables.timeMax * 6) + 2)));
            Print(border);
            Print(gridDisplay);
            Print(border);
        }
    }

    public static <T> void Print(T input){
        System.out.println(input);
    }
    public static void GridBuilder(int numJobs, int timeMax, String[][] gridArray, StringBuilder gridDisplay){
        for (int y = numJobs ; y > -1; y--){
            for (int x = 0; x <= timeMax; x++){
                if (gridArray[x][y] == null){
                    gridDisplay.append(String.format("%-6s", "  "));
                } else{
                    gridDisplay.append(String.format("%-6s", gridArray[x][y]));
                }
            }
            gridDisplay.append("\n");
        }
        gridDisplay.deleteCharAt(gridDisplay.length()-1);
    }
    public static void GridBuilderColor(int numJobs, int timeMax, String[][] gridArray, StringBuilder gridDisplay, List<String> solutionPath) {
        String currentBox = "";
        int currentX = 0;
        for (int y = numJobs; y > -1; y--) {
            for (int x = 0; x <= timeMax; x++) {
                if (gridArray[x][y] == null) {
                    gridDisplay.append(String.format("%-6s", "  "));
                } else {
                    if (y > 0 && solutionPath.contains(gridArray[x][y])) {
                        if (Objects.equals(currentBox, gridArray[x][y])){
                            gridDisplay.append(String.format("%-20s", ANSI_YELLOW_BACKGROUND  + ANSI_BLUE + gridArray[x][y] + ANSI_RESET));
                        } else{
                            gridDisplay.append(String.format("%-16s", ANSI_YELLOW_BACKGROUND  + ANSI_BLUE + gridArray[x][y]));
                        }
                        currentBox = gridArray[x][y];
                        currentX = x;
                    } else if(Objects.equals(currentBox, gridArray[currentX][y])){
                        gridDisplay.append(String.format("%-16s", ANSI_YELLOW_BACKGROUND + ANSI_BLUE + gridArray[x][y]));
                    }else {
                        gridDisplay.append(String.format("%-10s", ANSI_RESET + gridArray[x][y]));
                    }
                }
            }
            gridDisplay.append("\n");
        }
        gridDisplay.deleteCharAt(gridDisplay.length()-1);
    }
    public static void FileRead(GridVariables gridVariables){
        try{
            String name = gridVariables.fileName;
            File local = new File(WeightedIntervalS.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String source = local.getParent() + File.separator + name;
            File address = new File(source);
            String scannerInput;
            Scanner fileRead = new Scanner(address);
            int i = 0;

            while (fileRead.hasNextLine()) {

                scannerInput = fileRead.nextLine();
                Scanner lineRead = new Scanner(scannerInput);
                lineRead.useDelimiter(",");

                int fieldIndex = 0; // keeps track of what to put in Course object through the switch case

                while (lineRead.hasNextLine()){
                    scannerInput = lineRead.next();
                    scannerInput = scannerInput.strip();
                    if (scannerInput.matches(".*[a-zA-Z].*") && scannerInput.length() > 1){
                        if (gridVariables.jobInput.size() == 0){
                            System.out.println("Error: job 1 has a corrupt value " + scannerInput);
                        }
                        else{
                            System.out.println("Error: job " + gridVariables.jobInput.size() + " has a corrupt value " + scannerInput);
                        }
                        Print("Only integers allowed");
                        System.exit(1);
                    }
                    else if (scannerInput.contains(" ")){
                        System.out.println("Error: job 1 has a corrupt value " + scannerInput);
                        System.exit(1);
                    }
                    if (scannerInput.matches("[a-zA-Z+]")){ // this is to ignore name when pasting output into csv file
                        continue;
                    }
                    switch (fieldIndex){
                        case 0:
                            Job temp = new Job();
                            temp.timeStart = Integer.parseInt(scannerInput);
                            gridVariables.jobInput.push(temp);
                            if (gridVariables.inputFromFile){
                                gridVariables.numJobs++;
                            }
                            fieldIndex++;
                            break;
                        case 1:
                            gridVariables.jobInput.peek().timeEnd = Integer.parseInt(scannerInput);
                            if (gridVariables.jobInput.peek().timeEnd > gridVariables.timeMax){
                                gridVariables.timeMax = gridVariables.jobInput.peek().timeEnd;
                            }
                            else if (gridVariables.jobInput.peek().timeEnd < gridVariables.jobInput.peek().timeStart){
                                System.out.println("Error: job " + gridVariables.jobInput.size() + " ends before it starts");
                                System.exit(1);
                            }
                            else if (gridVariables.jobInput.peek().timeEnd == gridVariables.jobInput.peek().timeStart){
                                System.out.println("Error: job " + gridVariables.jobInput.size() + " start time and end time are equal");
                                System.exit(1);
                            }
                            fieldIndex++;
                            break;
                        case 2:
                            gridVariables.jobInput.peek().weight = Integer.parseInt(scannerInput);
                            fieldIndex++;
                            break;
                    }
                }
                if (gridVariables.numJobs > 26){
                    name = "1";
                } else{
                    name = "";
                }
                int intToChar = 65 + i;
                int wrapAround = (26 * ((i)/26)); // 90 is Z, this wraps around to A after Z
                if (wrapAround > 0){
                    intToChar = intToChar - wrapAround;
                    wrapAround = wrapAround - 25*((i)/26);
                    name = String.valueOf(wrapAround + 1);
                }
                char letter = (char)intToChar;
                name += letter;
                gridVariables.jobInput.peek().name = name;
                i++;
            }
            fileRead.close();
        }catch(FileNotFoundException e) {
            String error = "File ";
            error += gridVariables.fileName + " not found";
            System.out.println(error);
            System.exit(1);
        } catch (URISyntaxException e) {
            System.out.println("URI Syntax Exeption");
            e.printStackTrace();
        }
    }
}