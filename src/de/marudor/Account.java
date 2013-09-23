package de.marudor;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.*;


public class Account {
	private String Name;
	private String Password;
	private String Email;
	private String Captcha;
	
	public Account(String Name, String Password, String Email, String Captcha) {
		this.Name = Name;
		this.Password = Password;
		this.Email = Email;
		this.Captcha = Captcha;
	}
	
	public String getJSON() {
		JSONObject obj=new JSONObject();
		try {
			obj.put("name",this.Name);
			obj.put("password",this.Password);
			obj.put("mail",this.Email);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	public void upload() {
		MechanizeAgent m = new MechanizeAgent();
		Resource r = m.get("http://twitter.marudor.de/upload?captcha="+this.Captcha.replace(" ", "*")+"&password="+this.Password+"&name="+this.Name+"&mail="+this.Email);
	}

}
