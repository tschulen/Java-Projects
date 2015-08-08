  package tschulenbergaddressbook;


public class Contact {
	
	public String ID;
	public String lastName;
	public String firstName;
	public String phoneNumber;
	public String email;
	
	 Contact prev; //linked list
	 Contact next;
	
	
	public String getID(){
		return ID;
	}
	public void setID(String iDString) {
		this.ID = iDString;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

	

}