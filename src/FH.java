import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.RefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class FH {
	public static void main(String[] args) {
		WebClient wc = new WebClient(BrowserVersion.FIREFOX_3_6);

		wc.getOptions().setCssEnabled(false);
		wc.getOptions().setJavaScriptEnabled(false);
		wc.getOptions().setRedirectEnabled(true);
		wc.getOptions().setUseInsecureSSL(true);

		RefreshHandler rh = new RefreshHandler() {
			public void handleRefresh(final Page page, final URL url, final int seconds) {
			}
		};

		wc.setRefreshHandler(rh);
		wc.getOptions().setThrowExceptionOnFailingStatusCode(false);

		try {
			HtmlPage loginPage = wc.getPage("http://www.forumhouse.ru/login/");

			HtmlForm loginForm = (HtmlForm)loginPage.getElementById("pageLogin");

			HtmlInput login = (HtmlInput)loginForm.getElementById("ctrl_pageLogin_login");
			HtmlRadioButtonInput usePassword = (HtmlRadioButtonInput)loginForm.getElementById("ctrl_pageLogin_registered");
			HtmlPasswordInput password = (HtmlPasswordInput)loginForm.getElementById("ctrl_pageLogin_password");
			HtmlCheckBoxInput rememberMe = (HtmlCheckBoxInput)loginForm.getElementById("ctrl_pageLogin_remember");

			HtmlSubmitInput submit = null;

			DomNodeList<HtmlElement> inpList = loginForm.getElementsByTagName("input");
			for(int i=0; i<inpList.size(); i++) {
				if(inpList.get(i).getAttribute("type").toLowerCase().equals("submit")) {
					submit = (HtmlSubmitInput)inpList.get(i);
				}
			}

			login.setValueAttribute("<E-MAIL>");
			usePassword.setChecked(true);
			password.setValueAttribute("<PASSWORD>");
			rememberMe.setChecked(true);

			HtmlPage resPage = submit.click();

			System.out.println(resPage.asText().indexOf("noppixmeister"));

			Page att = wc.getPage("http://www.forumhouse.ru/attachments/1530190/");

			InputStream inputStream = att.getWebResponse().getContentAsStream();
			OutputStream outputStream = new FileOutputStream(new File("img.jpg"));
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
			outputStream.close();

			System.out.println("END");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
