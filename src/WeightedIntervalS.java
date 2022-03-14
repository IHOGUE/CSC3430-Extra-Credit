import java.net.URISyntaxException;
import java.util.*;
import java.io.File;  // Import the File class <- neccessary?
import java.io.FileNotFoundException;  // Import this class to handle errors <- neccessary?

class Job{
    public int timeStart;
    public int timeEnd;
    public int weight;
    public String name;
    public Job p;
    public Job max;
    public boolean usesSelf = false;
    public Job (int a, int b, int c){
        this.timeStart = a;
        this.timeEnd = b;
        this.weight = c;
    }
    public void Print(){
        System.out.println(timeStart + ", " + timeEnd + ", " + weight + ", " + name);
    }
    public int GetEnd(){
        return timeEnd;
    }
    public int GetStart(){
        return timeStart;
    }
}

// store requests on csv file
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
        boolean color = false;
        try{
            String name = "test.csv";
            File CSV = new File(WeightedIntervalS.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String source = CSV.getParent() + File.separator + name;
            File address = new File(source);
            Scanner fileRead = new Scanner(address);
            while (fileRead.hasNextLine()) {
                String data = fileRead.nextLine();
                System.out.println(data);
            }
            fileRead.close();
        } catch(FileNotFoundException e) {
            System.out.println("The file indicated was not found.");
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            System.out.println("URI Syntax Error.");
            e.printStackTrace();
        }
        Print(5/90);
        long startTime = System.currentTimeMillis();
        Random rand = new Random();
        int numJobs = 5; //5    rand.nextInt(11) + 1
        int timeMax = 20;
        int weightMax = 10;
        HashMap<Job, Integer> MaxMap = new HashMap<>(numJobs + 1); // initial capacity good? size or size + 1?
        Job[] Jobs = new Job[numJobs];

        // easy job initiate
        String name = "";
        for (int i = 0; i < numJobs; i++) {
            // to give each job a name in alphabetical order...  ends at z, whatever ascii is past z is next job
            if (numJobs > 26){
                name = "1";
            } else{
                name = "";
            }
            int intToChar = 65 + i;
            int wrapAround = (26 * (i/26)); // 90 is Z, this wraps around to A after Z
            if (wrapAround > 0){
                intToChar = intToChar - wrapAround;
                wrapAround = wrapAround - 25;
                name = String.valueOf(wrapAround + 1);
            }
            char letter = (char)intToChar;
            name += letter;

            // random start time for a
            int a = rand.nextInt(timeMax);
            // generates random length using the subtraction of the start time from maximum time
            int len = rand.nextInt(timeMax - a) + 1;
            // start time plus random length equals end time
            int b = a + len;
            // random weight
            int c = rand.nextInt(weightMax) + 1;
            Job temp = new Job(2*i, 2*i+3, i+1); // 2*i, 2*i+3, i+1
            temp.name = String.valueOf(name); // not part of constructor, not sure if i'm keeping name variable
            // add job to array of jobs
            Jobs[i] = temp;
        }

        /*Jobs[0].timeStart = 19;
        Jobs[1].timeStart = 5;
        Jobs[2].timeStart = 2;
        Jobs[3].timeStart = 6;
        Jobs[4].timeStart = 14;

        Jobs[0].timeEnd = 20;
        Jobs[1].timeEnd = 16;
        Jobs[2].timeEnd = 17;
        Jobs[3].timeEnd = 16;
        Jobs[4].timeEnd = 18;

        Jobs[0].weight = 9;
        Jobs[1].weight = 3;
        Jobs[2].weight = 10;
        Jobs[3].weight = 5;
        Jobs[4].weight = 7;*/

        Arrays.sort(Jobs, Comparator.comparing(Job::GetStart)); //this puts in nice order, but how much time complexity added? adds 7.2 milliseconds at 10,000 samples
        Arrays.sort(Jobs, Comparator.comparing(Job::GetEnd));
        //for (Job ree : Jobs){
        //    ree.Print();
        //}

        // --------------------------------------------------------------------------------------------find p?
        // better way to do this?
        for(int i = 0; i < numJobs; i++){
            int start = Jobs[i].timeStart;
            for(int j = i ; j > -1; j--){
                if (Jobs[j].timeEnd < start){   // breaks at first compatible index found
                    Jobs[i].p = Jobs[j];
                    break;
                }
            } // no compatible index was found
        }


        // loop without function?
        int numMax = 0;
        Job jobMax = null;
        List<String> solutionPath = new ArrayList<>();
       //                                   <----------------- begining of max
        MaxMap.put(null, 0);
        int max1 = 0; // current job value + job.p max value
        int max2 = 0; // 1 index lower than current job's max value
        for (int j = 1; j < numJobs +1; j++){
            if (MaxMap.get(Jobs[j-1].p) == null){
                max1 = 0;
            } else{
                max1 = MaxMap.get(Jobs[j-1].p);
            }
            if (j >=2){
                max2 = MaxMap.get(Jobs[j-2]);
            }
            int sum = 0;
//---------------------------------------------------------------------------------------------------j.max scrounging
            if (Jobs[j-1].weight + max1 > max2){
                sum = Jobs[j-1].weight + max1;
                Jobs[j-1].max = Jobs[j-1].p;
                Jobs[j-1].usesSelf = true;
            } else{
                sum = max2;
                Jobs[j-1].max = Jobs[j-2];
            }
            // total max
            MaxMap.put(Jobs[j-1],sum);
            if(sum > numMax){
                numMax = sum;
                jobMax = Jobs[j-1];
            }
        }
        long endTime = System.currentTimeMillis();
        long fin = endTime - startTime;
        Print("\n"+fin+" milliseconds"); //-------------------------------------   time
        String message = "";
        //--------------------------------------------------------------------------------------------------------------------------------checker

        // temp traverses the path of greatest weight
        Job temp = jobMax;
        while(temp != null){
            if (temp.usesSelf) {        // complexity O(n)
                solutionPath.add(temp.name);
            }
            temp = temp.max;
        }
        message += String.valueOf(numMax) + " is the highest value by taking job";
        if (solutionPath.size() > 0){
            message += 's';
        }
        Print(message);
        //Collections.sort(solutionPath); // sort alphabetically [time complexity?]
        Print(solutionPath);

        //                  Grid Printer
        Print("--------------------------------------------------------------------------------------------------------------------------");
        String[][] gridArray = new String[timeMax+1][numJobs+1];
        // initialize bottom row of grid, the x axis of time
        for (int i = 0; i <=timeMax ; i++){
            gridArray[i][0] = String.valueOf(i);
        }
        int count = numJobs;
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
            GridBuilder(numJobs,timeMax,gridArray,gridDisplay);
        } else{
            GridBuilderColor(numJobs,timeMax,gridArray,gridDisplay, solutionPath, Jobs);
        }

         // gets rid of last extra \n
        Print(gridDisplay);
        Print("--------------------------------------------------------------------------------------------------------------------------");
    }

    public static <T> void Print(T input){
        System.out.println(input);
    }
    public static void GridBuilder(int numJobs, int timeMax, String[][] gridArray, StringBuilder gridDisplay){
        for (int y = numJobs ; y > -1; y--){
            for (int x = 0; x <= timeMax; x++){
                if (gridArray[x][y] == null){
                    gridDisplay.append(String.format("%-6s", "[]"));
                } else{
                    gridDisplay.append(String.format("%-6s", gridArray[x][y]));
                }
            }
            gridDisplay.append("\n");
        }
        gridDisplay.deleteCharAt(gridDisplay.length()-1);
    }
    public static void GridBuilderColor(int numJobs, int timeMax, String[][] gridArray, StringBuilder gridDisplay, List<String> solutionPath, Job[] Jobs) {
        for (int y = numJobs; y > -1; y--) {
            for (int x = 0; x <= timeMax; x++) {
                if (gridArray[x][y] == null) {
                    //gridDisplay.append(String.format("%-6s", "[]"));
                    System.out.printf("%-6s", "[]");
                } else {
                    //gridDisplay.append(String.format("%-6s", gridArray[x][y]));
                    if (y > 1 && solutionPath.contains(Jobs[y - 1].name)) {        //y > 1 && solutionPath.contains(Jobs[y-1].name
                        System.out.printf("%-6s", ANSI_BLACK_BACKGROUND + ANSI_WHITE + gridArray[x][y] + "     " + ANSI_RESET);
                    } else {
                        System.out.printf("%-6s", gridArray[x][y]);
                    }
                }
            }
            System.out.println("");
        }
    }
}


/*

//                  Grid Printer Color
        Print("--------------------------------------------------------------------------------------------------------------------------");
        String[][] gridArray = new String[timeMax+1][numJobs+1];
        // initialize bottom row of grid, the x axis of time
        for (int i = 0; i <=timeMax ; i++){
            gridArray[i][0] = String.valueOf(i);
        }
        int count = numJobs;
        for (Job j: Jobs){
            gridArray[j.timeEnd][count] = String.valueOf(j.name);
            gridArray[j.timeStart][count] = String.valueOf(j.name);
            for (int i = j.timeStart + 1; i < j.timeEnd; i++){
                gridArray[i][count] = String.valueOf(j.weight);
            }
            count--;
        }
        StringBuilder gridDisplay = new StringBuilder();
        for (int y = numJobs ; y > -1; y--){
            for (int x = 0; x <= timeMax; x++){
                if (gridArray[x][y] == null){
                    //gridDisplay.append(String.format("%-6s", "[]"));
                    System.out.printf("%-6s","[]");
                } else{
                    //gridDisplay.append(String.format("%-6s", gridArray[x][y]));
                    if (y > 1 && solutionPath.contains(Jobs[y-1].name)){        //y > 1 && solutionPath.contains(Jobs[y-1].name
                        System.out.printf("%-6s", ANSI_BLACK_BACKGROUND+ ANSI_WHITE + gridArray[x][y]+  "     " + ANSI_RESET );
                    } else{
                        System.out.printf("%-6s", gridArray[x][y]);
                    }
                }
            }
            System.out.println("");
        }
        //gridDisplay.deleteCharAt(gridDisplay.length()-1); // gets rid of last extra \n
       // Print(gridDisplay);
        Print("--------------------------------------------------------------------------------------------------------------------------");
    }

 */