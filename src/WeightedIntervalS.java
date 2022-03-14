
import java.net.URISyntaxException;
import java.util.*;
import java.io.File;  // Import the File class <- neccessary?
import java.io.FileNotFoundException;  // Import this class to handle errors <- neccessary?


class Job{
    public int timeStart;
    public int timeEnd;
    public int weight;
    public int len = 0;
    public int index = 0;
    public char name;
    public Job p;
    public boolean usesSelf = false;
    public Job max;
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
    public static void main(String[] args) {
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

        long startTime = System.currentTimeMillis();
        Random rand = new Random();
        int size = 5; //5    rand.nextInt(11) + 1
        HashMap<Job, Integer> MaxMap = new HashMap<>(size + 1); // initial capacity good? size or size + 1?
        Job[] Jobs = new Job[size];

        // easy job initiate
        for (int i = 0; i < size; i++) {
            int conv = 65 + i;
            char name = (char)conv;
            int a = rand.nextInt(20);
            int len = rand.nextInt(20 - a) + 1;
            int b = a + len;
            int c = rand.nextInt(10) + 1;
            int index = 0;
            Job temp = new Job(a,b,c); // 2*i, 2*i+3, i+1
            temp.len = len;
            temp.name = name;
            //temp.weight = rand.nextInt(5 + 1) + 1;
            //temp.Print();
            temp.index = i;
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
        int counter = 0;
        for (Job ree : Jobs){
            ree.Print();
            ree.index = counter;
            counter++;
        }

        Print("\np equals:");
        // find p?
        // better way to do this?
        for(int i = 0; i < size; i++){
            int start = Jobs[i].timeStart;
            for(int j = i ; j > -1; j--){
                if (Jobs[j].timeEnd < start){
                    Jobs[i].p = Jobs[j];
                    System.out.println(Jobs[i].name + "'s p is " + Jobs[i].p.name);
                    break;
                }
                else if (j == 0){
                    System.out.println(Jobs[i].name + "'s p is 0");
                }
            }
        }
        System.out.println(Jobs[0].name + "- 1 is null");
        for (int i = 1; i < size; i++){
            System.out.println(Jobs[i].name + "- 1 is " + Jobs[i-1].name);
        }

        // loop without function?
        int numMax = 0;
        Job jobMax = null;
        int jMax = 0;
        List<Character> solutionPath = new ArrayList<>();
       //                                   <----------------- begining of max
        //Max[0] = 0;
        MaxMap.put(null, 0);
        int max1 = 0;
        int max2 = 0;
        boolean nonP = true;
        for (int j = 1; j < size +1; j++){
            if (MaxMap.get(Jobs[j-1].p) == null){
                max1 = 0;
            } else{
                max1 = MaxMap.get(Jobs[j-1].p);//Max[Jobs[j-1].p]
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
                if (j > 1){
                    //Jobs[j-2].usesSelf = false;
                }
            } else{
                sum = max2;
                nonP = false;
                //Jobs[j-1].usesSelf = false;
                Jobs[j-1].max = Jobs[j-2];
            }
            // total max
            MaxMap.put(Jobs[j-1],sum);
            if(sum > numMax){
                numMax = sum;
                jobMax = Jobs[j-1];
                jMax = j;
            }

        }
        Print("Beginning of Max");
        for (int j = 0; j < size; j++){
            Print(MaxMap.get(Jobs[j]));
        }

        long endTime = System.currentTimeMillis();
        long fin = endTime - startTime;
        Print("\n"+fin+" milliseconds"); //-------------------------------------   time
        String message = "";
        //--------------------------------------------------------------------------------------------------------------------------------checker

        message += String.valueOf(numMax) + " is the highest value by taking job";
        Job checker = jobMax; // checker holds the max job rn


        Print(message);
        Job temp = jobMax;
        while(temp != null){
            if (temp.usesSelf) {        // complexity O(n)
                solutionPath.add(temp.name);
            }
            temp = temp.max;
        }
        if (!nonP){
            Print(solutionPath);
        }
        //                  Grid Printer
        Print("--------------------------------------------------------------------------------------------------------------------------");
        String[][] grid = new String[21][size+1];
        for (int i = 0; i <=20 ; i++){
            grid[i][0] = String.valueOf(i);
        }
        int count = size;
        for (Job j: Jobs){
            grid[j.timeEnd][count] = String.valueOf(j.name);
            grid[j.timeStart][count] = String.valueOf(j.name);
            for (int i = j.timeStart + 1; i < j.timeEnd; i++){
                grid[i][count] = String.valueOf(j.weight);
            }
            count--;
        }
        String bob = "";
        for (int y = size ; y > -1; y--){
            for (int x = 0; x <= 20; x++){
                if (grid[x][y] == null){
                    bob += String.format("%-6s", "[]");
                } else{
                    bob += String.format("%-6s", grid[x][y]);
                }
            }
            bob += "\n";
        }
        Print(bob);
    }

    public static <T> void Print(T input){
        System.out.println(input);
    }

}
/*

 while (checker != null && nonP){
            if (checker.p == null){     // meaning if nothing else is compat, this is the best
                message +=  " " + checker.name;
            }
            else if(checker.p.p == null){
                if (message.endsWith("b")){
                    message +=  "s " + checker.name + " and";
                } else{
                    message +=  checker.name + ", and";
                }
            }
            else{
                if (message.endsWith("b")){
                    message += "s " + checker.name + ", ";
                } else{
                    message += checker.name + ", ";
                }
            }
            checker = checker.p;
        }

 */






/*
solutionPath.add(String.valueOf(jobMax.name));
                if (solutionPath.size() > 1 && jobMax.index > 1 && MaxMap.get(jobMax) > MaxMap.get(Jobs[jobMax.index-1])){
                    if (jobMax.p == null){

                    }
                    else if(jobMax.p.name != solutionPath.get(solutionPath.size()-2)){
                        solutionPath.remove(solutionPath.size()-2);
                    }

                    Print("SOMeTIUNG)");
                } else{
                    //solutionPath.remove(solutionPath.size()-1);
                    if (jobMax.index > 0){
                        Print(jobMax.weight + " versus  " + MaxMap.get(Jobs[jobMax.index-1]));
                    }

                }
 */


/*
 if (max2 > max1){
                    solutionPath.add(String.valueOf(Jobs[j-1].name));
                } else{
                    if (j >= 2){
                        solutionPath.add(String.valueOf(Jobs[j-2].name));

                    } else{
                        solutionPath.add(String.valueOf(Jobs[j-1].name));
                    }
                }
 */