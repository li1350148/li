import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.io.*;
class Ui implements ActionListener{
	 JFrame jr=new JFrame("服务端-li");
	 static JTextArea jta=null;
	 static JTextField jtf=new JTextField(20);
	 JTextField jtf2=new JTextField("5555",4);
	 JScrollPane listPanel;//创建一个滚条
	 JButton jbt=null,jbt2=new JButton("创建"),jbt3=new JButton("监听");
	 JLabel jbl=new JLabel("端口");
	 JPanel pl1,pl2,pl3=new JPanel();
	 DataOutputStream dos=null;
	 DataInputStream dis=null;
	 Socket s=null;
	 ServerSocket ss=null;
	 Ui(){
		jbt=new JButton("发送");
		jr.getContentPane().setLayout(new BorderLayout());
		jta=new JTextArea(12,25);
		pl1=new JPanel();
		pl2=new JPanel();
		listPanel=new JScrollPane(jta);
		pl1.add(listPanel);
		pl2.add(jtf);
		pl2.add(jbt);
		pl3.add(jbl);
		pl3.add(jtf2);
		pl3.add(jbt2);
		pl3.add(jbt3);
		jbt3.setBounds(2, 2, 3, 3);
		jr.getContentPane().add(pl3,BorderLayout.NORTH);
		jr.getContentPane().add(pl2,BorderLayout.SOUTH);
		jr.getContentPane().add(pl1);
		jr.setLocation(350,200);
		jr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jr.setResizable(false);
		jr.setVisible(true);
		jr.pack();
		}
	void start(){
		jbt.addActionListener(this);
		jbt2.addActionListener(this);
		jbt3.addActionListener(this);
	}
  public void actionPerformed(ActionEvent e){
	  if(e.getSource()==jbt){
		  new ServerWriter(dos).start();//启动写入线程
	  }
	  else if(e.getSource()==jbt2){
		  try{
			  ss=new ServerSocket(Integer.parseInt(jtf2.getText()));
			  jta.append("创建端口"+jtf2.getText()+"成功");
			  }
		  catch(Exception a){}
		  }
	  else if(e.getSource()==jbt3){
		  try{
			  s=ss.accept();
			  dis=new DataInputStream(s.getInputStream());
			  dos=new DataOutputStream(s.getOutputStream());
			  jta.append("\n"+"用户成功连接成功....");
			  new ServerRead(dis,s).start();
		//	  s.close();
			  }
		  catch(Exception a){}
		  }
	  }
}
class ServerWriter extends Thread{
   private DataOutputStream dos;
    ServerWriter(DataOutputStream dos){
           this.dos=dos;
   }
   public void run(){
	   try{
		   String str=Ui.jtf.getText();
		   dos.writeUTF(str);
		   Ui.jta.append("\n"+"自己："+str);
		   Ui.jta.setSelectionStart(Ui.jta.getText().length());//自动向下
		   Ui.jtf.setText("");
		   }catch(Exception e){}
	   }
}
class ServerRead extends Thread {
   private DataInputStream dis;
   private Socket s;
   public ServerRead(DataInputStream dis,Socket s){
           this.dis=dis;
           this.s=s;
   }
   public void run(){
	   while(true){
		   try{
			   s.sendUrgentData(0xFF);//心跳包  用来检测对方是否已经断开连接   断开抛出异常
			   String str = dis.readUTF();
			   Ui.jta.append("\n"+"对方: " + str);
			   Ui.jta.setSelectionStart(Ui.jta.getText().length());//自动向下
			   }
		   catch(Exception e) {
			   Ui.jta.append("\n"+"对方断开连接...");
			   Ui.jta.setSelectionStart(Ui.jta.getText().length());//自动向下
			   break;
		   }
		   }
	   }
}
public class Server{
	public static void main(String args[])throws Exception{
		 new Ui().start();;
	}
}