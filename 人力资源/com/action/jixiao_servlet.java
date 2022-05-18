package com.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DB;
import com.orm.Jixiao;
import com.orm.TAdmin;
import com.util.jixiaoService;
import com.util.yuangongService;
public class jixiao_servlet extends HttpServlet
{
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException 
	{
        String type=req.getParameter("type");
		
		if(type.endsWith("jixiaoMana"))
		{
			jixiaoMana(req, res);
		}
		if(type.endsWith("jixiaoAdd"))
		{
			jixiaoAdd(req, res);
		}
		if(type.endsWith("jixiaoDel"))
		{
			jixiaoDel(req, res);
		}
		if(type.endsWith("jixiaoEdit"))
		{
			jixiaoEdit(req, res);
		}
		if(type.endsWith("jixiaoChaxunByYuangong_yuefen"))
		{
			jixiaoChaxunByYuangong_yuefen(req, res);
		}
		
	}
	
	
	public void jixiaoAdd(HttpServletRequest req,HttpServletResponse res)
	{
		String jixiao_yufen=req.getParameter("jixiao_yufen");
		int jixiao_yuangong_id=Integer.parseInt(req.getParameter("jixiao_yuangong_id"));
		String jixiao_yaungong_chidao=req.getParameter("jixiao_yaungong_chidao");
		int jixiao_jiangjin=500-Integer.parseInt(jixiao_yaungong_chidao)*50;//�ٵ�һ�ο�50��һ��500�鼨Ч����
		String del="no";
		
		boolean b=jixiaoService.shifoulurujixiao(jixiao_yuangong_id, jixiao_yufen);
		System.out.println(b+"^^^");
		if(b==true)
		{
			req.setAttribute("message", "��Ա���ڴ��·ݵļ�Ч������Ϣ��¼�롣�벻Ҫ�ظ�����");
			req.setAttribute("path", "jixiao?type=jixiaoMana");
			
	        String targetURL = "/common/success.jsp";
			dispatch(targetURL, req, res);
		}
		else
		{
			String sql="insert into t_jixiao(jixiao_yufen,jixiao_yuangong_id,jixiao_yaungong_chidao,jixiao_jiangjin,del) values(?,?,?,?,?)";
			Object[] params={jixiao_yufen,jixiao_yuangong_id,jixiao_yaungong_chidao,jixiao_jiangjin,del};
			DB mydb=new DB();
			mydb.doPstm(sql, params);
			mydb.closed();
			
			req.setAttribute("message", "�����ɹ�");
			req.setAttribute("path", "jixiao?type=jixiaoMana");
			
	        String targetURL = "/common/success.jsp";
			dispatch(targetURL, req, res);
		}
		
	}
	
	public void jixiaoDel(HttpServletRequest req,HttpServletResponse res)
	{
		String sql="update t_jixiao set del='yes' where jixiao_id="+Integer.parseInt(req.getParameter("jixiao_id"));
		Object[] params={};
		DB mydb=new DB();
		mydb.doPstm(sql, params);//����DB���е�doPstm()�����������ݿ���в���,DB.java�е�doPstm()
		mydb.closed();
		
		req.setAttribute("message", "�����ɹ�");
		req.setAttribute("path", "jixiao?type=jixiaoMana");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
	}

	public void jixiaoMana(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		List jixiaoList=new ArrayList();
		String sql="select * from t_jixiao where del='no'";
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Jixiao jixiao=new Jixiao();
				
				jixiao.setJixiao_id(rs.getInt("jixiao_id"));
				jixiao.setJixiao_yufen(rs.getString("jixiao_yufen"));
				jixiao.setJixiao_yuangong_id(rs.getInt("jixiao_yuangong_id"));
				jixiao.setJixiao_yaungong_chidao(rs.getInt("jixiao_yaungong_chidao"));
				jixiao.setJixiao_jiangjin(rs.getInt("jixiao_jiangjin"));
				jixiao.setJixiao_yuangong_Name(yuangongService.getYuangongName(rs.getInt("jixiao_yuangong_id")));
				
				jixiaoList.add(jixiao);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("jixiaoList", jixiaoList);
		req.getRequestDispatcher("admin/jixiao/jixiaoMana.jsp").forward(req, res);
	}
	
	public void jixiaoEdit(HttpServletRequest req,HttpServletResponse res)
	{
		String jixiao_yufen=req.getParameter("jixiao_yufen");
		int jixiao_yuangong_id=Integer.parseInt(req.getParameter("jixiao_yuangong_id"));
		int jixiao_yaungong_chidao=Integer.parseInt(req.getParameter("jixiao_yaungong_chidao"));
		int jixiao_jiangjin=500-jixiao_yaungong_chidao*50;//�ٵ�һ�ο�50��һ��500�鼨Ч����
		
		
		String sql="update t_jixiao set jixiao_yufen=?,jixiao_yuangong_id=?,jixiao_yaungong_chidao=?,jixiao_jiangjin=? where jixiao_id="+Integer.parseInt(req.getParameter("jixiao_id"));
		Object[] params={jixiao_yufen,jixiao_yuangong_id,jixiao_yaungong_chidao,jixiao_jiangjin};
		DB mydb=new DB();
		mydb.doPstm(sql, params);
		mydb.closed();
		req.setAttribute("message", "�����ɹ�");
		req.setAttribute("path", "jixiao?type=jixiaoMana");
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
		
		
	}
	
	public void jixiaoChaxunByYuangong_yuefen(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		String yuefen=req.getParameter("yuefen");
		int yuangongId=Integer.parseInt(req.getParameter("yuangongId"));
		
		boolean b=jixiaoService.shifoulurujixiao(yuangongId, yuefen);
		if(b==false)
		{
			req.getRequestDispatcher("admin/jixiao/jixiaoWeiLuru.jsp").forward(req, res);
		}
		else
		{
			int jixiaojiangji=jixiaoService.getJixiaoJiangji(yuangongId, yuefen);
			req.setAttribute("jixiaojiangji", jixiaojiangji);
			req.getRequestDispatcher("admin/jixiao/jixiaoYiLuru.jsp").forward(req, res);
		}
	}
	
	public void dispatch(String targetURI,HttpServletRequest request,HttpServletResponse response) 
	{
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(targetURI);
		try 
		{
		    dispatch.forward(request, response);
		    return;
		} 
		catch (ServletException e) 
		{
                    e.printStackTrace();
		} 
		catch (IOException e) 
		{
			
		    e.printStackTrace();
		}
	}
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
	}
	
	public void destroy() 
	{
		
	}
}
