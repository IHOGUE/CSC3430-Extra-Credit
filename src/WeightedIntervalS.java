
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

class Job{
    public int timeStart;
    public int timeEnd;
    public int weight;
    public int len = 0;
    public int index = 0;
    public char name;
    public Job p;
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
            String input = CSV.getParent() + File.separator + name;
            File Jim = new File(input);
            try {
                //File myObj = new File("src/com/example/helloworld2/resources/test.csv");
                Scanner myReader = new Scanner(Jim);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    System.out.println(data);
                }
                myReader.close();
            } catch(FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        } catch (URISyntaxException e) {
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
            Job temp = new Job(a,b,c); // 2*i, 2*i+3, i+1
            temp.len = len;
            temp.name = name;
            //temp.weight = rand.nextInt(5 + 1) + 1;
            //temp.Print();
            temp.index = i;
            Jobs[i] = temp;
        }

        /*Jobs[2].timeStart = 3;
        Jobs[1].timeStart = 1;
        Jobs[4].timeStart = 3;
        Jobs[0].timeStart = 16;
        Jobs[3].timeStart = 16;

        Jobs[2].timeEnd = 5;
        Jobs[1].timeEnd = 8;
        Jobs[4].timeEnd = 10;
        Jobs[0].timeEnd = 18;
        Jobs[3].timeEnd = 20;

        Jobs[2].weight = 10;
        Jobs[1].weight = 2;
        Jobs[4].weight = 1;
        Jobs[0].weight = 1;
        Jobs[3].weight = 3;*/

        Arrays.sort(Jobs, Comparator.comparing(Job::GetStart)); //this puts in nice order, but how much time complexity added? adds 7.2 milliseconds at 10,000 samples
        Arrays.sort(Jobs, Comparator.comparing(Job::GetEnd));
        for (Job ree : Jobs){
            ree.Print();
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
        Print("Beginning of Max"); //                                   <----------------- begining of max
        //Max[0] = 0;
        MaxMap.put(null, 0);
        int max1 = 0;
        int max2 = 0;
        for (int j = 1; j < size +1; j++){
            if (MaxMap.get(Jobs[j-1].p) == null){
                max1 = 0;
            } else{
                max1 = MaxMap.get(Jobs[j-1].p);//Max[Jobs[j-1].p]
            }
            if (j >=2){
                max2 = MaxMap.get(Jobs[j-2]);
            }
            int sum = Math.max(Jobs[j-1].weight + max1, max2);

            // total max
            if(sum > numMax){
                numMax = sum;
                jobMax = Jobs[j-1];
            }
            MaxMap.put(Jobs[j-1],sum);
        }
        for (int j = 0; j < size; j++){
            Print(MaxMap.get(Jobs[j]));
        }

        long endTime = System.currentTimeMillis();
        long fin = endTime - startTime;
        Print("\n"+fin+" milliseconds"); //-------------------------------------   time
        String message = "";

        message += String.valueOf(numMax) + " is the highest value by taking job";
        Job checker = jobMax;
        while (checker != null){
            if (checker.p == null){
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
        Print(message);
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