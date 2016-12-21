import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.io.*;
 class Uia implements ActionListener{
	 JFrame jr=new JFrame("客户端-li");
	 static JTextArea jta=new JTextArea(12,25);
	 DataOutputStream dos=null;
	 DataInputStream dis=null;
	 static JTextField jtf=new JTextField(20),jtf2=new JTextField("103.44.145.243",8),jtf3=new JTextField("25873",4);
	 JScrollPane listPanel=new JScrollPane(jta);;//创建一个滚条
	 JButton jbt=new JButton("发送"),jbt2=new JButton("连接");
	 JLabel jbl=new JLabel("地址:"),jbl2=new JLabel("端口:");
	 JPanel pl1=new JPanel(),pl2=new JPanel(),pl3=new JPanel();
         Socket s=null;
	Uia(){
		jr.getContentPane().setLayout(new BorderLayout());
        pl1.add(listPanel);
        pl2.add(jtf);
        pl2.add(jbt);
        pl3.add(jbl);
        pl3.add(jtf2);
        pl3.add(jbl2);
        pl3.add(jtf3);
        pl3.add(jbt2);
        jr.getContentPane().add(pl3,BorderLayout.NORTH);
        jr.getContentPane().add(pl2,BorderLayout.SOUTH);
        jr.getContentPane().add(pl1);
        jr.setLocation(350,250);
        jr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jr.setResizable(false);
        jr.setVisible(true);
        jr.pack();
	}
	void start(){
		 jbt.addActionListener(this);
		 jbt2.addActionListener(this);
	}
    public void actionPerformed(ActionEvent e){
    	if(e.getSource()==jbt){
    		new ClientWriter(dos).start();//启动写入线程
    	}
    	else if(e.getSource()==jbt2){
    		try{
    		 s=new Socket(jtf2.getText(),Integer.parseInt(jtf3.getText()));
    		 jta.setText("恭喜你连接成功...");
             dos = new DataOutputStream(s.getOutputStream());
			 dis = new DataInputStream(s.getInputStream());//读出来
			new ClientRead(dis,s).start();//启动读取线程
                      //  s.close();
			}catch(Exception a){}

    	}
    }
 }
 class ClientWriter extends Thread{
     private DataOutputStream dos;
     public ClientWriter(DataOutputStream dos){
             this.dos=dos;
     }
     public void run(){
    	 try{
    		 String str=Uia.jtf.getText();
    		 dos.writeUTF(str);
    		 Uia.jta.append("\n"+"自己："+str);
    		 Uia.jta.setSelectionStart(Uia.jta.getText().length());//自动向下                         
    		 Uia.jtf.setText("");
    		 }catch(Exception e){}      
}
}
 class ClientRead extends Thread {
     private DataInputStream dis;
     private Socket s;
     public ClientRead(DataInputStream dis,Socket s){
             this.dis=dis;
             this.s=s;
     }
     public void run(){
    	 while(true){
    		 try{
  			     s.sendUrgentData(0xFF);//心跳包  用来检测对方是否已经断开连接   断开抛出异常
    			 String str = dis.readUTF();
    			 Uia.jta.append("\n"+"对方: " + str);
    			 Uia.jta.setSelectionStart(Uia.jta.getText().length());//自动向下
    			 }
    		 catch(Exception e) {
  			   Uia.jta.append("\n"+"对方断开连接...");
  			   Uia.jta.setSelectionStart(Uia.jta.getText().length());//自动向下
  			   break;
    		 }
           }
 //    dis.close();
     }
}
public class Client{
	public static void main(String args[])throws Exception{
		new Uia().start();;
	}
}