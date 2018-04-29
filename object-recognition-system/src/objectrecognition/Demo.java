package objectrecognition;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import org.gui.JDirectoryDialog;

public class Demo implements ActionListener
{
    JLabel j1;
    JLabel j2;
    JTextField jt1;
    JTextField jt2;
    JButton jb1;
    JButton jb2;
    JButton jb3;
    File f;
    File f1;
    String path;          // path of the input directory
    String path1;        // path of the input image
    String msg;
    private JDirectoryDialog directoryDialog;

    public Demo()
    {
        JFrame j= new JFrame("Face Recognition");
        Container c= j.getContentPane();
        c.setLayout(null);
        j1= new JLabel("Select the directory");
        j2= new JLabel("Select the File");
        jt1= new JTextField();
        jt2= new JTextField();
        jb1= new JButton("Compare");            //  a button in GUI for Compare
        jb2= new JButton("Browse");              //  button for browsing the directory
        jb3= new JButton("Browse");              //  button for browsing the location of image
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);

        j1.setBounds(20,50,150,25);
        c.add(j1);
        jt1.setBounds(150,50,120,25);
        c.add(jt1);
        j2.setBounds(20,80,150,25);
        c.add(j2);
        jt2.setBounds(150,80,120,25);
        c.add(jt2);
        jb2.setBounds(300,50,120,25);
        c.add(jb2);
        jb3.setBounds(300,80,120,25);
        c.add(jb3);
        jb1.setBounds(200,250,120,25);
        c.add(jb1);
        j.setSize(500,500);
        j.setVisible(true);
    }


    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource()==jb2)
        {
            if(directoryDialog == null)
            {
                directoryDialog = new JDirectoryDialog(null);
            }
            if(directoryDialog.showDirectoryDialog())
            {
                File destFile = directoryDialog.getSelectedFolder();
                path =destFile.getAbsolutePath();
                jt1.setText(path);
            }
        }

        else if(ae.getSource()==jb3)
        {
            JFileChooser tFileChooser=new JFileChooser();
            tFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int tResult=tFileChooser.showOpenDialog(null);

            if(tResult==JFileChooser.APPROVE_OPTION)
            {
                path1=tFileChooser.getSelectedFile().toString();
                jt2.setText(path1);
            }
        }

        else
        {
            if(path==null)
            {	JOptionPane.showMessageDialog(null,"Please select database","database not selected",1);
            }
            if(path1==null)
            {	JOptionPane.showMessageDialog(null,"Please select input file","input not selected",1);
            }
            TestFaceRecognition t=new TestFaceRecognition(path,path1);
        }

    }

}

class sow
{
    public static void main(String a[])
    {
        Demo d= new Demo();
    }
}
