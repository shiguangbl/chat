import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends Frame{
	
	TextField sr = new TextField();
	TextArea ta = new TextArea();
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;

	public static void main(String[] args) {
		new ChatClient().LaunchFrame();
	}
	
	public void LaunchFrame(){
		setBounds(100,200,300,300);
		setVisible(true);
		add(sr,BorderLayout.SOUTH);
		add(ta,BorderLayout.NORTH);
		pack();//��ȥ�����ֹ�������û�ñ��ֲ�����λ��
		addWindowListener(new WindowMointor());
		sr.addActionListener(new TFlistener());
		connect();
		ReadFrame rf = new ReadFrame();
		new Thread(rf).start();//new�����̺߳�Ҫ����start����
	}
	
	public void connect(){
			try {
				s = new Socket("1.195.108.210",8888);
				dos = new DataOutputStream(s.getOutputStream());
				dis = new DataInputStream(s.getInputStream());
				System.out.println("�������������");				
			} catch (UnknownHostException e) {
				System.out.println("������δ��������������������");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void disconnect(){
		try {
			dos.flush();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class WindowMointor extends WindowAdapter
	{
		public void windowClosing(WindowEvent e){
			disconnect();
			System.exit(0);
		}
	}
	
	class TFlistener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String str = sr.getText().trim();
//			ta.setText(str);
			sr.setText("");
			try {
					dos.writeUTF(str);				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class ReadFrame implements Runnable{

		public void run() {
			try{
				while(true){
					String str = dis.readUTF();
					ta.setText(ta.getText()+str+'\n');
				}
			} catch(SocketException e){
				System.out.println("�����ѶϿ�");	
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
