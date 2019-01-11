import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class FileHelper {

    public static boolean createFile(File fileName){
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String readTxtFile(File fileName){
        String result = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                String read = null;
                while ((read = bufferedReader.readLine()) != null) {
                    result = result + read + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean writeTxtFile(String content, File fileName){
        boolean flag = false;
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes());
            o.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}