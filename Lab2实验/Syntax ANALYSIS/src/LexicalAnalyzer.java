import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LexicalAnalyzer {
    public static boolean validChar(char c){
        return (c=='_'||(c>='A'&&c<='Z')||(c>='a'&&c<='z')||(c>='0'&&c<='9'));
    }
    public static void tokenizer(){
        String[] keywords = {"public","private","protected","package","abstract","new","assert","default","synchronized","volatile","if","else","do","while","for","break","continue","switch","case","import","throws","throw","instanceof","return","transient","try","catch","finally","final","void","int","short","char","long","boolean","double","byte","class","enum","super","this","implements","extends","interface","static","const","then"};
        List<String> keyword = Arrays.asList(keywords);

        String[] operators = {"!","+","-","*","/","%","<<",">>","<=","<",">=",">","==","!=","~","^","&","|","&&","||","=","+=","-=","*=","/="};
        List<String> operator = Arrays.asList(operators);

        String[] delimiters = {"(",")","[","]","{","}",";",",",".","?",":"};
        List<String> delimiter = Arrays.asList(delimiters);

        List<Character> ws=new ArrayList<>();
        ws.add(' ');
        ws.add('\t');
        ws.add('\n');
        ws.add('\r');
        //char ''
        //string ""
        //"NUMBER": ["([0-9][0-9]*)|([0-9][0-9]*\.[0-9]*)"]
        //"ID": ["[A-Za-z_][A-Za-z0-9_]*"]

        //识别顺序：
        //delimiter, operator, char, string, ws
        //number
        //id&keyword
        File input = new File("input.txt");
        if(!input.exists())
            FileHelper.createFile(input);
        StringBuilder src=new StringBuilder(FileHelper.readTxtFile(input));
        StringBuilder temp=new StringBuilder(src.length());
        int index=0;
        boolean literal=false;
        while(index<src.length()){
            char c=src.charAt(index);
            if(c=='"'){
                if(literal)
                    literal=false;
                else
                    literal=true;
            }
            if(!literal){
                switch(c){
                    case '/':
                        index++;
                        c=src.charAt(index);
                        if(c=='/'){
                            do{
                                index++;
                                c=src.charAt(index);
                            }while(index<src.length()-1&&c!='\n');
                            if(c=='\n'){
                                temp.append(c);
                                index++;
                            }else{
                                index++;
                            }
                        }else if(c=='*'){
                            do{
                                index++;
                                c=src.charAt(index);
                                if(c=='\n'){
                                    temp.append(c);
                                    index++;
                                }else if(c=='*'){
                                    index++;
                                    c=src.charAt(index);
                                    if(c=='/'){
                                        index++;
                                        break;
                                    }
                                }
                            }while(index<src.length()-1);
                        }else{
                            temp.append('/');
                            temp.append(c);
                            index++;
                        }
                        break;
                    default:
                        temp.append(c);
                        index++;
                        break;
                }
            }
            else{
                temp.append(c);
                index++;
            }
        }

        String tmp=temp.toString();

        int lexemeBeg=0;
        int forward=1;
        String value;
        String token="";
        int lineCount=1;
        ArrayList<String> tokens=new ArrayList<>();
        while(lexemeBeg<tmp.length()){
            value=tmp.substring(lexemeBeg,forward);
            if(delimiter.contains(value)){
                token="<Delimiter,"+value+">";
                tokens.add(token);
                lexemeBeg=forward;
                forward=lexemeBeg+1;
            }else if(operator.contains(value)){
                if(forward<tmp.length()){
                    String preCheck=tmp.substring(lexemeBeg,forward+1);
                    if(operator.contains(preCheck)){
                        token="<Operator,"+preCheck+">";
                        tokens.add(token);
                        lexemeBeg=forward+1;
                        forward=lexemeBeg+1;
                    }else{
                        token="<Operator,"+value+">";
                        tokens.add(token);
                        lexemeBeg=forward;
                        forward=lexemeBeg+1;
                    }
                }else{
                    token="<Operator,"+value+">";
                    tokens.add(token);
                    lexemeBeg=forward;
                    forward=lexemeBeg+1;
                }
            }else if(value.equals("\'")){
                char end='\0';
                while(end!='\''&&forward<tmp.length()){
                    if(end=='\\'){
                        forward++;
                    }
                    end=tmp.charAt(forward);
                    forward++;
                }
                if(end=='\''){
                    value=tmp.substring(lexemeBeg,forward);
                    token="<Char,"+value+">";
                    tokens.add(token);
                    lexemeBeg=forward;
                    forward=lexemeBeg+1;
                }else{
                    System.out.println("Line "+lineCount+" @ Recognize Error:No Matched single quotes for Char To the end");
                    return;
                }
            }else if(value.equals("\"")){
                char end='\0';
                while(end!='"'&&forward<tmp.length()){
                    if(end=='\\'){
                        forward++;
                    }
                    end=tmp.charAt(forward);
                    forward++;
                }
                if(end=='"'){
                    value=tmp.substring(lexemeBeg,forward);
                    token="<String,"+value+">";
                    tokens.add(token);
                    lexemeBeg=forward;
                    forward=lexemeBeg+1;
                }else{
                    System.out.println("Line "+lineCount+" @ Recognize Error:No Matched double quotes for String To the end");
                    return;
                }
            }else if(ws.contains(value.charAt(0))){
                if(value.charAt(0)=='\n')
                    lineCount++;
                lexemeBeg=forward;
                forward=lexemeBeg+1;
            }else if(value.charAt(0)>='0'&&value.charAt(0)<='9'){
                boolean dot=false;
                while(forward<tmp.length()&&((tmp.charAt(forward)>='0'&&tmp.charAt(forward)<='9')||(tmp.charAt(forward)=='.'&&!dot))){
                    if(tmp.charAt(forward)=='.')
                        dot=true;
                    forward++;
                }
                value=tmp.substring(lexemeBeg,forward);
                token="<Number,"+value+">";
                tokens.add(token);
                lexemeBeg=forward;
                forward=lexemeBeg+1;
            }else if(value.charAt(0)=='_'||(value.charAt(0)>='A'&&value.charAt(0)<='Z')||(value.charAt(0)>='a'&&value.charAt(0)<='z')){
                while(forward<tmp.length()&&LexicalAnalyzer.validChar(tmp.charAt(forward))){
                    forward++;
                }
                value=tmp.substring(lexemeBeg,forward);
                if(keyword.contains(value)){
                    token="<Keyword,"+value+">";
                    tokens.add(token);
                    lexemeBeg=forward;
                    forward=lexemeBeg+1;
                }
                else{
                    token="<ID,"+value+">";
                    tokens.add(token);
                    lexemeBeg=forward;
                    forward=lexemeBeg+1;
                }
            }else{
                System.out.println("Line "+lineCount+" @ Illegal Start Char:Can't Match any pattern——\\"+(int)value.charAt(0));
                return;
            }


        }




        File output = new File("tokens.txt");
        if(!output.exists())
            FileHelper.createFile(output);
        StringBuilder outStream=new StringBuilder();
        for(String s:tokens){
            outStream.append(s);
            outStream.append('\n');
        }
        FileHelper.writeTxtFile(outStream.toString(),output);

    }
}
