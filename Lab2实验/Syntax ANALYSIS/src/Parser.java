import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {
    //row:57 column:15
    public static int[][] parsingTb = {
            {2,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,Integer.MAX_VALUE,0,0,0,0},
            {0,0,0,0,6,5,0,0,0,0,0,0,3,4,0},
            {0,7,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,8,0,0,0,9,10,0,0,0,0,0,0},
            {0,0,0,0,13,12,0,0,0,0,0,0,0,11,0},
            {0,0,0,-8,-8,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,15,0,0,0,0,0,0,0,0,0,14},
            {0,0,0,0,18,17,0,0,0,0,0,0,0,16,0},
            {0,0,0,0,6,5,0,0,0,0,0,0,0,19,0},
            {0,0,0,0,6,5,0,0,0,0,0,0,0,20,0},
            {0,0,0,0,0,0,21,22,23,0,0,0,0,0,0},
            {0,0,0,0,13,12,0,0,0,0,0,0,0,24,0},
            {0,0,0,0,0,0,-8,-8,-8,0,0,0,0,0,0},
            {0,0,25,0,0,0,0,0,0,0,-1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,26,0,0,0,0,0},
            {0,-3,0,0,0,0,0,27,28,0,0,0,0,0,0},
            {0,0,0,0,18,17,0,0,0,0,0,0,0,29,0},
            {0,-8,0,0,0,0,0,-8,-8,0,0,0,0,0,0},
            {0,0,0,-5,0,0,0,-5,-5,0,0,0,0,0,0},
            {0,0,0,-6,0,0,0,9,-6,0,0,0,0,0,0},
            {0,0,0,-7,0,0,0,-7,-7,0,0,0,0,0,0},
            {0,0,0,0,13,12,0,0,0,0,0,0,0,30,0},
            {0,0,0,0,13,12,0,0,0,0,0,0,0,31,0},
            {0,0,0,0,0,0,32,22,23,0,0,0,0,0,0},
            {0,0,0,0,34,0,0,0,0,0,0,0,0,0,33},
            {0,0,0,0,37,36,0,0,0,0,0,0,0,35,0},
            {0,0,0,0,18,17,0,0,0,0,0,0,0,38,0},
            {0,0,0,0,18,17,0,0,0,0,0,0,0,39,0},
            {0,0,0,0,0,0,40,27,28,0,0,0,0,0,0},
            {0,0,0,0,0,0,-5,-5,-5,0,0,0,0,0,0},
            {0,0,0,0,0,0,-6,22,-6,0,0,0,0,0,0},
            {0,0,0,0,0,0,-7,-7,-7,0,0,0,0,0,0},
            {0,0,0,0,34,0,0,0,0,0,-2,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,41,0,0,0,0,0},
            {0,0,-4,0,0,0,0,42,43,0,-4,0,0,0,0},
            {0,0,0,0,37,36,0,0,0,0,0,0,0,44,0},
            {0,0,-8,0,0,0,0,-8,-8,0,-8,0,0,0,0},
            {0,-5,0,0,0,0,0,-5,-5,0,0,0,0,0,0},
            {0,-6,0,0,0,0,0,27,-6,0,0,0,0,0,0},
            {0,-7,0,0,0,0,0,-7,-7,0,0,0,0,0,0},
            {0,0,0,0,47,46,0,0,0,0,0,0,0,45,0},
            {0,0,0,0,37,36,0,0,0,0,0,0,0,48,0},
            {0,0,0,0,37,36,0,0,0,0,0,0,0,49,0},
            {0,0,0,0,0,0,56,42,43,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,50,51,0,-4,0,0,0,0},
            {0,0,0,0,47,46,0,0,0,0,0,0,0,52,0},
            {0,0,0,0,0,0,0,-8,-8,0,-8,0,0,0,0},
            {0,0,-5,0,0,0,0,-5,-5,0,-5,0,0,0,0},
            {0,0,-6,0,0,0,0,42,-6,0,-6,0,0,0,0},
            {0,0,0,0,47,46,0,0,0,0,0,0,0,53,0},
            {0,0,0,0,47,46,0,0,0,0,0,0,0,54,0},
            {0,0,0,0,0,0,55,50,51,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,-5,-5,0,-5,0,0,0,0},
            {0,0,0,0,0,0,0,50,-6,0,-6,0,0,0,0},
            {0,0,0,0,0,0,0,-7,-7,0,-7,0,0,0,0},
            {0,0,-7,0,0,0,0,-7,-7,0,-7,0,0,0,0}
    };

    public static void main(String[] args){

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("i",0);
        map.put("t",1);
        map.put("e",2);
        map.put("cmp",3);
        map.put("id",4);
        map.put("(",5);
        map.put(")",6);
        map.put("*",7);
        map.put("/",7);
        map.put("+",8);
        map.put("-",8);
        map.put("=",9);
        map.put("$",10);
        map.put("L",11);
        map.put("C",12);
        map.put("E",13);
        map.put("A",14);

        Map<Integer,String> prodMap=new HashMap<>();
        prodMap.put(0,"S->L");
        prodMap.put(1,"L->iCtA");
        prodMap.put(2,"L->iCtAeA");
        prodMap.put(3,"C->EcmpE");
        prodMap.put(4,"A->id=E");
        prodMap.put(5,"E->E*/E");
        prodMap.put(6,"E->E+-E");
        prodMap.put(7,"E->(E)");
        prodMap.put(8,"E->id");

        Map<Integer,Integer> bodyMap=new HashMap<>();
        bodyMap.put(0,1);
        bodyMap.put(1,4);
        bodyMap.put(2,6);
        bodyMap.put(3,3);
        bodyMap.put(4,3);
        bodyMap.put(5,3);
        bodyMap.put(6,3);
        bodyMap.put(7,3);
        bodyMap.put(8,1);

        Map<Integer,String> headMap=new HashMap<>();
        headMap.put(0,"S");
        headMap.put(1,"L");
        headMap.put(2,"L");
        headMap.put(3,"C");
        headMap.put(4,"A");
        headMap.put(5,"E");
        headMap.put(6,"E");
        headMap.put(7,"E");
        headMap.put(8,"E");

        LexicalAnalyzer.tokenizer();
        ArrayList<String> tokenList=Formatter.transform();
        Stack stateStack = new Stack<Integer>();
        stateStack.push(0);
        int cur_state;
        int symbol;
        int action;
        ArrayList<String> reductions=new ArrayList<>();
        String s="";
        int index=0;
        while(index<tokenList.size()){
            s=tokenList.get(index);
            cur_state=(int)stateStack.peek();
            symbol=map.get(s);
            action=parsingTb[cur_state][symbol];
            if(action==0){
                reductions.add("Error!!!(Current State:"+cur_state+"&&Input Symbol:"+s+")");
                break;
            }else if(action>0){
                if(action==Integer.MAX_VALUE){
                    if(stateStack.search(0)==2){
                        reductions.add(prodMap.get(0));
                        reductions.add("Accept!");
                        break;
                    }
                    else{
                        reductions.add(prodMap.get(0));
                        reductions.add("Unexpected End!!!");
                        break;
                    }
                }else{
                    stateStack.push(action);
                    index++;
                }
            }else{
                reductions.add(prodMap.get(-action));
                for(int i=0;i<bodyMap.get(-action);i++){
                    stateStack.pop();
                }
                index--;
                tokenList.set(index,headMap.get(-action));
            }
        }
        if(!reductions.get(reductions.size()-1).equals("Accept!")&&index==tokenList.size()-1){
            reductions.add("The input ends in an unacceptable state...");
        }

        File output = new File("rSequence.txt");
        if(!output.exists())
            FileHelper.createFile(output);
        StringBuilder outStream=new StringBuilder();
        for(String reduction:reductions){
            outStream.append(reduction);
            outStream.append('\n');
        }
        FileHelper.writeTxtFile(outStream.toString(),output);
    }
}
