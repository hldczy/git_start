package com.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import com.orm.Tdiaocha;
import com.orm.Tdiaochajieguo;
import com.util.jixiaoService;
import com.util.yuangongService;
public class diaocha_servlet extends HttpServlet
{
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException 
	{
        String type=req.getParameter("type");
		
		if(type.endsWith("diaochaMana"))
		{
			diaochaMana(req, res);
		}
		if(type.endsWith("diaochaAdd"))
		{
			diaochaAdd(req, res);
		}
		if(type.endsWith("diaochaDel"))
		{
			diaochaDel(req, res);
		}
		if(type.endsWith("diaochaAll"))
		{
			diaochaAll(req, res);
		}
		if(type.endsWith("diaochajieguoAdd"))
		{
			diaochajieguoAdd(req, res);
		}
		
		if(type.endsWith("diaochajieguoMana"))
		{
			diaochajieguoMana(req, res);
		}
	}
	
	
	public void diaochaAdd(HttpServletRequest req,HttpServletResponse res)//������Ŀ���
	{
		String name=req.getParameter("name");
		String shijian=req.getParameter("shijian");
		String del="no";
		
		String sql="insert into t_diaocha(name,shijian,del) values(?,?,?)";
		Object[] params={name,shijian,del};
		DB mydb=new DB();
		mydb.doPstm(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "�����ɹ�");
		req.setAttribute("path", "diaocha?type=diaochaMana");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
		
	}
	
	public void diaochaDel(HttpServletRequest req,HttpServletResponse res)//������Ŀɾ��
	{
		String sql="update t_diaocha set del='yes' where id="+Integer.parseInt(req.getParameter("id"));
		Object[] params={};
		DB mydb=new DB();
		mydb.doPstm(sql, params);
		mydb.closed();
		
		req.setAttribute("message", "�����ɹ�");
		req.setAttribute("path", "diaocha?type=diaochaMana");
		
        String targetURL = "/common/success.jsp";
		dispatch(targetURL, req, res);
	}

	public void diaochaMana(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{                                                      
		List diaochaList=new ArrayList();
		String sql="select * from t_diaocha where del='no'";
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tdiaocha diaocha=new Tdiaocha();
				
				diaocha.setId(rs.getInt("id"));
				diaocha.setName(rs.getString("name"));
				diaocha.setShijian(rs.getString("shijian"));
				diaochaList.add(diaocha);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("diaochaList", diaochaList);
		req.getRequestDispatcher("admin/diaocha/diaochaMana.jsp").forward(req, res);
	}
	
	
	public void diaochaAll(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{                                             //������Ŀ�����鿴���
		List diaochaList=new ArrayList();
		String sql="select * from t_diaocha where del='no'";
		Object[] params={};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tdiaocha diaocha=new Tdiaocha();
				
				diaocha.setId(rs.getInt("id"));
				diaocha.setName(rs.getString("name"));
				diaocha.setShijian(rs.getString("shijian"));
				diaochaList.add(diaocha);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		
		req.setAttribute("diaochaList", diaochaList);
		req.getRequestDispatcher("admin/diaocha/diaochaAll.jsp").forward(req, res);
	}
	
	public void diaochajieguoAdd(HttpServletRequest req,HttpServletResponse res)
	{
		int diaocha_id=Integer.parseInt(req.getParameter("diaocha_id"));
		int yuangong_id=Integer.parseInt(req.getParameter("yuangong_id"));
		
		String daan=req.getParameter("daan");
		String shijian=new Date().toLocaleString();
		
		String sql="insert into t_diaochajieguo(diaocha_id,yuangong_id,daan,shijian) values(?,?,?,?)";
		Object[] params={diaocha_id,yuangong_id,daan,shijian};
		DB mydb=new DB();
		mydb.doPstm(sql, params);
		mydb.closed();
		
		req.setAttribute("msg", "�����ɹ�");
        String targetURL = "/common/msg.jsp";
		dispatch(targetURL, req, res);
		
	}
	
	public void diaochajieguoMana(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException
	{
		List diaochajieguoList=new ArrayList();
		String sql="select * from t_diaochajieguo where diaocha_id=?";
		Object[] params={Integer.parseInt(req.getParameter("diaocha_id"))};
		DB mydb=new DB();
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			while(rs.next())
			{
				Tdiaochajieguo diaochajieguo=new Tdiaochajieguo();
				
				diaochajieguo.setId(rs.getInt("id"));
				diaochajieguo.setDiaocha_id(rs.getInt("diaocha_id"));
				diaochajieguo.setYuangong_id(rs.getInt("yuangong_id"));
				diaochajieguo.setDaan(rs.getString("daan"));
				diaochajieguo.setShijian(rs.getString("shijian"));
				diaochajieguoList.add(diaochajieguo);
		    }
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		mydb.closed();
		System.out.println(diaochajieguoList.size()+"^^^");
		req.setAttribute("diaochajieguoList", diaochajieguoList);
		req.getRequestDispatcher("admin/diaocha/diaochajieguoMana.jsp").forward(req, res);
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
