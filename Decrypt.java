import java.io.*;
import java.math.BigInteger;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

class DecryptPanel extends JPanel implements ActionListener{
	JLabel l1,l2,l3;
	JPasswordField pw;
	JTextField t1,t2;
	JButton b1,b2,b3;
	File textfile,imagefile;
	static final int N=50;
	
	DecryptPanel(){
		
		l1=new JLabel("Select the image file: ");
		l2=new JLabel("Select a text file:");
		l3=new JLabel("Password:");
		
		
		pw=new JPasswordField();


	
				
		t1=new JTextField(40);
		t2=new JTextField(40);
		
		b1=new JButton("Choose");
		b2=new JButton("Choose");
		
		b3=new JButton("Decrypt");
		
	

		b1.addActionListener(this);		
		b2.addActionListener(this);
		b3.addActionListener(this);		
		
		
		


		setLayout(null);
		l1.setBounds(20,90,200,20);
		t1.setBounds(200,90,200,20);
		b1.setBounds(450,90,80,20);

		l2.setBounds(20,120,200,20);
		t2.setBounds(200,120,200,20);
		b2.setBounds(450,120,80,20);

		l3.setBounds(20,150,200,20);
		pw.setBounds(200,150,100,20);
	

		b3.setBounds(200,200,80,20);
		

		add(l3);
		add(pw);

		add(l1);
		add(t1);
		add(b1);

		add(l2);
		add(t2);
		add(b2);

		add(b3);
		


	}
	public void actionPerformed(ActionEvent e){
		Object ob=e.getSource();
		if(ob==b1){
			JFileChooser file_chooser=new JFileChooser();
			int v=file_chooser.showOpenDialog(this);
			FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & GIF File","jpg","gif","png");
			file_chooser.setFileFilter(filter);
			if(v==JFileChooser.APPROVE_OPTION){
				imagefile=file_chooser.getSelectedFile();
				String abspath=imagefile.getAbsolutePath();
				t1.setText(abspath);
			}	
			
		}

		if(ob==b2){
			JFileChooser file_chooser=new JFileChooser();
			int v=file_chooser.showSaveDialog(this);
			FileNameExtensionFilter filter=new FileNameExtensionFilter("Text File","txt");
			file_chooser.setFileFilter(filter);
			if(v==JFileChooser.APPROVE_OPTION){
				textfile=file_chooser.getSelectedFile();
				String abspath=textfile.getAbsolutePath();
				t2.setText(abspath);
			}
		}


		if(ob==b3){
			try{
				int index=0,cnt=0;
				RandomAccessFile file=new RandomAccessFile(imagefile,"r");
				int i=0;
				while((i=file.read())!=-1){
					cnt++;
					if(i==32)
						index=cnt;
				}
				file.seek(index);
				String str="";
				while((i=file.read())!=-1){
					str=str+(char)i;
				}
				System.out.println("str: "+str);
				str=str.trim();
				
				String msg[]=str.split("#");
				String encpwd=msg[0];
				String modulus=msg[msg.length-2];
				String privatekey=msg[msg.length-1];
				
				System.out.println("Enc Password: "+encpwd);
				System.out.println("Modulus: "+modulus);
				System.out.println("Private Key: "+privatekey);
				
				BigInteger p=new BigInteger(encpwd);
				BigInteger m=new BigInteger(modulus);
				BigInteger pk=new BigInteger(privatekey);
				BigInteger pwd=p.modPow(pk,m);
				
				byte b[]=pwd.toByteArray();
				String pass=new String(b);
				System.out.println("Actual Password: "+pass);
				
				String passwd=pw.getText();
				if(pass.equals(passwd)){
					String original="";
					for(i=1;i<=msg.length-3;i++){
						BigInteger em=new BigInteger(msg[i]);
						BigInteger dm=em.modPow(pk,m);
						byte b1[]=dm.toByteArray();
						String om=new String(b1);
						original=original+om;
					}
					System.out.println("Original Message: "+original);
					FileOutputStream out=new FileOutputStream(textfile);
					out.write(original.getBytes());
					JOptionPane.showMessageDialog(this,"Message has been successfully decrypted to "+textfile.getName());
					out.close();
				}
				else
					JOptionPane.showMessageDialog(this,"Invalid Password");
				
				
				file.close();
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
	
	}
}
public class Decrypt extends JFrame{
	Decrypt(String title){
		super(title);

		add(new DecryptPanel());
		setVisible(true);
		setSize(600,300);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	public static void main(String args[]){
		new Decrypt("Decrypt");
		

	}
}
