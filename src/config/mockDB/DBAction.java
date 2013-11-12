package config.mockDB;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.List;

public class DBAction {
	public static String pathname;
	
	public DBAction(){
	}
	
	public void setPath(String path){
		this.pathname = path;
	}
	
	public String getPath(){
		return pathname;
	}
	
	public static List<DBConfig> getScanConfig(){
		ArrayList<DBConfig> list=new ArrayList<DBConfig>();
		String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
		String url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ=" + pathname;
		Connection conn = null;
		Statement stmt = null;	
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "","");
			stmt = conn.createStatement();
			String sql = "";
			sql = "select * from ScanSet order by num";
			
			ResultSet rs = stmt.executeQuery(sql);
			/*
			 * 判断config.mdb的ScanSet表的ScanFlag列，该列为真的就将该模式加入模式列表。
			 */
			while (rs.next()) {
				DBConfig c=new DBConfig();
				c.num = rs.getInt(1);
				c.defect=rs.getString(2);
				c.category=rs.getString(3);
				c.description = rs.getString(4);
				c.scanFlag = rs.getBoolean(5);
				list.add(c);
			}
		} catch (ClassNotFoundException ex) {
			//ex.printStackTrace();
			throw new RuntimeException("Access database driver error",ex);
		} catch (SQLException ex) {
			//ex.printStackTrace();
			throw new RuntimeException("Access database connect error",ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					//ex.printStackTrace();
					throw new RuntimeException("Access database connect error",ex);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					//ex.printStackTrace();
					throw new RuntimeException("Access database connect error",ex);
				}
			}
		}
		return list;
	}
	
	public int update(Object newValue, int num){
		boolean scanflag = Boolean.parseBoolean(newValue.toString());
		String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
		String url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ=" + pathname;
		Connection conn = null;
		Statement stmt = null;	
		int i= 0;
		try {
			Class.forName(driver);
			System.out.println("config path: " + pathname);
			conn = DriverManager.getConnection(url, "","");
			stmt = conn.createStatement();
			String sql = "";
			sql = "update ScanSet set ScanFlag=" + scanflag + " where Num=" + num;
			i = stmt.executeUpdate(sql);
			return i;
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Access database driver error",ex);
		} catch (SQLException ex) {
			throw new RuntimeException("Access database connect error",ex);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					throw new RuntimeException("Access database connect error",ex);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					throw new RuntimeException("Access database connect error",ex);
				}
			}
		}
	}
}
