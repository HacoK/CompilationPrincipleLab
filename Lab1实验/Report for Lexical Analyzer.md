# Lab Document for Lexical Analyzer

#### a)Motivation/Aim

构建一个简单的词法分析器来识别输入中出现的每个词素，并返回识别到的词法单元的有关信息。

#### b)Content description

程序以java编写，包含Tokenizer、FileHelper两个类，其中Tokenizer为启动类，将input.txt中的输入经过预处理输出到check.txt，然后将分析生成的词法单元tokens输出到output.txt，而FileHelper为封装文件操作的工具类，包含文件创建及读写的方法。

#### c)Ideas/Methods

程序可以分成两个级联的处理阶段：

预处理：

将input.txt中的注释部分（//或/**/）删除，但保留换行符\n以保证出错信息行号与input.txt一致。

基于FA实现的代码如下：

```
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
```


词法分析：

以预处理产物为基础，根据预先定义的词法单元类别（REs），分析并生成对应词法单元<Name,Value>。

采取最长匹配，使得doSomeThing可以被识别为标识符而不是关键字do和标识符SomeThing;

设定优先级，在同时匹配ID及Keyword模式时，优先识别为Keyword，保证if被识别为Keyword而不是ID。

代码于后续部分再行说明。

#### d)Assumptions

预先定义的词法单元模式如下：

```
"Delimiter":["(",")","[","]","{","}",";",",",".","?",":"]

"Operator":
["!","+","-","*","/","%","<<",">>","<=",
"<",">=",">","==","!=","~","^","&","|",
"&&","||","=","+=","-=","*=","/="]

"Char":'.*'

"String":".*"

"ws":[' ','\t','\n','\r']

"Number": [0-9]+\.?[0-9]*

"ID": [A-Za-z_][A-Za-z0-9_]*

"Keyword":["public","private","protected","package","abstract","new","assert","default","synchronized","volatile","if","else","do","while","for","break","continue","switch","case","import","throws","throw","instanceof","return","transient","try","catch","finally","final","void","int","short","char","long","boolean","double","byte","class","enum","super","this","implements","extends","interface","static","const"]
```

#### e)Related FA descriptions

Delimiter、Operator、ws、Keyword对应模式含词素个数有限，可直接列举，故直接建立列表List<String>存储（ws为List<Character>），而无需建立FA辅助编程；

Char、String、Number及ID则可建立对应的FA，鉴于四个FA均属于Simple FA，故可以”并行地“运行各个状态转换图，将输入字符提供给所有的状态转换图，并使得每个起始状态满足的状态转换图做出它应该执行的变换。

#### f)Description of important Data Structures

```
//用于存储Delimiter、Operator、ws、Keyword模式词素的List
List<String> keyword
List<String> operator
List<String> delimiter
List<Character> ws
//预处理中避免将""中字符串常量识别为注释的flag
boolean literal=false
//lexemeBeg记录词素起始位置，forward记录词素当前推进位置，value为此时词素值
int lexemeBeg=0
int forward=1
String value=tmp.substring(lexemeBeg,forward)
//token,格式为<Name,Value>
String token=""
//lineCount遇\n时自增，用于记录当前行号供错误处理使用
int lineCount=1
//tokens用于保存词法分析阶段识别出的token
ArrayList<String> tokens
```

#### g)Description of core Algorithms

先贴代码，再行分析:)

```
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
        while(forward<tmp.length()&&Tokenizer.validChar(tmp.charAt(forward))){
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
```

```
识别顺序：
delimiter, operator, char, string, ws
number
id&keyword
通过读取第一个字符即可通过List.contains()先行判断是否匹配delimiter、operator、char、string及ws的模式（ws模式识别后不生成token，直接开始下一个词法单元的识别），然后再通过判断起始字符是否为数字判断是否匹配number模式（用boolean dot指示小数点是否已经存在，避免词素中出现两个及以上小数点），最后就是id和keyword的识别了，首先确定最长匹配的词素，然后鉴于优先级，判断词素是否属于keyword，若属于则为keyword，否则为id。
对于无法匹配任何模式的前缀，程序会打印出错信息。
```

#### h)Use cases on running

用于测试的用例input.txt中内容即为Tokenizer类的代码，不过为测试预处理部分的功能，额外在input.txt开头加入如下注释：

```
/*Just
  A
  Test*/

//check discard annotation preprocessor
```

预处理结果保存在check.txt，词法分析结果保存在output.txt。

#### i)Problems occurred and related solutions

错误处理的话，均采用控制台打印出错信息然后返回的方式（例子见下）：

```
//Char识别中未找到匹配的单引号
System.out.println("Line "+lineCount+" @ Recognize Error:No Matched single quotes for Char To the end");
return;
//String识别中未找到匹配的双引号
System.out.println("Line "+lineCount+" @ Recognize Error:No Matched double quotes for String To the end");
return;
//词素前缀无法匹配任何模式，打印异常字符ascii码
System.out.println("Line "+lineCount+" @ Illegal Start Char:Can't Match any pattern——\\"+(int)value.charAt(0));
return;
```

#### j) Your feelings and comments

词法分析是编译的第一阶段，通过简易词法分析器的构建，可以对其主要任务有更加清晰的认识：

读入源程序的输入字符、将它们组成词素，生成并输出一个词法单元序列，其中每个词法单元对应于一个词素。



By 161250098 彭俊杰