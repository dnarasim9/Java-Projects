/*------ main module------*/ 
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import org.gui.JDirectoryDialog; 
class demo implements ActionListener
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
	
          demo()
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
		demo d= new demo();
	}
}



/*----- Module2 : Test Face Recognition------*/

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


/*-----Module 3: Eigen Face Creator-----*/

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
}


/*--------Module 4: Face Bundle---------*/

import java.io.*;
import javax.swing.*;
/**
 * This bundle has all the relevant information to try to match an image
 * The computation to be done (to find the submitted image in this
 * bundles' face-space) is quite fast.
 */
public class FaceBundle implements Serializable, Comparable 
{
private double[] avgFace = null;
private double[] cmpFace = null;
private double[][] eigVector = null;
private double[][] wk = null;
private String[] id   = null;
private transient double minD   = Double.MAX_VALUE;
public int length = Integer.MIN_VALUE;
private transient boolean computed = false;
private transient int idx = Integer.MAX_VALUE;

public FaceBundle(double[] avgF, double wk[][], double[][] eigV, String[] files) 
{

avgFace = new double[avgF.length];
  	this.wk = new double[wk.length][wk[0].length];
    	eigVector = new double[eigV.length][eigV[0].length];
length = avgFace.length;

    	System.arraycopy(avgF,0,this.avgFace,0,avgFace.length);
    	System.arraycopy(eigV,0,this.eigVector,0,eigVector.length);
    	System.arraycopy(wk,  0,this.wk, 0, wk.length);
this.id = files;
  }

public void submitFace(byte[] face) 
{
for (int i = 0; i< length;i++)
      		cmpFace[i] = (double) (face[i] & 0xFF);

   	compute();
  }


public void submitFace(int[] face)
{
for (int i = 0; i< length;i++)
      		cmpFace[i] = face[i];

   	 compute();
}

public void submitFace(double[] face) 
{
this.cmpFace = face;
compute();
}
 
 /**
   * Clear the submitted image from the face-space object.
 */
public void clearFace()
{
cmpFace = null;
    	computed = false;
  	idx = Integer.MAX_VALUE;
   	minD = Double.MAX_VALUE;
}

  /**
   * The distance of how far away the submitted image is in this
   */
public double distance() 
{
return minD;
}

public String getID()
{
return this.id[idx];
}

public String[] getNames()
{
return id;
}

  /**
   * Compare this face-space bundle to another. If this bundle has
   * a smaller distance than the other, -1 is returned. 1 if its opposite.
   * There is no checking if the other face-space bundle
   * has computed its values for the same image.
   */
  

public int compareTo(Object o)
{
if (((FaceBundle)o).minD > minD)
    		return 1;
  	if (((FaceBundle)o).minD < minD)
return -1;
return 0;
}

public String toString() 
{
if (computed)
      		return "["+id[idx] + "] with "+minD;
    	return "No image supplied";
}

private void compute() 
{
double[] inputFace = new double[length];
  	int nrfaces = eigVector.length;
    	int MAGIC_NR = wk[0].length;
   	int j, pix, image;
computed = false;
   	
try
{ 	System.arraycopy(cmpFace,0,inputFace,0,length);	 }
  	catch (ArrayIndexOutOfBoundsException e)
   	{  	JOptionPane.showMessageDialog(null," Input image not of size 200x200 ","Error",1);
    		System.exit(0);
   	}
   	for ( pix = 0; pix < inputFace.length; pix++)
{
        		inputFace[pix] = inputFace[pix] - avgFace[pix];
  	  }

double[] input_wk = new double[MAGIC_NR];
    	double temp = 0;

    	/* Subtract the image from the average image */

 	for (j = 0; j < MAGIC_NR; j++) 
{
    		temp = 0.0;
      		for ( pix=0; pix <length; pix++)
       		temp += eigVector[j][pix] * inputFace[pix];
input_wk[j] = Math.abs( temp );
    	}

    /* weight vector computation
     * Find the minimun distance from the input_wk as compared to wk
     */

double[] distance = new double[MAGIC_NR];
    	double[] minDistance = new double[MAGIC_NR];
  	idx = 0;
    	for (image = 0; image < nrfaces; image++)
{
     		temp = 0.0;
        		
for (j = 0; j < MAGIC_NR; j++) 
{
        			distance[j] = Math.abs(input_wk[j] - wk[image][j]);
               	}
if (image == 0)
          			System.arraycopy(distance,0,minDistance,0,MAGIC_NR);
  		if (sum(minDistance) > sum(distance)) 
{
this.idx = image;
          			System.arraycopy(distance,0,minDistance,0,MAGIC_NR);
        		}
 }


    /*
     * Normalize our minimum distance.
     */
if (max(minDistance) > 0.0)
      		divide(minDistance, max(minDistance));
minD = sum(minDistance);
computed = true;
}


static void divide(double[] v, double b)
{
for (int i = 0; i< v.length; i++)
      		v[i] = v[i] / b;
}

static double sum(double[] a) 
{
double b = a[0];
 	for (int i = 0; i < a.length; i++)
    		b += a[i];
return b;
}

static double max(double[] a) 
{
    double b = a[0];
    for (int i = 0; i < a.length; i++)
    if (a[i] > b) b = a[i];
return b;
}

}


/*---- Module 5 : Eigen Face Computation-----*/

public class EigenFaceComputation
{

 private final static int MAGIC_NR = 11;


public static FaceBundle submit(double[][] face_v, int width, int height, String[] id,
 boolean debug) {

int length = width*height;
int nrfaces = face_v.length;
int i, j, col,rows, pix, image;
double temp = 0.0;
double[][] faces = new double[nrfaces][length];
double[] avgF = new double[length];

   	 /*
  	   Compute average face of all of the faces. 1xN^2
    	 */

  	for ( pix = 0; pix < length; pix++) 
{
     		temp = 0;
      		for ( image = 0; image < nrfaces; image++) 
{
       			temp +=  face_v[image][pix];
    		}
    		avgF[pix] = temp / nrfaces;
    	}
 
	/* compute difference images*/

for ( image = 0; image < nrfaces; image++) 
{
      	for ( pix = 0; pix < length; pix++)
{
      			face_v[image][pix] = face_v[image][pix] - avgF[pix];
      		}
   	 }

Matrix faceM = new Matrix(face_v, nrfaces,length);
Matrix faceM_transpose = faceM.transpose();
Matrix covarM = faceM.times(faceM_transpose);

    /*
     Compute eigenvalues and eigenvector. Both are MxM
    */

EigenvalueDecomposition E = covarM.eig();

  	double[] eigValue = diag(E.getD().getArray());
double[][] eigVector = E.getV().getArray();

 /*
  * We only need the largest associated values of the eigenvalues.
   	  * Thus we sort them (and keep an index of them)
  	  */

int[] index = new int[nrfaces];
double[][] tempVector = new double[nrfaces][nrfaces];  /* Temporary new eigVector */

   	for ( i = 0; i <nrfaces; i++) /* Enumerate all the entries */
     		index[i] = i;

    	doubleQuickSort(eigValue, index,0,nrfaces-1);
int[] tempV = new int[nrfaces];
  	for ( j = 0; j < nrfaces; j++)
   		tempV[nrfaces-1-j] = index[j];
index = tempV;

for ( col = nrfaces-1; col >= 0; col --) 
{
for ( rows = 0; rows < nrfaces; rows++ )
{
       			tempVector[rows][col] = eigVector[rows][index[col]];
}
}
 	eigVector = tempVector;
  	tempVector = null;
  	eigValue = null;

Matrix eigVectorM = new Matrix(eigVector, nrfaces,nrfaces);
eigVector = eigVectorM.times(faceM).getArray();

for ( image = 0; image < nrfaces; image++)
{
    		temp = max(eigVector[image]);
for ( pix = 0; pix < eigVector[0].length; pix++)
eigVector[image][pix] = Math.abs( eigVector[image][pix] / temp);
}

double[][] wk = new double[nrfaces][MAGIC_NR]; // M rows, 11 columns




/*
  	Compute wk.
  	*/

for (image = 0; image < nrfaces; image++) 
{
   		for (j  = 0; j <  MAGIC_NR; j++) 
{
     			temp = 0.0;
    			for ( pix=0; pix< length; pix++)
          				temp += eigVector[j][pix] * faces[image][pix];
      			  wk[image][j] = Math.abs( temp );
      		}
  	  }

FaceBundle b = new FaceBundle(avgF, wk, eigVector ,id);

double[] inputFace = new double[length];
    	System.arraycopy(faces[14],0,inputFace,0,length);

return b;
  }


static double[] diag(double[][] m) 
{

double[] d = new double[m.length];
  	for (int i = 0; i< m.length; i++)
d[i] = m[i][i];
  	return d;
}

 static void divide(double[] v, double b)
{
for (int i = 0; i< v.length; i++)
 		v[i] = v[i] / b;
}
static double sum(double[] a) 
{
double b = a[0];
 	for (int i = 0; i < a.length; i++)
 		b += a[i];
return b;
}

static double max(double[] a) 
{
double b = a[0];
for (int i = 0; i < a.length; i++)
if (a[i] > b) b = a[i];
return b;
}

// Quick Sort

static void doubleQuickSort(double a[], int index[], int lo0, int hi0) 
{
     	int lo = lo0;
      	int hi = hi0;
        	double mid;

      	if ( hi0 > lo0)
{
mid = a[ ( lo0 + hi0 ) / 2 ];
while( lo <= hi ) 
{
 while( ( lo < hi0 ) && ( a[lo] < mid )) 
           			 	 ++lo;
                	                while( ( hi > lo0 ) && ( a[hi] > mid )) 
                   			 --hi;

                if( lo <= hi ) 
{
swap(a, index, lo, hi);
                			++lo;
                  			--hi;
             			}	
            		}
if( lo0 < hi )
{
              			doubleQuickSort( a, index, lo0, hi );
          		}
           		if( lo < hi0 )
{
          			doubleQuickSort( a, index,lo, hi0 );
        		}
      	}
  }

/*--------- Module 6 : Maths---------*/

public class Maths 
{
public static double hypot(double a, double b) 
{
double r;
if (Math.abs(a) > Math.abs(b)) 
{
r = b/a;
r = Math.abs(a)*Math.sqrt(1+r*r);
  		} 
else if (b != 0) 
{
        			r = a/b;
        			r = Math.abs(b)*Math.sqrt(1+r*r);
      		} 
else 
{
r = 0.0;
      		}
   	return r;
   	}
}


/*----------- Module 7 : Matrix computations-----------*/

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.text.FieldPosition;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StreamTokenizer;

public class Matrix implements Cloneable, java.io.Serializable 
{

private double[][] A;
private int m, n;
public Matrix (int m, int n) 
{
    		this.m = m;
     		this.n = n;
    		A = new double[m][n];
}

   	public Matrix (int m, int n, double s) 
{
      		this.m = m;
this.n = n;
A = new double[m][n];

for (int i = 0; i < m; i++) 
{
        			for (int j = 0; j < n; j++) 
{
          				A[i][j] = s;
       			}
    		}
   	}


public Matrix (double[][] A) 
{
 	m = A.length;
     	n = A[0].length;
     	for (int i = 0; i < m; i++) 
{
        		if (A[i].length != n) 
{
        			throw new IllegalArgumentException("All rows must have the same length.");
      		}
     	}
   	this.A = A;
   }

public Matrix (double[][] A, int m, int n) 
{
  	this.A = A;
 	this.m = m;
this.n = n;
 }

public Matrix (double vals[], int m)
{
 	this.m = m;
n = (m != 0 ? vals.length/m : 0);
      	if (m*n != vals.length) 
{
      		throw new IllegalArgumentException("Array length must be a multiple of m.");
     	}
 	A = new double[m][n];
   	for (int i = 0; i < m; i++) 
{
        		for (int j = 0; j < n; j++) 
{
           			A[i][j] = vals[i+j*m];
       		}
    	}
  }

public static Matrix constructWithCopy(double[][] A)
{
   	int m = A.length;
     	int n = A[0].length;
  	Matrix X = new Matrix(m,n);
double[][] C = X.getArray();
   	
for (int i = 0; i < m; i++)
{
    		if (A[i].length != n)
{
          		  throw new IllegalArgumentException("All rows must have the same length.");
       		}
      		for (int j = 0; j < n; j++) 
{
           			C[i][j] = A[i][j];
        		}
     	}
    	return X;
   }


public Matrix copy () 
{
    	Matrix X = new Matrix(m,n);
     	double[][] C = X.getArray();
      	for (int i = 0; i < m; i++)
{
        		for (int j = 0; j < n; j++) 
{
          			C[i][j] = A[i][j];
    		}
      	}
   	return X;
 }

public Object clone () 
{
     	return this.copy();
}

public double[][] getArray ()
{
    	return A;
 }

public double[][] getArrayCopy ()
{
   	double[][] C = new double[m][n];
   	for (int i = 0; i < m; i++)
{
        		for (int j = 0; j < n; j++) 
{
            			C[i][j] = A[i][j];
    		}
}
   	return C;
  }

public double[] getColumnPackedCopy ()
{
 	double[] vals = new double[m*n];
  	
for (int i = 0; i < m; i++) 
{
for (int j = 0; j < n; j++) 
{
vals[i+j*m] = A[i][j];
        		}
    	}
     	return vals;
}

public int getRowDimension ()
{
return m;
}

public int getColumnDimension () 
{
return n;
}

public double get (int i, int j) 
{
    	return A[i][j];
}

   /** Get a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param j0   Initial column index
   @param j1   Final column index
   @return     A(i0:i1,j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

public Matrix getMatrix (int i0, int i1, int j0, int j1) 
{
 	Matrix X = new Matrix(i1-i0+1,j1-j0+1);
  	double[][] B = X.getArray();
    	try {
     		for (int i = i0; i <= i1; i++) 
{
          			for (int j = j0; j <= j1; j++) 
{
               			B[i-i0][j-j0] = A[i][j];
      			}
}
   	   } 
catch(ArrayIndexOutOfBoundsException e) 
{
       		throw new ArrayIndexOutOfBoundsException("Submatrix indices");
 	}
   	return X;
}

 /** Set a submatrix.
   @param r    Array of row indices.
   @param j0   Initial column index
   @param j1   Final column index
   @param X    A(r(:),j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

public void setMatrix (int[] r, int j0, int j1, Matrix X) 
{
   	try {
   		for (int i = 0; i < r.length; i++) 
{
           			for (int j = j0; j <= j1; j++) 
{
        				A[r[i]][j] = X.get(i,j-j0);
        			}
        		}
     	      } 
catch(ArrayIndexOutOfBoundsException e) 
{
        		throw new ArrayIndexOutOfBoundsException("Submatrix indices");
}
}

public Matrix transpose ()
{
  	Matrix X = new Matrix(n,m);
    	double[][] C = X.getArray();
 	for (int i = 0; i < m; i++) 
{
       		for (int j = 0; j < n; j++) 
{
      			C[j][i] = A[i][j];
        		}
   	}
    	return X;
}

public void print (PrintWriter output, NumberFormat format, int width) 
{
output.println();                              // start on new line
      	for (int i = 0; i < m; i++)
{
      		for (int j = 0; j < n; j++)
{
          			String s = format.format(A[i][j]);                      // format the number
          			int padding = Math.max(1,width-s.length());   // At _least_ 1 space
        			for (int k = 0; k < padding; k++)
           			output.print(' ');
            			output.print(s);
      		}
      		output.println();
      	}
     	output.println();   // end with blank line
}


/*--------- Module 8 : Image processing------*/

import java.awt.*;
import java.awt.image.*;

/**
 * Canvas used to display any type of image. As long as its submitted in byte[], int[] or double[] format with the  
 *   appropiate width and height. Also the image is converted into grayscale.
*/

class ImageCanvas extends Canvas 
{
private Image memImage=null;      // for image construction

  	public void readImage(byte[] bytes, int width, int height)
{
		int pix[] = new int[width * height];
		int index = 0;
       		int ofs = 0;

  		for (index = 0; index < pix.length-2; index++) 
{
        			pix[index] = 255 << 24 /*alpha*/ |
                  		(int)(bytes[ofs] & 0xFF) << 16 /*R*/ |
                 		(int)(bytes[ofs] & 0xFF) << 8 /*G*/ |
                		(int)(bytes[ofs] & 0xFF) /*B*/;
          			ofs += 1;
   		}

      	memImage = createImage(new MemoryImageSource(width, height, pix, 0, width));
	repaint();
}

public void readImage(double[] doubles,  int width, int height) 
{
int w = width;
		int h = height;
		int pix[] = new int[w * h];
		int index = 0;
       		int avg = 0;
      		for (index = 0; index < pix.length-2; index++) 
{
avg = (int)doubles[index];
  			pix[index] = 255 << 24 |/* avg << 16 | avg << 8 |*/ avg;
}

memImage = createImage(new MemoryImageSource(width, height, pix, 0, 
                                             width));
		repaint();
 	}

public void readImage(int[] ints, int width, int height) 
{
memImage = createImage(new MemoryImageSource(width, height, ints, 0, 
                                            width));
		repaint();
   	}


public void paint(Graphics g) 
{
		Dimension d = getSize();      // get size of drawing area
		g.setColor(getBackground());  // clear drawing area
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(getForeground());

		if (memImage != null) 
{
	   		g.drawImage(memImage, 0, 0, this);
		}
}
}

