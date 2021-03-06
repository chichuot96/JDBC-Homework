package com.topica.edu.itlab.jdbc.tutorial.LoadingData;

import java.sql.Statement;
import java.util.List;

import com.topica.edu.itlab.jdbc.tutorial.entity.ClassEntity;
/*
 * example about lazy loading
 */
public class LazyLoading {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statement stmt=Process.connectDB();
		List<ClassEntity> listC=Process.loadListClass(stmt,ClassEntity.class);
		for(ClassEntity c:listC) {
			if(c.getListStudent()==null) {
				c.setListStudent(Process.loadListStudent(stmt, c));
			}
			System.out.println(c.toString());
		}
	}

}
