package hello;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("!testing")
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter
{  
    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security.authorizeRequests()
            .antMatchers("/index", "/")
                .permitAll()
            .antMatchers("/ws")
                .hasRole("USER");
        
        // Configure authentication mechanism
        security.httpBasic()
            .realmName("Hello");
        
        // Do not create unnessesary sessions (all requests will be stateless) 
        security.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        // Configure CSRF protection (disable on WS requests)
        security.csrf()
            .requireCsrfProtectionMatcher((HttpServletRequest req) -> {
                String method = req.getMethod();
                if (method.equals("GET") || method.equals("HEAD"))
                    return false;
                String path = req.getServletPath();
                if (path.startsWith("/ws"))
                    return false;
                return true;
            });
        
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        // Create a dummy in-memory user database 
        
        // Note: Generate some BCrypt-ed passwords with: 
        //    echo -n "secr3t" | htpasswd -bn -B -C 10 -i "user" | cut -b "6-" | sed 's/^[$]2y/$2a/'
        
        auth.inMemoryAuthentication()
            .passwordEncoder(new BCryptPasswordEncoder())
            .withUser("totos")
                .roles("USER")
                .password("$2a$10$EqosCLmdOS051f9boIUoteiwBcvQk8pAeS/tdEYIUOIClbJjYmzrG") // tot0s
            .and()
            .withUser("malex")
                .roles("USER", "MANAGER")
                .password("$2a$10$fUyHsSLPnPvkjAr5komS7eTIZV9iVV1NvCgMCXL2IBVlqAzDVZWoW"); // secr3t
    }
}
