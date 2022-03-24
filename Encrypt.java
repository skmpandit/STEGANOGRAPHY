import java.io.*;
import java.math.BigInteger;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

class EncryptPanel extends JPanel implements ActionListener{
	JLabel l1,l2,l3;
	JPasswordField pw;
	JTextField t1,t2;
	JButton b1,b2,b3,ok;
	File textfile,imagefile;
	static final int N=50;
	RSA rsa;
	EncryptPanel(){
		
		rsa=new RSA();

		l1=new JLabel("Select the text file: ");
		l2=new JLabel("Select an image:");
		l3=new JLabel("Password:");
		
		ok=new JButton("OK");
		pw=new JPasswordField();


		l3.setVisible(false);
		ok.setVisible(false);
		pw.setVisible(false);
		
				
		t1=new JTextField(40);
		t2=new JTextField(40);
		
		b1=new JButton("Choose");
		b2=new JButton("Choose");
		
		b3=new JButton("Encrypt");
		
		

		b1.addActionListener(this);		
		b2.addActionListener(this);
		b3.addActionListener(this);		
		
		ok.addActionListener(this);
		


		setLayout(null);
		l1.setBounds(20,90,200,20);
		t1.setBounds(200,90,200,20);
		b1.setBounds(450,90,80,20);

		l2.setBounds(20,120,200,20);
		t2.setBounds(200,120,200,20);
		b2.setBounds(450,120,80,20);

		l3.setBounds(20,150,200,20);
		pw.setBounds(200,150,100,20);
		ok.setBounds(450,150,80,20);

		b3.setBounds(200,200,80,20);
		

		add(l3);
		add(ok);
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
FileNameExtensionFilter filter=new FileNameExtensionFilter("Text File","txt");
			file_chooser.setFileFilter(filter);
			if(v==JFileChooser.APPROVE_OPTION){
				textfile=file_chooser.getSelectedFile();
				String abspath=textfile.getAbsolutePath();
				t1.setText(abspath);
			}	
			
		}

		if(ob==b2){
		JFileChooser file_chooser=new JFileChooser();
		int v=file_chooser.showSaveDialog(this);
		FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & GIF","jpg","gif","png");
		file_chooser.setFileFilter(filter);
		if(v==JFileChooser.APPROVE_OPTION){
		imagefile=file_chooser.getSelectedFile();
		String abspath=imagefile.getAbsolutePath();
		t2.setText(abspath);
			}
		}


		if(ob==b3){
			l3.setVisible(true);
			ok.setVisible(true);
			pw.setVisible(true);
			b3.setEnabled(false);
			ok.setEnabled(true);
		}

		
		if(ob==ok){
			
			try{
				
				String text="";
				FileInputStream fis=new FileInputStream(textfile);
				int i=0;
				while((i=fis.read())!=-1){
						text=text+(char)i;
				}
				System.out.println("Actual Message: "+text);

				/*--- dividing the text into N/8 i.e 6 bytes each and encrypting each chunk -----*/
				String enc_msg="";
				int max=N/8;
				for(i=0;i<text.length();i=i+max){
						if(text.length()-i>max){
							int j=i+max;
					String sub=text.substring(i,j);
					byte b[]=sub.getBytes();
					BigInteger bin=new BigInteger(b);
					BigInteger enc=rsa.encrypt(bin);
					String enc_s=enc.toString();
					enc_msg=enc_msg+enc_s+"#";
							
							System.out.println(sub+"->"+enc_s);
						}
						else{
							String sub=text.substring(i);
							byte b[]=sub.getBytes();
							BigInteger bin=new BigInteger(b);
							BigInteger enc=rsa.encrypt(bin);
							String enc_s=enc.toString();
							enc_msg=enc_msg+enc_s+"#";
							
							System.out.println(sub+"->"+enc_s);
						}
				}
				System.out.println("Encrypted Message: "+enc_msg);
				

String pwd=pw.getText();
if(pwd.length()>=4 && pwd.length()<=6){
byte bb[]=pwd.getBytes();
BigInteger pass=new BigInteger(bb);
BigInteger encpwd=rsa.encrypt(pass);
enc_msg=encpwd+"#"+enc_msg+rsa.getModulus()+"#"+rsa.getPrivateKey();
System.out.println("Encrypted Message with password and key: "+enc_msg);
	
enc_msg=" "+enc_msg;
									
FileOutputStream out=new FileOutputStream(imagefile,true);
byte ascii[]=enc_msg.getBytes();
out.write(ascii);
					
JOptionPane.showMessageDialog(this,"Message has been encrypted to "+imagefile.getName());
					
					out.close();	
					fis.close();
	
					ok.setEnabled(false);
				//	b4.setEnabled(true);
				}
				else
					JOptionPane.showMessageDialog(this,"Password must contain 4 to 6 chars");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}

		}

		
	}
}
public class Encrypt extends JFrame{
	Encrypt(String title){
		super(title);

		add(new EncryptPanel());
		setVisible(true);
		setSize(600,300);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	public static void main(String args[]){
		new Encrypt("Encrypt");
		

	}
}
