package LoadingData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.topica.edu.itlab.jdbc.tutorial.annotation.Column;
import com.topica.edu.itlab.jdbc.tutorial.annotation.OneToMany;
import com.topica.edu.itlab.jdbc.tutorial.annotation.Table;
import com.topica.edu.itlab.jdbc.tutorial.entity.ClassEntity;
import com.topica.edu.itlab.jdbc.tutorial.entity.StudentEntity;

/*
 * this class have some method process
 * connect database
 * lazyloading data
 * eagerloading data
 */
public class Process {
	private final static String name="root";
	private final static String password="";
	private final static String port="3306";
	private final static String dbName="demo";
	
	public static Statement connectDB() {
		
		Statement stmt=null;
		Connection con;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:"+port+"/"+dbName+"?serverTimezone=UTC",name,password);
			stmt=con.createStatement(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return stmt;
	}
	
	public static List<ClassEntity> loadListClass(Statement stmt){
		List<ClassEntity> listC=new ArrayList<ClassEntity>();
		String sql="select * from "+ClassEntity.class.getAnnotation(Table.class).name();
		
		try {
			String columnId=ClassEntity.class.getDeclaredField("id").getAnnotation(Column.class).name();
			String columnName=ClassEntity.class.getDeclaredField("name").getAnnotation(Column.class).name();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {
				ClassEntity classEn=new ClassEntity();
				
				classEn.setId(rs.getLong(columnId));
				classEn.setName(rs.getString(columnName));
				listC.add(classEn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listC;
	}
	
	public static List<StudentEntity> loadListStudent(Statement stmt, ClassEntity c){

		List<StudentEntity> listSt=new ArrayList<StudentEntity>();
		try {
			String table=ClassEntity.class.getDeclaredField("listStudent").getAnnotation(OneToMany.class).mappedBy();
			String columnId=StudentEntity.class.getDeclaredField("id").getAnnotation(Column.class).name();
			String columnName=StudentEntity.class.getDeclaredField("name").getAnnotation(Column.class).name();
			String sql="select * from "+table;
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {
				if(rs.getLong("class_id")==c.getId()) {
					StudentEntity st=new StudentEntity();
					st.setId(rs.getLong(columnId));
					st.setName(rs.getString(columnName));
					listSt.add(st);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listSt;
	}
	
	public static List<ClassEntity> eagerLoading(Statement stmt){
		List<ClassEntity> list=new ArrayList<ClassEntity>();
		String sql="Select c.id as class_id,c.name as class_name,s.id as student_id, s.name as student_name from Class c, Student s where c.id = s.class_id";
		try {
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {
				StudentEntity st=new StudentEntity();
				st.setId(rs.getLong("student_id"));
				st.setName(rs.getString("student_name"));
				boolean existed=false;
				for(ClassEntity cl: list) {
					if(cl.getId()==rs.getLong("class_id")) {
						List<StudentEntity> listStu=cl.getListStudent();
						listStu.add(st);
						cl.setListStudent(listStu);
						existed=true;
					}
				}
				if(!existed) {
					ClassEntity classEn=new ClassEntity();
					classEn.setId(rs.getLong("class_id"));
					classEn.setName(rs.getString("class_name"));
					List<StudentEntity> listStu1=new ArrayList<StudentEntity>();
					listStu1.add(st);
					classEn.setListStudent(listStu1);
					list.add(classEn);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
