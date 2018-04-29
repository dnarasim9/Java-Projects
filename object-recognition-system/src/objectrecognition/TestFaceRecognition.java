package objectrecognition;

import java.lang.*;
        import java.io.*;
        import javax.swing.*;

public class TestFaceRecognition
{
    String dir = null;
    String file =null;

    TestFaceRecognition(String dir,String file)
    {
        this.dir=dir;
        this.file=file;
        try {
            EigenFaceCreator creator = new EigenFaceCreator();
            System.out.println("Constructing face-spaces from "+dir+" ...");
            creator.readFaceBundles(dir);

            System.out.println("Comparing "+file+" ...");
            String result = creator.checkAgainst(file);
            System.out.println(result);
            System.out.println("Most closly reseambling: "+result+" with
                    "+creator.DISTANCE+"     distance");

                    JFrame f=new JFrame(result);
            JOptionPane.showMessageDialog(null,"matched with "+result,"Macth Info",1);
            JLabel l=new JLabel( new ImageIcon("sow//"+result));
            f.add(l);
            f.setSize(320,240);
            f.setLocation(200,200);
            if(result!=null)
                f.show();
            else
            {
                JOptionPane.showMessageDialog(null,"null match found","Macth Info",1);
            }

        }

        catch (Exception e) { e.printStackTrace(); }
    }
}

