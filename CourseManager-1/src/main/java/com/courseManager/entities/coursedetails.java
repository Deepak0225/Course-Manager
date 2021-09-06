package com.courseManager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "coursedetails")
public class coursedetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idc;
	
	private int courseID;
	private String courseName;
	private String courseProficiencyLevel;
	private String courseDuration;
	private String courseCompletionDate;
	private String courseDescription;
	
	@ManyToOne
	private User user;
	
	
	public int getIdc() {
		return idc;
	}
	public void setIdc(int idc) {
		this.idc = idc;
	}
	public int getCourseID() {
		return courseID;
	}
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseDuration() {
		return courseDuration;
	}
	public void setCourseDuration(String courseDuration) {
		this.courseDuration = courseDuration;
	}
	public String getCourseCompletionDate() {
		return courseCompletionDate;
	}
	public void setCourseCompletionDate(String courseCompletionDate) {
		this.courseCompletionDate = courseCompletionDate;
	}
	public String getCourseDescription() {
		return courseDescription;
	}
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	
	
	
	public String getCourseProficiencyLevel() {
		return courseProficiencyLevel;
	}
	public void setCourseProficiencyLevel(String courseProficiencyLevel) {
		this.courseProficiencyLevel = courseProficiencyLevel;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "coursedetails [idc=" + idc + ", courseID=" + courseID + ", courseName=" + courseName
				+ ", courseDuration=" + courseDuration + ", courseCompletionDate=" + courseCompletionDate
				+ ", courseDescription=" + courseDescription + "]";
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.idc==((coursedetails)obj).getIdc();
	}
	
	
	
	
}
