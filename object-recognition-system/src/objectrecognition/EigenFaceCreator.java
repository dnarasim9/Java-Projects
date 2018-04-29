package objectrecognition;

import java.io.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;

public class EigenFaceCreator
{
    private File root_dir;
    private static final int MAGIC_SETNR = 16;
    private FaceBundle[] b = null;

    /**
     * Our threshold for accepting the matched image. Anything above this
     * number is considered as not found in any of the face-spaces.
     */
    public static double THRESHOLD = 4.0;
    public double DISTANCE = Double.MAX_VALUE;

    public int USE_CACHE = 1;

    /*
     * Match against the given file.
     * @return  The Identifier of the image in the face-space. If image not
     * found (based on THRESHOLD) null is returned.
     */
    public String checkAgainst(String f) throws FileNotFoundException, IOException {
        String id = null;
        if (b != null)
        {
            double small = Double.MAX_VALUE;
            int idx = -1;
            double[] img = readImage(f);

            for (int i = 0; i < b.length; i++)
            {
                b[i].submitFace(img);
                if (small > b[i].distance() ) {
                    small = b[i].distance();
                    idx = i;
                }
            }

            DISTANCE = small;
            if (small < THRESHOLD)
                id = b[idx].getID();
        }
        return id;
    }
    /**
     * Construct the face-spaces from the given directory. There must be at least sixteen images in that directory and
     *each image must have the same dimensions. The face-space bundles are also cached in that directory for speeding
     * up further initialization.
     */
    public void readFaceBundles(String n) throws FileNotFoundException, IOException, IllegalArgumentException,  ClassNotFoundException
    {

        root_dir = new File(n);
        File[] files= root_dir.listFiles(new ImageFilter());
        Vector filenames = new Vector();
        String[] set = new String[MAGIC_SETNR];
        int i= 0;

        // Sort the list of filenames
        for ( i = 0; i < files.length; i++)
        {
            filenames.addElement(files[i].getName());
        }
        Collections.sort((List)filenames);
        b = new FaceBundle[(files.length / MAGIC_SETNR)+1];

        // Read each set of 16 images.
        for (i = 0; i < b.length; i++)
        {
            for (int j = 0; j < MAGIC_SETNR;j++)
            {
                if (filenames.size() > j+MAGIC_SETNR*i)
                {
                    set[j] = (String)filenames.get(j+MAGIC_SETNR*i);
                }
            }
            b[i] = submitSet(root_dir.getAbsolutePath() + "/",set);
        }
    }

    /*
     * Submit a set of sixteen images in the directory and construct a face-space object. This
     * can be done either by reading the cached objects (if there are any)
     */

    private FaceBundle submitSet(String dir, String[] files) throws FileNotFoundException, IOException,
            IllegalArgumentException, ClassNotFoundException
    {

        if (files.length != MAGIC_SETNR)
            throw new IllegalArgumentException("Can only accept a set of "+MAGIC_SETNR+" files.");

        FaceBundle bundle  = null;
        int i =0;
        String name = "cache";

        try{
            for (i = 0; i < files.length; i++)
            {
                name = name + files[i].substring(0,files[i].indexOf('.')); // Construct the cache name
            }
        }


        catch (NullPointerException e)
        {  	JOptionPane.showMessageDialog(null,"database contains files other than imges or folder is empty","error",1);
        }
        // Check to see if a FaceBundle cache has been saved

        File f = new File(dir + name + ".cache");

        if (f.exists() && (USE_CACHE > 0))
            bundle = readBundle(f);
        else
        {
            bundle = computeBundle(dir, files);
            if (USE_CACHE > 0)
                saveBundle(f, bundle);
        }
        return bundle;
    }

    private void saveBundle(File f, FaceBundle bundle) throws FileNotFoundException,
            IOException
    {
        f.createNewFile();
        FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
        ObjectOutputStream fos = new ObjectOutputStream(out);
        fos.writeObject(bundle);
        fos.close();
    }

    private FaceBundle readBundle(File f) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream in = new FileInputStream(f);
        ObjectInputStream fo = new ObjectInputStream(in);
        FaceBundle bundle = (FaceBundle)fo.readObject();
        fo.close();
        return bundle;
    }

    private FaceBundle computeBundle(String dir, String[] id) throws IllegalArgumentException,
            FileNotFoundException, IOException
    {
        xxxFile[] files = new xxxFile[MAGIC_SETNR];
        xxxFile file = null;
        String temp = null;
        int width = 0;
        int height = 0;
        int i = 0;

        for (i = 0; i < files.length; i++)
        {
            temp = id[i].toLowerCase();
            temp = temp.substring(temp.lastIndexOf('.')+1,temp.length());
            if (temp.equals("jpg") || temp.equals("jpeg"))
                file = new JPGFile(dir+id[i]);
            else if (temp.equals("ppm") || temp.equals("pnm"))
                file = new PPMFile(dir+id[i]);
            if (file == null)
                throw new IllegalArgumentException(id[i]+" is not an image file!");

            files[i] = file;

            if (i == 0)
            {
                width = files[i].getWidth();
                height = files[i].getHeight();
            }
            if ((width != files[i].getWidth()) || (height != files[i].getHeight()) )
            { 	JOptionPane.showMessageDialog(null,"Images in database not of size
                    200x200","error",1);
                throw new IllegalArgumentException("All image files must have the same width and height!");

            }
        }

        double[][] face_v = new double[MAGIC_SETNR][width*height];

        for (i = 0; i < files.length; i++)
        {
            face_v[i] = files[i].getDouble();
        }

        return EigenFaceComputation.submit(face_v, width, height, id,true);
    }


    public double[] readImage(String f) throws FileNotFoundException, IllegalArgumentException, IOException
    {
        xxxFile file = null;
        String temp = f.toLowerCase();
        temp = temp.substring(temp.lastIndexOf('.')+1,temp.length());
        if (temp.equals("jpg"))
            file = new JPGFile(f);
        else if (temp.equals("ppm") || temp.equals("pnm"))
            file = new PPMFile(f);
        if (file == null)
        {	JOptionPane.showMessageDialog(null,f+" is not image","error",1);
            throw new IllegalArgumentException(f+" is not an image file!");
        }
        return file.getDouble();
    }

}

//for filetering of images into jpeg or jpg
class ImageFilter implements FileFilter
{
    public boolean accept(File f)
    {
        if (f.isDirectory())
            return true;
    }

    String extension = f.getName();
    int i = extension.lastIndexOf('.');

       	if (i > 0 &&  i < extension.length() - 1)
    extension = extension.substring(i+1).toLowerCase();

       	if (extension != null)
    {
        if ((extension.equals("ppm")) || (extension.equals("pnm")) || (extension.equals("jpg")) ||
                (extension.equals("jpeg")))
            return true;
    }
else return false;
           	return false;
}


