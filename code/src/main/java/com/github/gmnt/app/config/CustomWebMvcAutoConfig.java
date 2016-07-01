package hello;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
@EnableWebMvc
public class CustomWebMvcAutoConfig extends
                    WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String myExternalFilePath = "file:///C:/Users/bytecode/git/Gama-Music-Notation-Training/code/front-end-ionic2/www/";

    registry.addResourceHandler("/**").addResourceLocations(myExternalFilePath);

    super.addResourceHandlers(registry);
  }
  
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/").setViewName("redirect:/index.html?h=1");
  }

}