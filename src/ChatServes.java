import java.io.*;
import java.util.*;
import java.net.*;
public class ChatServes {//���ڴ˷�������ı����ܱ��ڲ��࣬�������� 
	List<Client> clients = new ArrayList<Client>();
	ServerSocket ss = null;
	
	public static void main(String[] args) {
		new ChatServes().start();
	}

	public void start (){//�����ɴ�����ں�����  ����������˼ά ����˾�̬�������޷�new���ڲ��Ǿ�̬��Ķ��������
		boolean started = false;
		try {
			ss = new ServerSocket(8888);
			started = true;
		}catch(BindException e){
			System.out.println("�˿�ʹ����...");
			System.out.println("��ص�����������������");
			System.exit(0);
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			while(started){
				Socket s = ss.accept();
				System.out.println("a client connected");
				Client c = new Client(s);
				new Thread(c).start();
				clients.add(c);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				ss.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	class Client implements Runnable{
		
		private Socket s;//ֻҪSocket�ڴ����г��־�Ҫ����Ŀ�ʼ���ж���
		private DataOutputStream dos = null;
		private DataInputStream dis = null;
		private boolean connected = false; 
		Client(Socket s){
			this.s = s;
			connected = true;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void send(String str){
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("һ���ͻ������˳�");
			}
		}
		
		public void run() {
			try{
				while(connected){
					String str = dis.readUTF();
//System.out.println(str);
					for(int i=0; i<clients.size();i++){
						Client c = clients.get(i);
						c.send(str);
					}
				}
			} catch(EOFException e){
				System.out.println("a client closed");
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dis != null)dis.close();
					if(s != null)s.close();
					if(dos != null) dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}	
	}
}
