package source;

import java.io.Serializable;
import java.util.Date;

public class UsersDet implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String name;
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getDob() {
			return dob;
		}

		public void setDob(Date dob) {
			this.dob = dob;
		}

		public String getAddr() {
			return addr;
		}

		public void setAddr(String addr) {
			this.addr = addr;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		private Date dob;
		private String addr;
		private String country;
		private String district;
		private String city;
		private byte[] pic;

		public byte[] getPic() {
			return pic;
		}

		public void setPic(byte[] pic) {
			this.pic = pic;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		UsersDet(String name,Date dob,String addr,String country,String district,String city,byte[] pic){
			this.name=name;
			this.dob=dob;
			this.addr=addr;
			this.country=country;
			this.district=district;
			this.city=city;
			this.pic=pic;
			
		}
}
