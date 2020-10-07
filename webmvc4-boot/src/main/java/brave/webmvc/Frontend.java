package brave.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {
  final RestTemplate restTemplate;
  final String backendEndpoint;

  @Autowired Frontend(RestTemplateBuilder restTemplateBuilder,
      @Value("${backend.endpoint:http://localhost:9000/api}") String backendEndpoint) {
    this.restTemplate = restTemplateBuilder.build();
    this.backendEndpoint = backendEndpoint;
  }

  @RequestMapping("/") public String callBackend() {
    return restTemplate.getForObject(backendEndpoint, String.class);
  }

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(Frontend.class);

    // Reduce startup overhead and clutter in logs
    application.setLogStartupInfo(false);
    application.setBannerMode(Banner.Mode.OFF);

    application.run(
        "--spring.application.name=frontend",
        "--server.port=8081"
    );
  }
}
