package LoadingData;

import java.sql.Statement;

import java.util.List;

import com.topica.edu.itlab.jdbc.tutorial.entity.ClassEntity;

/*
 * example about eager loading
 */
public class EagerLoading {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statement stmt=Process.connectDB();
		List<ClassEntity> listC=Process.eagerLoading(stmt);
		for(ClassEntity classE: listC) {
			System.out.println(classE.toString());
		}
		

	}

}
