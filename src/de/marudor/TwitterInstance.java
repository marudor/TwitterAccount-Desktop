package de.marudor;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.html.form.Form;

public class TwitterInstance {
	private MechanizeAgent agent;
	private com.gistlabs.mechanize.document.Document page;
	private List<String> namelist;
	
	public TwitterInstance(List<String> namelist) {
		agent = new MechanizeAgent();
		this.namelist = namelist;
	}
	
	public String getCaptchaURL() {
		try {
			page = agent.get("https://mobile.twitter.com/signup");
			String src = page.saveToString();
			Document doc = Jsoup.parse(src);
			String result = doc.select("div.captcha img").attr("src");
			return result;
		}
			
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public Image getCaptchaImage() {
		try {
			return ImageIO.read(new URL(getCaptchaURL()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Account createAccount(String captcha) {
		Random r = new Random();
		String name = namelist.get(r.nextInt(namelist.size()));
		String mail = UUID.randomUUID().toString() + "@marudor.de";
		String password = UUID.randomUUID().toString();
		Form form = page.form("");
		form.get("oauth_signup_client_fullname").set(name);
		form.get("oauth_signup_client_phone_number").set(mail);
		form.get("oauth_signup_client_password").set(password);
		form.get("captcha_response_field").set(captcha);
		com.gistlabs.mechanize.document.Document resp = form.submit();
		String src = resp.saveToString();
		Document doc = Jsoup.parse(src);
		Elements message = doc.select("div.hint label");
		if (message != null && message.text().contains("Type the words"))
			return null;
		if (src.contains("https://mobile.twitter.com/signup/screen_name")) {
			src = agent.get("https://mobile.twitter.com/signup/screen_name").saveToString();
		}
		doc = Jsoup.parse(src);
		Elements nameInput = doc.select("div.setting input.screen_name");
		name = nameInput.attr("value");
		System.out.println(name);
		return new Account(name, password, mail, captcha);
	}
	
	public String getStatistic() {
		MechanizeAgent m = new MechanizeAgent();
		Resource resp = m.get("http://twitter.marudor.de/statistics");
		String src = resp.saveToString();
		JSONObject r;
		try {
			r = new JSONObject(src);
			Integer accounts = (Integer) r.get("accounts");
			Integer suspended = (Integer) r.get("suspended");
			return accounts+" accounts; "+suspended+" suspended";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failed to load Statistics";
		}
		
		
	}
}
