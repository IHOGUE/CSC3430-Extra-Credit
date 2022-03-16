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
    public Job max; // keeps track of what job has the maximum weight for the currently selected job (the job one index below or the current job + job.p)
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
    public int GetStart(){
        return timeStart;
    }
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
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void main(String[] args) {
        GridVariables gridVariables = new GridVariables();
        Map<String, String> settingsMap = new HashMap<>();
        String[] settingNames = new String[] {"DISPLAY_GRID", "GRID_COLOR","LOAD_FILE_NAME","NUMBER_OF_JOBS","MAXIMUM_TIME","MAXIMUM_WEIGHT"};
        for (String i : settingNames){
            settingsMap.put(i,null);
        }

        Random rand = new Random();
        boolean color = false;
        boolean displayGrid = true;

        long startTime = System.currentTimeMillis();
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

        HashMap<Job, Integer> MaxMap = new HashMap<>( gridVariables.numJobs);
       if (settingsMap.get("LOAD_FILE_NAME") == null){
           // easy random job initiation
           String name = "";
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
               temp.name = String.valueOf(name);
               // add job to array of jobs
               Jobs[i] = temp;
           }
       }
        /*Jobs[0].timeStart = 2;
        Jobs[1].timeStart = 5;
        Jobs[2].timeStart = 0;
        Jobs[3].timeStart = 6;
        Jobs[4].timeStart = 14;

        Jobs[0].timeEnd = 20;
        Jobs[1].timeEnd = 16;
        Jobs[2].timeEnd = 17;
        Jobs[3].timeEnd = 16;
        Jobs[4].timeEnd = 18;

        Jobs[0].weight = 4;
        Jobs[1].weight = 4;
        Jobs[2].weight = 10;
        Jobs[3].weight = 5;
        Jobs[4].weight = 7;*/

        // O(nlog(n))
        //Arrays.sort(Jobs, Comparator.comparing(Job::GetStart));
        // ^ this puts in nice order, but how much time complexity added? adds 7.2 milliseconds at 10,000 samples
        // O(nlog(n))
        Arrays.sort(Jobs, Comparator.comparing(Job::GetEnd));

        // --------------------------------------------------------------------------------------------find p?
        // better way to do this?
        // O(n^2) but in practice will be way less, closer to O(n)
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
        int numMax = 0;
        Job jobMax = null;
        List<String> solutionPath = new ArrayList<>(); // keeps track of jobs to take for maximum weight
        MaxMap.put(null, 0); // if null is sent into map, returns max value of 0
        int max1 = 0; // current job value + job.p max value
        int max2 = 0; // 1 index lower than current job's max value
        for (int j = 1; j <  gridVariables.numJobs +1; j++){
            if (MaxMap.get(Jobs[j-1].p) == null){ // max is zero is j doesn't have a compatible index
                max1 = 0;
            } else{
                max1 = MaxMap.get(Jobs[j-1].p);
            }
            if (j >=2){
                max2 = MaxMap.get(Jobs[j-2]);
            }
            int sum = 0;
//------------------------------this goes through each job's max, and if it includes itself, marks it
            if (Jobs[j-1].weight + max1 > max2){
                sum = Jobs[j-1].weight + max1;
                Jobs[j-1].max = Jobs[j-1].p;
                Jobs[j-1].usesSelf = true;
            } else{
                sum = max2;
                if (j >= 2){ // <- maybe not necessary
                    Jobs[j-1].max = Jobs[j-2];
                }
            }
            // total max
            MaxMap.put(Jobs[j-1],sum);
            if(sum > numMax){
                numMax = sum;
                jobMax = Jobs[j-1];
            }
        }
        String message = "";
        //------------------------------------------------------------------------- print jobs
        Print("For jobs:");
        Map<String,Job> jobMap = new HashMap<>();
        for (Job i : Jobs){
            i.Print();
            jobMap.put(i.name, i);
        }
        // temp traverses the path of greatest weight
        Job temp = jobMax;
        while(temp != null){
            if (temp.usesSelf) {        // complexity O(n)
                solutionPath.add(temp.name);
            }
            temp = temp.max;
        }
        message += "\n" + String.valueOf(numMax) + " is the highest weight sum possible by taking job";
        if (solutionPath.size() > 1){
            message += 's';
        }
        //Collections.sort(solutionPath); // sort alphabetically [time complexity?]
        Print(message + " " + String.valueOf(solutionPath));
        for (String s : solutionPath) {
            jobMap.get(s).Print();
        }

        long endTime = System.currentTimeMillis();
        long fin = endTime - startTime;
        Print("\n"+fin+" milliseconds"); //-------------------------------------   time

        //                  Grid Printer
        if (displayGrid){
            String[][] gridArray = new String[ gridVariables.timeMax+1][ gridVariables.numJobs+1];
            // initialize bottom row of grid, the x axis of time
            for (int i = 0; i <= gridVariables.timeMax ; i++){
                gridArray[i][0] = String.valueOf(i);
            }
            int count =  gridVariables.numJobs;
            for (Job j: Jobs){
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
                    //System.out.printf("%-6s", "[]");
                } else {
                    if (y > 0 && solutionPath.contains(gridArray[x][y])) {        //y > 1 && solutionPath.contains(Jobs[y-1].name
                        if (Objects.equals(currentBox, gridArray[x][y])){
                            gridDisplay.append(String.format("%-20s", ANSI_YELLOW_BACKGROUND  + ANSI_BLUE + gridArray[x][y] + ANSI_RESET));
                            //System.out.printf("%-20s", ANSI_BLACK_BACKGROUND + ANSI_WHITE + gridArray[x][y] + ANSI_RESET);
                        } else{
                            gridDisplay.append(String.format("%-16s", ANSI_YELLOW_BACKGROUND  + ANSI_BLUE + gridArray[x][y]));
                            //System.out.printf("%-16s", ANSI_BLACK_BACKGROUND + ANSI_WHITE + gridArray[x][y]);
                        }
                        currentBox = gridArray[x][y];
                        currentX = x;
                    } else if(Objects.equals(currentBox, gridArray[currentX][y])){
                        gridDisplay.append(String.format("%-16s", ANSI_YELLOW_BACKGROUND + ANSI_BLUE + gridArray[x][y]));
                        //System.out.printf("%-16s", ANSI_BLACK_BACKGROUND + ANSI_WHITE + gridArray[x][y]);
                    }else {
                        gridDisplay.append(String.format("%-10s", ANSI_RESET + gridArray[x][y]));
                        //System.out.printf("%-10s", ANSI_RESET + gridArray[x][y]);
                    }
                }
            }
            //System.out.println("");
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
                                //System.out.println("Start time " + gridVariables.jobInput.peek().timeEnd + " versus end time " + gridVariables.jobInput.peek().timeStart);
                                System.exit(1);
                            }
                            else if (gridVariables.jobInput.peek().timeEnd == gridVariables.jobInput.peek().timeStart){
                                System.out.println("Error: job " + gridVariables.jobInput.size() + " start time and end time are equal");
                                //System.out.println("Start time " + gridVariables.jobInput.peek().timeEnd + " equals end time " + gridVariables.jobInput.peek().timeStart);
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

                if ( gridVariables.numJobs > 26){
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