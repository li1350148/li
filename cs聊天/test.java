package test;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.io.*;
class word extends Thread{
	ListSocket first,p,ps,q;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	int time;
	public word(ListSocket first,ListSocket ps,Socket s){
		this.first=first;
		this.s=s;
		this.ps=ps;
	}
	public void run(){
                try{
                	dis=new DataInputStream(s.getInputStream());
                	}
              	catch(Exception e){
		        }   
		while(true){
			try{
				String str=dis.readUTF();
				p=first.next;
				while(p!=null){
					if(p.getSocket()!=s){
						dos=new DataOutputStream(p.getSocket().getOutputStream());
						dos.writeUTF(str);
					}
				    p=p.next;
				}
				}
			catch(Exception e){
				first.delete(first, ps);
				break;
			}   
		}
	}
}
class start
{
	ServerSocket ss;
    ListSocket first=new ListSocket();
    ListSocket p,p1,p2;
	Socket s=null;
    int time=0;
	public void word()
	{
		first.next=null;
		p1=first;
		try{
			ss=new ServerSocket(5555);
			}
		catch(Exception e){
			
		}			
		while(true){
		try{
			s=ss.accept();
		}
		catch(Exception e)
		{			
		}
		p=new ListSocket();
		p.setSocket(s);
		p.setid(10);
		p.next=null;
		first.add(first,p);
		System.out.println("第"+(time+1)+"个用户连接进来");
		new word(first,p,s).start();
		p2=first.next;
		while(p2!=null){
			System.out.println(p2.getid());
			p2=p2.next;
		}
		time++;
	}
	}
}
class ListSocket{
    ListSocket next;
    int id;
    int key;
    String name;
    Socket s;
    public void setid(int id){
        this.id=id;
    }
    public void setkey(int key){
        this.key=key;
    }
    public void setSocket(Socket s){
        this.s=s;
    }
    public void setnext(ListSocket next){
        this.next=next;
    }
    public void setname(String name){
    	this.name=name;
    }
    public int getid(){
        return id;
    }
    public int getkey(){
        return key;
    }
    public Socket getSocket(){
        return s;
    }
    public ListSocket getnext(){
        return next;
    }
    public String getname(){
    	return name;
    }
    public void add(ListSocket first,ListSocket next){
    	ListSocket p;
    	p=first;
    	while(p.next!=null){
    		p=p.next;
    	}
    	p.next=next;
    }
    public void delete(ListSocket first,ListSocket next){
    	ListSocket p;//指向上一节点
    	p=first;
    	while(p!=null){
    		if(p.next==next){
    			p.next=next.next;
    			break;
    		}
    		p=p.next;
    	}
    }
}
public class test
{
	public static void main(String args[]){
		new start().word();
	}
}