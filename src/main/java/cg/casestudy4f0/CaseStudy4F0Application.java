package cg.casestudy4f0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.servlet.http.HttpSession;
import java.net.HttpURLConnection;

@ServletComponentScan
@SpringBootApplication
public class CaseStudy4F0Application {

	public static void main(String[] args) {
		SpringApplication.run(CaseStudy4F0Application.class, args);
	}

}
