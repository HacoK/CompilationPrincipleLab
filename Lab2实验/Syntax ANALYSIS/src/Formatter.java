import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Formatter {
    public static ArrayList<String> transform(){
        ArrayList<String> simp_tokens=new ArrayList<>();
        String[] cmps = {">",">=","<","<=","==","!="};
        List<String> cmp = Arrays.asList(cmps);
        File input = new File("tokens.txt");
        if(!input.exists())
            FileHelper.createFile(input);
        StringBuilder src=new StringBuilder(FileHelper.readTxtFile(input));
        String[] tokens=src.toString().split("\n");
        for(String s:tokens){
            String fst_unit=s.substring(1,s.indexOf(','));
            String sec_unit=s.substring(s.indexOf(',')+1,s.length()-1);
            if(fst_unit.equals("Keyword"))
                simp_tokens.add(sec_unit.substring(0,1));
            else if(fst_unit.equals("ID"))
                simp_tokens.add("id");
            else if(fst_unit.equals("Operator")){
                if(cmp.contains(sec_unit))
                    simp_tokens.add("cmp");
                else
                    simp_tokens.add(sec_unit);
            }
        }
        simp_tokens.add("$");
        File output = new File("simp_tokens.txt");
        if(!output.exists())
            FileHelper.createFile(output);
        StringBuilder outStream=new StringBuilder();
        for(String s:simp_tokens){
            outStream.append(s);
            outStream.append('\n');
        }
        FileHelper.writeTxtFile(outStream.toString(),output);
        return simp_tokens;
    }
}
