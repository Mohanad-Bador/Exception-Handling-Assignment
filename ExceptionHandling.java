package Lab6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ExceptionHandling{
    public static void main(String[] args) {
        try {
            String filename = args[0];
            validateExtension(filename);
            File F = new File(filename);
            if (!F.exists())
                throw new FileNotFoundException("This file is not found");
            emptyCheck(F);
            FileInputStream fis= new FileInputStream(F);
            StringBuilder SB= new StringBuilder();
            int s;
            while((s=fis.read())!=-1){
                SB.append((char)s);
            }
            String data = SB.toString();
            Scanner sc = new Scanner(data);
            ArrayList <Container> containers= new ArrayList<>();
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.contains("<CONTAINER")){
                    String containerID = line.substring(line.indexOf("UUID="), line.indexOf(">"));
                    String ShortName = sc.nextLine();
                    String SN = ShortName.substring(ShortName.indexOf(">")+1, ShortName.indexOf("</"));
                    String LongName = sc.nextLine();
                    String LN = LongName.substring(LongName.indexOf(">")+1, LongName.indexOf("</"));
                    Container cont = new Container();
                    cont.setContainerID(containerID);
                    cont.setShortName(SN);
                    cont.setLongName(LN);
                    containers.add(cont);
                }
            }
            Collections.sort(containers);
            String Outfilename = filename.substring(0, filename.indexOf("."))+"_mod.arxml";
            FileOutputStream fos = new FileOutputStream(Outfilename);
            fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
            fos.write("<AUTOSAR>\n".getBytes());
            for(int i=0;i<containers.size();i++){
                fos.write(containers.get(i).toString().getBytes());
            }
            fos.write("</AUTOSAR>\n".getBytes());

        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (NotVaildAutosarFileException e) {
            System.out.println(e);
        }
        catch (EmptyAutosarFileException e){
            System.out.println(e);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            System.out.println("end ofcode");
        }
    }
    public static void validateExtension (String S)throws NotVaildAutosarFileException{
        int i = S.lastIndexOf(".");
        if(i>=0){
            String extension=S.substring(i+1);
            if (!extension.equals("arxml")){
                throw new NotVaildAutosarFileException();
            }
        }
        else throw new NotVaildAutosarFileException();
    }
    public static void emptyCheck(File F)throws EmptyAutosarFileException{
        if (F.length()==0){
            throw new EmptyAutosarFileException();
        }
    }
}
class NotVaildAutosarFileException extends Exception{
    public NotVaildAutosarFileException(){
        super("This is not an arxml file");
    }
    public NotVaildAutosarFileException(String S){
        super(S);
    }
}
class EmptyAutosarFileException extends RuntimeException{
    public EmptyAutosarFileException(){
        super("This file is empty");
    }
    public EmptyAutosarFileException(String S){
        super(S);
    }
}
class Container implements Comparable<Container>{
    private String containerID;
    private String shortName;
    private String longName;
    
    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
    
    @Override
    public String toString() {
        return   "  <CONTAINER UUID=" + this.getContainerID() + ">\n"
                +"      <SHORT-NAME>" + this.getShortName() +"</SHORT-NAME>\n" 
                +"      <LONG-NAME>" + this.getLongName() + "/<LONG-NAME>\n"
                +"  </CONTAINER>\n";
    }

    @Override
    public int compareTo(Container c) {
        if (this.getShortName().charAt(9)>c.getShortName().charAt(9)){
            return 1;
        }
        else if (this.getShortName().charAt(9)<c.getShortName().charAt(9)){
            return -1;
        }
        else return 0;
    }

}